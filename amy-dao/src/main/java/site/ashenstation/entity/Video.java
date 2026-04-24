package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@Table("mda_video")
public class Video {
    private String summaryId;
    private Long size;
    private String filePath;
    private String fileUrl;
    private String folderPath;
    private String seriesId;
    private Long duration;
}
