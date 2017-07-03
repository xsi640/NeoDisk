package com.neodisk.mongo.store;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.neodisk.mongo.exceptions.StoreException;

public interface StoreTemplate {
	public void save(String id, int chunkSize, InputStream inputStream) throws IOException;

	public void read(String id, OutputStream outputStream) throws StoreException, IOException;

	public void delete(String id);
	
	public void clearUnit();
}
