package cn.com.easy.persistence;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import cn.com.easy.persistence.listener.BaseEntityListener;
import cn.com.easy.utils.FastJSONUtils;
import cn.com.easy.utils.JacksonJsonDateSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 统一定义id的entity基类.
 * 
 * @author zhanglj 2014-12-21
 * 
 */
@MappedSuperclass
@EntityListeners(value = { BaseEntityListener.class })
@DynamicInsert
@DynamicUpdate
public abstract class BaseEntity implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 45586345374901436L;

	/** 实体主键. */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** 是否删除状态 */
	@Column(columnDefinition = " bit default 0 ")
	private boolean deleteStatus = false;
	/** 实体版本 */
	@Version
	@Column(columnDefinition = " bigint default 0 ")
	private long version = 0l;
	/** 创建时间. */
	private Date createTime;
	/** 创建人. */
	private String createBy;
	/** 创建人. */
	private Long createById;
	/** 最后修改时间. */
	private Date lastModifyTime;
	/** 最后修改人. */
	private String lastModifyBy;
	/** 最后修改人. */
	private Long lastModifyById;

	/**
	 * get 实体主键.
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	public Long getId() {
		return id;
	}

	/**
	 * set 实体主键.
	 * 
	 * @param id
	 * @author zhanglj 2016年5月20日
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * get 是否删除状态
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	public Boolean getDeleteStatus() {
		return deleteStatus;
	}

	/**
	 * set 是否删除状态
	 * 
	 * @param deleteStatus
	 * @author zhanglj 2016年5月20日
	 */
	public void setDeleteStatus(Boolean deleteStatus) {
		this.deleteStatus = deleteStatus;
	}

	/**
	 * get 实体版本
	 * 
	 * @return
	 * @author zhanglj 2016年5月24日
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * set 实体版本
	 * 
	 * @param version
	 * @author zhanglj 2016年5月24日
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

	/**
	 * get 创建时间.
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	@JsonSerialize(using = JacksonJsonDateSerializer.class)
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * set 创建时间.
	 * 
	 * @param createTime
	 * @author zhanglj 2016年5月20日
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * get 创建人.
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * set 创建人.
	 * 
	 * @param createBy
	 * @author zhanglj 2016年5月20日
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * get 创建人.
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	public Long getCreateById() {
		return createById;
	}

	/**
	 * set 创建人.
	 * 
	 * @param createById
	 * @author zhanglj 2016年5月20日
	 */
	public void setCreateById(Long createById) {
		this.createById = createById;
	}

	/**
	 * get 最后修改时间.
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	@JsonSerialize(using = JacksonJsonDateSerializer.class)
	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	/**
	 * set 最后修改时间.
	 * 
	 * @param lastModifyTime
	 * @author zhanglj 2016年5月20日
	 */
	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	/**
	 * get 最后修改人.
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	public String getLastModifyBy() {
		return lastModifyBy;
	}

	/**
	 * set 最后修改人.
	 * 
	 * @param lastModifyBy
	 * @author zhanglj 2016年5月20日
	 */
	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	/**
	 * get 最后修改人.
	 * 
	 * @return
	 * @author zhanglj 2016年5月20日
	 */
	public Long getLastModifyById() {
		return lastModifyById;
	}

	/**
	 * set 最后修改人.
	 * 
	 * @param lastModifyById
	 * @author zhanglj 2016年5月20日
	 */
	public void setLastModifyById(Long lastModifyById) {
		this.lastModifyById = lastModifyById;
	}

	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {

		return FastJSONUtils.toJsonString(this);
	}

}
