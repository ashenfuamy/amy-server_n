package site.ashenstation.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.ashenstation.utils.RedisUtils;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RedisUtils redisUtils;

    public void setResourcePermission(String username) {}

}
