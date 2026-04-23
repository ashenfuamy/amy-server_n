package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.abstraction.BaseEntity;
import site.ashenstation.enums.MosaicType;
import site.ashenstation.enums.SummaryType;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Table("mda_summary")
public class Summary extends BaseEntity {
    private String title;
    private String subtitle;
    private String posterUrl;
    private String posterPath;
    private String serialNumber;
    private String resourceId;
    private MosaicType mosaicType;
    private SummaryType type;
    private String publisher;
}
