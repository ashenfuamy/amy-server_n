package site.ashenstation.app.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import site.ashenstation.app.vo.AuthResVo;
import site.ashenstation.dto.AuthByUsernamePasswordDto;
import site.ashenstation.entity.AppUserResourcePermission;
import site.ashenstation.entity.table.AppUserResourcePermissionTableDef;
import site.ashenstation.mapper.AppUserResourcePermissionMapper;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.RedisUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUtils redisUtils;
    private final AppUserResourcePermissionMapper appUserResourcePermissionMapper;
    private final SecurityProperties securityProperties;

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
                if (i.getExpire() == null || new Date().before(i.getExpire())) {
                    String resourceId = i.getResourceId();
                    permissions.add(new SimpleGrantedAuthority(resourceId));
                    redisUtils.set(key, resourceId);
                }
            });
        }

        return permissions;
    }

    public AuthResVo loginByUsernamePassword(AuthByUsernamePasswordDto dto, HttpServletRequest request) {

        return new AuthResVo();
    }

}
