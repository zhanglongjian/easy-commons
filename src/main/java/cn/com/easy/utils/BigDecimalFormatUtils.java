package cn.com.easy.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * bigdecimal的工具类
 * 
 * @author zhanglj
 * 
 */
public class BigDecimalFormatUtils {

	/**
	 * 格式化BigDecimall为字符串形式，还",",如 123,234.00,默认小数是四舍五入
	 * 
	 * @param bd
	 * @return
	 */
	public static String formatWithComma(BigDecimal bd) {
		if (bd == null) {
			return "";
		}
		if (bd.compareTo(new BigDecimal(1000)) >= 0) {
			DecimalFormat f = new DecimalFormat("0,000.00");
			return f.format(bd);
		} else {
			DecimalFormat f = new DecimalFormat("0.00");
			return f.format(bd);
		}
	}

	public static void main(String[] args) {
		System.out.println(BigDecimalFormatUtils.formatWithComma(new BigDecimal(567480957548.408)));
		System.out.println(BigDecimalFormatUtils.formatWithComma(new BigDecimal(48.058)));
	}
}
