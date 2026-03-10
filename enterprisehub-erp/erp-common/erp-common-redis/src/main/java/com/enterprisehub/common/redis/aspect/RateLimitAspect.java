package com.enterprisehub.common.redis.aspect;

import com.enterprisehub.common.core.annotation.RateLimit;
import com.enterprisehub.common.core.exception.BusinessException;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 限流切面
 * 
 * @author EnterpriseHub
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RateLimitAspect {

    private final RedisTemplate<String, Object> redisTemplate;
    
    // 本地缓存，避免频繁创建Bucket
    private final ConcurrentHashMap<String, Bucket> bucketCache = new ConcurrentHashMap<>();

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint point, RateLimit rateLimit) throws Throwable {
        String key = getCombineKey(rateLimit, point);
        
        // 获取或创建Bucket
        Bucket bucket = bucketCache.computeIfAbsent(key, k -> createBucket(rateLimit));
        
        // 尝试消费一个令牌
        if (bucket.tryConsume(1)) {
            return point.proceed();
        } else {
            log.warn("请求被限流，key: {}", key);
            throw new BusinessException("访问过于频繁，请稍后再试");
        }
    }

    /**
     * 创建Bucket
     */
    private Bucket createBucket(RateLimit rateLimit) {
        Bandwidth limit = Bandwidth.classic(
            rateLimit.count(),
            Refill.intervally(rateLimit.count(), Duration.ofSeconds(rateLimit.time()))
        );
        return Bucket.builder()
            .addLimit(limit)
            .build();
    }

    /**
     * 组合限流key
     */
    private String getCombineKey(RateLimit rateLimit, ProceedingJoinPoint point) {
        StringBuilder key = new StringBuilder(rateLimit.key());
        
        if (rateLimit.limitType() == RateLimit.LimitType.IP) {
            key.append(":").append(getIpAddress());
        } else if (rateLimit.limitType() == RateLimit.LimitType.USER) {
            key.append(":").append(getUserId());
        }
        
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        key.append(":").append(method.getDeclaringClass().getSimpleName());
        key.append(":").append(method.getName());
        
        return key.toString();
    }

    /**
     * 获取请求IP
     */
    private String getIpAddress() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return "unknown";
        }
        
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 获取用户ID（从SecurityContext或其他地方）
     */
    private String getUserId() {
        // TODO: 从SecurityContext获取当前用户ID
        return "anonymous";
    }
}
