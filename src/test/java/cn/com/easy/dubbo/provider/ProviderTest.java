package cn.com.easy.dubbo.provider;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务生产者测试
 * @author nibili	2015年11月11日
 *
 */
public class ProviderTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/dubbo/demo/applicationContext-dubbo-provider-demo.xml");
		context.start();

		System.in.read(); // 按任意键退出
	}
}
