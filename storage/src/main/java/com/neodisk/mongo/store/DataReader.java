package com.neodisk.mongo.store;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.domain.Store;
import com.neodisk.mongo.store.domain.StorePart;

public class DataReader {
	private Store storeInfo;
	private MongoTemplate mongoTemplate;
	private OutputStream outputStream;

	private long currentUnitIndex = -1;
	private long position;
	private long startPos;
	private byte[] buffer = null;

	public DataReader(Store storeInfo, long position, MongoTemplate mongoTemplate, OutputStream outputStream) {
		this.storeInfo = storeInfo;
		this.mongoTemplate = mongoTemplate;
		this.outputStream = outputStream;
		this.buffer = new byte[this.storeInfo.getPartSize()];
		this.position = position;
	}

	public void read() throws StoreException, IOException {
		long unitIndex = this.position / this.storeInfo.getPartSize();
		this.startPos = unitIndex * this.storeInfo.getPartSize();
		for(;unitIndex < this.storeInfo.getPartCount(); unitIndex++){
			if (currentUnitIndex != unitIndex) {
				this.readUnit(unitIndex);
			}
			if(this.position != this.startPos){
				int offset = (int)(this.position - this.startPos);
				byte[] b = Arrays.copyOfRange(buffer, offset, buffer.length);
				outputStream.write(b, 0, b.length);
				position += buffer.length;
			}else{
				outputStream.write(buffer, 0, buffer.length);
				position += buffer.length;
			}
		}
	}

	private void readUnit(long unitIndex) throws StoreException {
		Query q = new Query(Criteria.where("storeId").is(this.storeInfo.getId())
				.andOperator(Criteria.where("index").is(unitIndex)));
		StorePart unit = mongoTemplate.findOne(q, StorePart.class);
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
