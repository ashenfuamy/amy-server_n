package site.ashenstation.admin.service;

import com.mybatisflex.core.mask.MaskManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.ashenstation.dto.JwtUserDto;
import site.ashenstation.entity.AdminUser;
import site.ashenstation.entity.table.AdminUserTableDef;
import site.ashenstation.mapper.AdminUserMapper;
import site.ashenstation.utils.UserCacheManager;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final AdminUserMapper adminUserMapper;
    private final UserCacheManager userCacheManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);

        if (jwtUserDto == null) {
            AdminUser adminUser = MaskManager.execWithoutMask(() -> adminUserMapper.selectOneByCondition(AdminUserTableDef.ADMIN_USER.USERNAME.eq(username)));
            if (adminUser == null) {
                throw new UsernameNotFoundException(username);
            }

            jwtUserDto = new JwtUserDto(adminUser);
            userCacheManager.addUserCache(username, jwtUserDto);

        }


        return jwtUserDto;
    }
}
