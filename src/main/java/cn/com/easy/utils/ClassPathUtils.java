package cn.com.easy.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取 class path 路径等的工具类
 * 
 * @author zhanglj 2015年5月12日
 * 
 */
public class ClassPathUtils {

	private static Logger logger = LoggerFactory.getLogger(ClassPathUtils.class);

	/**
	 * 获取class loader的根路径<br>
	 * 会对路径进行一次decode，防止中文路径导致的异常
	 * 
	 * @return
	 * @auth zhanglj 2015年5月12日
	 */
	public static String getClassesLoadRootPath() {
		String path = ClassPathUtils.class.getResource("/").getFile();
		if (logger.isDebugEnabled() == true) {
			path = path.replaceAll("test-classes", "classes");
		}
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");
			// windows的路径开头会有 一个 '/' 字符，要去掉
			if (SystemUtils.IS_OS_WINDOWS == true && path.startsWith("/") == true) {
				path = path.substring(1);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("路径进行URL decode异常!");
		}
		return path;
	}

	/**
	 * 获取 test class loader的根路径<br>
	 * 会对路径进行一次decode，防止中文路径导致的异常
	 * 
	 * @return
	 * @auth zhanglj 2015年5月12日
	 */
	public static String getTestClassesLoadRootPath() {
		String path = ClassPathUtils.class.getResource("/").getFile();
		if (logger.isDebugEnabled() == true) {
			path = path.replaceAll("classes", "test-classes");
		}
		try {
			path = java.net.URLDecoder.decode(path, "UTF-8");
			// windows的路径开头会有 一个 '/' 字符，要去掉
			if (SystemUtils.IS_OS_WINDOWS == true && path.startsWith("/") == true) {
				path = path.substring(1);
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("路径进行URL decode异常!");
		}
		return path;
	}

}
