package cn.pdc.base.core.annotation;

import java.lang.annotation.*;

/**
 * <h1>分布式锁注解</h1>
 * @Author: sam
 * @Date: 9:56 2019/8/27
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DistributedLock {
    String key() default "";
    /**分布式锁失效时间(租约时间)**/
    long leaseTime() default 30;
}
