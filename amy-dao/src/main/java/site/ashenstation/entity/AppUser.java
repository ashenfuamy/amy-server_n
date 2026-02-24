package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.abstraction.BaseUser;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Table("sys_app_user")
@Data
@ToString(callSuper = true)
public class AppUser extends BaseUser {
    private String mobile;
    private String email;
    private String nickname;
    private String lastLoginIp;
    private Date createdAt;
    private Date updatedAt;
    private Date deletedAt;
    private Boolean enabled;
}
