package org.oatesonline.yaffle.data;

public class DataServiceCorruptionException extends Exception {
	
	String msg;

	public DataServiceCorruptionException(String msg) {
		super();
		this.msg = msg;
	}
}
