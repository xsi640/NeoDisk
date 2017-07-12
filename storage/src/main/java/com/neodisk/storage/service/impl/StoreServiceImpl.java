package com.neodisk.storage.service.impl;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neodisk.api.APIMsgType;
import com.neodisk.exceptions.NeoException;
import com.neodisk.mongo.exceptions.StoreException;
import com.neodisk.mongo.store.StoreTemplate;
import com.neodisk.mongo.store.domain.Store;
import com.neodisk.storage.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {

	private static final int PART_SIZE = 500 * 1024;
	private static Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

	@Autowired
	private StoreTemplate storeTemplate;

	@Override
	public Store save(String id, long length) {
		return storeTemplate.save(id, PART_SIZE, length);
	}

	@Override
	public Store get(String id) {
		return storeTemplate.get(id);
	}

	@Override
	public void delete(String id) {
		storeTemplate.delete(id);
	}

	@Override
	public Store upload(String id, long length, InputStream inputStream) throws NeoException {
		try {
			return storeTemplate.save(id, PART_SIZE, length, inputStream);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new NeoException("save store error.", APIMsgType.inner_store_error);
		}
	}

	@Override
	public Store finish(String id) throws NeoException {
		try {
			return storeTemplate.finishPart(id);
		} catch (StoreException e) {
			logger.error(e.getMessage());
			throw new NeoException("store not found. id:" + id, APIMsgType.inner_store_not_found);
		}
	}

	@Override
	public void download(String id, long position, ServletOutputStream outputStream) throws NeoException {
		try {
			storeTemplate.read(id, position, outputStream);
		} catch (StoreException | IOException e) {
			logger.error(e.getMessage());
			throw new NeoException("read store error id:" + id, APIMsgType.inner_store_error);
		}
	}

}
