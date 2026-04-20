package site.ashenstation.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import site.ashenstation.annotation.UpdateResourceCache;
import site.ashenstation.app.utils.SummaryDto;
import site.ashenstation.entity.*;
import site.ashenstation.enums.SummaryType;
import site.ashenstation.mapper.*;
import site.ashenstation.utils.SecurityUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SerialService {

    private final SummaryService summaryService;
    private final SummaryMapper summaryMapper;
    private final MdaVideoTagMapper videoTagMapper;
    private final SummaryTagMapper summaryTagMapper;
    private final SerialMapper serialMapper;
    private final SummaryParticipantsMapper summaryParticipantsMapper;


    @UpdateResourceCache
    @Transactional
    public Boolean createSerial(@Validated SummaryDto dto) {
        String currentUserId = SecurityUtils.getCurrentUserId();

        Summary summary = summaryService.createSummary(dto);
        summary.setType(SummaryType.SERIAL);


        summaryMapper.insert(summary);

        ArrayList<SummaryTag> summaryTags = new ArrayList<>();
        List<MdaVideoTag> tags = dto.getTags();

        tags.forEach(tag -> {
            if (tag.getId() == null) {
                videoTagMapper.insert(tag);
            }
            SummaryTag summaryTag = new SummaryTag();
            summaryTag.setTagId(tag.getId());
            summaryTag.setSummaryId(summary.getId());
            summaryTags.add(summaryTag);
        });

        summaryTagMapper.insertBatch(summaryTags);

        Serial serial = new Serial();
        serial.setSummaryId(summary.getId());

        serialMapper.insert(serial);

        ArrayList<SummaryParticipants> summaryParticipants = new ArrayList<>();

        List<String> actors = dto.getActors();
        actors.forEach(actor -> {
            SummaryParticipants participants = new SummaryParticipants();
            participants.setSummaryId(summary.getId());
            participants.setActorId(actor);
            summaryParticipants.add(participants);
        });

        summaryParticipantsMapper.insertBatch(summaryParticipants);

        return true;
    }
}
