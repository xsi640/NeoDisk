package com.neodisk.service;

import java.io.IOException;
import java.io.InputStream;

public interface StoreService {
	void save(String id, long size, InputStream stream) throws IOException;

	void update(String id, long size, InputStream stream) throws IOException;

	void delete(String id);
}
