package com.neodisk.mongo.store.domain;

import org.springframework.data.annotation.Id;

import com.neodisk.commons.UUIDUtils;

public class StoreInfo {
	@Id
	private String id = UUIDUtils.randomUUIDString();
	private int chunkSize;
	private long chunkCount;
	private long writeIndex;
	private long length;
	private boolean isOk;

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

	public long getWriteIndex() {
		return writeIndex;
	}

	public void setWriteIndex(long writeIndex) {
		this.writeIndex = writeIndex;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
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
