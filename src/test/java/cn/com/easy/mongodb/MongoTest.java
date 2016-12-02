package cn.com.easy.mongodb;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.com.easy.mongodb.dao.WordDao;
import cn.com.easy.mongodb.model.WordModel;
import cn.com.easy.utils.FastJSONUtils;

/**
 * spring data mongodb测试
 * @author nibili	2015年6月19日
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/applicationContext-mongodb-demo.xml")
public class MongoTest {

	private static final Logger log = LoggerFactory.getLogger(MongoTest.class);

	/** */
	@Autowired
	private WordDao wordDao;

	/** */
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * mongo插入
	 * 
	 * @auth nibili 2015年6月19日
	 */
	@Test
	public void insert() {
		try {
			WordModel word = new WordModel();
			word.setInitials("sj");
			word.setPinyin("shouji");
			word.setResultCount(10);
			word.setText("手机");
			word.setUseCount(100);
			//
			mongoTemplate.insert(word);
			//
			word = mongoTemplate.findOne(new Query(where(WordModel.TEXT_FIELD).is("手机")), WordModel.class);
			log.info("结果：" + FastJSONUtils.toJsonString(word));

			List<WordModel> list = mongoTemplate.findAll(WordModel.class);
			log.info("结果List：" + FastJSONUtils.toJsonString(list));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 分页查找
	 * 
	 * @auth nibili 2015年6月19日
	 */
	@Test
	public void findByPage() {
		Iterable<WordModel> it = wordDao.findAll();
		if (it != null && it.iterator() != null) {
			Iterator<WordModel> iterator = it.iterator();
			while (iterator.hasNext()) {
				System.out.println(FastJSONUtils.toJsonString(iterator.next()));
			}
		}
	}
}
