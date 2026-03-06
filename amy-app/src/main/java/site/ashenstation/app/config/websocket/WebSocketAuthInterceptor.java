package site.ashenstation.app.config.websocket;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import site.ashenstation.properties.SecurityProperties;
import site.ashenstation.utils.TokenProvider;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        System.out.println("beforeHandshake");

//        HttpServletRequest servletRequest = (HttpServletRequest) request;
////        String token = tokenProvider.resolveToken(servletRequest);
////        if (StringUtils.hasText(token)) {
////            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
////            return false;
////        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
