package site.ashenstation.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ashenstation.admin.dto.PublishApplicationArchiveDto;
import site.ashenstation.entity.table.ApplicationArchiveTableDef;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.ApplicationArchiveMapper;
import site.ashenstation.properties.ApplicationArchiveProperties;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class ApplicationArchiveService {

    private final ApplicationArchiveMapper applicationArchiveMapper;
    private final ApplicationArchiveProperties applicationArchiveProperties;

    public void publish(PublishApplicationArchiveDto dto) {
        if (!dto.getReplaceExisting()) {
            long l = applicationArchiveMapper.selectCountByCondition(
                    ApplicationArchiveTableDef.APPLICATION_ARCHIVE
                            .APPLICATION_NAME.eq(dto.getName())
                            .and(ApplicationArchiveTableDef.APPLICATION_ARCHIVE.ARCH.eq(dto.getArch()))
                            .and(ApplicationArchiveTableDef.APPLICATION_ARCHIVE.PLATFORM.eq(dto.getPlatform())
                                    .and(ApplicationArchiveTableDef.APPLICATION_ARCHIVE.VERSION.eq(dto.getVersion()))
                            ));
            if (l > 0) {
                throw new BadRequestException("应用版本已存在");
            }
        }

        ApplicationArchiveProperties.Application application = applicationArchiveProperties.getApplication(dto.getName());

        File directoryFile = application.getDirectoryFile();

        File rootFolder = new File(directoryFile, dto.getPlatform().getType() + "/" + dto.getArch() + "/" + dto.getVersion());

        try {
            Files.createDirectories(Paths.get(rootFolder.getAbsolutePath()));
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }


    }
}
