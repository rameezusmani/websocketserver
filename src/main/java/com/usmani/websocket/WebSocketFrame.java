package com.usmani.websocket;

public class WebSocketFrame {
	
	public static byte FRAME_CONTINUATION=0x0;
	public static byte FRAME_TEXT=0x1;
	public static byte FRAME_BINARY=0x2;
	public static byte FRAME_PING=0x9;
	public static byte FRAME_PONG=0xA;
	public static byte FRAME_CLOSE=0x8;
	
	private boolean fin=false;
	private WebSocketFrameType frameType = WebSocketFrameType.UNKNOWN;
	private boolean mask=false;
	private int payloadLength;
	private byte[] maskingKey;
	private byte[] payload;
	
	public boolean isContinuation() {
		return frameType==WebSocketFrameType.CONTINUATION;
	}
	
	public boolean isText() {
		return frameType==WebSocketFrameType.TEXT;
	}
	
	public boolean isBinary() {
		return frameType==WebSocketFrameType.BINARY;
	}
	
	public boolean isPing() {
		return frameType==WebSocketFrameType.PING;
	}
	
	public boolean isPong() {
		return frameType==WebSocketFrameType.PONG;
	}
	
	public boolean isClose() {
		return frameType==WebSocketFrameType.CLOSE;
	}
	
	public boolean isFin() {
		return fin;
	}
	
	public void setFin(boolean fin) {
		this.fin=fin;
	}
	
	public WebSocketFrameType getFrameType() {
		return frameType;
	}
	
	public void setFrameType(WebSocketFrameType ft) {
		this.frameType=ft;
	}
	
	public boolean isMasked() {
		return mask;
	}
	
	public void setMasked(boolean m) {
		this.mask=m;
	}
	
	public int getPayloadLength() {
		return payloadLength;
	}
	
	public void setPayloadLength(int len) {
		this.payloadLength=len;
	}
	
	public byte[] getMaskingKey() {
		return maskingKey;
	}
	
	public void setMaskingKey(byte[] k) {
		this.maskingKey=k;
	}
	
	public byte mask(byte b,int i) {
		return (byte)(b ^ maskingKey[i%4]);
	}
	
	public byte[] getPayload() {
		return payload;
	}
	
	public void setPayload(byte[] p) {
		this.payload=p;
		this.payloadLength=p.length;
	}
	
	public void setPayload(String str) {
		this.setPayload(str.getBytes());
	}
	
	public String getPayloadAsText() {
		return new String(payload,0,payloadLength);
	}
	
	public void destroy() {
		this.payload=null;
		this.maskingKey=null;
	}
	
	public byte getFrameTypeByte() {
		if (frameType==WebSocketFrameType.CONTINUATION) {
			return FRAME_CONTINUATION;
		}else if (frameType==WebSocketFrameType.TEXT) {
			return FRAME_TEXT;
		}else if (frameType==WebSocketFrameType.BINARY) {
			return FRAME_BINARY;
		}else if (frameType==WebSocketFrameType.PING) {
			return FRAME_PING;
		}else if (frameType==WebSocketFrameType.PONG) {
			return FRAME_PONG;
		}else if (frameType==WebSocketFrameType.CLOSE) {
			return FRAME_CLOSE;
		}
		return 0xC; //no meaning
	}
	
	public static WebSocketFrameType getFrameTypeFromByte(byte opCode) {
		if (opCode==FRAME_CONTINUATION) {
			return WebSocketFrameType.CONTINUATION;
		} else if (opCode==FRAME_TEXT) {
			return WebSocketFrameType.TEXT;
		} else if (opCode==FRAME_BINARY) {
			return WebSocketFrameType.BINARY;
		} else if (opCode==FRAME_PING) {
			return WebSocketFrameType.PING;
		} else if (opCode==FRAME_PONG) {
			return WebSocketFrameType.PONG;
		} else if (opCode==FRAME_CLOSE) {
			return WebSocketFrameType.CLOSE;
		}
		return WebSocketFrameType.UNKNOWN;
	}
}
