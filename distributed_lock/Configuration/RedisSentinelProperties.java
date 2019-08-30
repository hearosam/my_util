package cn.pdc.base.core.configurer.redisson;

import lombok.Data;
import lombok.ToString;

/**
 * <h1>redis哨兵版属性配置类</h1>
 * author  sam
 * date   2019/8/27
 */
@Data
@ToString
public class RedisSentinelProperties {
    /**
     * 哨兵master 名称
     */
    private String master;
    /**
     * 哨兵节点
     */
    private String nodes;
    /**
     * 哨兵配置
     */
    private boolean masterOnlyWrite;
    /**
     *
     */
    private int failMax;
}
