package site.ashenstation.abstraction;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.utils.SecurityUtils;

import java.util.Date;

@Data
@ToString
public abstract class BaseEntity {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    private String id;
    private Date createdAt;
    private String createBy;

    public BaseEntity() {
        String currentUserId = SecurityUtils.getCurrentUserId();
        if (currentUserId == null)
            this.createBy = "System";
        else
            this.createBy = SecurityUtils.getCurrentUserId();
        this.createdAt = new Date();
    }
}
