package cn.com.easy.cache.service;

import java.io.Serializable;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Cacheable(key = "'user'+#id", value = { "default" })
	public User getUserById(Long id) {
		return this.getUserByIdWithJdk(id);
	}

	@Cacheable(key = "'username'+#id", value = { "day" })
	public String getUserNameByIdWithFastJson(Long id) {
		return this.getUserNameByIdWithJdk(id);
	}

	@Cacheable(key = "'user'+#id", value = { "day" })
	public User getUserByIdWithFastjson(Long id) {
		return this.getUserById(id);
	}

	@Cacheable(key = "'username'+#id", value = { "default" })
	public String getUserNameByIdWithJdk(Long id) {
		System.out.println("根据id获取用户名，id：" + id);
		return "billy--" + id;
	}

	@Cacheable(key = "'user'+#id", value = { "default" })
	public User getUserByIdWithJdk(Long id) {
		System.out.println("根据id获取用户，id：" + id);
		User user = new User();
		if (id == 1l) {
			user.setId(1l);
			user.setAge(18);
			user.setName("倪billy");
		} else if (id == 2l) {
			user.setId(2l);
			user.setAge(20);
			user.setName("高MM");
		} else {
			user.setId(id);
			user.setAge(20);
			user.setName("高MM # " + id);
		}
		return user;
	}

	public static class User implements Serializable {

		/** */
		private static final long serialVersionUID = -4260332486904930483L;
		private Long id;
		private String name;
		private int age;
		private String nickname = "";

		private User partner;

		public User() {

		}

		/**
		 * get id
		 * 
		 * @return
		 * @auth nibili 2015年4月19日
		 */
		public Long getId() {
			return id;
		}

		/**
		 * set id
		 * 
		 * @param id
		 * @auth nibili 2015年4月19日
		 */
		public void setId(Long id) {
			this.id = id;
		}

		/**
		 * get name
		 * 
		 * @return
		 * @auth nibili 2015年4月19日
		 */
		public String getName() {
			return name;
		}

		/**
		 * set name
		 * 
		 * @param name
		 * @auth nibili 2015年4月19日
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * get age
		 * 
		 * @return
		 * @auth nibili 2015年4月19日
		 */
		public int getAge() {
			return age;
		}

		/**
		 * set age
		 * 
		 * @param age
		 * @auth nibili 2015年4月19日
		 */
		public void setAge(int age) {
			this.age = age;
		}

		/**
		 * get nickname
		 * 
		 * @return
		 * @auth nibili 2015年5月14日
		 */
		public String getNickname() {
			return nickname;
		}

		/**
		 * set nickname
		 * 
		 * @param nickname
		 * @auth nibili 2015年5月14日
		 */
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		/**
		 * get partner
		 * 
		 * @return
		 * @auth nibili 2015年5月14日
		 */
		public User getPartner() {
			return partner;
		}

		/**
		 * set partner
		 * 
		 * @param partner
		 * @auth nibili 2015年5月14日
		 */
		public void setPartner(User partner) {
			this.partner = partner;
		}

	}
}
