package site.ashenstation.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.ashenstation.admin.service.UserService;
import site.ashenstation.admin.vo.AuthResVo;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.dto.AuthByUsernamePasswordDto;
import site.ashenstation.entity.AdminUser;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @AnonymousPostMapping("login")
    private ResponseEntity<AuthResVo> login(@RequestBody @Valid AuthByUsernamePasswordDto dto, HttpServletRequest request) {
        return ResponseEntity.ok(userService.login(dto, request));
    }

    @GetMapping("info")
    private ResponseEntity<AdminUser> info() {
        return ResponseEntity.ok(userService.getInfo());
    }

    @DeleteMapping("logout")
    private ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok(userService.logout(request));
    }
}
