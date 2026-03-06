package site.ashenstation.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.app.service.UserService;
import site.ashenstation.dto.AuthByUsernamePasswordDto;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @AnonymousPostMapping("/loginByUsernamePassword")
    private ResponseEntity<?> loginByUsernamePassword(@RequestBody @Valid AuthByUsernamePasswordDto dto, HttpServletRequest request) {
        return ResponseEntity.ok(userService.loginByUsernamePassword(dto, request));
    }
}
