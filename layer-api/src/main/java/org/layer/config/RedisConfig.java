package org.layer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    RedisTemplate<String, String> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }


    // 최근 서비스 이용 시점 기록 - 1번 데이터베이스
    // @Bean
    // @Qualifier("recentActivityDateConnectionFactory")
    // public RedisConnectionFactory recentActivityDateRedisConnectionFactory() {
    //     RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    //     redisStandaloneConfiguration.setHostName("redis");
    //     redisStandaloneConfiguration.setPort(6379);
    //     redisStandaloneConfiguration.setDatabase(1); // 1번 데이터베이스
    //
    //     return new LettuceConnectionFactory(redisStandaloneConfiguration);
    // }
    //
    // @Bean
    // @Qualifier("recentActivityDate")
    // public RedisTemplate<String, Object> recentActivityDateRedisTemplate(@Qualifier("recentActivityDateConnectionFactory") RedisConnectionFactory redisConnectionFactory) {
    //     RedisTemplate<String, Object> template = new RedisTemplate<>();
    //     template.setConnectionFactory(redisConnectionFactory);
    //
    //     PolymorphicTypeValidator typeValidator = BasicPolymorphicTypeValidator
    //             .builder()
    //             .allowIfSubType(Object.class)
    //             .build();
    //
    //     ObjectMapper objectMapper = new ObjectMapper()
    //             .findAndRegisterModules()
    //             .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    //             .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
    //             .activateDefaultTyping(typeValidator, ObjectMapper.DefaultTyping.NON_FINAL)
    //             .registerModule(new JavaTimeModule());
    //
    //     template.setKeySerializer(new StringRedisSerializer());
    //     template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
    //
    //     return template;
    // }

}