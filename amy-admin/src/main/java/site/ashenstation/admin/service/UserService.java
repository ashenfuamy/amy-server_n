package site.ashenstation.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import site.ashenstation.dto.AuthByUsernamePasswordDto;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.properties.RsaProperties;
import site.ashenstation.utils.RsaUtils;

@Service
@RequiredArgsConstructor
public class UserService {
    private final AuthenticationManager authenticationManager;


    public void login(AuthByUsernamePasswordDto dto) {
        String password;

        try {
            password = RsaUtils.decryptByPrivateKey(RsaProperties.privateKey, dto.getPassword());
        } catch (Exception e) {
            throw new BadRequestException("密码错误");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(dto.getUsername(), password);

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authenticate);
    }
}
