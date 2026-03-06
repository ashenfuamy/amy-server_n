package site.ashenstation.admin.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.admin.dto.RegisterNewAppUserDto;
import site.ashenstation.entity.AppUser;
import site.ashenstation.entity.AppUserResourcePermission;
import site.ashenstation.entity.table.AppUserTableDef;
import site.ashenstation.enums.ResourceType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.AppUserMapper;
import site.ashenstation.mapper.AppUserResourcePermissionMapper;
import site.ashenstation.properties.StaticResourceProperties;

import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AppUserService {

    private final AppUserMapper appUserMapper;
    private final StaticResourceProperties staticResourceProperties;
    private final AppUserResourcePermissionMapper appUserResourcePermissionMapper;

    @Transactional
    public Boolean registerNewAppUser(RegisterNewAppUserDto dto) {
        String username = dto.getUsername();
        AppUser appUser;

        appUser = appUserMapper.selectOneByCondition(AppUserTableDef.APP_USER.USERNAME.eq(username));
        if (appUser != null) {
            throw new BadRequestException("用户名已存在");
        }

        appUser = appUserMapper.selectOneByCondition(AppUserTableDef.APP_USER.EMAIL.eq(dto.getEmail()));
        if (appUser != null) {
            throw new BadRequestException("邮箱已存在");
        }

        appUser = new AppUser();
        BeanUtil.copyProperties(dto, appUser);

        appUser.setPassword(new BCryptPasswordEncoder(12).encode(dto.getPassword()));

        MultipartFile avatarFile = dto.getAvatarFile();

        String avatarId = IdUtil.fastSimpleUUID();
        String avatarName = avatarId + staticResourceProperties.getImageExt();
        File avatarDest = new File(staticResourceProperties.getUserAvatarRoot(), avatarName);

        try {
            avatarFile.transferTo(avatarDest);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

        appUser.setAvatar(staticResourceProperties.getUserAvatarResourcePrefix() + "/" + avatarName);

        appUser.setCreatedAt(new Date());
        appUser.setUpdatedAt(new Date());
        appUser.setLocked(false);
        appUser.setEnabled(true);
        appUser.setExpired(false);
        appUser.setCredentialsExpired(false);

        appUserMapper.insert(appUser);

        AppUserResourcePermission appUserResourcePermission = new AppUserResourcePermission();
        appUserResourcePermission.setUserId(appUser.getId());
        appUserResourcePermission.setResourceId(avatarId);
        appUserResourcePermission.setResourceType(ResourceType.PICTURE);

        appUserResourcePermissionMapper.insert(appUserResourcePermission);

        return true;
    }
}
