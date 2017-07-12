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

import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.DataReader;
import com.neodisk.mongo.store.DataWriter;
import com.neodisk.mongo.store.PartWriter;
import com.neodisk.mongo.store.StoreTemplate;
import com.neodisk.mongo.store.domain.Store;
import com.neodisk.mongo.store.domain.StorePart;

@Service
public class StoreTemplateImpl implements StoreTemplate {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public Store save(String id, int partSize, long length, InputStream inputStream) throws IOException {
		DataWriter dw = new DataWriter(id, partSize, length, inputStream, mongoTemplate);
		dw.write();
		return dw.getStore();
	}

	@Override
	public Store save(String id, int partSize, long length){
		this.delete(id);
		Store s = new Store();
		s.setId(id);
		s.setPartSize(partSize);
		s.setPartCount(length / partSize + 1);
		s.setLength(length);
		s.setOk(false);
		mongoTemplate.save(s);
		return s;
	}

	@Override
	public void savePart(String id, long index, InputStream inputStream) throws IOException, StoreException {
		Query q = new Query(Criteria.where("storeId").is(id).andOperator(Criteria.where("index").is(index)));
		if(mongoTemplate.exists(q, StorePart.class)){
			return;
		}
		Store storeInfo = this.get(id);
		if(storeInfo == null){
			throw new StoreException("store is not exists.");
		}
		PartWriter pw = new PartWriter(storeInfo, index, inputStream, mongoTemplate);
		pw.write();
		storeInfo.setLatestPartIndex(index);
		mongoTemplate.save(storeInfo);
	}

	@Override
	public void deletePart(String id, long index) {
		Query q = new Query(Criteria.where("storeId").is(id).andOperator(Criteria.where("index").is(index)));
		mongoTemplate.findAndRemove(q, StorePart.class);
	}

	@Override
	public Store finishPart(String id) throws StoreException {
		Store storeInfo = this.get(id);
		if(storeInfo == null){
			throw new StoreException("store is not exists.");
		}
		storeInfo.setOk(true);
		mongoTemplate.save(storeInfo);
		return storeInfo;
	}

	@Override
	public void read(String id, long position, OutputStream outputStream)
			throws StoreException, IOException {
		Store storeInfo = this.get(id);
		if(storeInfo == null){
			throw new StoreException("store is not exists.");
		}
		if(!storeInfo.isOk()){
			throw new StoreException("store is not ready.");
		}
		DataReader dr = new DataReader(storeInfo, position, mongoTemplate, outputStream);
		dr.read();
	}

	@Override
	public void read(String id, OutputStream outputStream) throws StoreException, IOException {
		Store storeInfo = this.get(id);
		if(storeInfo == null){
			throw new StoreException("store is not exists.");
		}
		if(!storeInfo.isOk()){
			throw new StoreException("store is not ready.");
		}
		DataReader dr = new DataReader(storeInfo, 0, mongoTemplate, outputStream);
		dr.read();
	}

	@Override
	public void delete(String id) {
		Query q = new Query(Criteria.where("id").is(id));
		mongoTemplate.findAllAndRemove(q, Store.class);
		
		q = new Query(Criteria.where("storeId").is(id));
		mongoTemplate.findAndRemove(q, StorePart.class);
	}

	@Override
	public Store get(String id) {
		Query q = new Query(Criteria.where("id").is(id));
		return mongoTemplate.findOne(q, Store.class);
	}

	@Override
	public List<StorePart> findStorePartByStoreId(String storeId) {
		Query q = new Query(Criteria.where("storeId").is(storeId));
		return mongoTemplate.find(q, StorePart.class);
	}

	@Override
	public List<StorePart> findStorePartByStoreId(String storeId, int firstResult, int maxResult) {
		Query q = new Query(Criteria.where("storeId").is(storeId));
		q.skip(firstResult);
		q.limit(maxResult);
		return mongoTemplate.find(q, StorePart.class);
	}

	@Override
	public long countByStoreId(String storeId) {
		Query q = new Query(Criteria.where("storeId").is(storeId));
		return mongoTemplate.count(q, Long.class);
	}
}
