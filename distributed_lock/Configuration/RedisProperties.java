package cn.pdc.base.core.configurer.redisson;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;


/**
 * <h1>Redis属性类</h1>
 * author  sam
 * date   2019/8/27
 */
@Data
@ToString
@ConfigurationProperties(prefix = "spring.redis",ignoreInvalidFields = false)
public class RedisProperties {
    private int database;
    private int timeout;
    private String password;
    private String mode;

    /**
     * 池配置
     */
    private RedisPoolProperties pool;

    /**
     * 单机信息配置
     */
    private RedisSingleProperties single;

    /**
     * 集群 信息配置
     */
    private RedisClusterProperties cluster;

    /**
     * 哨兵配置
     */
    private RedisSentinelProperties sentinel;
}
