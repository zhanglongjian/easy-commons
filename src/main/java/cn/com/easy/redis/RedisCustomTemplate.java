package cn.com.easy.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * RedisTemplate重写方法，主要是捕获异常
 * 
 * @author zhanglj 2016年4月1日下午11:36:51
 * 
 */
public class RedisCustomTemplate<K, V> extends RedisTemplate<K, V> {

	private Logger logger = LoggerFactory.getLogger(RedisCustomTemplate.class);

	public <T> T execute(RedisCallback<T> action) {
		try {
			return execute(action, isExposeConnection());
		} catch (Exception ex) {
			logger.error("", ex);
			return null;
		}
	}
}
