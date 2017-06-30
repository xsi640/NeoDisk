package com.neodisk.domain;

import java.util.Arrays;

public class SmallData {
	private String id;
	private byte[] data;

	public SmallData() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof SmallData) {
			SmallData smallData = (SmallData) obj;
			result = smallData.getId().equals(smallData.getId());
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
