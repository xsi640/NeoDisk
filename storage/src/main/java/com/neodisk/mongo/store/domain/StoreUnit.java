package com.neodisk.mongo.store.domain;

import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

@CompoundIndexes({ 
	@CompoundIndex(name = "index_storeId_idx", def = "{index : 1, storeId : 1}") 
}) 
public class StoreUnit {
	@Id
	private String id;
	private long index;
	private byte[] data;
	private String storeId;

	public StoreUnit() {
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof StoreUnit) {
			StoreUnit storeUnit = (StoreUnit) obj;
			result = storeUnit.getId().equals(storeUnit.getId());
		}
		return result;
	}

	@Override
	protected void finalize() throws Throwable {
		Arrays.fill(data, (byte) 1);
		data = null;
		super.finalize();
	}
}
