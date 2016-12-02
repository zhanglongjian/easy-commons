package cn.com.easy.dubbo.consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.easy.dubbo.IUserService;

/**
 * dubbo消费者测试
 * 
 * @author nibili 2015年11月11日
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/dubbo/demo/applicationContext-dubbo-consumer-demo.xml")
public class ConsumerTest {

	@Autowired
	private IUserService userService;

	@Test
	public void getUserById() {
		try {
			System.out.println("user 1 :" + userService.getUserById(1l));
			System.out.println("user 2 :" + userService.getUserById(2l));
			System.out.println("user 3 :" + userService.getUserById(3l));
			System.out.println("user 4 :" + userService.getUserById(4l));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
