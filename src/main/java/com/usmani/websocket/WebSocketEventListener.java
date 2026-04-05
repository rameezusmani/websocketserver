package com.usmani.websocket;

public interface WebSocketEventListener {
	public void onOpen(WebSocketClient client);
	public void onHandshake(WebSocketClient client,HandshakeRequest request,HandshakeResponse response);
	public void onMessage(WebSocketClient client,WebSocketFrame frame);
	public void onError(WebSocketClient client,String err);
	public void onClose(WebSocketClient client);
}
