package com.usmani.websocket;

import java.net.Socket;

public interface WebSocketClientFactory {
	public WebSocketClient create(Socket s);
}
