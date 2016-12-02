package cn.com.easy.redis;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import cn.com.easy.utils.FastJSONUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * redis fastjson序列化实现<br>
 * 速度较 jdk的序列化 、 反序列化慢<br>
 * 但是占用空间小，是jdk序列化反序列化的一半
 * 
 * @author zhanglj 2015年5月14日
 * 
 */
public class FastJsonRedisSerializer implements RedisSerializer<Object> {

	private Logger logger = LoggerFactory.getLogger(FastJsonRedisSerializer.class);

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
	public byte[] serialize(Object object) throws SerializationException {
		if (object == null) {
			return new byte[0];
		}
		try {
			Class<?> clazz = object.getClass();
//			if (this.isBaseDataType(clazz) == true) {
//				// 基础类型直接返回
//				String json = FastJSONUtils.toJsonString(object);
//				return json.getBytes("UTF-8");
//			} else {
				// 将类名和对象序列化数据，添加到entity中
				ClazzObjectMappingEntity clazzObjectMappingEntity;
				if (object instanceof List || object instanceof Map) {
					// class要写入
					clazzObjectMappingEntity = new ClazzObjectMappingEntity(clazz.getName(), JSON.toJSONString(object, features2));
				} else {
					clazzObjectMappingEntity = new ClazzObjectMappingEntity(clazz.getName(), JSON.toJSONString(object, features1));
				}
				// 将entity序列化
				return FastJSONUtils.toJsonString(clazzObjectMappingEntity).getBytes("UTF-8");
//			}

		} catch (Exception e) {
			throw new SerializationException("序列化对象异常", e);
		}

	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (ArrayUtils.isEmpty(bytes) == true) {
			return null;
		}
		try {
			// redis中取出的数据
			String stringTemp = new String(bytes, "UTF-8");
			if (StringUtils.startsWith(stringTemp, "{\"a\":") == true || StringUtils.startsWith(stringTemp, "{\"b\":") == true) {
				try {
					// 进行反序列化对象
					ClazzObjectMappingEntity clazzObjectMappingEntity = FastJSONUtils.toObject(stringTemp, ClazzObjectMappingEntity.class);
					return FastJSONUtils.toObject(clazzObjectMappingEntity.getB(), Class.forName(clazzObjectMappingEntity.getA()));
				} catch (Exception ex) {
					logger.error("反邓列化对象异常，string:" + stringTemp, ex);
				}
			}
			return stringTemp;
		} catch (Exception e) {
			throw new RuntimeException("反序列化对象异常", e);
		}
	}

	/**
	 * 用于将class name 与 对象序列化的json数据对应实体
	 * 
	 * @author zhanglj 2015年5月14日
	 * 
	 */
	public static class ClazzObjectMappingEntity {

		/** 对象名 */
		private String a;
		/** 对象序列化数据 */
		private String b;

		public ClazzObjectMappingEntity() {

		}

		public ClazzObjectMappingEntity(String clazzName, String objectSerialJson) {
			a = clazzName;
			b = objectSerialJson;
		}

		/**
		 * get a
		 * 
		 * @return
		 * @auth zhanglj 2015年5月14日
		 */
		public String getA() {
			return a;
		}

		/**
		 * get b
		 * 
		 * @return
		 * @auth zhanglj 2015年5月14日
		 */
		public String getB() {
			return b;
		}

		/**
		 * set 对象名
		 * 
		 * @param a
		 * @对象名uth zhanglj 2015年5月14日
		 */
		public void setA(String a) {
			this.a = a;
		}

		/**
		 * set 对象序列化数据
		 * 
		 * @param b
		 * @auth ni对象序列化数据ili 2015年5月14日
		 */
		public void setB(String b) {
			this.b = b;
		}

	}

//	/**
//	 * 是否是基础类型数据
//	 * 
//	 * @param clazz
//	 * @return
//	 * @auth zhanglj 2015年5月14日
//	 */
//	private boolean isBaseDataType(Class<?> clazz) {
//		return clazz.equals(String.class) || clazz.equals(Integer.class) || clazz.equals(Byte.class) || clazz.equals(Long.class)
//				|| clazz.equals(Double.class) || clazz.equals(Float.class) || clazz.equals(Character.class) || clazz.equals(Short.class)
//				|| clazz.equals(BigDecimal.class) || clazz.equals(BigInteger.class) || clazz.equals(Boolean.class);
//	}
}
