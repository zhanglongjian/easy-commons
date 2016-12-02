/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.com.easy.cache.ehcache;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

/**
 * memcache 缓存管理类
 * 
 * @author zhanglj 2015年4月17日
 * 
 */
public class EhcacheCacheManager extends AbstractTransactionSupportingCacheManager {

	private Logger logger = LoggerFactory.getLogger(EhcacheCacheManager.class);
	/** ehcache命名空间的xml文件名 */
	private String ehcacheFileName;
	/** */
	private net.sf.ehcache.CacheManager cacheManager;

	public EhcacheCacheManager() {
		invokeOnClose();
	}

	@Override
	protected Collection<Cache> loadCaches() {
		net.sf.ehcache.CacheManager cacheManager = getCacheManager();
		Assert.notNull(cacheManager, "A backing EhCache CacheManager is required");
		Status status = cacheManager.getStatus();
		Assert.isTrue(Status.STATUS_ALIVE.equals(status), "An 'alive' EhCache CacheManager is required - current cache is " + status.toString());

		String[] names = cacheManager.getCacheNames();
		Collection<Cache> caches = new LinkedHashSet<Cache>(names.length);
		for (String name : names) {
			caches.add(new EhcacheCache(cacheManager.getEhcache(name)));
		}
		return caches;
	}

	@Override
	public Cache getCache(String name) {
		Cache cache = super.getCache(name);
		if (cache == null) {
			// Check the EhCache cache again (in case the cache was added at
			// runtime)
			Ehcache ehcache = getCacheManager().getEhcache(name);
			if (ehcache != null) {
				addCache(new EhcacheCache(ehcache));
				cache = super.getCache(name); // potentially decorated
			}
		}
		return cache;
	}

	/**
	 * 注册到应用退出时，会把Ehcache持 久化到磁盘
	 * 
	 * @auth nibili 2015年10月22日
	 */
	private void invokeOnClose() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {

				List<CacheManager> knownCacheManagers = CacheManager.ALL_CACHE_MANAGERS;

				while (!knownCacheManagers.isEmpty()) {
					// 关闭的时候 刷到磁盘
					((CacheManager) CacheManager.ALL_CACHE_MANAGERS.get(0)).shutdown();
				}

			}
		});
	}

	/**
	 * 获取cacheManager
	 * 
	 * @return
	 * @throws Exception
	 * @auth nibili 2015年10月22日
	 */
	private net.sf.ehcache.CacheManager getCacheManager() {
		if (this.cacheManager == null) {
			try {
				Resource configLocation = new ClassPathResource(this.ehcacheFileName);
				InputStream is = (configLocation != null ? configLocation.getInputStream() : null);
				Configuration configuration = (is != null ? ConfigurationFactory.parseConfiguration(is) : ConfigurationFactory.parseConfiguration());
				this.cacheManager = CacheManager.create(configuration);
			} catch (IOException e) {
				logger.error("", e);
			}
		}
		return this.cacheManager;
	}

	/**
	 * get ehcache命名空间的xml文件名
	 * 
	 * @return
	 * @auth nibili 2015年10月22日
	 */
	public String getEhcacheFileName() {
		return ehcacheFileName;
	}

	/**
	 * set ehcache命名空间的xml文件名
	 * 
	 * @param ehcacheFileName
	 * @auth nibili 2015年10月22日
	 */
	public void setEhcacheFileName(String ehcacheFileName) {
		this.ehcacheFileName = ehcacheFileName;
	}

}
