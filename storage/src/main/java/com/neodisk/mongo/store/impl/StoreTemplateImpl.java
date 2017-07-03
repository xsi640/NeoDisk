package com.neodisk.mongo.store.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.DataReader;
import com.neodisk.mongo.store.DataWriter;
import com.neodisk.mongo.store.StoreTemplate;
import com.neodisk.mongo.store.domain.StoreInfo;
import com.neodisk.mongo.store.domain.StoreUnit;

@Service
public class StoreTemplateImpl implements StoreTemplate {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void save(String id, int chunkSize, InputStream inputStream) throws IOException {
		if (inputStream == null) {
			throw new IllegalArgumentException("inputStream is null");
		}
		if (StringUtils.isEmpty(id)) {
			throw new IllegalArgumentException("id is null");
		}
		DataWriter writer = new DataWriter(id, chunkSize, inputStream, mongoTemplate);
		writer.write();
	}

	@Override
	public void read(String id, OutputStream outputStream) throws StoreException, IOException {
		Query q = new Query(Criteria.where("id").is(id));
		StoreInfo s = mongoTemplate.findOne(q, StoreInfo.class);
		if (s != null) {
			throw new StoreException("not found storeInfo id:" + id);
		}
		DataReader reader = new DataReader(s, mongoTemplate, outputStream);
		reader.read();
	}

	@Override
	public void delete(String id) {
		mongoTemplate.remove(new Query(Criteria.where("storeId").is(id)), StoreUnit.class);
		mongoTemplate.remove(new Query(Criteria.where(id).is(id)), StoreInfo.class);
	}

	@Override
	public void clearUnit() {
		@SuppressWarnings("unchecked")
		List<String> storeIds = mongoTemplate.getCollection(mongoTemplate.getCollectionName(StoreUnit.class))
									.distinct("storeId");
		for(String storeId : storeIds){
			long count = mongoTemplate.count(new Query(Criteria.where("id").is(storeId)), StoreInfo.class);
			if(count == 0){
				mongoTemplate.remove(new Query(Criteria.where("storeId").is(storeId)), StoreUnit.class);
			}
		}		
	}
}
