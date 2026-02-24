package site.ashenstation.entity;

import lombok.Data;
import site.ashenstation.enums.ResourceType;

import java.util.Date;

@Data
public class AppUserResourcePermission {
    private String id;
    private String resourceId;
    private String userId;
    private ResourceType resourceType;
    private Date expire;
}
