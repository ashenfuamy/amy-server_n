package site.ashenstation.app.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.exception.BadRequestException;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.RedisUtils;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Slf4j
@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {
    private final TokenProvider tokenProvider;
    private final SecurityProperties securityProperties;
    private final RedisUtils redisUtils;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String token = tokenProvider.resolveToken(httpServletRequest);

        if (StringUtils.hasText(token)) {

            if (tokenProvider.isTokenExpired(token)) {
                throw new BadRequestException(UNAUTHORIZED ,"Token expired");
            }

            Claims claims = tokenProvider.getClaims(token);
            String username = claims.get(AmyConstants.JWT_CLAIM_USERNAME, String.class);

            String key = securityProperties.getResourcePermissionKey() + username;
            java.util.Set<Object> members = redisUtils.members(key);
        }

        chain.doFilter(request, response);
    }
}
