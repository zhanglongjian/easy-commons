package cn.com.easy.cache;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.easy.cache.service.UserService;
import cn.com.easy.cache.service.UserService.User;
import cn.com.easy.utils.FastJSONUtils;

/**
 * 
 * @author nibili 2015年4月19日
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-guava-cache-demo.xml")
public class GuavaCacheTest {

	@Autowired
	private UserService userService;

	@Test
	public void getUserById() {
		try {
			for (int i = 0; i < 10; i++) {
				String user = userService.getUserNameByIdWithFastJson(1l);
				System.out.println(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			for (int i = 0; i < 10; i++) {
				User user = userService.getUserById(1l);
				System.out.println(FastJSONUtils.toJsonString(user));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
