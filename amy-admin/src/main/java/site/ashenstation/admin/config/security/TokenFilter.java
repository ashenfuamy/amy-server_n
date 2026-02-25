package site.ashenstation.admin.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.exception.JwtTokenException;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String token = tokenProvider.resolveToken(httpServletRequest);

        if (StringUtils.hasText(token)) {
            if (tokenProvider.isTokenExpired(token)) {
                throw new JwtTokenException();
            }

            Claims claims = tokenProvider.getClaims(token);
            String type = claims.get(AmyConstants.JWT_CLAIM_TOKEN_TYPE, String.class);

        }

        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
