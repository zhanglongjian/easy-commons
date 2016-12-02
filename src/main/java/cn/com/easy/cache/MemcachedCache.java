package cn.com.easy.cache;

import java.io.Serializable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/**
 * 
 * 
 * 基于Spring Cache抽象体系的Memcached缓存实现
 * 
 */
public class MemcachedCache implements Cache, InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(MemcachedCache.class);

	private String name;

	private MemcachedClient memcachedClient;

	/**
	 * 默认最长缓存时间为1小时
	 */
	private static final int MAX_EXPIRED_DURATION = 60 * 60;

	/** Null值的最长缓存时间 */
	private static final int NULL_VALUE_EXPIRATION = 60 * 60 * 24 * 7;

	/** 增量过期时间允许设置的最大值 */
	private static final int DELTA_EXPIRATION_THRESHOLD = 60 * 60 * 24 * 30;

	/**
	 * 缓存数据超时时间
	 */
	private int expiredDuration = MAX_EXPIRED_DURATION;

	private static final Object NULL_HOLDER = new NullHolder();

	private boolean allowNullValues = true;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(memcachedClient, "memcachedClient must not be null!");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public MemcachedClient getNativeCache() {
		return this.memcachedClient;
	}

	@Override
	public ValueWrapper get(Object key) {
		String cacheKey = getCacheKey(key);
		try {
			StopWatch sw = new StopWatch();
			sw.start();
			Object value = memcachedClient.get(cacheKey);
			sw.stop();
			if (sw.getTime() > 50) {
				logger.info("读取memcached用时{}, key={}", sw.getTime(), cacheKey);
			}
			return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
		} catch (Exception e) {
			logger.error("读取memcached缓存发生异常, key={}, server={}", cacheKey, memcachedClient.getNodeLocator().getPrimary(cacheKey).getSocketAddress(), e.getCause());
			return null;
		}
	}

	/**
	 * 存入到缓存的key，由缓存的区域+key对象值串接而成
	 * 
	 * @param key
	 *            key对象
	 * @return
	 */
	private String getCacheKey(Object key) {
		return this.name + key.toString();
	}

	/**
	 * 安全的Set方法,在3秒内返回结果, 否则取消操作.
	 */
	@Override
	public void put(Object key, Object value) {
		String cacheKey = getCacheKey(key);
		logger.debug("放入缓存的Key:{}, Value:{}, StoreValue:{}", cacheKey, value, toStoreValue(value));
		int expiration = expiredDuration;
		if (value == null) {
			if (allowNullValues) {
				value = NULL_HOLDER; // 若允许缓存空值，则替换null为占坑对象；不允许直接缓存null，因为无法序列化
			}
			if (expiredDuration > NULL_VALUE_EXPIRATION) {
				expiration = NULL_VALUE_EXPIRATION; // 缩短空值的过期时间，最长缓存7天
			}
		} else if (expiredDuration > DELTA_EXPIRATION_THRESHOLD) {
			expiration += (int) (System.currentTimeMillis() / 1000); // 修改为UNIX时间戳类型的过期时间，使能够设置超过30天的过期时间
																		// 注意：时间戳计算这里有2038问题，
																		// 2038-1-19
																		// 11:14:07
																		// (GMT
																		// +8)
																		// 后，转换成的
																		// int
																		// 会溢出，导致出现负值
		}

		Future<Boolean> future = memcachedClient.set(cacheKey, expiration, value);
		try {
			future.get(3, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
			logger.error("memcached写入缓存发生异常, key={}, server={}", cacheKey, memcachedClient.getNodeLocator().getPrimary(cacheKey).getSocketAddress(), e);
		}
	}

	/**
	 * 安全的evict方法,在3秒内返回结果, 否则取消操作.
	 */
	@Override
	public void evict(Object key) {
		String cacheKey = getCacheKey(key);
		logger.debug("删除缓存的Key:{}", cacheKey);
		Future<Boolean> future = memcachedClient.delete(cacheKey);
		try {
			future.get(3, TimeUnit.SECONDS);
		} catch (Exception e) {
			future.cancel(false);
			logger.error("memcached清除缓存出现异常, key={}, server={}", cacheKey, memcachedClient.getNodeLocator().getPrimary(cacheKey).getSocketAddress(), e);
		}
	}

	@Override
	public void clear() {
		try {
			memcachedClient.flush();
		} catch (Exception e) {
			logger.error("memcached执行flush出现异常", e);
		}
	}

	protected Object fromStoreValue(Object storeValue) {
		if (this.allowNullValues && storeValue instanceof NullHolder) {
			return null;
		}
		return storeValue;
	}

	private static class NullHolder implements Serializable {

		private static final long serialVersionUID = -99681708140860560L;
	}

	protected Object toStoreValue(Object userValue) {
		if (this.allowNullValues && userValue == null) {
			return NULL_HOLDER;
		}
		return userValue;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMemcachedClient(MemcachedClient memcachedClient) {
		this.memcachedClient = memcachedClient;
	}

	public void setExpiredDuration(int expiredDuration) {
		this.expiredDuration = expiredDuration;
	}

	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		String cacheKey = getCacheKey(key);
		try {
			StopWatch sw = new StopWatch();
			sw.start();
			Object value = memcachedClient.get(cacheKey);
			sw.stop();
			if (sw.getTime() > 50) {
				logger.info("读取memcached用时{}, key={}", sw.getTime(), cacheKey);
			}
			return (T) value;
		} catch (Exception e) {
			logger.error("读取memcached缓存发生异常, key={}, server={}", cacheKey, memcachedClient.getNodeLocator().getPrimary(cacheKey).getSocketAddress(), e.getCause());
			return null;
		}
	}

}
