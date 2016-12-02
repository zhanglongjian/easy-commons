package cn.com.easy.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.google.common.collect.Maps;

/**
 * 邮件发送的工具类 <br>
 * 使用时，必须先引入 ：<import resource="classpath*:/applicationContext-mail.xml" /><br>
 * MailUtils.send("ni350305@sina.com", "ni350305@163.com", "标题",
 * "mailTemplateTest.vm", map);
 * 
 * @author zhanglj 2015年4月16日
 * 
 */
public abstract class MailUtils {

	/** */
	private static MailService mailService;

	/**
	 * 发送邮件
	 * 
	 * @param to
	 * @param subject
	 * @throws Exception
	 * @auth zhanglj 2015年4月16日
	 */
	public static void send(String to, String subject) throws Exception {
		MailUtils.getMailService().send(to, subject);
	}

	/**
	 * 发送邮件
	 * 
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param templateName
	 *            the template name
	 * @param map
	 *            the map
	 * @throws Exception
	 */
	public static void send(String to, String subject, String templateName, Map<String, Object> map) throws Exception {
		MailUtils.getMailService().send(to, subject, templateName, map);
	}

	/**
	 * Send.
	 * 
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param templateName
	 *            the template name
	 * @param map
	 *            the map
	 * @throws Exception
	 */
	public static void send(String[] to, String subject, String templateName, Map<String, Object> map) throws Exception {
		MailUtils.getMailService().send(to, subject, templateName, map);
	}

	/**
	 * Send.
	 * 
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param templateName
	 *            the template name
	 * @param map
	 *            the map
	 * @param fileName
	 *            the file name
	 * @throws Exception
	 */
	public static void send(String to, String subject, String templateName, Map<String, Object> map, String fileName) throws Exception {
		MailUtils.getMailService().send(to, subject, templateName, map, fileName);
	}

	/**
	 * send text to user
	 * 
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param context
	 *            the context
	 * @throws Exception
	 */
	public static void sendText(String to, String subject, String context) throws Exception {
		MailUtils.getMailService().sendText(new String[] { to }, subject, context);
	}

