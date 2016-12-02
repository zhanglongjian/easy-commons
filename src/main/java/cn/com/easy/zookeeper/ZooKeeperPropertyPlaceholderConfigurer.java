package cn.com.easy.zookeeper;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.google.common.collect.Maps;

/**
 * zookeeper配置<br>
 * 继承spring加载上下文属性文件的类<br>
 * 如果properties中的属性名与zookeeper中的一样，那么参数值将会被zookeeper上的值覆盖。<br>
 * properties文件配置两个参数:<br>
 * zk.servers=192.168.1.156:2181,192.168.1.120:2181 <br>
 * #zk.config.root.path defaut value id "/cn/com/easy/config",u could delete the
 * set<br>
 * #可选，默认为/cn/com/easy/config<br>
 * zk.config.root.path=/cn/com/easy/config<br>
 * 
 * 
 * @author zhanglj 2015年5月7日
 * 
 */
public class ZooKeeperPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private Logger logger = LoggerFactory.getLogger(ZooKeeperPropertyPlaceholderConfigurer.class);

	/** zookeeper服务器地址 的properties参数名,在properties文件中设置 */
	private final String ZOOKEEPER_SERVERS_PRO = "zk.servers";

	/** 根节点路径 */
	private String rootPath = "";

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

		try {
			// zookeeper服务器
			String zookeeperServers = props.getProperty(ZOOKEEPER_SERVERS_PRO);
			// 配置的根节点
			String configRootPath = this.getRootPath();
			Map<String, String> customProperties = this.getConfigurationInZookeeper(zookeeperServers, configRootPath);
			props.putAll(customProperties);
			logger.debug(props.toString());
			super.processProperties(beanFactoryToProcess, props);
		} catch (Exception e) {
			logger.error("从Zookeeper获取配置异常!" + e.getMessage(), e);
		}
	}

	/**
	 * 获取zookeeper中的配置数据
	 * 
	 * @param zookeeperServers
	 * @param configRootPath
	 * @return
	 * @throws Exception
	 * @auth zhanglj 2015年5月7日
	 */
	private Map<String, String> getConfigurationInZookeeper(String zookeeperServers, String configRootPath) throws Exception {
		// 服务器地址不能为空
		if (StringUtils.isBlank(zookeeperServers) == true) {
			throw new Exception("Zookeeper服务器地址不能为空！");
		}
		// 根节点不能为空
		if (StringUtils.isBlank(configRootPath) == true) {
			throw new Exception("Zookeeper配置根节点不能为空！");
		}
		// 属性名，属性值对应的map
		Map<String, String> propertiesInZkMap = Maps.newHashMap();
		//
		ZooKeeperService zooKeeperService = new ZooKeeperService(zookeeperServers);
		// 获取所有子节点
		List<String> paths = zooKeeperService.getSubPaths(configRootPath);
		// 遍历所有子节点，以及节点值
		if (CollectionUtils.isNotEmpty(paths) == true) {
			// 遍历有子节点
			for (String path : paths) {
				String data = zooKeeperService.getPathValue(configRootPath + "/" + path);
				if (StringUtils.isNotBlank(data) == true) {
					propertiesInZkMap.put(path, data);
				}
			}
		}
		zooKeeperService.close();
		return propertiesInZkMap;
	}

	/**
	 * get 根节点路径
	 * 
	 * @return
	 * @author zhanglj 2016年1月10日
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * set 根节点路径
	 * 
	 * @param rootPath
	 * @author zhanglj 2016年1月10日
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

}
