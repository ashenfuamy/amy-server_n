package site.ashenstation.admin.service;

import cn.hutool.core.util.IdUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ashenstation.admin.dto.GenerateCustomerTokenDto;
import site.ashenstation.entity.CustomToken;
import site.ashenstation.enums.JwtTokenType;
import site.ashenstation.mapper.CustomTokenMapper;
import site.ashenstation.utils.AmyConstants;
import site.ashenstation.utils.SecurityUtils;
import site.ashenstation.utils.TokenProvider;

import java.time.ZonedDateTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class CustomTokenService {

    private final TokenProvider tokenProvider;
    private final CustomTokenMapper customTokenMapper;


    public String generateToken(GenerateCustomerTokenDto dto) {
        HashMap<String, String> claims = new HashMap<>();

        String id = IdUtil.fastSimpleUUID();

        claims.put(AmyConstants.JWT_CLAIM_TOKEN_TYPE, JwtTokenType.Authorization.getType());
        claims.put(AmyConstants.JWT_CLAIM_UID, id);
        claims.put(AmyConstants.JWT_CLAIM_CREATOR_ID, SecurityUtils.getCurrentUserId());

        String token = tokenProvider.createToken("anonymous", claims);

        CustomToken customToken = new CustomToken();
        customToken.setToken(token);
        customToken.setId(id);
        customToken.setTitle(dto.getTitle());
        customToken.setCreatedAt(new Date());
        customToken.setCreatorId(SecurityUtils.getCurrentUserId());

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime targetTime = now.plusMinutes(dto.getExpire());

        ZonedDateTime zonedDateTime = targetTime.atZone(ZoneId.systemDefault());

        Date date = Date.from(zonedDateTime.toInstant());
        customToken.setExpiredAt(date);

        customTokenMapper.insert(customToken);

        return token;
    }
}
