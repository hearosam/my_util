package cn.pdc.base.core.aspect;

import cn.pdc.base.core.annotation.DistributedLock;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * <h1>分布式锁切面</h1>
 * author  sam
 * date   2019/8/27
 */
@Aspect
@Component
public class DistributedKeyAspect {

    //自动注入redisson客户端
    @Autowired(required = false) //required = false 这里需要给配置指定工程是否启用分布式锁
    private RedissonClient redisson;

    //声明切面
    @Pointcut("@annotation(cn.pdc.base.core.annotation.DistributedLock)")
    public void lockPointCut(){}

    //配置是否开启了分布式锁，true:开启，false：关闭
    @Value("${spring.redis.enable-distributed-lock:false}")
    private boolean enable_distributed_lock;

    /** 
     * @Description: 环绕通知处理方法
     * @Param: {@link Object}
     * @return: java.lang.Object
     * @Author: sam
     * @Date: 2019/8/27 11:40
    */
    @Around("lockPointCut()")
    public Object around(ProceedingJoinPoint point) {
        Object obj = null;
        RLock lock = null;
        try {
            if (!enable_distributed_lock) {//不开启分布式锁，直接执行业务代码
                return point.proceed();
            }
            //获取切入点的方法对象
            MethodSignature signature = (MethodSignature) point.getSignature();
            Method method = signature.getMethod();
            //获取放上上面的注解
            DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);
            if(distributedLock != null) {
                //获取分布式锁的key的值
                String distributedKey = distributedLock.key();
                long leaseTime = distributedLock.leaseTime();//分布式锁失效时间

                if (StringUtils.isNotBlank(distributedKey)) {
                    lock = redisson.getLock(distributedKey);
                } else {//如果不指定分布式锁的key，默认按照全方法名com.pdc.base.storehouseServerImpl.save
                    lock = redisson.getLock(point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
                }
                //获取锁
                boolean res = lock.tryLock(0, leaseTime, TimeUnit.SECONDS);
                if (res) {
                    obj = point.proceed();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            if(lock != null ){//释放分布式锁
                lock.unlock();
            }
        }
        return obj;
    }
}
