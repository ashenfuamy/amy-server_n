package site.ashenstation.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.admin.dto.PublishApplicationArchiveDto;
import site.ashenstation.admin.service.ApplicationArchiveService;
import site.ashenstation.annotation.rest.AnonymousPostMapping;

@RestController
@RequestMapping("/api/archive")
@RequiredArgsConstructor
public class ApplicationArchiveController {
    private final ApplicationArchiveService applicationArchiveService;

    @AnonymousPostMapping("publish")
    public void publish(@Valid PublishApplicationArchiveDto dto) {
        applicationArchiveService.publish(dto);
    }
}
