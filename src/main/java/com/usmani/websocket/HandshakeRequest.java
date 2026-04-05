package com.usmani.websocket;

public class HandshakeRequest extends HandshakeMessage {
	public String uri=""; //uri
	public String webSocketKey="";
	public String webSocketVersion="";
	
	public static HandshakeRequest create(String str) 
			throws Exception {
		String[] lines=str.split("\r\n");
		if (lines.length<4) {
			throw new Exception("Not enough request headers");
		}
		String line=lines[0].trim();
		String[] lineVals=line.split(" ");
		if (lineVals.length!=3) {
			throw new Exception("Invalid request line");
		}
		HandshakeRequest request=new HandshakeRequest();
		request.uri=lineVals[1].trim();
		for (int a=1;a<lines.length;a++) {
			line=lines[a].trim();
			String[] vals=line.split(":");
			if (vals.length<2) {
				throw new Exception("Invalid header: "+line);
			}
			String key=vals[0].trim();
			String value=vals[1].trim();
			request.set(key, value);
			if (key.toLowerCase().compareTo(HEADER_SEC_WEBSOCKET_KEY.toLowerCase())==0) {
				request.webSocketKey=value;
			}else if (key.toLowerCase().compareTo(HEADER_SEC_WEBSOCKET_VERSION.toLowerCase())==0) {
				request.webSocketVersion=value;
			}
		}
		if (!request.hasWebSocketKey()) {
			throw new Exception("Header '"+HEADER_SEC_WEBSOCKET_KEY+"' not found");
		}
		return request;
	}
	
	public boolean hasWebSocketKey() {
		return !this.webSocketKey.isEmpty();
	}
	
	public String toHttpFormat() {
		String headers=super.toHttpFormat();
		String str="GET "+uri+" HTTP/1.1\r\n";
		str+=headers;
		str+="\r\n";
		return str;
	}
}
