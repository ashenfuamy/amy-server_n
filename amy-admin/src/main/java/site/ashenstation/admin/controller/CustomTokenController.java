package site.ashenstation.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.admin.dto.GenerateCustomerTokenDto;
import site.ashenstation.admin.service.CustomTokenService;

@RestController
@RequestMapping("/api/token")
@RequiredArgsConstructor
public class CustomTokenController {

    private final CustomTokenService customTokenService;

    @PostMapping("generate")
    public ResponseEntity<String> generateToken(@RequestBody @Valid GenerateCustomerTokenDto dto) {
        return ResponseEntity.ok(customTokenService.generateToken(dto));
    }
}
