package com.neodisk.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.neodisk.commons.IOUtils;
import com.neodisk.domain.BigData;
import com.neodisk.domain.SmallData;
import com.neodisk.service.StoreService;
import com.neodisk.storage.dao.BigDataDao;
import com.neodisk.storage.dao.SmallDataDao;

@Service
public class StoreServiceImpl implements StoreService {

	private static final long SIZE = 15 * 1024 * 1024;

	@Autowired
	private SmallDataDao smallDataDao;
	@Autowired
	private BigDataDao bigDataDao;

	@Override
	public void save(String id, long size, InputStream stream) throws IOException {
		if (size > SIZE) {
			BigData data = new BigData();
			data.setId(id);
			data.setInputStream(stream);
			bigDataDao.save(data);
		} else {
			SmallData data = new SmallData();
			data.setId(id);
			data.setData(IOUtils.toByteArray(stream));
			smallDataDao.save(data);
		}
	}

	@Override
	public void update(String id, long size, InputStream stream) throws IOException {
		SmallData smallData = smallDataDao.findOne(id);
		if (smallData != null) {
			if (size > SIZE) {
				smallDataDao.delete(id);

				BigData newBigData = new BigData();
				newBigData.setId(id);
				newBigData.setInputStream(stream);
				bigDataDao.save(newBigData);
			} else {
				smallData.setData(IOUtils.toByteArray(stream));
			}
		} else {
			BigData bigData = bigDataDao.findOne(id);
			if (bigData != null) {
				if (size > SIZE) {
					bigData.setInputStream(stream);
					bigDataDao.save(bigData);
				} else {
					bigDataDao.delete(id);
					SmallData newSmallData = new SmallData();
					newSmallData.setId(id);
					newSmallData.setData(IOUtils.toByteArray(stream));
					smallDataDao.save(newSmallData);
				}
			}
		}
	}

	@Override
	public void delete(String id) {
		smallDataDao.delete(id);
		bigDataDao.delete(id);
	}

}
