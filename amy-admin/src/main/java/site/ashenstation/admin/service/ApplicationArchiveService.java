package site.ashenstation.admin.service;

import com.mybatisflex.core.util.UpdateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.admin.config.properties.ApplicationArchiveProperties;
import site.ashenstation.admin.dto.PublishApplicationArchiveDto;
import site.ashenstation.entity.ApplicationArchive;
import site.ashenstation.entity.table.ApplicationArchiveTableDef;
import site.ashenstation.enums.ApplicationArchiveStatus;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.ApplicationArchiveMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ApplicationArchiveService {

    private final ApplicationArchiveMapper applicationArchiveMapper;
    private final ApplicationArchiveProperties applicationArchiveProperties;

    public Boolean publish(PublishApplicationArchiveDto dto) {
        ApplicationArchive archive = applicationArchiveMapper.selectOneByCondition(
                ApplicationArchiveTableDef.APPLICATION_ARCHIVE
                        .APPLICATION_NAME.eq(dto.getName())
                        .and(ApplicationArchiveTableDef.APPLICATION_ARCHIVE.ARCH.eq(dto.getArch()))
                        .and(ApplicationArchiveTableDef.APPLICATION_ARCHIVE.PLATFORM.eq(dto.getPlatform())
                                .and(ApplicationArchiveTableDef.APPLICATION_ARCHIVE.VERSION.eq(dto.getVersion()))
                        ));

        boolean exit = archive != null;


        if (!dto.getReplaceExisting() && exit) {
            throw new BadRequestException("应用版本已存在");
        }

        ApplicationArchiveProperties.Application application = applicationArchiveProperties.getApplication(dto.getName());

        File directoryFile = application.getDirectoryFile();

        File rootFolder = new File(directoryFile, dto.getPlatform().getType() + "/" + dto.getArch() + "/" + dto.getVersion());

        try {
            Files.createDirectories(Paths.get(rootFolder.getAbsolutePath()));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

        List<MultipartFile> files = dto.getFiles();

        for (MultipartFile file : files) {
            try {
                file.transferTo(new File(rootFolder, Objects.requireNonNull(file.getOriginalFilename())));
            } catch (IOException e) {
                throw new BadRequestException(e.getMessage());
            }
        }

        if (dto.getReplaceExisting() && exit) {
            ApplicationArchive applicationArchive = UpdateEntity.of(ApplicationArchive.class, archive.getId());
            applicationArchive.setUpdateAt(new Date());
            applicationArchiveMapper.update(applicationArchive);

        } else {
            ApplicationArchive applicationArchive = new ApplicationArchive();
            applicationArchive.setApplicationName(dto.getName());
            applicationArchive.setArch(dto.getArch());
            applicationArchive.setPlatform(dto.getPlatform());
            applicationArchive.setVersion(dto.getVersion());
            applicationArchive.setPackageName(dto.getPackageName());
            applicationArchive.setStatus(ApplicationArchiveStatus.TO_BE_RELEASED);
            applicationArchive.setCreatedAt(new Date());
            applicationArchive.setIsLatest(dto.getSetToLatest());
            applicationArchive.setRootPath(rootFolder.getAbsolutePath());

            applicationArchiveMapper.insert(applicationArchive);
        }

        return true;
    }

    public String getArchiveFilePath(String application, String platform, String arch, String archive) {
        ApplicationArchiveTableDef archiveTableDef = ApplicationArchiveTableDef.APPLICATION_ARCHIVE;
        ApplicationArchive applicationArchive = applicationArchiveMapper.selectOneByCondition(
                archiveTableDef.APPLICATION_NAME.eq(application)
                        .and(archiveTableDef.PLATFORM.eq(platform))
                        .and(archiveTableDef.ARCH.eq(arch))
                        .and(archiveTableDef.IS_LATEST.eq(true))
                        .and(archiveTableDef.STATUS.eq(ApplicationArchiveStatus.RELEASED))
        );

        if (applicationArchive == null) {
            throw new BadRequestException("版本不存在");

        }


        return applicationArchiveProperties.getUriPath() + "/" + application + "/" + platform + "/" + arch + "/" + applicationArchive.getVersion() + "/" + archive;
    }
}
