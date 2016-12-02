package cn.com.easy.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import cn.com.easy.mongodb.model.WordModel;

/**
 * word集合的dao
 * 
 * @author nibili 2015年6月19日
 * 
 */
public interface WordDao extends MongoRepository<WordModel, Long> {

}
