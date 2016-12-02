package cn.com.easy.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.collect.Lists;

/**
 * 
 * @author zhanglj 2015年11月9日
 * 
 */
public class PageRequest implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9100768518781654128L;

	/** 搜索关键词 */
	private String searchText = "";

	/** The page no. */
	protected int pageNo = 1;

	/** The page size. */
	protected int pageSize = 10;

	/** The order by. */
	protected String orderBy = null;

	/** The order dir. */
	protected String orderDir = null;

	/** The order type. */
	protected String orderType = null;

	/** The count total. */
	protected boolean countTotal = true;

	/** The sort. */
	protected Sort sort;

	/** The search debug. */
	protected boolean searchDebug = false;

	/**
	 * Instantiates a new page request.
	 */
	public PageRequest() {
	}

	/**
	 * Instantiates a new page request.
	 * 
	 * @param pageNo
	 *            the page no
	 * @param pageSize
	 *            the page size
	 */
	public PageRequest(int pageNo, int pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
	}

	/**
	 * Gets the page no.
	 * 
	 * @return the page no
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * Sets the page no.
	 * 
	 * @param pageNo
	 *            the new page no
	 */
	public void setPageNo(final int pageNo) {
		this.pageNo = pageNo;

		if (pageNo < 1) {
			this.pageNo = 1;
		}
	}

	/**
	 * Gets the page size.
	 * 
	 * @return the page size
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * Sets the page size.
	 * 
	 * @param pageSize
	 *            the new page size
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;

		if (pageSize < 1) {
			this.pageSize = 10;
		}
	}

	/**
	 * Gets the order by.
	 * 
	 * @return the order by
	 */
	public String getOrderBy() {
		return orderBy;
	}

	/**
	 * Sets the order by.
	 * 
	 * @param orderBy
	 *            the new order by
	 */
	public void setOrderBy(final String orderBy) {
		this.orderBy = orderBy;
	}

	/**
	 * Gets the order dir.
	 * 
	 * @return the order dir
	 */
	public String getOrderDir() {
		return orderDir;
	}

	/**
	 * Sets the order dir.
	 * 
	 * @param orderDir
	 *            the new order dir
	 */
	public void setOrderDir(final String orderDir) {
		if (StringUtils.isNotBlank(orderDir)) {
			String lowcaseOrderDir = StringUtils.lowerCase(orderDir);

			// 检查order字符串的合法值
			String[] orderDirs = StringUtils.split(lowcaseOrderDir, ',');
			for (String orderDirStr : orderDirs) {
				if (!StringUtils.equalsIgnoreCase(Direction.DESC.name(), orderDirStr) && !StringUtils.equalsIgnoreCase(Direction.ASC.name(), orderDirStr)) {
					throw new IllegalArgumentException("排序方向" + orderDirStr + "不是合法值");
				}
			}

			this.orderDir = lowcaseOrderDir;
		}
	}

	/**
	 * Gets the order type.
	 * 
	 * @return the order type
	 */
	public String getOrderType() {
		return orderType;
	}

	/**
	 * Sets the order type.
	 * 
	 * @param orderType
	 *            the new order type
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	/**
	 * Gets the sort.
	 * 
	 * @return the sort
	 */
	public Sort getSort() {
		List<Order> orders = Lists.newArrayList();
		if (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(orderDir)) {
			String[] orderBys = StringUtils.split(orderBy, ',');
			String[] orderDirs = StringUtils.split(orderDir, ',');
			Validate.isTrue(orderBys.length == orderDirs.length, "分页多重排序参数中,排序字段与排序方向的个数不相等");
			for (int i = 0; i < orderBys.length; i++) {
				orders.add(new Order(Direction.fromString(orderDirs[i]), orderBys[i]));
			}
		}
		if (this.sort != null) {
			orders.addAll(this.sort.orders);
		}
		if (!orders.isEmpty())
			return new Sort(orders);
		return new Sort();
	}

	/**
	 * Sets the sort.
	 * 
	 * @param sort
	 *            the new sort
	 */
	public void setSort(Sort sort) {
		this.sort = sort;
	}

	/**
	 * Checks if is order by setted.
	 * 
	 * @return true, if is order by setted
	 */
	public boolean isOrderBySetted() {
		return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(orderDir));
	}

	/**
	 * Checks if is count total.
	 * 
	 * @return true, if is count total
	 */
	public boolean isCountTotal() {
		return countTotal;
	}

	/**
	 * Sets the count total.
	 * 
	 * @param countTotal
	 *            the new count total
	 */
	public void setCountTotal(boolean countTotal) {
		this.countTotal = countTotal;
	}

	/**
	 * Gets the offset.
	 * 
	 * @return the offset
	 */
	public int getOffset() {
		return ((pageNo - 1) * pageSize);
	}

	/**
	 * The Enum Direction.
	 * 
	 * @author
	 */
	public static enum Direction {

		/** The ASC. */
		ASC,

		/** The DESC. */
		DESC;

		/**
		 * From string.
		 * 
		 * @param value
		 *            the value
		 * @return the direction
		 */
		public static Direction fromString(String value) {

			try {
				return Direction.valueOf(value.toUpperCase(Locale.US));
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format("Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).", value), e);
			}
		}
	}

	/**
	 * The Class Order.
	 * 
	 * @author
	 */
	public static class Order implements Serializable {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 1522511010900108987L;

		/** The direction. */
		private Direction direction;

		/** The property. */
		private String property;

		/**
		 * Instantiates a new order.
		 */
		public Order() {
			super();
		}

		/**
		 * Instantiates a new order.
		 * 
		 * @param direction
		 *            the direction
		 * @param property
		 *            the property
		 */
		public Order(Direction direction, String property) {

			// if (property == null || "".equals(property.trim())) {
			// throw new
			// IllegalArgumentException("PropertyPath must not null or empty!");
			// }

			this.direction = direction == null ? Sort.DEFAULT_DIRECTION : direction;
			this.property = property;
		}

		/**
		 * Instantiates a new order.
		 * 
		 * @param property
		 *            the property
		 */
		public Order(String property) {
			this(Sort.DEFAULT_DIRECTION, property);
		}

		/**
		 * Creates the.
		 * 
		 * @param direction
		 *            the direction
		 * @param properties
		 *            the properties
		 * @return the list
		 */
		public static List<Order> create(Direction direction, Iterable<String> properties) {

			List<Order> orders = new ArrayList<Order>();
			for (String property : properties) {
				orders.add(new Order(direction, property));
			}
			return orders;
		}

		/**
		 * Gets the direction.
		 * 
		 * @return the direction
		 */
		public Direction getDirection() {
			return direction;
		}

		/**
		 * Gets the property.
		 * 
		 * @return the property
		 */
		public String getProperty() {
			return property;
		}

		/**
		 * Checks if is ascending.
		 * 
		 * @return true, if is ascending
		 */
		public boolean isAscending() {
			return this.direction.equals(Direction.ASC);
		}

		/**
		 * With.
		 * 
		 * @param order
		 *            the order
		 * @return the order
		 */
		public Order with(Direction order) {
			return new Order(order, this.property);
		}

		/**
		 * With properties.
		 * 
		 * @param properties
		 *            the properties
		 * @return the sort
		 */
		public Sort withProperties(String... properties) {
			return new Sort(this.direction, properties);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {

			int result = 17;

			result = 31 * result + direction.hashCode();
			result = 31 * result + property.hashCode();

			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {

			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Order)) {
				return false;
			}

			Order that = (Order) obj;

			return this.direction.equals(that.direction) && this.property.equals(that.property);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return String.format("%s: %s", property, direction);
		}
	}

	/**
	 * The Class Sort.
	 * 
	 * @author
	 */
	public static class Sort implements Serializable, Iterable<Order> {

		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = -6732052557382112251L;

		/** The Constant DEFAULT_DIRECTION. */
		public static final Direction DEFAULT_DIRECTION = Direction.ASC;

		/** The orders. */
		private List<Order> orders = Lists.newArrayList();

		/**
		 * Instantiates a new sort.
		 */
		public Sort() {
			super();
		}

		/**
		 * Instantiates a new sort.
		 * 
		 * @param orders
		 *            the orders
		 */
		public Sort(Order... orders) {
			this(Arrays.asList(orders));
		}

		/**
		 * Instantiates a new sort.
		 * 
		 * @param orders
		 *            the orders
		 */
		public Sort(List<Order> orders) {

			if (null == orders || orders.isEmpty()) {
				throw new IllegalArgumentException("You have to provide at least one sort property to sort by!");
			}

			this.orders = orders;
		}

		/**
		 * Instantiates a new sort.
		 * 
		 * @param properties
		 *            the properties
		 */
		public Sort(String... properties) {

			this(DEFAULT_DIRECTION, properties);
		}

		/**
		 * Instantiates a new sort.
		 * 
		 * @param direction
		 *            the direction
		 * @param properties
		 *            the properties
		 */
		public Sort(Direction direction, String... properties) {

			this(direction, properties == null ? new ArrayList<String>() : Arrays.asList(properties));
		}

		/**
		 * Instantiates a new sort.
		 * 
		 * @param direction
		 *            the direction
		 * @param properties
		 *            the properties
		 */
		public Sort(Direction direction, List<String> properties) {

			if (properties == null || properties.isEmpty()) {
				throw new IllegalArgumentException("You have to provide at least one property to sort by!");
			}

			this.orders = new ArrayList<Order>(properties.size());

			for (String property : properties) {
				this.orders.add(new Order(direction, property));
			}
		}

		/**
		 * And.
		 * 
		 * @param sort
		 *            the sort
		 * @return the sort
		 */
		public Sort and(Sort sort) {

			if (sort == null) {
				return this;
			}

			ArrayList<Order> these = new ArrayList<Order>(this.orders);

			for (Order order : sort) {
				these.add(order);
			}

			return new Sort(these);
		}

		/**
		 * Gets the order for.
		 * 
		 * @param property
		 *            the property
		 * @return the order for
		 */
		public Order getOrderFor(String property) {

			for (Order order : this) {
				if (order.getProperty().equals(property)) {
					return order;
				}
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Iterable#iterator()
		 */
		public Iterator<Order> iterator() {

			return this.orders.iterator();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {

			if (this == obj) {
				return true;
			}

			if (!(obj instanceof Sort)) {
				return false;
			}

			Sort that = (Sort) obj;

			return this.orders.equals(that.orders);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {

			int result = 17;
			result = 31 * result + orders.hashCode();
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {

			return org.springframework.util.StringUtils.collectionToCommaDelimitedString(orders);
		}

		/**
		 * Gets the orders.
		 * 
		 * @return the orders
		 */
		public List<Order> getOrders() {
			return orders;
		}

		/**
		 * Sets the orders.
		 * 
		 * @param orders
		 *            the new orders
		 */
		public void setOrders(List<Order> orders) {
			this.orders = orders;
		}

	}

	/**
	 * Checks if is search debug.
	 * 
	 * @return true, if is search debug
	 */
	public boolean isSearchDebug() {
		return searchDebug;
	}

	/**
	 * Sets the search debug.
	 * 
	 * @see cn.com.summall.commons.Page#debugMsg
	 * @param searchDebug
	 *            the new search debug
	 */
	public void setSearchDebug(boolean searchDebug) {
		this.searchDebug = searchDebug;
	}

	/**
	 * get searchText
	 * 
	 * @return
	 * @author zhanglj 2016年1月8日
	 */
	public String getSearchText() {
		return searchText;
	}

	/**
	 * set searchText
	 * 
	 * @param searchText
	 * @author zhanglj 2016年1月8日
	 */
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}
