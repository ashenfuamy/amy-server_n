package site.ashenstation.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RegisterNewAppUserDto {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private String mobile;
    private String email;
    @NotBlank(message = "昵称不能为空")
    private String nickname;
    @NotNull(message = "头像不能为空")
    private MultipartFile avatarFile;
    private Boolean locked = false;
    private Boolean enabled = true;
}
