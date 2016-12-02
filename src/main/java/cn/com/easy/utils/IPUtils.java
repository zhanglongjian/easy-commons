package cn.com.easy.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

/**
 * ip工具类
 * 
 * @author zhanglj 2016年2月25日
 * 
 */
public class IPUtils {

	/** 查找ip所在城市：新浪 */
	private static String IP_CITY_URL_FIRST = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=";

	/** 查找ip所在城市：淘宝 */
	private static String IP_CITY_URL_SECOND = "http://ip.taobao.com/service/getIpInfo.php?ip=";

	public static void main(String[] args) throws Exception {
		String result = IPUtils.getCityNameByIP("58.211.8.108", 30000, 30000);
		if (StringUtils.isNotBlank(result) == true) {
			System.out.println(result);
		} else {
			System.out.println("无法识别");
		}
	}

	/**
	 * 根据ip获取所属城市名称
	 * 
	 * @param ipString
	 * @return
	 * @author zhanglj 2016年2月25日
	 * @throws Exception
	 */
	public static String getCityNameByIP(String ipString, int connectTimeOut, int readTimeOut) throws Exception {
		String cityName = "";
		String temp = "";
		// 新浪
		temp = IPUtils.doGet(IP_CITY_URL_FIRST + ipString, connectTimeOut, readTimeOut);
		if (StringUtils.isNotBlank(temp) == true) {
			AreaDTO areaDTO = FastJSONUtils.toObject(temp, AreaDTO.class);
			if (areaDTO != null) {
				String city = areaDTO.getCity();
				if (StringUtils.isNotBlank(city) == true && StringUtils.endsWithAny(city, "市") == false) {
					cityName = city + "市";
				}
			}
		}
		if (StringUtils.isBlank(cityName) == true) {
			// 淘宝
			temp = IPUtils.doGet(IP_CITY_URL_SECOND + ipString, connectTimeOut, readTimeOut);
			if (StringUtils.isNotBlank(temp) == true) {
				AreaResultDTO areaResultDTO = FastJSONUtils.toObject(temp, AreaResultDTO.class);
				AreaDTO areaDTO = areaResultDTO.getData();
				if (areaDTO != null) {
					return areaDTO.getCity();
				}
			}
		}
		return cityName;
	}

	/**
	 * 淘宝接口返回结果数据
	 * 
	 * @author zhanglj 2016年5月6日
	 * 
	 */
	public static class AreaResultDTO {
		private AreaDTO data;

		/**
		 * get data
		 * 
		 * @return
		 * @author zhanglj 2016年5月6日
		 */
		public AreaDTO getData() {
			return data;
		}

		/**
		 * set data
		 * 
		 * @param data
		 * @author zhanglj 2016年5月6日
		 */
		public void setData(AreaDTO data) {
			this.data = data;
		}
	}

	/** 区域dto */
	public static class AreaDTO {
		/** 国家 */
		private String country;
		/** 省 */
		private String province;
		/** 市 */
		private String city;

		/**
		 * get country
		 * 
		 * @return
		 * @author zhanglj 2016年5月6日
		 */
		public String getCountry() {
			return country;
		}

		/**
		 * set country
		 * 
		 * @param country
		 * @author zhanglj 2016年5月6日
		 */
		public void setCountry(String country) {
			this.country = country;
		}

		/**
		 * get province
		 * 
		 * @return
		 * @author zhanglj 2016年5月6日
		 */
		public String getProvince() {
			return province;
		}

		/**
		 * set province
		 * 
		 * @param province
		 * @author zhanglj 2016年5月6日
		 */
		public void setProvince(String province) {
			this.province = province;
		}

		/**
		 * get city
		 * 
		 * @return
		 * @author zhanglj 2016年5月6日
		 */
		public String getCity() {
			return city;
		}

		/**
		 * set city
		 * 
		 * @param city
		 * @author zhanglj 2016年5月6日
		 */
		public void setCity(String city) {
			this.city = city;
		}

	}

	/**
	 * 获取客户端真实ip
	 * 
	 * @param request
	 * @return
	 * @author zhanglj 2016年2月25日
	 */
	public static String getRealIP(HttpServletRequest request) {
		String ip = request.getHeader("X-Real-IP");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	// /**
	// * unicode 转换成 中文
	// *
	// * @author fanhui 2007-3-15
	// * @param theString
	// * @return
	// */
	// private static String decodeUnicode(String theString) {
	// char aChar;
	// int len = theString.length();
	// StringBuffer outBuffer = new StringBuffer(len);
	// for (int x = 0; x < len;) {
	// aChar = theString.charAt(x++);
	// if (aChar == '\\') {
	// aChar = theString.charAt(x++);
	// if (aChar == 'u') {
	// int value = 0;
	// for (int i = 0; i < 4; i++) {
	// aChar = theString.charAt(x++);
	// switch (aChar) {
	// case '0':
	// case '1':
	// case '2':
	// case '3':
	// case '4':
	// case '5':
	// case '6':
	// case '7':
	// case '8':
	// case '9':
	// value = (value << 4) + aChar - '0';
	// break;
	// case 'a':
	// case 'b':
	// case 'c':
	// case 'd':
	// case 'e':
	// case 'f':
	// value = (value << 4) + 10 + aChar - 'a';
	// break;
	// case 'A':
	// case 'B':
	// case 'C':
	// case 'D':
	// case 'E':
	// case 'F':
	// value = (value << 4) + 10 + aChar - 'A';
	// break;
	// default:
	// throw new IllegalArgumentException("Malformed      encoding.");
	// }
	// }
	// outBuffer.append((char) value);
	// } else {
	// if (aChar == 't') {
	// aChar = '\t';
	// } else if (aChar == 'r') {
	// aChar = '\r';
	// } else if (aChar == 'n') {
	// aChar = '\n';
	// } else if (aChar == 'f') {
	// aChar = '\f';
	// }
	// outBuffer.append(aChar);
	// }
	// } else {
	// outBuffer.append(aChar);
	// }
	// }
	// return outBuffer.toString();
	// }

	/**
	 * do get
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	private static String doGet(String url, int connectTimeOut, int readTimeOut) {

		String result = "";
		try {
			BufferedReader in = null;
			try {
				URL realUrl = new URL(url);
				// open connection
				URLConnection connection = realUrl.openConnection();
				connection.setConnectTimeout(connectTimeOut);
				connection.setReadTimeout(readTimeOut);
				// connect
				connection.connect();
				// define BufferedReader to read input content
				in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String line;
				while ((line = in.readLine()) != null) {
					result += line;
				}
			} finally {
				if (in != null) {
					in.close();
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}
}
