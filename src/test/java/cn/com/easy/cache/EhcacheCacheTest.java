package cn.com.easy.cache;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.easy.cache.service.EhcacheUserService;
import cn.com.easy.utils.FastJSONUtils;

/**
 * ehcache测试
 * 
 * @author nibili 2015年10月22日
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-ehcache-cache-demo.xml")
public class EhcacheCacheTest {

	@Autowired
	private EhcacheUserService userService;

	@Test
	public void getUserById() {

		try {
			for (int i = 0; i < 3; i++) {
				String user = userService.getUserNameByIdWithFastJson(2l);
				System.out.println(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			for (int i = 0; i < 3; i++) {
				int user = userService.getUserAgeByIdWithFastJson(3l);
				System.out.println(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			for (int i = 0; i < 3; i++) {
				Long user = userService.getUserIdByIdWithFastJson(2l);
				System.out.println(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			for (int i = 0; i < 3; i++) {
				Float user = userService.getUserPriceByIdWithFastJson(2l);
				System.out.println(user);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		try {
			for (int i = 0; i < 3; i++) {
				List<cn.com.easy.cache.service.EhcacheUserService.User> user = userService.getUserByIdList(1l);
				System.out.println(FastJSONUtils.toJsonString(user));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			for (int i = 0; i < 3; i++) {
				Map<String, cn.com.easy.cache.service.EhcacheUserService.User> map = userService.getUserByIdMap(1l);
				System.out.println(FastJSONUtils.toJsonString(map));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
