package cn.com.easy.utils;

import org.junit.Test;

public class PhantomjsUtilsTest {

	@Test
	public void getJsHtmlContent() {
		
		try {
			String temp = PhantomjsUtils.getJsHtmlCotnent("http://top.taobao.com/index.php?spm=a1z5i.1.2.2.h20Jtj&topId=TR_FS&leafId=50010850");
			System.out.println(temp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
