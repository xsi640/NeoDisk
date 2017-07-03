package com.neodisk.service.impl;

import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neodisk.mongo.store.StoreTemplate;
import com.neodisk.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

	private static final int CHUNK_SIZE = 500 * 1024;

	@Autowired
	private StoreTemplate storeTemplate;

	@Override
	public void save(String id, InputStream stream) throws Exception {
		storeTemplate.save(id, CHUNK_SIZE, stream);
	}

	@Override
	public void read(String id, OutputStream stream) throws Exception {
		storeTemplate.read(id, stream);
	}

	@Override
	public void delete(String id) {
		storeTemplate.delete(id);
	}
}
