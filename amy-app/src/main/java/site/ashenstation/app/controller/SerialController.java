package site.ashenstation.app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.app.dto.CreateSerialDto;
import site.ashenstation.app.service.SerialService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/serial")
public class SerialController {
    private final SerialService serialService;

    @PostMapping("/create")
    public ResponseEntity<Boolean> createSummary(@Validated CreateSerialDto dto) {
        return ResponseEntity.ok(serialService.createSerial(dto));
    }
}
