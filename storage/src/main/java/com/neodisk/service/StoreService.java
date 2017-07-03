package com.neodisk.service;

import java.io.InputStream;
import java.io.OutputStream;

public interface StoreService {
	void save(String id, InputStream stream) throws Exception;

	void read(String id, OutputStream stream) throws Exception;
	
	void delete(String id);
}
