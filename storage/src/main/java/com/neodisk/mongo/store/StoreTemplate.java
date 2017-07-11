package com.neodisk.mongo.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.domain.StoreInfo;

public interface StoreTemplate {
	void save(String id, int chunkSize, long size, InputStream inputStream) throws IOException;
	void read(String id, OutputStream outputStream) throws StoreException, IOException;
	void delete(String id);
	void clearUnit();
	StoreInfo get(String id);
}
