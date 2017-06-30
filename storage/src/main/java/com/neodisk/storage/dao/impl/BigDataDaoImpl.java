package com.neodisk.storage.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Repository;

import com.mongodb.gridfs.GridFSDBFile;
import com.neodisk.domain.BigData;
import com.neodisk.storage.dao.BigDataDao;

@Repository
public class BigDataDaoImpl implements BigDataDao {

	@Autowired
	private GridFsTemplate gridFsTemplate;

	@Override
	public void save(BigData bigData) {
		gridFsTemplate.store(bigData.getInputStream(), bigData.getId());
	}

	@Override
	public BigData findOne(String id) {
		GridFSDBFile file = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
		
		BigData result = new BigData();
		result.setId(file.getFilename());
		result.setInputStream(file.getInputStream());
		return result;
	}

	@Override
	public void delete(String id) {
		gridFsTemplate.delete(Query.query(Criteria.where("_id").is(id)));
	}
}
