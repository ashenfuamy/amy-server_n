package site.ashenstation.app.vo;

import lombok.Data;
import lombok.ToString;
import site.ashenstation.entity.Summary;

import java.util.List;

@Data
@ToString
public class ActorSummariesVo {
    private String actorId;
    private List<Summary> actorSummaries;
}
