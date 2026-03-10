package site.ashenstation.admin.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "用户管理控制器", description = "管理用户登录，登出，信息更新等")
public class UserController {
    private final UserService userService;

    @AnonymousPostMapping("login")
    @Operation(summary = "用户名密码登录")
    private ResponseEntity<AuthResVo> login(@RequestBody @Valid AuthByUsernamePasswordDto dto, HttpServletRequest request) {
        return ResponseEntity.ok(userService.login(dto, request));
    }

    @GetMapping("info")
    @Operation(summary = "用户名信息")
    private ResponseEntity<AdminUser> info() {
        return ResponseEntity.ok(userService.getInfo());
    }

    @DeleteMapping("logout")
    @Operation(summary = "用户名登出")
    private ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok(userService.logout(request));
    }
}
