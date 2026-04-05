package com.usmani.websocket;

import java.net.Socket;

public class DefaultClientFactory implements WebSocketClientFactory {
	@Override
	public WebSocketClient create(Socket s) {
		return new WebSocketClient(s);
	}

}
