package com.neodisk.storage.dao;

import com.neodisk.domain.SmallData;

public interface SmallDataDao {
	public void save(SmallData smallData);
	public SmallData findOne(String id);
	public void delete(String id);
}
