package site.ashenstation.admin.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import site.ashenstation.mapper.CustomTokenMapper;
import site.ashenstation.service.OnlineUserService;
import site.ashenstation.utils.TokenProvider;

@Configuration
@RequiredArgsConstructor
public class TokenConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;
    private final OnlineUserService onlineUserService;
    private final CustomTokenMapper customTokenMapper;

    @Override
    public void configure(HttpSecurity builder) {
        TokenFilter tokenFilter = new TokenFilter(tokenProvider, onlineUserService, customTokenMapper);
        builder.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
