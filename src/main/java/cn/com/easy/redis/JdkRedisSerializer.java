package cn.com.easy.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * 使用 sdk的序列 、反序列<br>
 * 序列化，反序列化 与fastjson相比较快，所以取数据较快<br>
 * 但是占用空间较大,是 fastjson策略的一倍多
 * 
 * @author zhanglj 2015年5月14日
 * 
 */
public class JdkRedisSerializer implements RedisSerializer<Object> {

	@Override
	public byte[] serialize(Object obj) throws SerializationException {
		if (obj == null) {
			return new byte[0];
		}
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (Exception ex) {
			throw new SerializationException("序列化对象异常", ex);
		}
		return bytes;
	}

	@Override
	public Object deserialize(byte[] bytes) throws SerializationException {
		if (ArrayUtils.isEmpty(bytes) == true) {
			return null;
		}
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (Exception ex) {
			throw new RuntimeException("反序列化对象异常", ex);
		}
		return obj;
	}

}
