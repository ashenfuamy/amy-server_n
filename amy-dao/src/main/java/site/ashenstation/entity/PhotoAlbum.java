package site.ashenstation.entity;

import com.mybatisflex.annotation.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import site.ashenstation.abstraction.BaseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
@Table("mda_photo_album")
public class PhotoAlbum extends BaseEntity {
    private String title;
    private String subtitle;
    private String actorId;
    private String posterUrl;
    private String posterPath;
    private String resourceId;
    private String folderPath;
    private String url;
}
