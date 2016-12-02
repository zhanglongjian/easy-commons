package cn.com.easy.utils;

import java.util.List;

import cn.com.easy.utils.Page;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Lists;

/**
 * mybatis分页结果对象，spring-data分页结果对象，转成easy-commons中的Page对象工具类
 * 
 * @author zhanglj 2015年11月13日
 * 
 */
public class PageUtils {


	/**
	 * 生成page对象
	 * 
	 * @param springDataPage
	 * @return
	 * @author zhanglj 2015年12月8日
	 */
	public static <T> Page<T> getPage(org.springframework.data.domain.Page<T> springDataPage) {
		Page<T> page = new Page<T>();
		if (springDataPage != null) {
			page.setPageNo(springDataPage.getNumber()+1);
			page.setPageSize(springDataPage.getSize());
			page.setTotalItems(springDataPage.getTotalElements());
			page.setResult(springDataPage.getContent());
		}
		return page;
	}

	/**
	 * 生成前端能显示的page对象
	 * 
	 * @param pageObject
	 * @return
	 * @author zhanglj 2015年12月31日
	 */
	public static <T> Page<T> getPage(PageList<T> pageList) {
		Page<T> page = new Page<T>();
		if (pageList != null) {
			page.setPageNo(pageList.getPaginator().getPage());
			page.setPageSize(pageList.getPaginator().getLimit());
			page.setTotalItems(pageList.getPaginator().getTotalCount());
			List<T> lists = Lists.newArrayList(pageList);
			page.setResult(lists);
		}
		return page;
	}
}
