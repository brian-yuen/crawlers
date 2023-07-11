package com.norconex.crawler.core.store.impl.ksqldb;

import com.google.gson.Gson;

class TestObject {
	
	private static final Gson GSON = new Gson();
	
	public TestObject(String reference, int count, boolean valid) {
		super();
		this.reference = reference;
		this.count = count;
		this.valid = valid;
	}
	private String reference;
	private int count;
	private boolean valid;
	
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public boolean isValid() {
		return valid;
	}
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public String toJson() {
		return GSON.toJson(this);
	}
}
