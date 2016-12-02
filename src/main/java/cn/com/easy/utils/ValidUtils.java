/** 
 * @autor linwk 2015年12月28日
 * 
 */
package cn.com.easy.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 日常检验工具类
 * 
 * @author zhanglj 2016年9月1日
 * 
 */
public class ValidUtils {
	/**
	 * 验证输入手机号码
	 * 
	 * @param 待验证的字符串
	 * @return 如果是符合格式的字符串,返回 <b>true </b>,否则为 <b>false </b>
	 * @author zhanglj 2016年9月1日
	 */
	public static boolean IsMobileNumber(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		} else {
			String regex = "^[1]+[3,4,5,7,8]+\\d{9}$";
			return match(regex, str);
		}
	}

	/**
	 * 
	 * 是否匹配正则
	 * 
	 * @param regex
	 *            正则表达式字符串
	 * @param str
	 *            要匹配的字符串
	 * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
	 * @author zhanglj 2016年9月1日
	 */
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 邮箱验证
	 * 
	 * @param email
	 * @return
	 * @author zhanglj 2016年9月1日
	 */
	public static boolean IsEmail(String email) {
		if (StringUtils.isBlank(email)) {
			return false;
		} else {
			String str = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
			Pattern p = Pattern.compile(str);
			Matcher m = p.matcher(email);
			return m.matches();
		}
	}

	/**
	 * 检验密码是否符合要求，最码最少6个，最多32位
	 * 
	 * @param password
	 * @return
	 * @author zhanglj 2016年9月1日
	 */
	public static boolean IsPassword(String password) {
		if (StringUtils.isBlank(password)) {
			return false;
		} else {
			if (StringUtils.length(password) > 32 || StringUtils.length(password) < 6) {
				return false;
			} else {
				return true;
			}
		}
	}
}
