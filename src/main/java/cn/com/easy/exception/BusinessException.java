package cn.com.easy.exception;

/**
 * 自定义异常处理基类
 * 
 * @author zhanglj 2016年5月4日
 * 
 */
public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -832356732171720583L;

	public BusinessException() {
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}