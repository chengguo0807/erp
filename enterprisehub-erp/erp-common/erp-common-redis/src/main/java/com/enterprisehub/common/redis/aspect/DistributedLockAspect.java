package com.enterprisehub.common.redis.aspect;

import com.enterprisehub.common.core.annotation.DistributedLock;
import com.enterprisehub.common.core.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁切面
 * 
 * @author EnterpriseHub
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;
    private final ExpressionParser parser = new SpelExpressionParser();
    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint point, DistributedLock distributedLock) throws Throwable {
        String lockKey = parseKey(distributedLock.key(), point);
        
        RLock lock = distributedLock.fair() 
            ? redissonClient.getFairLock(lockKey)
            : redissonClient.getLock(lockKey);
        
        boolean acquired = false;
        try {
            // 尝试获取锁
            acquired = lock.tryLock(
                distributedLock.waitTime(),
                distributedLock.leaseTime(),
                distributedLock.timeUnit()
            );
            
            if (acquired) {
                log.debug("获取分布式锁成功，key: {}", lockKey);
                return point.proceed();
            } else {
                log.warn("获取分布式锁失败，key: {}", lockKey);
                throw new BusinessException("系统繁忙，请稍后再试");
            }
        } finally {
            if (acquired && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("释放分布式锁，key: {}", lockKey);
            }
        }
    }

    /**
     * 解析SpEL表达式
     */
    private String parseKey(String key, ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        
        // 获取参数名
        String[] paramNames = discoverer.getParameterNames(method);
        if (paramNames == null) {
            return key;
        }
        
        // 创建SpEL上下文
        EvaluationContext context = new StandardEvaluationContext();
        Object[] args = point.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        
        // 解析表达式
        try {
            Expression expression = parser.parseExpression(key);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : key;
        } catch (Exception e) {
            log.warn("解析锁key失败，使用原始key: {}", key, e);
            return key;
        }
    }
}
