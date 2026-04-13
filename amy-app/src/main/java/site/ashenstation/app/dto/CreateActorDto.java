package site.ashenstation.app.dto;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.entity.ActorTag;

@Data
@ToString
public class CreateActorDto {
    private String name;
    private String introduction;
    private MultipartFile avatarFile;
    private ActorTag actorTag;
}
