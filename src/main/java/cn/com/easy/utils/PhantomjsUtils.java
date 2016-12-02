package cn.com.easy.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过工具软件 phantomjs 获取js等动态加载网页内容的页面的内容，与浏览器所看到内容一致
 * 
 * @author zhanglj 2015年1月30日上午9:00:33
 * 
 */
public class PhantomjsUtils {

	private static Logger logger = LoggerFactory.getLogger(PhantomjsUtils.class);

	/**
	 * 获取动态页面内容
	 * 
	 * @auth zhanglj 2015年1月30日 上午9:46:46
	 */
	public static String getJsHtmlCotnent(String url) throws Exception {
		Runtime rt = Runtime.getRuntime();
		String command = PhantomjsUtils.getPhantomjsCommand(url);
		// System.out.println(command);
		logger.debug("command:" + command);
		// 执行外部程序
		Process p = rt.exec(command);
		InputStream is = p.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		StringBuffer sbf = new StringBuffer();
		String tmp = "";
		while ((tmp = br.readLine()) != null) {
			sbf.append(tmp);
		}
		logger.debug("url:" + url + "爬取返回内容:" + sbf.toString());
		return sbf.toString();
	}

	/**
	 * 获取phantom 命令行文本
	 * 
	 * @param url
	 * @throws IOException
	 * 
	 * @auth zhanglj 2015年1月30日 上午9:29:12
	 */
	private static String getPhantomjsCommand(String url) throws Exception {

		// phantomjs文件位置
		String phantomjs = ClassPathUtils.getClassesLoadRootPath();
		// 执行脚本
		String codesjs = "";
		// 操作系统
		if (SystemUtils.IS_OS_WINDOWS == true) {
			if (phantomjs.startsWith("/") == true) {
				phantomjs = phantomjs.substring(1);
			}
			codesjs = phantomjs + "phantomjs/phantomjs-1.9.8-windows/codes.js";
			phantomjs = phantomjs + "phantomjs/phantomjs-1.9.8-windows/phantomjs.exe";
			String command = phantomjs + " " + codesjs + " '" + url + "'";
			// System.out.println(command);
			return command;
		} else {
			//
			codesjs = phantomjs + "phantomjs/phantomjs-1.9.8-linux-x86_64/bin/codes.js";
			phantomjs = phantomjs + "phantomjs/phantomjs-1.9.8-linux-x86_64/bin/phantomjs";
			// 让文件有可执行的权限
			Runtime rt = Runtime.getRuntime();
			rt.exec("chmod +x " + phantomjs);
			String command = phantomjs + " " + codesjs + " " + url;

			// System.out.println(command);
			return command;
		}

	}

	public static void main(String[] args) throws Exception {
		String temp = PhantomjsUtils.getJsHtmlCotnent("http://top.taobao.com/index.php?spm=a1z5i.1.2.2.h20Jtj&topId=TR_FS&leafId=50010850");
		System.out.println(temp);
		// getPhantomjsCommand();
	}

}
