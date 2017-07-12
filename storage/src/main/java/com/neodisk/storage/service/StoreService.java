package com.neodisk.storage.service;

import java.io.InputStream;

import javax.servlet.ServletOutputStream;

import com.neodisk.exceptions.NeoException;
import com.neodisk.mongo.store.domain.Store;

public interface StoreService {
	Store save(String id, long length);
	Store get(String id);
	void delete(String id);
	Store upload(String id, long length, InputStream inputStream) throws NeoException;
	Store finish(String id) throws NeoException;
	void download(String id, long position, ServletOutputStream outputStream) throws NeoException;
}
