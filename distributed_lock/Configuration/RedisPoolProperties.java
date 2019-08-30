package cn.pdc.base.core.configurer.redisson;

import lombok.Data;
import lombok.ToString;

/**
 * <h1>redis池配置</h1>
 * author  sam
 * date   2019/8/27
 */
@Data
@ToString
public class RedisPoolProperties {
    private int maxIdle;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int connTimeout;
    private int soTimeout;
    /**
     * 池大小
     */
    private  int size;
}
