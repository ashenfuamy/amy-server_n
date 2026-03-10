package site.ashenstation.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.admin.dto.RegisterNewAppUserDto;
import site.ashenstation.admin.service.AppUserService;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appuser")
@Tag(name = "APP 用户管理")
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping("/create")
    @Operation(summary = "创建用户")
    public ResponseEntity<Boolean> create(@Valid RegisterNewAppUserDto dto) {
        return ResponseEntity.ok(appUserService.registerNewAppUser(dto));
    }
}
