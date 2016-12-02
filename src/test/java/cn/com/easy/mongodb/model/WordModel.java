package cn.com.easy.mongodb.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 搜索建议词条
 * 
 * @author nibili 2015年6月19日
 * 
 */
@Document(collection="word")
public class WordModel {

	public static final String ID_FIELD = "id";
	public static final String TEXT_FIELD = "text";
	public static final String USECOUNT_FIELD = "useCount";
	public static final String RESULTCOUNT_FIELD = "resultCount";

	@Id
	private String id;
	/** 文本 */
	private String text;
	/** 拼音 */
	private String pinyin;
	/** 拼音首字母缩写 */
	private String initials;
	/** 使用次数 */
	private Integer useCount;
	/** 结果集大小 */
	private Integer resultCount;

	/**
	 * get id
	 * 
	 * @return
	 * @auth nibili 2015年6月19日
	 */
	public String getId() {
		return id;
	}

	/**
	 * set id
	 * 
	 * @param id
	 * @auth nibili 2015年6月19日
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * get 文本
	 * 
	 * @return
	 * @auth nibili 2015年6月19日
	 */
	public String getText() {
		return text;
	}

	/**
	 * set 文本
	 * 
	 * @param text
	 * @auth nibili 2015年6月19日
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * get 拼音
	 * 
	 * @return
	 * @auth nibili 2015年6月19日
	 */
	public String getPinyin() {
		return pinyin;
	}

	/**
	 * set 拼音
	 * 
	 * @param pinyin
	 * @auth nibili 2015年6月19日
	 */
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	/**
	 * get 拼音首字母缩写
	 * 
	 * @return
	 * @auth nibili 2015年6月19日
	 */
	public String getInitials() {
		return initials;
	}

	/**
	 * set 拼音首字母缩写
	 * 
	 * @param initials
	 * @auth nibili 2015年6月19日
	 */
	public void setInitials(String initials) {
		this.initials = initials;
	}

	/**
	 * get 使用次数
	 * 
	 * @return
	 * @auth nibili 2015年6月19日
	 */
	public Integer getUseCount() {
		return useCount;
	}

	/**
	 * set 使用次数
	 * 
	 * @param useCount
	 * @auth nibili 2015年6月19日
	 */
	public void setUseCount(Integer useCount) {
		this.useCount = useCount;
	}

	/**
	 * get 结果集大小
	 * 
	 * @return
	 * @auth nibili 2015年6月19日
	 */
	public Integer getResultCount() {
		return resultCount;
	}

	/**
	 * set 结果集大小
	 * 
	 * @param resultCount
	 * @auth nibili 2015年6月19日
	 */
	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}

}