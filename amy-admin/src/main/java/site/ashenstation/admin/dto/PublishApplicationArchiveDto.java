package site.ashenstation.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.enums.ApplicationArchivePlatform;

import java.util.List;

@Data
@ToString
public class PublishApplicationArchiveDto {
    @NotNull(message = "不支持的平台类型")
    private ApplicationArchivePlatform platform;
    @NotNull(message = "应用架构不能为空")
    private String arch;
    @NotNull(message = "应用名不能为空")
    private String name;
    @NotNull(message = "应用版本不能为空")
    private String version;
    private Boolean replaceExisting = false;
    private Boolean setToLatest = false;
    @NotNull(message = "应用归档文件不能为空")
    private List<MultipartFile> files;
}
