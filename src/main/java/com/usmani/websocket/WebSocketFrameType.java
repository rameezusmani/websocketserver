package com.usmani.websocket;

public enum WebSocketFrameType {
	CONTINUATION,
	TEXT,
	BINARY,
	PING,
	PONG,
	CLOSE,
	UNKNOWN
}
