package site.ashenstation.app.service;

import com.mybatisflex.core.mask.MaskManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.ashenstation.app.dto.JwtUserDto;
import site.ashenstation.entity.AppUser;
import site.ashenstation.entity.table.AppUserTableDef;
import site.ashenstation.mapper.AppUserMapper;
import site.ashenstation.service.UserCacheManager;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserCacheManager userCacheManager;
    private final AppUserMapper appUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        JwtUserDto jwtUserDto = (JwtUserDto) userCacheManager.getUserCache(username);

        if (jwtUserDto == null) {
            AppUser appUser = MaskManager.execWithoutMask(() -> appUserMapper.selectOneByCondition(AppUserTableDef.APP_USER.USERNAME.eq(username)));
            if (appUser == null) {
                throw new UsernameNotFoundException(username);
            }

            jwtUserDto = new JwtUserDto(appUser);
            userCacheManager.addUserCache(username, jwtUserDto);
        }
        return jwtUserDto;
    }
}
