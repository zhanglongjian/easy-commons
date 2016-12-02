package cn.com.easy.persistence.listener;

import java.util.Date;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.easy.persistence.BaseEntity;
import cn.com.easy.utils.DateUtils;

/**
 * The listener interface for receiving baseEntity events. The class that is
 * interested in processing a baseEntity event implements this interface, and
 * the object created with that class is registered with a component using the
 * component's <code>addBaseEntityListener<code> method. When
 * the baseEntity event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see BaseEntityEvent
 */
public class BaseEntityListener {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(BaseEntityListener.class);

	/**
	 * Pre persist.
	 * 
	 * @param object
	 *            the object
	 */
	@PrePersist
	public void prePersist(Object object) {

		if (object instanceof BaseEntity) {
			BaseEntity baseEntity = (BaseEntity) object;
			// 创建新对象
			baseEntity.setCreateTime(DateUtils.getCurrentDateTime());
			baseEntity.setLastModifyTime(DateUtils.getCurrentDateTime());
			// baseEntity.setCreateBy(loginName);
		}
	}

	/**
	 * Pre update.
	 * 
	 * @param object
	 *            the object
	 */
	@PreUpdate
	public void preUpdate(Object object) {

		if (object instanceof BaseEntity) {
			BaseEntity baseEntity = (BaseEntity) object;
			// 修改旧对象
			baseEntity.setLastModifyTime(DateUtils.getCurrentDateTime());
			// baseEntity.setLastModifyBy(loginName);
			logger.info("{}对象(ID:{}) 被 {} 在 {} 修改", new Object[] { object.getClass().getName(), baseEntity.getId(), null, new Date() });
		}
	}
}
