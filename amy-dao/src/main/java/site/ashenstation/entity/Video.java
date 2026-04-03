package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.abstraction.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Table("mda_video")
public class Video extends BaseEntity {
    private String summaryId;
    private Long size;
    private String filePath;
    private String fileUrl;
    private String folderPath;
    private String seriesId;
    private Long duration;
}
