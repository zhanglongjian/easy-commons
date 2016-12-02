package cn.com.easy.dto;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.google.common.collect.Lists;

/**
 * client page request
 * 
 * @author zhanglj 2015-2-12
 * 
 */
public class PageRequestParamDTO {

	private String searchText;
	/** 页码 */
	private int pageNumber = 1;
	/** 每页数量 */
	private int pageSize = 10;
	/** 排序字段 */
	private String sortName;
	/** 排序类型:desc，asc */
	private String sortOrder;
	/** 排序集合 */
	private Map<String, String> sorts;

	public PageRequestParamDTO() {

	}

	//
	// public PageRequestParamDTO(DatatableRequestParamDTO
	// datatableRequestParamDTO) {
	// if (datatableRequestParamDTO != null) {
	//
	// pageSize = datatableRequestParamDTO.getLength();
	// pageNumber = (int) Math.floor((double)
	// (datatableRequestParamDTO.getStart()) / (double) pageSize);
	// }
	//
	// }

	/**
	 * 当前页码
	 * 
	 * @return
	 */
	public int getPageNumber() {
		return this.pageNumber;
	}

	/**
	 * get sortName
	 * 
	 * @return
	 * @auth zhanglj 2015-3-22
	 */
	public String getSortName() {
		return sortName;
	}

	/**
	 * set sortName
	 * 
	 * @param sortName
	 * @auth zhanglj 2015-3-22
	 */
	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	/**
	 * get sortOrder
	 * 
	 * @return
	 * @auth zhanglj 2015-3-22
	 */
	public String getSortOrder() {
		return sortOrder;
	}

	/**
	 * set sortOrder
	 * 
	 * @param sortOrder
	 * @auth zhanglj 2015-3-22
	 */
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * 每页数量
	 * 
	 * @return
	 */
	public int getPageSize() {
		return this.pageSize;
	}

	/** 当前页码 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	/** 每页数量 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 转成easy-commons中的pageRequest
	 * 
	 * @return
	 * @author zhanglj 2015年12月18日
	 */
	public cn.com.easy.utils.PageRequest buildCommonsPageRequest() {
		cn.com.easy.utils.PageRequest pageRequest = new cn.com.easy.utils.PageRequest(this.pageNumber, this.pageSize);
		if (StringUtils.isNotBlank(this.sortName) == true && StringUtils.isNotBlank(this.sortOrder) == true) {
			pageRequest.setOrderBy(this.sortName);
			pageRequest.setOrderDir(this.sortOrder);
		}
		if (MapUtils.isNotEmpty(sorts) == true) {
			List<cn.com.easy.utils.PageRequest.Order> orders = Lists.newArrayList();
			for (Entry<String, String> entry : sorts.entrySet()) {
				if (entry.getValue().toLowerCase().equals("desc") == true) {
					orders.add(new cn.com.easy.utils.PageRequest.Order(cn.com.easy.utils.PageRequest.Direction.DESC, entry.getKey()));
				} else if (entry.getValue().toLowerCase().equals("asc") == true) {
					orders.add(new cn.com.easy.utils.PageRequest.Order(cn.com.easy.utils.PageRequest.Direction.ASC, entry.getKey()));
				}
			}
			pageRequest.setSort(new cn.com.easy.utils.PageRequest.Sort(orders));
		}
		return pageRequest;
	}

	/**
	 * 生成分页请求相关配置
	 * 
	 * @param pageParam
	 * @return
	 */
	public PageRequest buildSpringDataPageRequest() {
		int pageNo = 0;
		int pageSize = 10;
		if (getPageNumber() > 0) {
			pageNo = getPageNumber() - 1;
		}
		if (getPageSize() > 0) {
			pageSize = getPageSize();
		}
		// orders array
		List<Order> orders = Lists.newArrayList();
		// bootstrap-table控件，传递的排序参数
		if (StringUtils.isNotBlank(this.sortName) == true && StringUtils.isNotBlank(this.sortOrder) == true) {
			if (this.sortOrder.equals("asc") == true) {
				orders.add(new Order(Direction.ASC, this.sortName));
			} else if (this.sortOrder.equals("desc") == true) {
				orders.add(new Order(Direction.DESC, this.sortName));
			}
		}
		// /
		if (MapUtils.isNotEmpty(sorts) == true) {
			for (Entry<String, String> entry : sorts.entrySet()) {
				String direction = entry.getValue();
				if (direction == "asc") {
					orders.add(new Order(Direction.ASC, entry.getKey()));
				} else if (direction == "desc") {
					orders.add(new Order(Direction.DESC, entry.getKey()));
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
	public PageBounds buildMyBatisPageRequest() {
		int pageNo = 1;
		int pageSize = 10;
		if (getPageNumber() > 1) {
			pageNo = getPageNumber();
		}
		if (getPageSize() > 0) {
			pageSize = getPageSize();
		}
		// orders array
		List<com.github.miemiedev.mybatis.paginator.domain.Order> orders = Lists.newArrayList();
		if (MapUtils.isNotEmpty(sorts) == true) {
			for (Entry<String, String> entry : sorts.entrySet()) {
				String direction = entry.getValue();
				if (direction == "asc") {
					orders.add(new com.github.miemiedev.mybatis.paginator.domain.Order(entry.getKey(), com.github.miemiedev.mybatis.paginator.domain.Order.Direction.ASC, null));
				} else if (direction == "desc") {
					orders.add(new com.github.miemiedev.mybatis.paginator.domain.Order(entry.getKey(), com.github.miemiedev.mybatis.paginator.domain.Order.Direction.DESC, null));
				}
			}
		}
		// bootstrap-table控件，传递的排序参数
		if (StringUtils.isNotBlank(this.sortName) == true && StringUtils.isNotBlank(this.sortOrder) == true) {
			if (this.sortOrder.equals("asc") == true) {
				orders.add(new com.github.miemiedev.mybatis.paginator.domain.Order(this.sortName, com.github.miemiedev.mybatis.paginator.domain.Order.Direction.ASC, null));
			} else if (this.sortOrder.equals("desc") == true) {
				orders.add(new com.github.miemiedev.mybatis.paginator.domain.Order(this.sortName, com.github.miemiedev.mybatis.paginator.domain.Order.Direction.DESC, null));
			}
		}
		if (CollectionUtils.isEmpty(orders) == true) {
			orders = null;
			return new PageBounds(pageNo, pageSize);
		} else {
			return new PageBounds(pageNo, pageSize, orders);
		}

	}

	/**
	 * get searchText
	 * 
	 * @return
	 * @auth zhanglj 2015-3-25
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * set searchText
	 * 
	 * @param searchText
	 * @auth zhanglj 2015-3-25
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	/**
	 * get 排序集合
	 * 
	 * @return
	 * @author zhanglj 2015年12月31日
	 */
	public Map<String, String> getSorts() {
		return sorts;
	}

	/**
	 * set 排序集合
	 * 
	 * @param sorts
	 * @author zhanglj 2015年12月31日
	 */
	public void setSorts(Map<String, String> sorts) {
		this.sorts = sorts;
	}

}
