package cn.com.easy.utils;

public class HttpClientUtilsTest {

	public static void main(String[] args) throws Exception {

		UserDto userDto = new UserDto();
		userDto.setAge(10);
		userDto.setFav("上网，读书");
		userDto.setName("billy");
//		userDto.setUserDto(new UserDto());

		System.out.println(FastJSONUtils.toJsonString(HttpClientUtils.reflectObjectFieldsToMap(userDto)));

	}

	// public static void main(String[] args) throws Exception {
	// System.out.println((char)65);
	// System.out.println(isWrapClass(Long.class));
	// System.out.println(isWrapClass(Integer.class));
	// System.out.println(isWrapClass(String.class));
	// System.out.println(isWrapClass(HttpClientUtilsTest.class));
	// }
	//
	// public static boolean isWrapClass(Class clz) {
	// try {
	// return ((Class) clz.getField("TYPE").get(null)).isPrimitive();
	// } catch (Exception e) {
	// return false;
	// }
	// }
}
