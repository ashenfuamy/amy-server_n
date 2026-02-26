package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.abstraction.BaseUser;

@EqualsAndHashCode(callSuper = true)
@Table("sys_admin_user")
@Data
@ToString(callSuper = true)
public class AdminUser extends BaseUser {
}
