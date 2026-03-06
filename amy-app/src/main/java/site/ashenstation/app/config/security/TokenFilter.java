package site.ashenstation.app.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.app.service.UserService;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TokenFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final UserService userService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String token = tokenProvider.resolveToken(httpServletRequest);

        if (StringUtils.hasText(token)) {

            if (tokenProvider.isTokenExpired(token)) {
                throw new BadRequestException(UNAUTHORIZED, "Token expired");
            }

            Claims claims = tokenProvider.getClaims(token);
            String username = claims.get(AmyConstants.JWT_CLAIM_USERNAME, String.class);
            List<GrantedAuthority> resourcePermission = userService.getResourcePermission(username);

            User principal = new User(claims.getSubject(), "******", resourcePermission);
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, token, resourcePermission));
        }

        chain.doFilter(request, response);
    }
}
