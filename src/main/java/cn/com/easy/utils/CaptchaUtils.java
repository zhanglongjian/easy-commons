/**
 * 
 */
package cn.com.easy.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import com.github.cage.Cage;
import com.github.cage.GCage;

/**
 * 用于记录会话的工具体
 * 
 * @author zhanglj 2015年8月28日下午2:40:24
 * 
 */
public class CaptchaUtils {

	/** 验证码session key */
	private static final String CAPTCHA_TAG = "CAPTCHA_TAG";
	/** 生成验证码的类 */
	private static final Cage cage = new GCage();

	/**
	 * 发送给客户端验证码
	 * 
	 * @param response
	 * @throws Exception
	 * @auth zhanglj 2015年8月28日 下午4:05:29
	 */
	public static void sendCaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String token = RandomStringUtils.random(4,
		// "abcdefghijklmnopqrstuvwxyz1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		String token = RandomStringUtils.random(4, "1234567890");
		request.getSession().setAttribute(CAPTCHA_TAG, token.toLowerCase());
		cage.draw(token, response.getOutputStream());
	}

	/**
	 * 验正验证码
	 * 
	 * @param clientString
	 * @return
	 * @auth zhanglj 2015年8月28日 下午4:07:46
	 */
	public static boolean validCaptcha(HttpServletRequest request, String clientString) {
		if (StringUtils.isBlank(clientString) == true) {
			return false;
		}
		String inerCaptcha = (String) request.getSession().getAttribute(CAPTCHA_TAG);
		if (StringUtils.equals(inerCaptcha, clientString.toLowerCase()) == false) {
			// 不匹配
			return false;
		} else {
			request.getSession().setAttribute(CAPTCHA_TAG, null);
			return true;
		}
	}

}
