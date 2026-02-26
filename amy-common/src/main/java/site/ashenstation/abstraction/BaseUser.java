package site.ashenstation.abstraction;

import com.mybatisflex.annotation.ColumnMask;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public abstract class BaseUser {
    @Id
    private String id;
    private String username;
    @ColumnMask(Masks.PASSWORD)
    private String password;
    private String avatar;
    private Date lastLoginAt;
    private Boolean locked;
    private Boolean expired;
    private Boolean credentialsExpired;
    private Boolean enabled;
}
