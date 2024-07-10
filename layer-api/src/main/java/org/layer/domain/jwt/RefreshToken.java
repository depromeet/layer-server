package org.layer.domain.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Builder
@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {
    @Id
    private Long memberId;
    private String token;
    @TimeToLive
    private long ttl;
}
