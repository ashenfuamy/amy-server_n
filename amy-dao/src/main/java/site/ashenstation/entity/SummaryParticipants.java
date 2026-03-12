package site.ashenstation.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.Data;
import lombok.ToString;

@Data
@ToString()
@Table("mda_summary_participants")
public class SummaryParticipants {
    @Id(keyType = KeyType.Generator, value = KeyGenerators.uuid)
    private String id;
    private String summaryId;
    private String actorId;
    private String type;
}
