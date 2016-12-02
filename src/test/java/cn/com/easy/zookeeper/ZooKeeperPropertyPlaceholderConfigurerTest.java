package cn.com.easy.zookeeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.easy.utils.FastJSONUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/applicationContext-zookeeper-config-demo.xml")
public class ZooKeeperPropertyPlaceholderConfigurerTest {

	@Autowired
	private DBServiceTest serviceTest;

	@Test
	public void doTest() {
		 System.out.println(FastJSONUtils.toJsonString(serviceTest));
	}
}
