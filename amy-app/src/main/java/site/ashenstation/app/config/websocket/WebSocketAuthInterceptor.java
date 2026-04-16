package site.ashenstation.app.config.websocket;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.TokenProvider;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        String token = tokenProvider.resolveToken(request);

        if (token == null) {
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        }

        Claims claims = tokenProvider.getClaims(token);
        String uid = claims.get(AmyConstants.JWT_CLAIM_UID, String.class);
        String userId = claims.get(AmyConstants.JWT_CLAIM_USER_ID, String.class);
        String username = claims.get(AmyConstants.JWT_CLAIM_USERNAME, String.class);

        attributes.put("uid", uid);
        attributes.put("userId", userId);
        attributes.put("username", username);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
