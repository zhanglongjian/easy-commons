package cn.com.easy.cache.ehcache;

/**
 * Ehcache对象序列化接口
 * 
 * @author zhanglj 2015年9月23日
 * 
 */
public interface IEhcacheSerializer {

	
	public String  serialize(Object value) throws Exception;

	
	public Object deserialize(String value);
}
