package com.neodisk.storage.service;

import java.io.InputStream;
import java.io.OutputStream;

import com.neodisk.exceptions.NeoException;
import com.neodisk.mongo.store.domain.StoreInfo;

public interface StoreService {
	void save(String id, InputStream stream) throws NeoException;

	void read(String id, OutputStream stream) throws NeoException;
	
	void delete(String id);
	
	StoreInfo get(String id);
}
