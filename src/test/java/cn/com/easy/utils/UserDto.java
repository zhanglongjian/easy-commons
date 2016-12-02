package cn.com.easy.utils;

public class UserDto {

	private String name;
	private int age;
	private String fav;
	private UserDto userDto;

	/**
	 * get name
	 * 
	 * @return
	 * @author nibili 2016年3月16日
	 */
	public String getName() {
		return name;
	}

	/**
	 * set name
	 * 
	 * @param name
	 * @author nibili 2016年3月16日
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * get age
	 * 
	 * @return
	 * @author nibili 2016年3月16日
	 */
	public int getAge() {
		return age;
	}

	/**
	 * set age
	 * 
	 * @param age
	 * @author nibili 2016年3月16日
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * get fav
	 * 
	 * @return
	 * @author nibili 2016年3月16日
	 */
	public String getFav() {
		return fav;
	}

	/**
	 * set fav
	 * 
	 * @param fav
	 * @author nibili 2016年3月16日
	 */
	public void setFav(String fav) {
		this.fav = fav;
	}

	/**
	 * get userDto  
	 * @return   
	 * @author nibili 2016年3月16日
	 */
	public UserDto getUserDto() {
		return userDto;
	}

	/** 
	 *set userDto
	 * @param userDto 
	 * @author nibili 2016年3月16日
	 */
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}

}
