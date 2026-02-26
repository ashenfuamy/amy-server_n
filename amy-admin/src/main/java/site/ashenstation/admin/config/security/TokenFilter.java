package site.ashenstation.admin.config.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import site.ashenstation.dto.OnlineUserDto;
import site.ashenstation.entity.CustomToken;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.exception.JwtTokenException;
import site.ashenstation.mapper.CustomTokenMapper;
import site.ashenstation.service.OnlineUserService;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.TokenProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;
    private final OnlineUserService onlineUserService;
    private final CustomTokenMapper customTokenMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String token = tokenProvider.resolveToken(httpServletRequest);

        if (StringUtils.hasText(token)) {
            Claims claims = tokenProvider.getClaims(token);
            String type = claims.get(AmyConstants.JWT_CLAIM_TOKEN_TYPE, String.class);

            if (JwtTokenType.LOGIN == JwtTokenType.find(type)) {
                String key = tokenProvider.loginKey(token);

                OnlineUserDto onlineUserDto = onlineUserService.getOne(key);

                if (onlineUserDto != null) {
                    tokenProvider.checkRenewal(token);
                    User principal = new User(claims.getSubject(), "******", new ArrayList<>());
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(principal, token, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            if (JwtTokenType.Authorization == JwtTokenType.find(type)) {
                String id = claims.get(AmyConstants.JWT_CLAIM_UID, String.class);
                CustomToken customToken = customTokenMapper.selectOneById(id);

                if (customToken != null) {
                    Date expiredAt = customToken.getExpiredAt();
                    int i = expiredAt.compareTo(new Date());

                    if (i <= 0) {
                        throw new JwtTokenException();
                    }

                    AnonymousAuthenticationToken anonymousAuthenticationToken = new AnonymousAuthenticationToken(id, claims, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(anonymousAuthenticationToken);
                }
            }

        }

        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