	/**
	 * 文本发送器，多邮箱.
	 * 
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param context
	 *            the context
	 * @throws Exception
	 */
	public static void sendText(String[] to, String subject, String context) throws Exception {
		MailUtils.getMailService().sendText(to, subject, context);
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 * @auth zhanglj 2015年4月16日
	 */
	private static MailService getMailService() throws Exception {
		if (mailService == null) {
			mailService = SpringContextHolder.getBean(MailService.class);
		}
		if (mailService == null) {
			throw new RuntimeException("");
		}
		return mailService;
	}

	/**
	 * 邮件服务
	 * 
	 * @author zhanglj 2015年4月16日
	 * 
	 */
	public static class MailService {

		/** The Constant DEFAULT_ENCODING. */
		private static final String DEFAULT_ENCODING = "utf-8";

		/** The logger. */
		private static final Logger logger = LoggerFactory.getLogger(MailService.class);

		/** The mail sender. */
		private JavaMailSender mailSender;

		/** The velocity engine. */
		private VelocityEngine velocityEngine;

		/** 发送端 **/
		private String mailFrom;

		/**
		 * send text to user
		 * 
		 * @param to
		 *            the to
		 * @param subject
		 *            the subject
		 * @param context
		 *            the context
		 */
		public void sendText(String to, String subject, String context) {
			sendText(new String[] { to }, subject, context);
		}

		/**
		 * 文本发送器，多邮箱.
		 * 
		 * @param to
		 *            the to
		 * @param subject
		 *            the subject
		 * @param context
		 *            the context
		 */
		public void sendText(String[] to, String subject, String context) {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(this.getMailFrom());
			simpleMailMessage.setTo(to);
			simpleMailMessage.setSubject(subject);
			simpleMailMessage.setText(context);
			simpleMailMessage.setSentDate(new Date());
			try {
				mailSender.send(simpleMailMessage);
				logger.info("纯文本邮件已发送至{}", StringUtils.join(simpleMailMessage.getTo()), ",");
			} catch (Exception e) {
				logger.error("发送邮件失败", e);
			}

		}

		/**
		 * Send.
		 * 
		 * @param to
		 *            the to
		 * @param subject
		 *            the subject
		 * @throws Exception
		 */
		public void send(String to, String subject) throws Exception {
			send(to, subject, null, null);
		}

		/**
		 * Send.
		 * 
		 * @param to
		 *            the to
		 * @param subject
		 *            the subject
		 * @param templateName
		 *            the template name
		 * @param map
		 *            the map
		 * @throws Exception
		 */
		public void send(String to, String subject, String templateName, Map<String, Object> map) throws Exception {
			send(new String[] { to }, subject, templateName, map, null);
		}

		/**
		 * Send.
		 * 
		 * @param to
		 *            the to
		 * @param subject
		 *            the subject
		 * @param templateName
		 *            the template name
		 * @param map
		 *            the map
		 * @throws Exception
		 */
		public void send(String[] to, String subject, String templateName, Map<String, Object> map) throws Exception {
			send(to, subject, templateName, map, null);
		}

		/**
		 * Send.
		 * 
		 * @param to
		 *            the to
		 * @param subject
		 *            the subject
		 * @param templateName
		 *            the template name
		 * @param map
		 *            the map
		 * @param fileName
		 *            the file name
		 * @throws Exception
		 */
		public void send(String to, String subject, String templateName, Map<String, Object> map, String fileName) throws Exception {
			send(new String[] { to }, subject, templateName, map, fileName);
		}

		/**
		 * Send.
		 * 
		 * @param to
		 *            the to
		 * @param subject
		 *            the subject
		 * @param templateName
		 *            the template name
		 * @param map
		 *            the map
		 * @param fileName
		 *            the file name
		 * @throws Exception
		 */
		public void send(String[] to, String subject, String templateName, Map<String, Object> map, String fileName) throws Exception {
			try {
				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, DEFAULT_ENCODING);
				messageHelper.setFrom(this.getMailFrom());
				messageHelper.setTo(to);
				messageHelper.setSubject(subject);
				if (templateName == null || "".equals(templateName)) {
					templateName = "base.ftl";
				}
				if (map == null || map.isEmpty()) {
					map = Maps.newHashMap();
				}
				String context = VelocityUtils.renderFile(templateName, velocityEngine, DEFAULT_ENCODING, map);
				logger.debug("邮件内容:" + context);
				messageHelper.setText(context, true);
				if (!(fileName == null || "".equals(fileName.trim()))) {
					Map<String, Object> fileMap = generateAttachment(fileName);
					messageHelper.addAttachment((String) fileMap.get("fileName"), (File) fileMap.get("file"));
				}
				mailSender.send(message);
				logger.info("HTML版邮件已发送至{}", StringUtils.join(to, ","));
			} catch (MessagingException e) {
				throw new Exception("构造邮件失败", e);
			} catch (Exception e) {
				throw new Exception("发送邮件失败", e);
			}

		}

		/**
		 * Generate attachment.
		 * 
		 * @param fileName
		 *            the file name
		 * @return the map
		 * @throws MessagingException
		 *             the messaging exception
		 */
		private Map<String, Object> generateAttachment(String fileName) throws MessagingException {
			Map<String, Object> map = Maps.newHashMap();
			try {
				Resource resource = new UrlResource(fileName);
				map.put("file", resource.getFile());
				map.put("fileName", resource.getFilename());
				return map;
			} catch (IOException e) {
				logger.error("构造邮件失败,附件文件不存在", e);
				throw new MessagingException("附件文件不存在", e);
			}
		}

		/**
		 * Spring的MailSender.
		 * 
		 * @param mailSender
		 *            the new mail sender
		 */
		public void setMailSender(JavaMailSender mailSender) {
			this.mailSender = mailSender;
		}

		/**
		 * Sets the velocity engine.
		 * 
		 * @param velocityEngine
		 *            the new velocity engine
		 */
		public void setVelocityEngine(VelocityEngine velocityEngine) {
			this.velocityEngine = velocityEngine;
		}

		@Value("${mail.from}")
		public void setMailFrom(String mailFrom) {
			this.mailFrom = mailFrom;
		}

		public String getMailFrom() {
			return this.mailFrom;
		}
	}
}
