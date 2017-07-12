package com.neodisk.mongo.store.domain;

import org.springframework.data.annotation.Id;

import com.neodisk.commons.UUIDUtils;

public class Store {
	/**
	 * Id
	 */
	@Id
	private String id = UUIDUtils.randomUUIDString();
	private int partSize;
	private long partCount;
	private long latestPartIndex;
	private long length;
	private boolean isOk = false;

	public Store() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPartSize() {
		return partSize;
	}

	public void setPartSize(int partSize) {
		this.partSize = partSize;
	}

	public long getPartCount() {
		return partCount;
	}

	public void setPartCount(long partCount) {
		this.partCount = partCount;
	}

	public long getLatestPartIndex() {
		return latestPartIndex;
	}

	public void setLatestPartIndex(long latestPartIndex) {
		this.latestPartIndex = latestPartIndex;
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
		if (obj instanceof Store) {
			Store storeInfo = (Store) obj;
			result = storeInfo.getId().equals(this.getId());
		}
		return result;
	}
}
