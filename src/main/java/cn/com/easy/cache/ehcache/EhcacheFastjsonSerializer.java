package cn.com.easy.cache.ehcache;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.com.easy.utils.FastJSONUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * ehcache对象序列化接口，fastjson的实现
 * 
 * @author zhanglj 2015年9月23日
 * 
 */
public class EhcacheFastjsonSerializer implements IEhcacheSerializer {

	private Logger logger = LoggerFactory.getLogger(EhcacheFastjsonSerializer.class);

	private static final SerializerFeature[] features1 = { SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty,// 字符类型字段如果为null，输出为""，而不是null
			SerializerFeature.DisableCircularReferenceDetect // 禁止以引用形式输出
	};
	private static final SerializerFeature[] features2 = { SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty,// 字符类型字段如果为null，输出为""，而不是null
			SerializerFeature.DisableCircularReferenceDetect,// 禁止以引用形式输出
			SerializerFeature.WriteClassName };

	@Override
	public String serialize(Object object) throws Exception {

		if (object == null) {
			return "";
		}
		try {
			Class<?> clazz = object.getClass();
			// if (this.isBaseDataType(clazz) == true) {
			// // 基础类型直接返回
			// String json = JSON.toJSONString(object);
			// return json;
			//
			// } else {

			// 将类名和对象序列化数据，添加到entity中
			ClazzObjectMappingEntity clazzObjectMappingEntity;
			if (object instanceof List || object instanceof Map) {
				// class要写入
				clazzObjectMappingEntity = new ClazzObjectMappingEntity(clazz.getName(), JSON.toJSONString(object, features2));
			} else {
				clazzObjectMappingEntity = new ClazzObjectMappingEntity(clazz.getName(), JSON.toJSONString(object, features1));
			}
			// 将entity序列化
			return FastJSONUtils.toJsonString(clazzObjectMappingEntity);
			// }

		} catch (Exception e) {
			throw new Exception("序列化对象异常", e);
		}

	}

	@Override
	public Object deserialize(String value) {
		if (StringUtils.isBlank(value) == true) {
			return null;
		}
		try {
			// redis中取出的数据
			if (StringUtils.startsWith(value, "{\"a\":") == true || StringUtils.startsWith(value, "{\"b\":") == true) {
				try {
					// 进行反序列化对象
					ClazzObjectMappingEntity clazzObjectMappingEntity = JSON.parseObject(value, ClazzObjectMappingEntity.class);
					return FastJSONUtils.toObject(clazzObjectMappingEntity.getB(),
							Class.forName(clazzObjectMappingEntity.getA()));
				} catch (Exception ex) {
					logger.error("反邓列化对象异常，string:" + value, ex);
				}
			}
			return value;
		} catch (Exception e) {
			throw new RuntimeException("反序列化对象异常", e);
		}
	}

	/**
	 * 用于将class name 与 对象序列化的json数据对应实体
	 * 
	 * @author nibili 2015年5月14日
	 * 
	 */
	public static class ClazzObjectMappingEntity {

		/** 对象类名 */
		private String a;
		/** 对象序列化数据 */
		private String b;

		public ClazzObjectMappingEntity() {

		}
		public ClazzObjectMappingEntity(String clazzName, String objectSerialJson) {
			this.a = clazzName;
			this.b = objectSerialJson;
		}
		/**
		 * get 对象类名
		 * 
		 * @return
		 * @auth nibili 2015年11月19日
		 */
		public String getA() {
			return a;
		}

		/**
		 * set 对象类名
		 * 
		 * @param a
		 * @对象类名uth nibili 2015年11月19日
		 */
		public void setA(String a) {
			this.a = a;
		}

		/**
		 * get 对象序列化数据
		 * 
		 * @return
		 * @auth nibili 2015年11月19日
		 */
		public String getB() {
			return b;
		}

		/**
		 * set 对象序列化数据
		 * 
		 * @param b
		 * @auth ni对象序列化数据ili 2015年11月19日
		 */
		public void setB(String b) {
			this.b = b;
		}

	}

	// /**
	// * 是否是基础类型数据
	// *
	// * @param clazz
	// * @return
	// * @auth nibili 2015年5月14日
	// */
	// private boolean isBaseDataType(Class<?> clazz) {
	// return clazz.equals(String.class) || clazz.equals(Integer.class) ||
	// clazz.equals(Byte.class) || clazz.equals(Long.class)
	// || clazz.equals(Double.class) || clazz.equals(Float.class) ||
	// clazz.equals(Character.class) || clazz.equals(Short.class)
	// || clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) ||
	// clazz.equals(Boolean.class);
	// }

}
