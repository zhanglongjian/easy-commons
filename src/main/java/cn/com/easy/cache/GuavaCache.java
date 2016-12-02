package cn.com.easy.cache;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.cache.CacheBuilder;

/**
 * 基于spring的 guava cache实现（本地缓存）
 * 
 * @author zhanglj 2015年4月19日
 * 
 */
public class GuavaCache implements Cache, InitializingBean {

	private final static Logger logger = LoggerFactory.getLogger(GuavaCache.class);

	/** 缓存名称 */
	private String name;
	/** 缓存数据超时时间，默认最长缓存时间为1小时 */
	private int expiredDuration = 60 * 60;
	/** 最大缓存数量 */
	private Integer maximumSize;
	/** guava cache客户端 */
	private com.google.common.cache.Cache<Object, Object> guavaCacheClient = null;;

	private static final Object NULL_HOLDER = new NullHolder();

	private boolean allowNullValues = true;

	@Override
	public void afterPropertiesSet() throws Exception {
		if (this.guavaCacheClient == null) {
			CacheBuilder<Object, Object> cacheBuilder = CacheBuilder.newBuilder();
			if (this.maximumSize != null) {
				// 最多缓存数量
				cacheBuilder = cacheBuilder.maximumSize(this.maximumSize);
			}
			// // 自首次写入后，默认缓存1小时
			cacheBuilder = cacheBuilder.expireAfterWrite(expiredDuration, TimeUnit.SECONDS);
			this.guavaCacheClient = cacheBuilder.build();
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public com.google.common.cache.Cache<Object, Object> getNativeCache() {
		return this.guavaCacheClient;
	}

	@Override
	public ValueWrapper get(Object key) {
		String cacheKey = getCacheKey(key);
		try {
			StopWatch sw = new StopWatch();
			sw.start();
			Object value = guavaCacheClient.getIfPresent(cacheKey);
			sw.stop();
			if (sw.getTime() > 50) {
				logger.info("读取guava cache用时{}, key={}, cacheName={}", sw.getTime(), cacheKey, this.getName());
			}
			return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
		} catch (Exception e) {
			logger.error("读取guava cache缓存发生异常, key={}, cacheName={}", cacheKey, this.getName(), e.getCause());
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		String cacheKey = getCacheKey(key);
		try {
			StopWatch sw = new StopWatch();
			sw.start();
			Object value = fromStoreValue(this.guavaCacheClient.getIfPresent(cacheKey));
			if (value != null && type != null && !type.isInstance(value)) {
				throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
			}
			sw.stop();
			if (sw.getTime() > 50) {
				logger.info("读取guava cache用时{}, key={}, cacheName={}", sw.getTime(), cacheKey, this.getName());
			}
			return (T) value;
		} catch (Exception e) {
			logger.error("读取guava cache缓存发生异常, key={}, cacheName={}", cacheKey, this.getName(), e.getCause());
			return null;
		}
	}

	@Override
	public void put(Object key, Object value) {
		this.guavaCacheClient.put(getCacheKey(key), toStoreValue(value));
	}

	@Override
	public void evict(Object key) {
		this.guavaCacheClient.invalidate(getCacheKey(key));
	}

	@Override
	public void clear() {
		this.guavaCacheClient.invalidateAll();
	}

	/**
	 * Convert the given value from the internal store to a user value returned
	 * from the get method (adapting {@code null}).
	 * 
	 * @param storeValue
	 *            the store value
	 * @return the value to return to the user
	 */
	protected Object fromStoreValue(Object storeValue) {
		if (this.allowNullValues && storeValue == NULL_HOLDER) {
			return null;
		}
		return storeValue;
	}

	/**
	 * Convert the given user value, as passed into the put method, to a value
	 * in the internal store (adapting {@code null}).
	 * 
	 * @param userValue
	 *            the given user value
	 * @return the value to store
	 */
	protected Object toStoreValue(Object userValue) {
		if (this.allowNullValues && userValue == null) {
			return NULL_HOLDER;
		}
		return userValue;
	}

	@SuppressWarnings("serial")
	private static class NullHolder implements Serializable {
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
	 * 缓存名称
	 * 
	 * @param name
	 * @auth nibili 2015年4月19日
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 超时时间(单位:秒)
	 * 
	 * @param expiredDuration
	 * @auth nibili 2015年4月19日
	 */
	public void setExpiredDuration(int expiredDuration) {
		this.expiredDuration = expiredDuration;
	}

	/**
	 * 是否允许空值
	 * 
	 * @param allowNullValues
	 * @auth nibili 2015年4月19日
	 */
	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	/**
	 * set 最大缓存数量
	 * 
	 * @param maximumSize
	 * @auth nibili 2015年4月19日
	 */
	public void setMaximumSize(Integer maximumSize) {
		this.maximumSize = maximumSize;
	}

}
