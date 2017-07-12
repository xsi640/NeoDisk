package com.neodisk.mongo.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.domain.Store;
import com.neodisk.mongo.store.domain.StorePart;

public interface StoreTemplate {
	Store save(String id, int partSize, long length, InputStream inputStream) throws IOException;
	
	Store save(String id, int partSize, long length);
	void savePart(String id, long index, InputStream inputStream) throws IOException, StoreException;
	Store finishPart(String id) throws StoreException;
	void deletePart(String id, long index);
	
	void read(String id, long position, OutputStream outputStream) throws StoreException, IOException;
	void read(String id, OutputStream outputStream) throws StoreException, IOException;
	
	void delete(String id);
	
	Store get(String id);
	
	List<StorePart> findStorePartByStoreId(String storeId);
	List<StorePart> findStorePartByStoreId(String storeId, int firstResult, int maxResult);
	long countByStoreId(String storeId);
}
