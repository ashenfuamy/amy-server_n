package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import site.ashenstation.enums.ResourceType;

import java.util.Date;

@Data
@Table("sys_app_user_resource_permission")
public class AppUserResourcePermission {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    private String id;
    private String resourceId;
    private String userId;
    private ResourceType resourceType;
    private Date expireAt;

    public AppUserResourcePermission(String resourceId, String userId, ResourceType resourceType) {
        this.resourceId = resourceId;
        this.userId = userId;
        this.resourceType = resourceType;
    }

    public AppUserResourcePermission() {
    }
}
