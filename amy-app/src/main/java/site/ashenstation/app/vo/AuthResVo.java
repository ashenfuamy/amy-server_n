package site.ashenstation.app.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import site.ashenstation.entity.AppUser;

@Data
@AllArgsConstructor
public class AuthResVo {
    private String token;
    private AppUser adminUser;
}
