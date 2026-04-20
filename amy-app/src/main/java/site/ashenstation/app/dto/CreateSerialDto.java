package site.ashenstation.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.app.utils.SummaryDto;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaVideoTag;
import site.ashenstation.enums.MosaicType;

import java.util.List;

@Data
@ToString
public class CreateSerialDto implements SummaryDto {
    @NotBlank(message = "标题不能为空")
    private String title;
    private String subtitle;
    @NotNull(message = "是否打码不能为空")
    private MosaicType mosaicType;
    private String serialNumber;
    private MdaPublisher publisher;
    @NotNull(message = "海报不能为空")
    private MultipartFile posterFile;
    @NotEmpty(message = "参演不能为空")
    private List<String> actors;

    private List<MdaVideoTag> tags;
}
