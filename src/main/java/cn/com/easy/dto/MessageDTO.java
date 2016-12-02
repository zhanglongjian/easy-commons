package cn.com.easy.dto;

/**
 * 通用消息实体
 * 
 * @author zhanglj 2015年1月11日下午1:38:48
 * 
 */
public class MessageDTO {

	/** 消息标题 */
	private String title;
	/** 是否执行成功 */
	private boolean isSuccess;
	/** 消息内容 */
	private Object message;

	/**
	 * 
	 * @param title
	 *            消息标题
	 * @param isSuccess
	 *            是否执行成功
	 * @param message
	 *            消息内容
	 * @return
	 * @auth zhanglj 2015年1月11日 下午1:38:56
	 */
	public static MessageDTO newInstance(String title, boolean isSuccess, Object message) {

		return new MessageDTO(title, isSuccess, message);
	}

	/**
	 * 
	 * @param title
	 * @param isSuccess
	 * @param message
	 */
	public MessageDTO(String title, boolean isSuccess, Object message) {

		this.title = title;
		this.isSuccess = isSuccess;
		this.message = message;
	}

	/**
	 * 获取 消息标题
	 * 
	 * @return
	 * @auth zhanglj 2015年1月11日 下午1:38:20
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置 消息标题
	 * 
	 * @param title
	 * @auth zhanglj 2015年1月11日 下午1:38:20
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取 是否执行成功
	 * 
	 * @return
	 * @auth zhanglj 2015年1月11日 下午1:38:20
	 */
	public boolean getIsSuccess() {
		return isSuccess;
	}

	/**
	 * 设置 是否执行成功
	 * 
	 * @param isSuccess
	 * @auth zhanglj 2015年1月11日 下午1:38:20
	 */
	public void setIsSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	/**
	 * 获取 消息内容
	 * 
	 * @return
	 * @auth zhanglj 2015年1月11日 下午1:38:20
	 */
	public Object getMessage() {
		return message;
	}

	/**
	 * 设置 消息内容
	 * 
	 * @param message
	 * @auth zhanglj 2015年1月11日 下午1:38:20
	 */
	public void setMessage(Object message) {
		this.message = message;
	}

}
