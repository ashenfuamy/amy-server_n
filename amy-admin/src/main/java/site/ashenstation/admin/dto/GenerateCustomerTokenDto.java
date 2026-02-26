package site.ashenstation.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateCustomerTokenDto {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotNull(message = "过期时间不能为空")
    private Integer expire;
}
