package site.ashenstation.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.app.service.SummaryService;
import site.ashenstation.entity.MdaPublisher;
import site.ashenstation.entity.MdaVideoTag;

import java.util.List;

@RestController
@RequestMapping("/api/summary")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @GetMapping("/get-all-publisher")
    public ResponseEntity<List<MdaPublisher>> getAllPublisher() {
        List<MdaPublisher> publishers = summaryService.getPublishers();
        return ResponseEntity.ok(publishers);
    }


    @GetMapping("/get-all-video-tag")
    public ResponseEntity<List<MdaVideoTag>> getAllVideoTag() {
        List<MdaVideoTag> videoTag = summaryService.getVideoTag();
        return ResponseEntity.ok(videoTag);
    }
}
