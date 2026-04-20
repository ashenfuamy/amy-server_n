package site.ashenstation.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.app.dto.CreateVideoDto;
import site.ashenstation.app.dto.UploadChunkDto;
import site.ashenstation.app.service.VideoService;
import site.ashenstation.app.vo.VideoTaskVo;
import site.ashenstation.enums.UploadStatus;

@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;

    @PostMapping("create")
    private ResponseEntity<VideoTaskVo> create(@Validated CreateVideoDto dto) {
        return ResponseEntity.ok(videoService.create(dto));
    }

    @PostMapping("chunk")
    private void uploadChunk(UploadChunkDto dto) {
        videoService.uploadChunk(dto);
    }

    @GetMapping("upload-status")
    private ResponseEntity<UploadStatus> queryUploadStatus(String id) {
        return ResponseEntity.ok(videoService.queryUploadStatus(id));
    }

    @GetMapping("/finish-chunk")
    private ResponseEntity<Boolean> uploadChunkFinish(String id) {
        videoService.finishUpload(id);
        return ResponseEntity.ok(true);
    }

}
