package com.neodisk.mongo.store;

import java.io.IOException;
import java.io.OutputStream;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.domain.StoreInfo;
import com.neodisk.mongo.store.domain.StoreUnit;

public class DataReader {
	private StoreInfo storeInfo;
	private MongoTemplate mongoTemplate;
	private OutputStream outputStream;

	private long currentUnitIndex = -1;
	private byte[] buffer = null;
	private long position = 0;

	public DataReader(StoreInfo storeInfo, MongoTemplate mongoTemplate, OutputStream outputStream) {
		this.storeInfo = storeInfo;
		this.mongoTemplate = mongoTemplate;
		this.outputStream = outputStream;
		this.buffer = new byte[this.storeInfo.getChunkSize()];
	}

	public void read() throws StoreException, IOException {
		long unitIndex = position / this.storeInfo.getChunkSize();
		for(;unitIndex < this.storeInfo.getChunkCount(); unitIndex++){
			if (currentUnitIndex != unitIndex) {
				this.readUnit(unitIndex);
			}
			outputStream.write(buffer, 0, buffer.length);
			position += buffer.length;
		}
	}

	private void readUnit(long unitIndex) throws StoreException {
		Query q = new Query(Criteria.where("storeId").is(this.storeInfo.getId())
				.andOperator(Criteria.where("index").is(unitIndex)));
		StoreUnit unit = mongoTemplate.findOne(q, StoreUnit.class);
		if (unit == null) {
			throw new StoreException("unit is null, storeId:" + this.storeInfo.getId() + " index:" + unitIndex);
		}
		this.buffer = unit.getData();
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

}
