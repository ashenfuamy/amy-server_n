package site.ashenstation.abstraction;

import com.mybatisflex.annotation.ColumnMask;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import com.mybatisflex.core.mask.Masks;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public abstract class BaseUser {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
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
