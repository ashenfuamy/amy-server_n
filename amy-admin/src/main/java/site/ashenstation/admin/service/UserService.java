package site.ashenstation.admin.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.util.UpdateEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.ashenstation.admin.dto.JwtUserDto;
import site.ashenstation.admin.vo.AuthResVo;
import site.ashenstation.dto.AuthByUsernamePasswordDto;
import site.ashenstation.entity.AdminUser;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.AdminUserMapper;
import site.ashenstation.properties.LoginProperties;
import site.ashenstation.properties.RsaProperties;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.service.OnlineUserService;
import site.ashenstation.utils.*;

import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final SecurityProperties securityProperties;
    private final TokenProvider tokenProvider;
    private final AdminUserMapper adminUserMapper;
    private final OnlineUserService onlineUserService;
    private final LoginProperties loginProperties;


    public AuthResVo login(AuthByUsernamePasswordDto dto, HttpServletRequest request) {
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
        AdminUser adminUser = jwtUserDto.getAdminUser();

        adminUser.setPassword(null);
        HashMap<String, String> claims = new HashMap<>() {{
            put(AmyConstants.JWT_CLAIM_USERNAME, adminUser.getUsername());
            put(AmyConstants.JWT_CLAIM_TOKEN_TYPE, JwtTokenType.LOGIN.getType());
            put(AmyConstants.JWT_CLAIM_USER_ID, adminUser.getId());
            put(AmyConstants.JWT_CLAIM_UID, IdUtil.fastSimpleUUID());
        }};

        String token = tokenProvider.createToken(adminUser.getUsername(), claims, securityProperties.getTokenValidityInSeconds());

        AdminUser adminUser1 = UpdateEntity.of(AdminUser.class, adminUser.getId());
        adminUser1.setLastLoginAt(new Date());
        adminUserMapper.update(adminUser1);

        if (loginProperties.isSingleLogin()) {
            onlineUserService.kickOutForUsername(adminUser.getUsername());
        }

        onlineUserService.save(adminUser.getUsername(), token, request);

        return new AuthResVo(token, adminUser);
    }


    public AdminUser getInfo() {
        JwtUserDto jwtUser = (JwtUserDto) SecurityUtils.getCurrentUser();
        AdminUser adminUser = jwtUser.getAdminUser();
        adminUser.setPassword(null);
        return adminUser;
    }

    public Boolean logout(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);
        onlineUserService.logout(token);
        return true;
    }
}
