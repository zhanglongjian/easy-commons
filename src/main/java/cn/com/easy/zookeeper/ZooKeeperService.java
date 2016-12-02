package cn.com.easy.zookeeper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * zookeeper服务类，用于创建操作zookeeper的对象
 * 
 * @author zhanglj 2015年5月7日
 * 
 */
public class ZooKeeperService {

	private Logger logger = LoggerFactory.getLogger(ZooKeeperService.class);
	public static final int MAX_RETRIES = 5;
	public static final int BASE_SLEEP_TIMEMS = 3000;
	/** zookeeper服务器列表 */
	private String zookeeperServers = "";
	/** zookeeper客户端操纵对象 */
	private CuratorFramework client;
	/** 监听器集合(一键多值数据结构) */
	private Multimap<IZookeeperWatch, Object> watchesMap = ArrayListMultimap.create();

	public ZooKeeperService(String zookeeperServers) {
		this.zookeeperServers = zookeeperServers;
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIMEMS, MAX_RETRIES);
		this.client = CuratorFrameworkFactory.builder().connectString(this.zookeeperServers).retryPolicy(retryPolicy).build();
		client.start();
	}

	/**
	 * 使用分布式锁执行任务
	 * 
	 * @param path
	 * @param getLockTimeout
	 *            获取锁超时时间（单位ms)
	 * @param task
	 * @throws Exception
	 * @auth zhanglj 2015年5月8日
	 */
	public void distributeLock(String path, int getLockTimeout, Runnable task) throws Exception {
		InterProcessMutex lock = new InterProcessMutex(client, path);
		try {
			logger.debug("尝试获取锁。。。");
			if (lock.acquire(getLockTimeout, TimeUnit.MILLISECONDS)) {
				try {
					logger.debug("获得锁，开始执行任务。。。");
					task.run();
				} finally {
					lock.release();
					logger.debug("释放锁,path:" + path);
				}
			} else {
				logger.error("任务执行失败，在时间：" + getLockTimeout + "ms内，未获得分布式锁!");
				throw new Exception("任务执行失败，在时间：" + getLockTimeout + "ms内，未获得分布式锁!");
			}
		} catch (Exception e) {
			logger.error("执行分布式锁任务异常。", e);
			throw new Exception("执行分布式锁任务异常。");
		}

	}

	/**
	 * 取消监听，
	 * 
	 * @param zookeeperWatch
	 *            注册监听时的对象
	 * @auth zhanglj 2015年5月8日
	 */
	public void removeNodeWatch(IZookeeperWatch zookeeperWatch) {
		if (zookeeperWatch == null) {
			logger.info("称除节点监听，监听器对象不能为空!");
			return;
		}
		Collection<Object> values = watchesMap.get(zookeeperWatch);
		if (CollectionUtils.isNotEmpty(values) == true) {
			// 移除监听器
			NodeCache cache = null;
			NodeCacheListener nodeCacheListener = null;
			Iterator<Object> it = values.iterator();
			for (int i = 0; it.hasNext() && i < 2; i++) {
				if (i == 0) {
					cache = (NodeCache) it.next();
				} else if (i == 1) {
					nodeCacheListener = (NodeCacheListener) it.next();
				} else {
					break;
				}
			}
			if (cache != null && nodeCacheListener != null) {
				cache.getListenable().removeListener(nodeCacheListener);
			}

		} else {
			logger.info("没有找到对应的监听器！");
			return;
		}
	}

	/**
	 * 监听节点变化
	 * 
	 * @param zookeeperWatch
	 * @throws Exception
	 * @auth zhanglj 2015年5月8日
	 */
	public void addNodeWatch(final IZookeeperWatch zookeeperWatch) throws Exception {
		// 是否是每一次触发
		final AtomicBoolean isFirst = new AtomicBoolean(true);
		final NodeCache cache = new NodeCache(this.client, zookeeperWatch.getWatchPath());
		cache.start();
		NodeCacheListener nodeCacheListener = new NodeCacheListener() {

			@Override
			public void nodeChanged() throws Exception {
				// 节点数据
				String data = new String(cache.getCurrentData().getData(), "UTF-8");
				if (isFirst.get() == true) {
					isFirst.set(false);
					logger.debug("NodeCache loaded, data is: " + data);
					zookeeperWatch.handLoad(data);
				} else {
					logger.debug("NodeCache changed, data is: " + data);
					zookeeperWatch.handChange(data);
				}

			}
		};
		cache.getListenable().addListener(nodeCacheListener);
		watchesMap.put(zookeeperWatch, cache);
		watchesMap.put(zookeeperWatch, nodeCacheListener);
	}

	/**
	 * 断开连接
	 * 
	 * @auth zhanglj 2015年5月7日
	 */
	public void close() {
		client.close();
	}

	/**
	 * 获取zookeeper操纵对象
	 * 
	 * @param servers
	 * @return
	 * @auth zhanglj 2015年5月7日
	 */
	public CuratorFramework getClient() {
		return client;
	}

	/**
	 * 获取服务器地址
	 * 
	 * @return
	 * @throws Exception
	 * @auth zhanglj 2015年5月7日
	 */
	public String getServers() {
		return this.zookeeperServers;
	}

	/**
	 * 删除节点
	 * 
	 * @param path
	 * @throws Exception
	 * @auth zhanglj 2015年5月8日
	 */
	public void deletePath(String path) throws Exception {
		try {
			logger.debug("删除节点,path:" + path);
			this.client.delete().guaranteed().deletingChildrenIfNeeded().forPath(path);
		} catch (Exception e) {
			logger.error("删除zookeeper节点异常，path:" + path, e);
			throw new Exception("删除zookeeper节点异常，path:" + path, e);
		}
	}

	/**
	 * 创建 节点
	 * 
	 * @param path
	 * @param data
	 * @throws Exception
	 * @auth zhanglj 2015年5月8日
	 */
	public void createPath(String path, String data) throws Exception {
		try {
			// this.client.create().forPath(path, data.getBytes());
			client.newNamespaceAwareEnsurePath(path).ensure(client.getZookeeperClient());
			client.setData().forPath(path, data.getBytes());
		} catch (Exception ex) {
			logger.error("创建节点异常,path:" + path + " , data:" + data, ex);
			throw new Exception("创建节点异常,path:" + path + " , data:" + data, ex);
		}
	}

	/**
	 * 设置节点值
	 * 
	 * @param path
	 * @param data
	 * @throws Exception
	 * @auth zhanglj 2015年5月8日
	 */
	public void updatePathValue(String path, String data) throws Exception {
		try {
			logger.debug("设置结点值,path:" + path + "，data:" + data);
			this.client.setData().forPath(path, data.getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error("设置zookeeper节点值异常，path:" + path + "，data" + data, e);
			throw new Exception("设置zookeeper节点值异常，path:" + path + "，data" + data, e);
		}
	}

	/**
	 * 获取节点值
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @auth zhanglj 2015年5月7日
	 */
	public String getPathValue(String path) throws Exception {
		if (!this.exists(path)) {
			throw new RuntimeException("节点 " + path + " 不存在!.");
		}
		return new String(client.getData().forPath(path), "UTF-8");
	}

	/**
	 * 是否存在节点
	 * 
	 * @param path
	 * @return
	 * @author zhanglj 2016年1月7日
	 * @throws Exception
	 */
	public boolean exists(String path) throws Exception {
		Stat stat = this.client.checkExists().forPath(path);
		return !(stat == null);
	}

	/**
	 * 获取子节点
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 * @auth zhanglj 2015年5月7日
	 */
	public List<String> getSubPaths(String path) throws Exception {
		return client.getChildren().forPath(path);
	}

}