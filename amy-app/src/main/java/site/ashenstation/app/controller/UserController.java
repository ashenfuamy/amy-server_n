package site.ashenstation.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.app.service.UserService;
import site.ashenstation.dto.AuthByUsernamePasswordDto;
import site.ashenstation.entity.AppUser;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
@Tag(name = "用户管理")
public class UserController {
    private final UserService userService;

    @AnonymousPostMapping("/loginByUsernamePassword")
    @Operation(summary = "用户名密码登录")
    private ResponseEntity<?> loginByUsernamePassword(@RequestBody @Valid AuthByUsernamePasswordDto dto, HttpServletRequest request) {
        return ResponseEntity.ok(userService.loginByUsernamePassword(dto, request));
    }

    @GetMapping("info")
    @Operation(summary = "用户名信息")
    private ResponseEntity<AppUser> info() {
        return ResponseEntity.ok(userService.getInfo());
    }

    @DeleteMapping("logout")
    @Operation(summary = "用户名登出")
    private ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok(userService.logout(request));
    }
}
