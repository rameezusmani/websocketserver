package com.usmani.websocket;

import java.security.MessageDigest;
import java.util.Base64;

public class HandshakeResponse extends HandshakeMessage {
	
	private static final String TAG=HandshakeResponse.class.getSimpleName();
	
	private static final String MAGIC_STRING="258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
	
	public String statusCode="";
	public String statusText="";
	
	public static HandshakeResponse create(HandshakeRequest request) 
			throws Exception {
		HandshakeResponse response=new HandshakeResponse();
		response.statusCode="101";
		response.statusText="Switching Protocols";
		response.set(HEADER_CONNECTION, "upgrade");
		response.set(HEADER_UPGRADE, "websocket");
		response.set(HEADER_SEC_WEBSOCKET_ACCEPT, generateAcceptKey(request.webSocketKey));
		return response;
	}
	
	private static String generateAcceptKey(String key) 
			throws Exception {
		return generateAcceptKey(key,MAGIC_STRING);
	}
	
	private static String generateAcceptKey(String key,String magicString) 
			throws Exception {
		String str=key+magicString;
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] hashBytes = md.digest(str.getBytes());
		String encodedString = Base64.getEncoder().encodeToString(hashBytes);
		return encodedString;
	}
	
	public String toHttpFormat() {
		String headers=super.toHttpFormat();
		String str="HTTP/1.1 "+statusCode+" "+statusText+"\r\n";
		str+=headers;
		str+="\r\n";
		return str;
	}
}
