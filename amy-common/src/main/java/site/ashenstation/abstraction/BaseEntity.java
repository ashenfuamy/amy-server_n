package site.ashenstation.abstraction;

import com.mybatisflex.annotation.Column;
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
    @Column("create_by")
    private String creator;

    public BaseEntity() {
        try {
            String currentUserId = SecurityUtils.getCurrentUserId();
            if (currentUserId == null)
                this.creator = "System";
            else
                this.creator = SecurityUtils.getCurrentUserId();
            this.createdAt = new Date();
        } catch (Exception ignored) {
        }
    }
}

