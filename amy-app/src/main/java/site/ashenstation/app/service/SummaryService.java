package site.ashenstation.app.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import site.ashenstation.app.utils.SummaryDto;
import site.ashenstation.entity.AppUserResourcePermission;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaVideoTag;
import site.ashenstation.entity.Summary;
import site.ashenstation.enums.ResourceType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.AppUserResourcePermissionMapper;
import site.ashenstation.mapper.MdaPublisherMapper;
import site.ashenstation.mapper.MdaVideoTagMapper;
import site.ashenstation.mapper.SummaryMapper;
import site.ashenstation.properties.StaticResourceProperties;
import site.ashenstation.utils.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryService {

    private final MdaPublisherMapper publisherMapper;
    private final MdaVideoTagMapper videoTagMapper;
    private final SummaryMapper summaryMapper;
    private final StaticResourceProperties staticResourceProperties;
    private final AppUserResourcePermissionMapper appUserResourcePermissionMapper;

    public List<MdaPublisher> getPublishers() {
        return publisherMapper.selectAll();
    }

    public List<MdaVideoTag> getVideoTag() {
        return videoTagMapper.selectAll();
    }

    public Summary createSummary(SummaryDto dto) {
        String currentUserId = SecurityUtils.getCurrentUserId();

        Summary summary = new Summary();
        BeanUtils.copyProperties(dto, summary);

        String posterId = IdUtil.fastSimpleUUID();
        String posterName = posterId + staticResourceProperties.getImageExt();

        File posterDest = new File(staticResourceProperties.getPosterRoot(), posterName);

        try {
            dto.getPosterFile().transferTo(posterDest);
        } catch (IOException e) {
            throw new BadRequestException("");
        }

        appUserResourcePermissionMapper.insert(new AppUserResourcePermission(posterId, currentUserId, ResourceType.PICTURE));

        summary.setPosterUrl(staticResourceProperties.getPosterResourcePrefix() + "/" + posterName);
        summary.setMosaicType(dto.getMosaicType());
        summary.setCreatedAt(new Date());

        MdaPublisher publisher = dto.getPublisher();

        if (publisher.getId() == null) {
            publisherMapper.insert(publisher);
        }

        summary.setPublisher(publisher.getId());

        return summary;
    }

}
