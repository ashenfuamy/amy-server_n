package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@Table("sys_custom_token")
@ToString
public class CustomToken {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    private String id;
    private String token;
    private String title;
    private Date createdAt;
    private Date expiredAt;
    private String creatorId;
}
