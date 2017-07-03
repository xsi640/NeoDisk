package com.neodisk.mongo.store.domain;

import org.springframework.data.annotation.Id;

import com.neodisk.commons.UUIDUtils;

public class StoreInfo {
	@Id
	private String id = UUIDUtils.randomUUIDString();
	private int chunkSize;
	private long chunkCount;
	private long length;

	public StoreInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getChunkSize() {
		return chunkSize;
	}

	public void setChunkSize(int chunkSize) {
		this.chunkSize = chunkSize;
	}

	public long getChunkCount() {
		return chunkCount;
	}

	public void setChunkCount(long chunkCount) {
		this.chunkCount = chunkCount;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof StoreInfo) {
			StoreInfo storeInfo = (StoreInfo) obj;
			result = storeInfo.getId().equals(this.getId());
		}
		return result;
	}
}
