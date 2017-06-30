package com.neodisk.domain;

import java.io.InputStream;

public class BigData {
	private String id;
	private InputStream inputStream;

	public BigData() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	@Override
	public boolean equals(Object obj) {
		boolean flag = false;
		if(obj instanceof BigData){
			BigData storageUnit = (BigData)obj;
			flag = this.getId().equals(storageUnit.getId());
		}
		return flag;
	}
	
	@Override
	protected void finalize() throws Throwable {
		if(this.inputStream != null){
			this.inputStream.close();
		}
		super.finalize();
	}
}
