package io.github.tanxiaokai.croe;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
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

	private Logger LOG = Logger.getLogger(RedisCacheConfig.class);

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
	 * 获取注解上定义的名称
	 * 
	 * @param o
	 * @param method
	 * @return
	 */
	private String getCacheableName(Object o, Method method) {

		// 优先方法注解定义的名称
		Cacheable cache = method.getAnnotation(Cacheable.class);
		if (cache != null && cache.value().length > 0) {
			return cache.value()[0];
		}
		// 其次类上注解定义的名称
		cache = o.getClass().getAnnotation(Cacheable.class);
		if (cache != null && cache.value().length > 0) {
			return cache.value()[0];
		}

		return " ";
	}

	/**
	 * 修改Key生成器规则
	 * 
	 * @return
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		// 命名规则
		// 注解@Cacheable value定义的名称 - 方法名(参数值1 - 参数值2 ...)
		return new KeyGenerator() {
			public Object generate(Object o, Method method, Object... objects) {
				StringBuilder sb = new StringBuilder();
				// sb.append(o.getClass().getName());
				// sb.append(" - ");
				sb.append(getCacheableName(o, method));
				sb.append("#");
				sb.append(method.getName());
				sb.append("(");
				for (Object obj : objects) {
					sb.append(obj.toString());
					sb.append("#");
				}
				String key = sb.toString();
				key = key.substring(0, key.length() - 1);
				key = key + ")";
				return key;
			}
		};
	}

	@Bean
	//自定义一个Key生成器规则, 需要通过注解设置 @Cacheable(keyGenerator="customKeyGenerator") 
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
				key = key.substring(0, key.length() - 1);
				return key;
			}
		};
	}

	@Override
	public CacheErrorHandler errorHandler() {
		// 处理异常
		// 如果出现异常只进行记录日志，继续重数据库读取
		return new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException exception, Cache cache, Object key){
				LOG.error("Get Redis缓存失败! Key[" + key.toString() + "]" + exception.getMessage());
			}

			@Override
			public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
				LOG.error("Put Redis缓存失败! Key[" + key.toString() + "]" + e.getMessage());
			}

			@Override
			public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
				LOG.error("Evict Redis缓存失败! Key[" + key.toString() + "]" + e.getMessage());
			}

			@Override
			public void handleCacheClearError(RuntimeException e, Cache cache) {
				LOG.error("Clear Redis缓存失败! " + e.getMessage());
			}
		};
	}
}