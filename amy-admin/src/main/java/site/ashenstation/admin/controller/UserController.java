package site.ashenstation.admin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.ashenstation.admin.service.UserService;
import site.ashenstation.annotation.rest.AnonymousPostMapping;
import site.ashenstation.dto.AuthByUsernamePasswordDto;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @AnonymousPostMapping("login")
    private void login(@RequestBody @Valid AuthByUsernamePasswordDto dto) {
        userService.login(dto);
    }
}
