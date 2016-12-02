package cn.com.easy.cache;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.KeyGenerator;

/**
 * 实现新的key generator
 * 
 * @author zhang lj 2015年10月21日
 * 
 */
public class StringKeyGenerator implements KeyGenerator {

	/**
	 * 最大字符串长度
	 */
	private static final int MAX_KEY_SIZE = 200;

	@Override
	public Object generate(Object target, Method method, Object... params) {
		StringBuilder sb = new StringBuilder();
		// 增加方法名作为key，防止冲突
		sb.append(method.getName());
		for (Object o : params) {
			sb.append(o.toString());
			sb.append('.');
		}
		String newKey = sb.toString().replaceAll("\\p{Cntrl}]|\\p{Space}", "_");
		if (newKey.getBytes().length >= MAX_KEY_SIZE) {
			return newKey.substring(20, 60) + this.betterHashcode(newKey);
		} else {
			return newKey;
		}
	}

	public String betterHashcode(String str) {
		long h = 0;
		int len = str.length();
		for (int i = 0; i < len; i++) {
			h = 257 * h + str.charAt(i);
		}
		return String.valueOf(h);
	}

}
