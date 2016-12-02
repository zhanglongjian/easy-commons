package cn.com.easy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.RequestAcceptEncoding;
import org.apache.http.client.protocol.ResponseContentEncoding;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.google.common.collect.Maps;

public class HttpClientUtils {

	private static final String[] USER_AGENTS = new String[] { "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.102 Safari/537.36",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/36.0.1985.125 Safari/537.36",
			"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.17 (KHTML, like Gecko) Chrome/24.0.1309.0 Safari/537.17",
			"Mozilla/5.0 (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0",
			"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.94 Safari/537.36 OPR/24.0.1558.53" };

	private static final HttpClient HTTP_CLIENT;

	private static final RequestConfig DEFAULT_REQUEST_CONFIG;

	static {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();

		DEFAULT_REQUEST_CONFIG = RequestConfig.custom().setExpectContinueEnabled(false) // 设置不使用Expect:100-Continue握手
				.setConnectTimeout(10000) // 设置连接超时时间
				.setSocketTimeout(60000) // 设置读数据超时时间
				.setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY) // Cookie策略
				.build();
		httpClientBuilder.setDefaultRequestConfig(DEFAULT_REQUEST_CONFIG);

		httpClientBuilder.addInterceptorFirst(new RequestAcceptEncoding()); // 设置GZIP请求的支持
		httpClientBuilder.addInterceptorFirst(new ResponseContentEncoding()); // 设置GZIP响应的支持
		// httpClientBuilder.setUserAgent(getRandomUserAgent()); // 设置User-Agent

		HttpRequestRetryHandler retryHandler = new DefaultHttpRequestRetryHandler(1, true);
		httpClientBuilder.setRetryHandler(retryHandler); // 设置重试

		Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create().register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
		connectionManager.setMaxTotal(100);
		connectionManager.setDefaultMaxPerRoute(100);

		SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
		connectionManager.setDefaultSocketConfig(defaultSocketConfig);

		// Create connection configuration
		ConnectionConfig defaultConnectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
		connectionManager.setDefaultConnectionConfig(defaultConnectionConfig);

		httpClientBuilder.setConnectionManager(connectionManager);
		httpClientBuilder.setRedirectStrategy(DefaultRedirectStrategy.INSTANCE);
		HTTP_CLIENT = httpClientBuilder.build();
	}

	public static String execute(HttpUriRequest request) throws ClientProtocolException, IOException {
		return HTTP_CLIENT.execute(request, new BasicResponseHandler());
	}

	public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler) throws ClientProtocolException, IOException {
		return HTTP_CLIENT.execute(request, responseHandler);
	}

	public static String getRandomUserAgent() {
		return USER_AGENTS[RandomUtils.nextInt(0, USER_AGENTS.length)];
	}

	/**
	 * get 请求
	 * 
	 * @param url
	 * @param object
	 *            对象
	 * @return
	 * @throws Exception
	 * @author zhanglj 2016年3月16日
	 */
	public static String get(String url, Object object) throws Exception {
		return get(toRequestUrl(url, HttpClientUtils.reflectObjectFieldsToMap(object)));
	}

	/**
	 * get 请求
	 * 
	 * @param url
	 * @param paramMap
	 * @return
	 * @throws Exception
	 * @author zhanglj 2016年3月3日
	 */
	public static String get(String url, Map<String, String> paramMap) throws Exception {
		return get(toRequestUrl(url, paramMap));
	}

	/**
	 * get 结果
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @author zhanglj 2014-11-25
	 */
	public static String get(String url) throws Exception {
		HttpUriRequest request = new HttpGet(url);
		return HTTP_CLIENT.execute(request, new BasicResponseHandler());
	}

	/**
	 * post 结果
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @author zhanglj 2014-11-25
	 */
	public static String post(String url, Object object) throws Exception {
		HttpUriRequest request = postForm(url, HttpClientUtils.reflectObjectFieldsToMap(object));
		return HTTP_CLIENT.execute(request, new BasicResponseHandler());
	}

	/**
	 * post 结果
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 * @author zhanglj 2014-11-25
	 */
	public static String post(String url, Map<String, String> params) throws Exception {
		HttpUriRequest request = postForm(url, params);
		return HTTP_CLIENT.execute(request, new BasicResponseHandler());
	}

	@SuppressWarnings("deprecation")
	private static HttpUriRequest postForm(String url, Map<String, String> params) throws UnsupportedEncodingException {
		HttpPost httpost = new HttpPost(url);
		if (MapUtils.isNotEmpty(params) == true) {
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				nvps.add(new BasicNameValuePair(key, params.get(key)));
			}
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		}
		return httpost;
	}

	/**
	 * 把对像实例的字段取出转成map表现类形的数据
	 * 
	 * @param object
	 * @return
	 * @throws Exception
	 * @author zhanglj 2016年3月16日
	 */
	public static Map<String, String> reflectObjectFieldsToMap(Object object) throws Exception {

		Map<String, String> map = Maps.newHashMap();
		if (object != null) {
			List<Field> list = FieldUtils.getAllFieldsList(object.getClass());
			if (CollectionUtils.isNotEmpty(list) == true) {
				for (Field field : list) {
					Object valueObj = FieldUtils.readField(field, object, true);
					if (valueObj != null) {
						String value = FastJSONUtils.toJsonString(valueObj);
						if (StringUtils.isNotBlank(value) == true) {
							if (value.startsWith("\"") == true) {
								//
								value = value.substring(1);
								value = value.substring(0, value.lastIndexOf("\""));
							}
							if (StringUtils.isNotBlank(value) == true) {
								map.put(field.getName(), value);
							}
						}
					}
				}
			}
		}
		return map;
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
				String value = entry.getValue();
				if (StringUtils.isBlank(value) == true) {
					value = "";
				}
				if (i == 0) {
					stringBuffer.append(entry.getKey() + "=" + java.net.URLEncoder.encode(value, "UTF-8"));
				} else {
					stringBuffer.append("&" + entry.getKey() + "=" + java.net.URLEncoder.encode(value, "UTF-8"));
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

	public static void main(String[] args) throws Exception {
		// Map<String, String> paramMap = Maps.newHashMap();
		// paramMap.put("a", "我");
		// paramMap.put("b", "b");
		// paramMap.put("c", "b");
		// paramMap.put("d", "b");
		//
		// System.out.println(get("http://120.55.192.163?d=b", paramMap));
		getFile("http://imgs1.bzw315.com/uploadfiles/version2/118063/20160108/201601081503134966-c-290x215.jpg",
				"D:/home/data/attachs/company_photo/3301/uploadfiles/version2/118063/20160108/111/22.jpg");
	}

	/**
	 * 下载文件方法
	 * 
	 * @param url
	 * @param destFileName
	 * @author zhanglj 2016年9月20日
	 * @throws Exception
	 */
	public static void getFile(String url, String destFileName) throws Exception {
		// 生成一个httpclient对象
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		HttpResponse response = httpclient.execute(httpget);
		HttpEntity entity = response.getEntity();
		InputStream in = entity.getContent();
		File file = new File(destFileName);
		file.getParentFile().mkdirs();
		try {
			FileOutputStream fout = new FileOutputStream(file);
			int l = -1;
			byte[] tmp = new byte[1024];
			while ((l = in.read(tmp)) != -1) {
				fout.write(tmp, 0, l);
				// 注意这里如果用OutputStream.write(buff)的话，图片会失真，大家可以试试
			}
			fout.flush();
			fout.close();
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			// 关闭低层流。
			in.close();
		}
		httpclient.close();
	}
}
