package site.ashenstation.app.utils;

import org.springframework.web.multipart.MultipartFile;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaVideoTag;
import site.ashenstation.enums.MosaicType;

import java.util.List;

public interface SummaryDto {
    String getTitle();

    String getSubtitle();

    String getSerialNumber();

    MosaicType getMosaicType();

    MdaPublisher getPublisher();

    MultipartFile getPosterFile();

    List<String> getActors();

    List<MdaVideoTag> getTags();
}
