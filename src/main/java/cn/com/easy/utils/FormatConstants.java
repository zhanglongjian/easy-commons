package cn.com.easy.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * The Class FormatConstants.
 * 
 * @author
 */
public abstract class FormatConstants {

	/** The Constant DATE_FORMAT. */
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", java.util.Locale.CHINA);
	
	public static final DateFormat DATE_FORMAT2 = new SimpleDateFormat("yyyyMMdd", java.util.Locale.CHINA);

	/** The Constant TIME_FORMAT. */
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss", java.util.Locale.CHINA);

	/** The Constant DATE_TIME_FORMAT. */
	public static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			java.util.Locale.CHINA);

	/** The Constant DATE_TIME_FORMAT_IMAGE. */
	public static final DateFormat DATE_TIME_FORMAT_IMAGE = new SimpleDateFormat("yyyyMMddHHmmss",
			java.util.Locale.CHINA);
}
