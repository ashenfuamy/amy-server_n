package site.ashenstation.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.abstraction.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class Actor extends BaseEntity {
    private String name;
    private String tagId;
    private String introduction;
    private String avatarPath;
    private String avatarUrl;
}
