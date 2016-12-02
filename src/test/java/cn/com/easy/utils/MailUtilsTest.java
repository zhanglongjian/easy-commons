package cn.com.easy.utils;

import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.easy.utils.MailUtils;

import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/demo.xml")
public class MailUtilsTest {

	@Test
	public void send() {
		try {
			Map<String, Object> map = Maps.newHashMap();
			map.put("to", "ni350305@163.com");
			map.put("from", "ni350305@163.com");
			map.put("subject", "标题");
			MailUtils.send("ni350305@163.com", "标题", "mailTemplateTest.vm", map);

//			for (int i = 0; i < 100; i++) {
//				System.out.println("额外线程 : " + i);
//				Thread.sleep(1000);
//			}

			// MailUtils.sendText("ni350305@163.com", "标题", "纯文本邮件测试");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// MailUtils.send(from, to, subject, templateName, map);
		// MailUtils.send(from, to, subject, templateName, map);
		// MailUtils.send(from, to, subject, templateName, map, fileName);
	}
}
