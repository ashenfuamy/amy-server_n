package site.ashenstation.admin.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.ashenstation.entity.AdminUser;

@AllArgsConstructor
@Data
public class AuthResVo {
    private String token;
    private AdminUser adminUser;
}
