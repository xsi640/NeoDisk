package com.neodisk.mongo.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.neodisk.commons.UUIDUtils;
import com.neodisk.mongo.store.domain.Store;
import com.neodisk.mongo.store.domain.StorePart;

public class DataWriter {
	private String id;
	private InputStream inputStream;
	private int partSize;
	private long partCount;
	private long length;
	private MongoTemplate mongoTemplate;
	private byte[] buffer = null;
	private Store store = null;

	public DataWriter(String id, int chunkSize, long length, InputStream inputStream, MongoTemplate mongoTemplate) {
		this.id = id;
		this.length = length;
		this.inputStream = inputStream;
		this.mongoTemplate = mongoTemplate;
		if(chunkSize <= 0){
			this.partSize = StoreConfig.DEFAULT_PART_SIZE;
		}else{
			this.partSize = chunkSize;
		}
		this.partCount = this.length / this.partSize + 1;
		this.buffer = new byte[this.partSize];
	}
	
	public Store getStore(){
		return this.store;
	}

	public void write() throws IOException {
		saveStoreInfo(0, false);
		int readed = -1;
		long index = 0;
		long readedSize = 0;
		try {
			while ((readed = this.inputStream.read(buffer, 0, buffer.length)) != -1) {
				StorePart unit = new StorePart();
				unit.setId(UUIDUtils.randomUUIDString());
				unit.setIndex(index);
				unit.setStoreId(id);
				if (readed < partSize) {
					byte[] b = Arrays.copyOf(buffer, readed);
					unit.setData(b);
				} else {
					unit.setData(buffer);
				}
				mongoTemplate.save(unit);
				readedSize += readed;
				index++;
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			saveStoreInfo(index, this.length == readedSize);
		}
	}
	
	private void saveStoreInfo(long latestChunkIndex, boolean isOK){
		this.store = new Store();
		this.store.setId(this.id);
		this.store.setPartSize(this.partSize);
		this.store.setPartCount(this.partCount);
		this.store.setLatestPartIndex(latestChunkIndex);
		this.store.setLength(this.length);
		this.store.setOk(isOK);
		mongoTemplate.save(this.store);
	}
}
