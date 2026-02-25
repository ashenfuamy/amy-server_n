package site.ashenstation.dto;

import lombok.Data;
import lombok.ToString;
import jakarta.validation.constraints.NotBlank;

@Data
@ToString
public class AuthByUsernamePasswordDto {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
