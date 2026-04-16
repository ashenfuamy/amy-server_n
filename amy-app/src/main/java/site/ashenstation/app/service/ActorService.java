package site.ashenstation.app.service;

import cn.hutool.core.util.IdUtil;
import com.mybatisflex.core.query.QueryChain;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.ashenstation.app.dto.CreateActorDto;
import site.ashenstation.app.vo.ActorListByClassifyTagVo;
import site.ashenstation.entity.Actor;
import site.ashenstation.entity.ActorTag;
import site.ashenstation.entity.AppUserResourcePermission;
import site.ashenstation.entity.table.ActorTableDef;
import site.ashenstation.entity.table.ActorTagTableDef;
import site.ashenstation.enums.ResourceType;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.mapper.ActorMapper;
import site.ashenstation.mapper.ActorTagMapper;
import site.ashenstation.mapper.AppUserResourcePermissionMapper;
import site.ashenstation.properties.StaticResourceProperties;
import site.ashenstation.utils.SecurityUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActorService {

    private final ActorTagMapper actorTagMapper;
    private final ActorMapper actorMapper;
    private final StaticResourceProperties staticResourceProperties;
    private final AppUserResourcePermissionMapper appUserResourcePermissionMapper;

    public List<ActorTag> getActorTags() {
        return actorTagMapper.selectAll();
    }

    @Transactional
    public Boolean createActor(CreateActorDto dto) {
        String currentUserId = SecurityUtils.getCurrentUserId();

        Actor actor = actorMapper.selectOneByCondition(
                ActorTableDef.ACTOR.NAME.eq(dto.getName())
                        .and(ActorTableDef.ACTOR.CREATOR.eq(currentUserId))
        );

        if (actor != null) {
            throw new BadRequestException("已存在");
        }

        ActorTag actorTag = dto.getActorTag();

        if (actorTag.getId() == null) {
            actorTagMapper.insert(actorTag);
        }

        String avatarId = IdUtil.fastSimpleUUID();
        String avatarName = avatarId + staticResourceProperties.getImageExt();
        File avatarDest = new File(staticResourceProperties.getAvatarRoot(), avatarName);

        try {
            dto.getAvatarFile().transferTo(avatarDest);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }

        actor = new Actor();
        BeanUtils.copyProperties(dto, actor);

        actor.setCreator(currentUserId);
        actor.setCreatedAt(new Date());
        actor.setTagId(actorTag.getId());
//        actor.setAvatarPath(staticResourceProperties.getAvatarResourcePrefix() + "/" + avatarName);

        actor.setAvatarUrl(staticResourceProperties.getAvatarResourcePrefix() + "/" + avatarName);
        actor.setResourceId(avatarId);

        actorMapper.insert(actor);

        appUserResourcePermissionMapper.insert(new AppUserResourcePermission(avatarId, currentUserId, ResourceType.PICTURE));

        return true;
    }

    public List<ActorListByClassifyTagVo> getActorListByClassifyTag() {

        String currentUserId = SecurityUtils.getCurrentUserId();

        return QueryChain.of(actorTagMapper)
                .select(
                        ActorTagTableDef.ACTOR_TAG.ID,
                        ActorTagTableDef.ACTOR_TAG.TITLE,
                        ActorTableDef.ACTOR.ID,
                        ActorTableDef.ACTOR.NAME,
                        ActorTableDef.ACTOR.TAG_ID,
                        ActorTableDef.ACTOR.INTRODUCTION,
                        ActorTableDef.ACTOR.AVATAR_URL,
                        ActorTableDef.ACTOR.CREATOR
                )
                .from(ActorTagTableDef.ACTOR_TAG)
                .leftJoin(ActorTableDef.ACTOR).on(ActorTagTableDef.ACTOR_TAG.ID.eq(ActorTableDef.ACTOR.TAG_ID))
                .where(ActorTableDef.ACTOR.CREATOR.eq(currentUserId))
                .listAs(ActorListByClassifyTagVo.class);
    }
}
