package site.ashenstation.app.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.util.UpdateEntity;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import site.ashenstation.app.config.websocket.WebSocketHandler;
import site.ashenstation.app.dto.JwtUserDto;
import site.ashenstation.app.vo.AuthResVo;
import site.ashenstation.dto.AuthByUsernamePasswordDto;
import site.ashenstation.entity.AppUser;
import site.ashenstation.entity.AppUserResourcePermission;
import site.ashenstation.entity.table.AppUserResourcePermissionTableDef;
import site.ashenstation.enums.LoginPlatform;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.AppUserMapper;
import site.ashenstation.mapper.AppUserResourcePermissionMapper;
import site.ashenstation.properties.LoginProperties;
import site.ashenstation.properties.RsaProperties;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.service.OnlineUserService;
import site.ashenstation.utils.*;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUtils redisUtils;
    private final AppUserResourcePermissionMapper appUserResourcePermissionMapper;
    private final SecurityProperties securityProperties;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final AppUserMapper appUserMapper;
    private final OnlineUserService onlineUserService;
    private final LoginProperties loginProperties;

    public List<GrantedAuthority> getResourcePermission(String username) {
        String key = securityProperties.getResourcePermissionKey() + username;
        Set<Object> members = redisUtils.members(key);

        List<GrantedAuthority> permissions = new ArrayList<>();

        if (members != null && !members.isEmpty()) {
            for (Object obj : members) {
                permissions.add(new SimpleGrantedAuthority((String) obj));
            }
        } else {
            List<AppUserResourcePermission> appUserResourcePermissions = appUserResourcePermissionMapper.selectListByCondition(
                    AppUserResourcePermissionTableDef.APP_USER_RESOURCE_PERMISSION.USER_ID.eq(username)
            );

            appUserResourcePermissions.forEach(i -> {
                if (i.getExpireAt() == null || new Date().before(i.getExpireAt())) {
                    String resourceId = i.getResourceId();
                    permissions.add(new SimpleGrantedAuthority(resourceId));
                    redisUtils.set(key, resourceId);
                }
            });
        }

        return permissions;
    }

    public AuthResVo loginByUsernamePassword(AuthByUsernamePasswordDto dto, HttpServletRequest request) {
        String password;

        try {
            password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, dto.getPassword());
        } catch (Exception e) {
            throw new BadRequestException("密码错误");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(dto.getUsername(), password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        JwtUserDto jwtUserDto = (JwtUserDto) authenticate.getPrincipal();

        AppUser appUser = jwtUserDto.getAppUser();
        appUser.setPassword(null);

        LoginPlatform loginPlatform = LoginPlatform.find(request.getHeader(securityProperties.getClientHeader()));

        HashMap<String, String> claims = new HashMap<>() {{
            put(AmyConstants.JWT_CLAIM_USERNAME, dto.getUsername());
            put(AmyConstants.JWT_CLAIM_USER_ID, appUser.getId());
            put(AmyConstants.JWT_CLAIM_UID, IdUtil.fastSimpleUUID());
            assert loginPlatform != null;
            put(AmyConstants.JWT_CLAIM_PLATFORM, loginPlatform.getType());
        }};

        String token = tokenProvider.createToken(appUser.getUsername(), claims, securityProperties.getTokenValidityInSeconds());
        AppUser updateAppUser = UpdateEntity.of(AppUser.class, appUser.getId());

        updateAppUser.setLastLoginAt(new Date());
        String ip = IpAddrUtils.getIp(request);

        updateAppUser.setLastLoginIp(ip);

        appUserMapper.update(updateAppUser);

        if (loginProperties.isSingleLogin()) {
            assert loginPlatform != null;
            onlineUserService.kickOutForUsernameAndPlatform(appUser.getUsername(), loginPlatform);
        }

        onlineUserService.save(appUser.getUsername(), token, request, loginPlatform);

        return new AuthResVo(token, appUser);
    }

    public AppUser getInfo() {
        JwtUserDto jwtUser = (JwtUserDto) SecurityUtils.getCurrentUser();
        AppUser appUser = jwtUser.getAppUser();

        appUser.setPassword(null);
        return appUser;
    }

    public Boolean logout(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);

        LoginPlatform loginPlatform = LoginPlatform.find(request.getHeader(securityProperties.getClientHeader()));

        onlineUserService.logout(token, loginPlatform);

        Claims claims = tokenProvider.getClaims(token);
        String uid = claims.get(AmyConstants.JWT_CLAIM_UID, String.class);
        String userId = claims.get(AmyConstants.JWT_CLAIM_USER_ID, String.class);

        try {
            WebSocketSession webSocketSession = WebSocketHandler.ONLINE_SESSIONS.get(userId + ":" + uid);
            WebSocketHandler.ONLINE_SESSIONS.remove(userId + ":" + uid);
            webSocketSession.close();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;
    }
}
