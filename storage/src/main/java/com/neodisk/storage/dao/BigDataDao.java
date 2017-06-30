package com.neodisk.storage.dao;

import com.neodisk.domain.BigData;

public interface BigDataDao {
	public void save(BigData bigData);
	public BigData findOne(String id);
	public void delete(String id);
}
