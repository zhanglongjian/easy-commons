package cn.com.easy.rabbitmq.receive;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import cn.com.easy.utils.FastJSONUtils;

/**
 * 
 * @author nibili 2015年5月1日
 * 
 */
@Service
public class RabbitmqReceiverTest {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("/applicationContext-rabbitmq-receive-demo.xml");
	}

	/**
	 * 接收队列1消息
	 * 
	 * @param o
	 * @auth nibili 2015年5月1日
	 */
	public void handTest1Msg(Object o) {
		System.out.println("Test1 msg:" + FastJSONUtils.toJsonString(o));
	}

	/**
	 * 接收队列2消息
	 * 
	 * @param o
	 * @auth nibili 2015年5月1日
	 */
	public void handTest2Msg(Object o) {
		System.out.println("Test2 msg:" + FastJSONUtils.toJsonString(o));
	}
	
	/**
	 * 接收队列3消息
	 * 
	 * @param o
	 * @auth nibili 2015年5月1日
	 */
	public void handTest3Msg(Object o) {
		System.out.println("Test3 msg:" + FastJSONUtils.toJsonString(o));
	}
}
