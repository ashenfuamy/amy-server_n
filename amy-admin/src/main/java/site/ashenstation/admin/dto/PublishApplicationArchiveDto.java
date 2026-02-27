package site.ashenstation.admin.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.enums.ApplicationArchivePlatform;

import java.util.List;

@Data
@ToString
public class PublishApplicationArchiveDto {
    private ApplicationArchivePlatform platform;
    private String arch;
    private String name;
    private String version;
    private Boolean replaceExisting;
    private Boolean setToLatest;
    private List<MultipartFile> files;
}
