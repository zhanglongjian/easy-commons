package cn.com.easy.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于httpClient签名认证工具类<br/>
 * 类似于oauth认证<br/>
 * 密钥用于加密成一个字符串，不参与传输<br/>
 * 服务端用相同的密钥进行加密成一个字符串，然后与客户端加密字符串进行比对<br/>
 * 
 * @author zhanglj 2015年4月16日 <br/>
 *         This class defines common routines for generating authentication
 *         signatures
 */
public class HttpclientSignatureUtils {

	private static final String ENCODING = "UTF-8";

	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	private static Logger logger = LoggerFactory.getLogger(HttpclientSignatureUtils.class);

	/**
	 * Computes RFC 2104-compliant HMAC signature.
	 * 
	 * @param data
	 *            The signed data.
	 * @param key
	 *            The signing key.
	 * @return The Base64-encoded RFC 2104-compliant HMAC signature.
	 * @throws java.security.SignatureException
	 *             when signature generation fails
	 */
	protected static String calculateRFC2104HMAC(String data, String key) throws SignatureException {
		String result = null;
		try {

			// Get an hmac_sha256 key from the raw key bytes.
			SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(ENCODING), HMAC_SHA256_ALGORITHM);

			// Get an hmac_sha256 Mac instance and initialize with the signing
			// key.
			Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
			mac.init(signingKey);

			// Compute the hmac on input data bytes.
			byte[] rawHmac = mac.doFinal(data.getBytes(ENCODING));

			// Base64-encode the hmac by using the utility in the SDK
			result = Base64.encodeBase64String(rawHmac);

		} catch (Exception e) {
			throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
		}

		return result;
	}

	protected static long generateTimestamp() {
		return System.currentTimeMillis() / 1000;
	}

	protected static String generateNonce() {
		return RandomStringUtils.randomAlphanumeric(32);
	}

	protected static String percentEncode(String s) {
		if (s == null) {
			return "";
		}
		try {
			return URLEncoder.encode(s, ENCODING).replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
		} catch (UnsupportedEncodingException wow) {
			throw new RuntimeException(wow.getMessage(), wow);
		}
	}

	public static void sign(HttpGet httpGet, String secretKey) {
		try {
			URIBuilder uriBuilder = new URIBuilder(httpGet.getURI());
			uriBuilder.setParameter("timestamp", String.valueOf(generateTimestamp()));
			uriBuilder.setParameter("nonce", generateNonce());
			List<NameValuePair> httpGetParams = uriBuilder.getQueryParams();
			List<String> parts = new ArrayList<String>(httpGetParams.size());
			for (NameValuePair params : httpGetParams) {
				parts.add(percentEncode(params.getName()) + "=" + percentEncode(params.getValue()));
			}
			Collections.sort(parts);
			String baseString = StringUtils.join(parts, "&");
			String signature = calculateRFC2104HMAC(baseString, secretKey);
			logger.debug("签名BaseString：{}，签名值：{}", baseString, signature);
			uriBuilder.setParameter("signature", signature);
			httpGet.setURI(uriBuilder.build());
		} catch (Exception e) {
			throw new RuntimeException("签名GET请求出现异常", e);
		}
	}

	public static boolean validateSignature(HttpGet httpGet, String secretKey) {
		try {
			String signature = null;
			List<NameValuePair> httpGetParams = URLEncodedUtils.parse(httpGet.getURI(), ENCODING);
			List<String> parts = new ArrayList<String>(httpGetParams.size());
			for (NameValuePair params : httpGetParams) {
				if ("signature".equals(params.getName())) {
					signature = params.getValue();
				} else {
					parts.add(percentEncode(params.getName()) + "=" + percentEncode(params.getValue()));
				}
			}
			Collections.sort(parts);
			String baseString = StringUtils.join(parts, "&");
			String resigned = calculateRFC2104HMAC(baseString, secretKey);
			logger.debug("签名BaseString：{}，签名值：{}", baseString, signature);
			return StringUtils.equals(signature, resigned);
		} catch (Exception e) {
			throw new RuntimeException("校验GET请求签名出现异常", e);
		}
	}

}