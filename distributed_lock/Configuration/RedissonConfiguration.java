package cn.pdc.base.core.configurer.redisson;

import org.apache.commons.lang.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


/**
 * <h1>redisson配置类</h1>
 * author  sam
 * date   2019/8/27
 */

@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@ConditionalOnProperty(name = "spring.redis.enable-distributed-lock",havingValue = "true")
public class RedissonConfiguration {

    @Configuration
    @ConditionalOnClass({Redisson.class})
    @ConditionalOnExpression("'${spring.redis.mode}' == 'single' or '${spring.redis.mode}' =='cluster' or '${spring.redis.mode}'=='sentinel'")
    protected class RedissonSingleClientConfiguration {

        /**
         * @Description: 单机版redisson客户端
         * @Param: {@link RedissonClient}
         * @return: org.redisson.api.RedissonClient
         * @Author: sam
         * @Date: 2019/8/27 10:42
        */

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode",havingValue = "single")
        RedissonClient redissonSingle(RedisProperties redisProperties) {
            Config config = new Config();
            String node = redisProperties.getSingle().getAddress();
            SingleServerConfig serverConfig = config.useSingleServer()
                    .setAddress(node)
                    .setTimeout(redisProperties.getPool().getConnTimeout())
                    .setConnectionPoolSize(redisProperties.getPool().getSize())
                    .setConnectionMinimumIdleSize(redisProperties.getPool().getMinIdle());
            if(StringUtils.isNotBlank(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
            return Redisson.create(config);
        }

        /**
         * @Description: 集群模式 redisson 客户端
         * @Param: {@link RedissonClient}
         * @return: org.redisson.api.RedissonClient
         * @Author: sam
         * @Date: 2019/8/27 11:29
        */

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode",havingValue = "cluster")
        RedissonClient redissonCluster(RedisProperties redisProperties) {
            Config config = new Config();
            String[] nodes = redisProperties.getCluster().getNodes().split(",");
            List<String> newNodes = Arrays.asList(nodes);

            ClusterServersConfig serversConfig = config.useClusterServers()
                    .addNodeAddress(newNodes.toArray(new String[0]))
                    .setScanInterval(redisProperties.getCluster().getScanInterval())
                    .setIdleConnectionTimeout(redisProperties.getPool().getConnTimeout())
                    .setFailedAttempts(redisProperties.getCluster().getFailedAttempts())
                    .setRetryAttempts(redisProperties.getCluster().getRetryAttempts())
                    .setRetryInterval(redisProperties.getCluster().getRetryInterval())
                    .setMasterConnectionPoolSize(redisProperties.getCluster().getMasterConnectionPoolSize())
                    .setSlaveConnectionPoolSize(redisProperties.getCluster().getSlaveConnectionPoolSize())
                    .setTimeout(redisProperties.getTimeout());
            if(StringUtils.isNotBlank(redisProperties.getPassword())) {
                serversConfig.setPassword(redisProperties.getPassword());
            }
            return Redisson.create(config);
        }

        /**
         * @Description: 哨兵模式 redisson客户端
         * @Param: {@link RedissonClient}
         * @return: org.redisson.api.RedissonClient
         * @Author: sam
         * @Date: 2019/8/27 11:28
        */

        @Bean
        @ConditionalOnProperty(name = "spring.redis.mode",havingValue = "sentinel")
        RedissonClient redissonSentinel(RedisProperties redisProperties) {
            Config config = new Config();
            String[] nodes = redisProperties.getSentinel().getNodes().split(",");
            List<String> newNodes = Arrays.asList(nodes);
            SentinelServersConfig serversConfig = config.useSentinelServers()
                    .addSentinelAddress(newNodes.toArray(new String[0]))
                    .setMasterName(redisProperties.getSentinel().getMaster())
                    .setReadMode(ReadMode.SLAVE)
                    .setFailedAttempts(redisProperties.getSentinel().getFailMax())
                    .setTimeout(redisProperties.getTimeout())
                    .setMasterConnectionPoolSize(redisProperties.getPool().getSize())
                    .setSlaveConnectionPoolSize(redisProperties.getPool().getSize());
            if(StringUtils.isNotBlank(redisProperties.getPassword())) {
                serversConfig.setPassword(redisProperties.getPassword());
            }
            return Redisson.create(config);
        }
    }
}
