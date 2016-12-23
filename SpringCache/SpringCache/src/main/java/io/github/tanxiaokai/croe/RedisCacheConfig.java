package io.github.tanxiaokai.croe;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 以Spring与配置文件来管理的redis缓存配置类
 * 
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

	private volatile JedisConnectionFactory jedisConnectionFactory;
	private volatile RedisTemplate<String, String> redisTemplate;
	private volatile RedisCacheManager redisCacheManager;

	public RedisCacheConfig() {
		super();
	}

	public RedisCacheConfig(JedisConnectionFactory jedisConnectionFactory, RedisTemplate<String, String> redisTemplate,
			RedisCacheManager redisCacheManager) {
		super();
		this.jedisConnectionFactory = jedisConnectionFactory;
		this.redisTemplate = redisTemplate;
		this.redisCacheManager = redisCacheManager;
	}

	public JedisConnectionFactory redisConnectionFactory() {
		return jedisConnectionFactory;
	}

	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
		return redisTemplate;
	}

	public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
		return redisCacheManager;
	}

	/**
	 * 自定义Key生成器
	 * @return
	 */
	@Bean
	public KeyGenerator customKeyGenerator() {
		return new KeyGenerator() {
			public Object generate(Object o, Method method, Object... objects) {
				StringBuilder sb = new StringBuilder();
				sb.append(o.getClass().getName());
				sb.append("-");
				sb.append(method.getName());
				sb.append("-");
				for (Object obj : objects) {
					sb.append(obj.toString());
					sb.append("-");
				}
				
				String key = sb.toString();
				key = key.substring(0, key.length()-1);
				return key;
			}
		};
	}
}