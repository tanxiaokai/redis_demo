package io.github.tanxiaokai.croe;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;

import redis.clients.jedis.exceptions.JedisConnectionException;

public class MybatisRedisCache implements Cache{
	
	private static final Logger logger = LoggerFactory.getLogger(MybatisRedisCache.class);

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate = SpringContextHolder.getBean("redisTemplate");
	
	private final String id;

	
	private String generateKey(Object key){
		return key.toString();
	}
	
	/**
	 * The {@code ReadWriteLock}.
	 */
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

	public MybatisRedisCache(final String id) {
		if (id == null) {
			throw new IllegalArgumentException("Cache instances require an ID");
		}
		logger.info(">>>>>>>>> MybatisMybatisRedisCache:id=" + id);
		this.id = id;
	}

	public void clear() {
		RedisConnection connection = null;
		try {
			connection = redisTemplate.getConnectionFactory().getConnection();
			connection.flushDb();
			connection.flushAll();
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	public String getId() {
		return this.id;
	}

	public Object getObject(Object key) {
		logger.info(">>>>>>>>> getObject key=" + generateKey(key));
		Object result = redisTemplate.opsForValue().get(generateKey(key));
		return result;
	}

	public ReadWriteLock getReadWriteLock() {
		return this.readWriteLock;
	}

	public int getSize() {
		int result = 0;
		RedisConnection connection = null;
		try {
			connection = redisTemplate.getConnectionFactory().getConnection();
			result = Integer.valueOf(connection.dbSize().toString());
		} catch (JedisConnectionException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		return result;
	}

	public void putObject(Object key, Object value) {
		logger.info(">>>>>>>>> putObject key=" + generateKey(key));
		redisTemplate.opsForValue().set(generateKey(key), value);
	}

	public Object removeObject(Object key) {
		return redisTemplate.expire(new String(generateKey(key)), 0, TimeUnit.MICROSECONDS);
	}
}
