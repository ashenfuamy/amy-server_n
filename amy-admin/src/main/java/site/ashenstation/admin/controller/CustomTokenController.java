package site.ashenstation.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "自定义 Token 管理")
public class CustomTokenController {

    private final CustomTokenService customTokenService;

    @PostMapping("generate")
    @Operation(summary = "创建自定义 Token")
    public ResponseEntity<String> generateToken(@RequestBody @Valid GenerateCustomerTokenDto dto) {
        return ResponseEntity.ok(customTokenService.generateToken(dto));
    }
}
