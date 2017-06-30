package com.neodisk.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import com.neodisk.domain.SmallData;
import com.neodisk.storage.dao.SmallDataDao;

@Repository
public class SmallDataDaoImpl implements SmallDataDao {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(SmallData smallData) {
		mongoTemplate.save(smallData);
	}

	@Override
	public SmallData findOne(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(query, SmallData.class);
	}

	@Override
	public void delete(String id) {
		Query query = new Query(Criteria.where("id").is(id));
		mongoTemplate.remove(query, SmallData.class);
	}

}
