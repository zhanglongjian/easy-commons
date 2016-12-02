package cn.com.easy.utils;

import java.io.UnsupportedEncodingException;

import org.apache.commons.lang3.StringUtils;

/**
 * 汉字转成区位码
 * 
 * @author zhanglj 2016年4月11日
 * 
 */
public class ChineseWordsToNumberCodeUtils {

	private static String bytes2HexString(byte b) {
		return bytes2HexString(new byte[] { b });
	}

	private static String bytes2HexString(byte[] b) {
		String ret = "";
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}
		return ret;
	}

	/**
	 * 
	 * @return
	 * @author zhanglj 2016年4月11日
	 * @throws UnsupportedEncodingException
	 */
	public static String wordsToGbkCode(String words) throws UnsupportedEncodingException {
		if (StringUtils.isNotBlank(words) == true) {
			byte[] bs = words.getBytes("GB2312");
			String s = "";
			for (int i = 0; i < bs.length; i++) {
				int a = Integer.parseInt(bytes2HexString(bs[i]), 16);
				s += (a - 0x80 - 0x20) + "";
			}
			return s;
		} else {
			return null;
		}
	}

	public static void main(String[] args) throws Exception {
		String str = "中华人民";
		System.out.println(wordsToGbkCode(str));
	}
}
