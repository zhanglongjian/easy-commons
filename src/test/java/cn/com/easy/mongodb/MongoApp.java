package cn.com.easy.mongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import cn.com.easy.mongodb.model.WordModel;
import cn.com.easy.utils.FastJSONUtils;

import com.mongodb.Mongo;
/**
 * 无认证，测试
 * @author nibili	2015年6月19日
 *
 */
public class MongoApp {

	private static final Logger log = LoggerFactory.getLogger(MongoApp.class);

	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		MongoOperations mongoOps = new MongoTemplate(new Mongo("192.168.1.156", 27017), "autocomlete");
		WordModel word = new WordModel();
		word.setInitials("sj");
		word.setPinyin("shouji");
		word.setResultCount(10);
		word.setText("手机");
		word.setUseCount(100);
		//
		mongoOps.insert(word);
		//
		word = mongoOps.findOne(new Query(where(WordModel.TEXT_FIELD).is("手机")), WordModel.class);
		log.info(FastJSONUtils.toJsonString(word));

		mongoOps.dropCollection("person");
	}
}