package cn.com.easy.rabbitmq.entity;

import java.io.Serializable;

import cn.com.easy.utils.SecurityUtils;

/**
 * 
 * @author nibili 2015年4月30日
 * 
 */
public class UserEntity implements Serializable {

	/** */
	private static final long serialVersionUID = 4645527652929432522L;
	/** id */
	private Long id;
	/** 姓名 */
	private String name;
	/** 年龄 */
	private int age;
	/** 性别 */
	private String sex;

	public static UserEntity newInstance() {
		UserEntity userEntity = new UserEntity();
		userEntity.setId(SecurityUtils.getRandomLong());
		userEntity.setName("倪" + SecurityUtils.getRandomLong());
		userEntity.setAge(1);
		userEntity.setSex("男");
		return userEntity;
	}

	/**
	 * get 姓名
	 * 
	 * @return
	 * @auth nibili 2015年4月30日
	 */
	public String getName() {
		return name;
	}

	/**
	 * set 姓名
	 * 
	 * @param name
	 * @auth nibili 2015年4月30日
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get 年龄
	 * 
	 * @return
	 * @auth nibili 2015年4月30日
	 */
	public int getAge() {
		return age;
	}

	/**
	 * set 年龄
	 * 
	 * @param age
	 * @auth nibili 2015年4月30日
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * get 性别
	 * 
	 * @return
	 * @auth nibili 2015年4月30日
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * set 性别
	 * 
	 * @param sex
	 * @auth nibili 2015年4月30日
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * get id
	 * 
	 * @return
	 * @auth nibili 2015年4月30日
	 */
	public Long getId() {
		return id;
	}

	/**
	 * set id
	 * 
	 * @param id
	 * @auth nibili 2015年4月30日
	 */
	public void setId(Long id) {
		this.id = id;
	}

}
