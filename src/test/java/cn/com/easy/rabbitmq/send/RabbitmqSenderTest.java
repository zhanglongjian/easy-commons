package cn.com.easy.rabbitmq.send;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.easy.rabbitmq.entity.UserEntity;

/**
 * 
 * @author nibili 2015年4月30日
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-rabbitmq-send-demo.xml")
public class RabbitmqSenderTest {

	/** */
	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 
	 * 发送消息
	 * 
	 * @auth nibili 2015年4月30日
	 */
	@Test
	public void sendDirectMsg() {
		new Thread() {

			public void run() {
				//发送失败时，将会调用这个回调方法
				rabbitTemplate.setConfirmCallback(new ConfirmCallback() {

					@Override
					public void confirm(CorrelationData correlationData, boolean ack, String cause) {
						System.out.println("My Confirm Callback，ack:" + ack + " , cause:" + cause);
					}

				});
				System.out.println("开始发送.....");
				for (int i = 0; i < 100; i++) {
					try {
						rabbitTemplate.convertAndSend("exchange.direct", "queue.test.1", UserEntity.newInstance());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.out.println("发送成功!!");
			}
		}.start();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * 
	 * @auth nibili 2015年5月1日
	 */
	@Test
	public void sendFanoutMsg() {
		System.out.println("开始发送.....");
		for (int i = 0; i < 100; i++) {
			rabbitTemplate.convertAndSend("exchange.fanout", "", UserEntity.newInstance());
		}
		System.out.println("发送成功!!");
	}

	/**
	 * 
	 * 
	 * @auth nibili 2015年5月1日
	 */
	@Test
	public void sendTopicMsg() {
		System.out.println("开始发送.....");
		// for (int i = 0; i < 10; i++) {
		// rabbitTemplate.convertAndSend("exchange.topic", "a.1",
		// UserEntity.newInstance());
		// }
		for (int i = 0; i < 10; i++) {
			rabbitTemplate.convertAndSend("exchange.topic", "queue.test.2", UserEntity.newInstance());
		}
		// for (int i = 0; i < 10; i++) {
		// rabbitTemplate.convertAndSend("exchange.topic", "queue.test.3",
		// UserEntity.newInstance());
		// }
		System.out.println("发送成功!!");
	}
}
