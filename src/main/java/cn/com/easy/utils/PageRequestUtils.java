package cn.com.easy.utils;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.collect.Lists;

/**
 * 分页请求工具类
 * 
 * @author zhanglj 2016年1月8日
 * 
 */
public class PageRequestUtils {

	/**
	 * 生成分页请求相关配置
	 * 
	 * @param pageParam
	 * @return
	 */
	public static org.springframework.data.domain.PageRequest buildSpringDataPageRequest(cn.com.easy.utils.PageRequest pageRequest) {
		int pageNo = 0;
		int pageSize = 10;
		if (pageRequest.getPageNo() > 0) {
			pageNo = pageRequest.getPageNo() - 1;
		}
		if (pageRequest.getPageSize() > 0) {
			pageSize = pageRequest.getPageSize();
		}
		// orders array
		List<Order> orders = Lists.newArrayList();
		// // bootstrap-table控件，传递的排序参数
		// if (StringUtils.isNotBlank(pageRequest.getOrderBy()) == true &&
		// StringUtils.isNotBlank(pageRequest.getOrderDir()) == true) {
		// if (pageRequest.getOrderDir().equals("asc") == true) {
		// orders.add(new Order(Direction.ASC, pageRequest.getOrderBy()));
		// } else if (pageRequest.getOrderDir().equals("desc") == true) {
		// orders.add(new Order(Direction.DESC, pageRequest.getOrderBy()));
		// }
		// }
		// /
		List<cn.com.easy.utils.PageRequest.Order> list = pageRequest.getSort().getOrders();
		if (CollectionUtils.isNotEmpty(list) == true) {
			for (cn.com.easy.utils.PageRequest.Order order : list) {
				cn.com.easy.utils.PageRequest.Direction direction = order.getDirection();
				if (direction == cn.com.easy.utils.PageRequest.Direction.ASC) {
					orders.add(new Order(Direction.ASC, order.getProperty()));
				} else if (direction == cn.com.easy.utils.PageRequest.Direction.DESC) {
					orders.add(new Order(Direction.DESC, order.getProperty()));
				}
			}
		}
		if (CollectionUtils.isEmpty(orders) == true) {
			orders = null;
			return new PageRequest(pageNo, pageSize);
		} else {
			return new PageRequest(pageNo, pageSize, new Sort(orders));
		}
	}

	/**
	 * 生成分页请求相关配置
	 * 
	 * @param pageParam
	 * @return
	 */
	public static PageBounds buildMyBatisPageRequest(cn.com.easy.utils.PageRequest pageRequest) {

		int pageNo = 1;
		int pageSize = 10;
		if (pageRequest.getPageNo() > 1) {
			pageNo = pageRequest.getPageNo();
		}
		if (pageRequest.getPageSize() > 0) {
			pageSize = pageRequest.getPageSize();
		}

		// orders array
		List<com.github.miemiedev.mybatis.paginator.domain.Order> orders = Lists.newArrayList();
		// // bootstrap-table控件，传递的排序参数
		// if (StringUtils.isNotBlank(pageRequest.getOrderBy()) == true &&
		// StringUtils.isNotBlank(pageRequest.getOrderDir()) == true) {
		// if (pageRequest.getOrderDir().equals("asc") == true) {
		// orders.add(new
		// com.github.miemiedev.mybatis.paginator.domain.Order(pageRequest.getOrderBy(),
		// com.github.miemiedev.mybatis.paginator.domain.Order.Direction.ASC,
		// null));
		// } else if (pageRequest.getOrderDir().equals("desc") == true) {
		// orders.add(new
		// com.github.miemiedev.mybatis.paginator.domain.Order(pageRequest.getOrderBy(),
		// com.github.miemiedev.mybatis.paginator.domain.Order.Direction.DESC,
		// null));
		// }
		// }

		List<cn.com.easy.utils.PageRequest.Order> list = pageRequest.getSort().getOrders();
		if (CollectionUtils.isNotEmpty(list) == true) {
			for (cn.com.easy.utils.PageRequest.Order order : list) {
				cn.com.easy.utils.PageRequest.Direction direction = order.getDirection();
				if (direction == cn.com.easy.utils.PageRequest.Direction.ASC) {
					orders.add(new com.github.miemiedev.mybatis.paginator.domain.Order(order.getProperty(), com.github.miemiedev.mybatis.paginator.domain.Order.Direction.ASC, null));
				} else if (direction == cn.com.easy.utils.PageRequest.Direction.DESC) {
					orders.add(new com.github.miemiedev.mybatis.paginator.domain.Order(order.getProperty(), com.github.miemiedev.mybatis.paginator.domain.Order.Direction.DESC,
							null));
				}
			}
		}

		if (CollectionUtils.isEmpty(orders) == true) {
			orders = null;
			return new PageBounds(pageNo, pageSize);
		} else {
			return new PageBounds(pageNo, pageSize, orders);
		}
	}

}
