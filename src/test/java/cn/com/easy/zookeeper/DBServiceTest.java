package cn.com.easy.zookeeper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DBServiceTest {

	@Value("${db.url}")
	private String url;
	@Value("${db.username}")
	private String user;
	@Value("${db.password}")
	private String passwd;

	/**
	 * get url
	 * 
	 * @return
	 * @auth nibili 2015年5月7日
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * set url
	 * 
	 * @param url
	 * @auth nibili 2015年5月7日
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * get user
	 * 
	 * @return
	 * @auth nibili 2015年5月7日
	 */
	public String getUser() {
		return user;
	}

	/**
	 * set user
	 * 
	 * @param user
	 * @auth nibili 2015年5月7日
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * get passwd
	 * 
	 * @return
	 * @auth nibili 2015年5月7日
	 */
	public String getPasswd() {
		return passwd;
	}

	/**
	 * set passwd
	 * 
	 * @param passwd
	 * @auth nibili 2015年5月7日
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}
