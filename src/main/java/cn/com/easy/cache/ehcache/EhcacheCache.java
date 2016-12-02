package cn.com.easy.cache.ehcache;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.ehcache.EhCacheCache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

/**
 * ehcache缓存实现
 * 
 * @author zhanaglj 2015年9月19日
 * 
 */
public class EhcacheCache implements org.springframework.cache.Cache {
	private final static Logger logger = LoggerFactory.getLogger(EhcacheCache.class);
	private final Ehcache cache;
	/** 默认使用fastjson做为序列化与反序列化工具 */
	private IEhcacheSerializer ehcacheSerializer = new EhcacheFastjsonSerializer();

	/**
	 * Create an {@link EhCacheCache} instance.
	 * 
	 * @param ehcache
	 *            backing Ehcache instance
	 */
	public EhcacheCache(Ehcache ehcache) {
		Assert.notNull(ehcache, "Ehcache must not be null");
		Status status = ehcache.getStatus();
		Assert.isTrue(Status.STATUS_ALIVE.equals(status), "An 'alive' Ehcache is required - current cache is " + status.toString());
		this.cache = ehcache;
	}

	@Override
	public final String getName() {
		return this.cache.getName();
	}

	@Override
	public final Ehcache getNativeCache() {
		return this.cache;
	}

	@Override
	public ValueWrapper get(Object key) {
		String cacheKey = getCacheKey(key);
//		System.out.println(cacheKey);
		try {
			StopWatch sw = new StopWatch();
			sw.start();
			Element element = this.cache.get(cacheKey);
			sw.stop();
			if (sw.getTime() > 50) {
				logger.info("读取ehcache cache用时{}, key={}, cacheName={}", sw.getTime(), cacheKey, this.getName());
			}
			if (element != null) {
				String object = (String) element.getObjectValue();
				return new SimpleValueWrapper(this.ehcacheSerializer.deserialize(object));
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error("读取ehcache cache缓存发生异常, key={}, cacheName={}", cacheKey, this.getName(), e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		String cacheKey = getCacheKey(key);
//		System.out.println(cacheKey);
		Element element = this.cache.get(cacheKey);
		Object value = (element != null ? element.getObjectValue() : null);
		if (value != null && type != null && !type.isInstance(value)) {
			throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
		}
		String v = (String) value;
		if (v == null) {
			return null;
		} else {
			try {
				return (T) this.ehcacheSerializer.deserialize(v);
			} catch (Exception e) {
				logger.error("", e);
				return null;
			}
		}
	}

	@Override
	public void put(Object key, Object value) {
		String v = "";
		try {
			v = this.ehcacheSerializer.serialize(value);
			String cacheKey = getCacheKey(key);
			this.cache.put(new Element(cacheKey, v));
		} catch (Exception ex) {
			logger.error("", ex);
		}

	}

	@Override
	public void evict(Object key) {
		this.cache.remove(key);
	}

	@Override
	public void clear() {
		this.cache.removeAll();
	}

	/**
	 * 存入到缓存的key，由缓存的区域+key对象值串接而成
	 * 
	 * @param key
	 *            key对象
	 * @return
	 */
	private String getCacheKey(Object key) {
		return this.cache.getName() + key.toString();
	}

	/**
	 * get ehcacheSerializer
	 * 
	 * @return
	 * @auth nibili 2015年9月23日
	 */
	public IEhcacheSerializer getEhcacheSerializer() {
		return ehcacheSerializer;
	}

	/**
	 * set ehcacheSerializer
	 * 
	 * @param ehcacheSerializer
	 * @auth nibili 2015年9月23日
	 */
	public void setEhcacheSerializer(IEhcacheSerializer ehcacheSerializer) {
		this.ehcacheSerializer = ehcacheSerializer;
	}
}
