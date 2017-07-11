package com.neodisk.storage.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neodisk.api.APIMsgType;
import com.neodisk.exceptions.NeoException;
import com.neodisk.exceptions.NeoIOException;
import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.StoreTemplate;
import com.neodisk.storage.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

	private static final int CHUNK_SIZE = 500 * 1024;
	private static Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

	@Autowired
	private StoreTemplate storeTemplate;

	@Override
	public void save(String id, InputStream stream) throws NeoException {
		try {
			storeTemplate.save(id, CHUNK_SIZE, stream);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new NeoIOException(e.getMessage(), APIMsgType.inner_io_error, e);
		}
	}

	@Override
	public void read(String id, OutputStream stream) throws NeoException {
		try {
			storeTemplate.read(id, stream);
		} catch (StoreException e) {
			logger.error(e.getMessage());
			throw new NeoIOException(e.getMessage(), APIMsgType.inner_store_error, e);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new NeoIOException(e.getMessage(), APIMsgType.inner_io_error, e);
		}
	}

	@Override
	public void delete(String id) {
		storeTemplate.delete(id);
	}
}
