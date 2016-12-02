package cn.com.easy.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.MapUtils;

/**
 * java sdk 原生实现的http请求工具类
 * 
 * @author zhanglj 2015-02-02
 * 
 */
public class HttpUtils {

	/** the connection connect time out in millionseconds */
	private static final int CONNECT_TIME_OUT = 60000;
	/** the connection read time out in millionseconds */
	private static final int READ_TIME_OUT = 60000;

	public static void main(String[] args) throws Exception {

		// Map<String, String> paramMap = Maps.newHashMap();
		// // paramMap.put("a", "我");
		// // paramMap.put("b", "b");
		// // paramMap.put("c", "b");
		// // paramMap.put("d", "b");
		//
		// System.out.println(HttpUtils.doGet("http://120.55.192.163?d=b",
		// paramMap));
		// // System.out.println(HttpUtils.doPost("http://www.163.com", ""));
		System.out.println(doGet());

	}

	/**
	 * Get Request
	 * 
	 * @return
	 * @throws Exception
	 */
	public static String doGet() throws Exception {
		URL localURL = new URL("http://120.55.192.163?v=1");
		URLConnection connection = localURL.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

		httpURLConnection.setRequestProperty("Accept-Charset", "utf-8");
		httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader reader = null;
		StringBuffer resultBuffer = new StringBuffer();
		String tempLine = null;

		if (httpURLConnection.getResponseCode() >= 300) {
			throw new Exception("HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
		}

		try {
			inputStream = httpURLConnection.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream);
			reader = new BufferedReader(inputStreamReader);

			while ((tempLine = reader.readLine()) != null) {
				resultBuffer.append(tempLine);
			}

		} finally {

			if (reader != null) {
				reader.close();
			}

			if (inputStreamReader != null) {
				inputStreamReader.close();
			}

			if (inputStream != null) {
				inputStream.close();
			}

		}

		return resultBuffer.toString();
	}

	/**
	 * do get
	 * 
	 * @param url
	 * @param paramMap
	 *            参数map
	 * @return
	 * @author zhanglj 2016年3月3日
	 * @throws Exception
	 */
	public static String doGet(String url, Map<String, String> paramMap) throws Exception {
		return doGet(toRequestUrl(url, paramMap));
	}

	/**
	 * do get
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public static String doGet(String url) throws Exception {
		String result = "";
		BufferedReader in = null;
		try {
			URL realUrl = new URL(url.trim());
			// open connection
			URLConnection connection = realUrl.openConnection();
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			connection.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
			connection.setReadTimeout(READ_TIME_OUT);
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
		return result;
	}

	/**
	 * do post
	 * 
	 * @param url
	 * @param paramMap
	 *            参数map
	 * @return
	 * @author zhanglj 2016年3月3日
	 * @throws Exception
	 * @throws UnsupportedEncodingException
	 */
	public static String doPost(String url, Map<String, String> paramMap) throws UnsupportedEncodingException, Exception {
		return doPost(url, toNetParam(paramMap));
	}

	/**
	 * do post
	 * 
	 * @param url
	 * @param param
	 *            name1=value1&name2=value2
	 * @return
	 * @throws Exception
	 */
	public static String doPost(String url, String param) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
			connection.setConnectTimeout(CONNECT_TIME_OUT);
			connection.setReadTimeout(READ_TIME_OUT);
			// do post
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			// get URLConnection output stream
			out = new PrintWriter(connection.getOutputStream());
			// send param
			out.print(param);
			out.flush();
			// define BufferedReader input stream
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * get input stream
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @auth zhanglj 2015-3-20
	 */
	public static InputStream deGetFileInputSteam(String url) throws Exception {
		URL uploadUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) uploadUrl.openConnection();
		connection.setConnectTimeout(CONNECT_TIME_OUT);
		connection.setReadTimeout(READ_TIME_OUT);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestMethod("GET");
		return connection.getInputStream();
	}

	/**
	 * upload file
	 * 
	 * @param url
	 * @param fileInputStream
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static String uploadFile(String url, InputStream fileInputStream, String fileName) throws Exception {
		String BOUNDARY = "---------7d4a6d158c9"; // 定义数据分隔线
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "Keep-Alive");
		conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		conn.connect();
		OutputStream out = new DataOutputStream(conn.getOutputStream());
		byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();// 定义最后数据分隔线
		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + java.net.URLEncoder.encode(fileName, "UTF-8") + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");
		byte[] data = sb.toString().getBytes();
		out.write(data);
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = fileInputStream.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
		fileInputStream.close();
		out.write(end_data);
		out.flush();
		out.close();
		// 定义BufferedReader输入流来读取URL的响应
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		// 读取URLConnection的响应
		String result = "";
		String line;
		while ((line = reader.readLine()) != null) {
			result += line;
		}
		return result;
	}

	/**
	 * 把map转成：a=b&c=d&e=f
	 * 
	 * @param paramMap
	 * @return
	 * @author zhanglj 2016年3月3日
	 * @throws UnsupportedEncodingException
	 */
	public static String toNetParam(Map<String, String> paramMap) throws UnsupportedEncodingException {
		if (MapUtils.isNotEmpty(paramMap) == true) {

			StringBuffer stringBuffer = new StringBuffer();
			int i = 0;
			for (Entry<String, String> entry : paramMap.entrySet()) {
				if (i == 0) {
					stringBuffer.append(entry.getKey() + "=" + java.net.URLEncoder.encode(entry.getValue(), "UTF-8"));
				} else {
					stringBuffer.append("&" + entry.getKey() + "=" + java.net.URLEncoder.encode(entry.getValue(), "UTF-8"));
				}
				i++;
			}
			return stringBuffer.toString();
		} else {
			return "";
		}
	}

	/**
	 * url 和 参数map转成最终的请求url链接
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 * @author zhanglj 2016年3月3日
	 * @throws UnsupportedEncodingException
	 */
	private static String toRequestUrl(String url, Map<String, String> paramMap) throws UnsupportedEncodingException {
		if (MapUtils.isNotEmpty(paramMap) == true) {
			if (url.indexOf("?") > 0) {
				// 已有参数
				return url + "&" + toNetParam(paramMap);
			} else {
				return url + "?" + toNetParam(paramMap);
			}
		} else {
			return url;
		}
	}
}
