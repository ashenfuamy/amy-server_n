package site.ashenstation.app.vo;

import com.mybatisflex.annotation.TableRef;
import lombok.Data;
import lombok.ToString;
import site.ashenstation.entity.Actor;
import site.ashenstation.entity.ActorTag;

import java.util.List;

@Data
@ToString
@TableRef(ActorTag.class)
public class ActorListByClassifyTagVo {
    private String id;
    private String title;

    private List<Actor> actors;
}
