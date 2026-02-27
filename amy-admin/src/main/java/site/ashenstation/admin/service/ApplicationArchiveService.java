package site.ashenstation.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ashenstation.admin.dto.PublishApplicationArchiveDto;
import site.ashenstation.entity.ApplicationArchive;
import site.ashenstation.enums.ApplicationArchivePlatform;
import site.ashenstation.mapper.ApplicationArchiveMapper;

@Service
@RequiredArgsConstructor
public class ApplicationArchiveService {

    private final ApplicationArchiveMapper applicationArchiveMapper;

    public void publish(PublishApplicationArchiveDto dto) {
        System.out.println(dto.getPlatform() == ApplicationArchivePlatform.WIN);
    }
}
