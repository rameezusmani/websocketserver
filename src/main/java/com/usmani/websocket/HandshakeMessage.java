package com.usmani.websocket;

import java.util.HashMap;

public abstract class HandshakeMessage {
	
	public static final String HEADER_UPGRADE="Upgrade";
	public static final String HEADER_CONNECTION="Connection";
	public static final String HEADER_SEC_WEBSOCKET_KEY="Sec-WebSocket-Key";
	public static final String HEADER_SEC_WEBSOCKET_VERSION="Sec-WebSocket-Version";
	public static final String HEADER_SEC_WEBSOCKET_ACCEPT="Sec-WebSocket-Accept";
	public static final String HEADER_ORIGIN="Origin";
	
	protected HashMap<String,String> headers=new HashMap<>();
	
	public void set(String key,String value) {
		headers.put(key.toLowerCase(), value);
	}
	
	public String get(String key) {
		return headers.get(key.toLowerCase());
	}
	
	public boolean has(String key) {
		return headers.containsKey(key.toLowerCase());
	}
	
	public String toHttpFormat() {
		String str="";
		for (String key : headers.keySet()) {
		    str+=key+": "+headers.get(key)+"\r\n";
		}
		return str;
	}
}
