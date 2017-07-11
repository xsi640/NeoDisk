package com.neodisk.mongo.store;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.neodisk.commons.UUIDUtils;
import com.neodisk.mongo.store.domain.StoreInfo;
import com.neodisk.mongo.store.domain.StoreUnit;

public class DataWriter {
	private String id;
	private int chunkSize;
	private InputStream inputStream;
	private MongoTemplate mongoTemplate;
	private byte[] buffer = null;

	public DataWriter(String id, int chunkSize, InputStream inputStream, MongoTemplate mongoTemplate) {
		this.id = id;
		this.chunkSize = chunkSize;
		this.inputStream = inputStream;
		this.mongoTemplate = mongoTemplate;
		if (chunkSize <= 0) {
			chunkSize = StoreConfig.DEFAULT_CHUNK_SIZE;
		}
		this.buffer = new byte[this.chunkSize];
	}

	public void write() throws IOException {
		int readed = -1;
		long index = 0;
		long size = 0;
		try {
			while ((readed = this.inputStream.read(buffer, 0, buffer.length)) != -1) {
				StoreUnit unit = new StoreUnit();
				unit.setId(UUIDUtils.randomUUIDString());
				unit.setIndex(index);
				unit.setStoreId(id);
				if (readed < chunkSize) {
					byte[] b = Arrays.copyOf(buffer, readed);
					unit.setData(b);
				} else {
					unit.setData(buffer);
				}
				mongoTemplate.save(unit);
				size += readed;
				index++;
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			StoreInfo storeInfo = new StoreInfo();
			storeInfo.setId(id);
			storeInfo.setChunkSize(chunkSize);
			storeInfo.setChunkCount(index);
			storeInfo.setLength(size);
			mongoTemplate.save(storeInfo);
		}
	}
}
