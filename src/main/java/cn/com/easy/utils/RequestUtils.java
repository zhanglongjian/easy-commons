package cn.com.easy.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * request utils
 * 
 * @author zhanglj 2015-02-03
 */
public class RequestUtils {

	/** the user key in session */
	public static String USER_TAG = "user";
	/** 用户权限tag */
	public static String USER_PERMISSION_TAG = "permission";

	/**
	 * get current user
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentUser(HttpServletRequest request) {
		return (T) request.getSession().getAttribute(USER_TAG);
	}

	/**
	 * set current user
	 * 
	 * @auth zhanglj 2015-2-3
	 */
	public static void setCurrentUser(HttpServletRequest request, Object object) {
		request.getSession().setAttribute(USER_TAG, object);
	}

	/**
	 * 获取权限
	 * 
	 * @param request
	 * @return
	 * @auth zhanglj 2015年5月12日
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getUserPermission(HttpServletRequest request) {
		return (T) request.getSession().getAttribute(USER_PERMISSION_TAG);
	}

	/**
	 * 设置权限
	 * 
	 * @param request
	 * @param object
	 * @auth zhanglj 2015年5月12日
	 */
	public static void setUserPermission(HttpServletRequest request, Object object) {
		request.getSession().setAttribute(USER_PERMISSION_TAG, object);
	}

	/** session中微信授权用户实体对应的key */
	public static String USER_WEIXIN_TAG = "user_weixin";

	/**
	 * 获取当前授权的微信用户
	 * 
	 * @param request
	 * @return
	 * @auth zhanglj 2015年1月21日 下午9:53:25
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getCurrentWeixinUser(HttpServletRequest request) {
		return (T) request.getSession().getAttribute(USER_WEIXIN_TAG);
	}

	/**
	 * 设置当前授权的微信用户
	 * 
	 * @param request
	 * @return
	 * @auth zhanglj 2015年1月21日 下午9:53:25
	 */
	public static void setCurrentWeixinUser(HttpServletRequest request, Object object) {
		request.getSession().setAttribute(USER_WEIXIN_TAG, object);
	}
}
