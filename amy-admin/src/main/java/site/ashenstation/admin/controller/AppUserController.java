package site.ashenstation.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.admin.dto.RegisterNewAppUserDto;
import site.ashenstation.admin.service.AppUserService;
import site.ashenstation.annotation.rest.AnonymousPostMapping;

@Controller
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/appuser")
public class AppUserController {

    private final AppUserService appUserService;

    @AnonymousPostMapping("/create")
    public ResponseEntity<Boolean> create(@Valid RegisterNewAppUserDto dto) {
        return ResponseEntity.ok(appUserService.registerNewAppUser(dto));
    }
}
