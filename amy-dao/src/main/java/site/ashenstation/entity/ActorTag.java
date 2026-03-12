package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.abstraction.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Table("mda_actor_tag")
public class ActorTag extends BaseEntity {
    private String title;
}
