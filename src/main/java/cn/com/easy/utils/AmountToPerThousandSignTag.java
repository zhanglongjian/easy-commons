package cn.com.easy.utils;

import java.math.BigDecimal;

/**
 * 用于前端 金额 输出用千分号格式式的，自定义JSTL
 * 
 * @author zhanglj 2014-12-04
 * 
 */
public class AmountToPerThousandSignTag {

	/**
	 * 千分号格式化金额输出
	 * 
	 * @param amount
	 *            金额
	 * @return
	 */
	public static String toPerThousandSign(BigDecimal amount) {
		if (amount != null) {
			return BigDecimalFormatUtils.formatWithComma(amount);
		} else {
			return "0.00";
		}
	}

}
