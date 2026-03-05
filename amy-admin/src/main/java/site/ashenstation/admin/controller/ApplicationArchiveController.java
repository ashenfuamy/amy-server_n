package site.ashenstation.admin.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import site.ashenstation.admin.dto.PublishApplicationArchiveDto;
import site.ashenstation.admin.service.ApplicationArchiveService;
import site.ashenstation.annotation.rest.AnonymousGetMapping;
import site.ashenstation.annotation.rest.AnonymousPostMapping;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ApplicationArchiveController {
    private final ApplicationArchiveService applicationArchiveService;

    @ResponseBody
    @AnonymousPostMapping("/api/archive/publish")
    public ResponseEntity<Boolean> publish(@Valid PublishApplicationArchiveDto dto) {
        return ResponseEntity.ok(applicationArchiveService.publish(dto));
    }

    @AnonymousGetMapping("/{application}/{platform}/{arch}/{archive}")
    public void archiveFilePath(@PathVariable String application, @PathVariable String platform, @PathVariable String arch, @PathVariable String archive, HttpServletResponse response) throws IOException {
        String archiveFilePath = applicationArchiveService.getArchiveFilePath(application, platform, arch, archive);
        response.sendRedirect(archiveFilePath);
    }
}
