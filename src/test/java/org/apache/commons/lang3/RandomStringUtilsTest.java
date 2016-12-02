package org.apache.commons.lang3;

import org.junit.Test;

public class RandomStringUtilsTest {

	@Test
	public void random() {
		for (int i = 0; i < 10; i++) {
		print(RandomStringUtils.random(8));
		print(RandomStringUtils.random(7, "中华人民共和国"));
		print(RandomStringUtils.randomAlphabetic(8));
		print(RandomStringUtils.randomAlphanumeric(8));
		print(RandomStringUtils.randomAscii(8));
		print(RandomStringUtils.randomNumeric(8));

		System.out.println(RandomUtils.nextLong(10000000l, 999999999l));
		 }
	}

	private void print(String str) {
		System.out.println("#" + str + "#  length:" + str.length());
	}
}
