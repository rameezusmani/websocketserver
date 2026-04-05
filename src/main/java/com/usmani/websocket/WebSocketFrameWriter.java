package com.usmani.websocket;

import java.io.OutputStream;

public class WebSocketFrameWriter {
	
	private static String TAG = WebSocketFrameWriter.class.getSimpleName();
	
	public static void write(WebSocketFrame frame,OutputStream oStream) 
			throws Exception {
		byte b1=0x00;
		if (frame.isFin()) {
			b1=(byte)0x80;
		}
		b1=(byte)(b1 | frame.getFrameTypeByte());
		oStream.write(b1);
		byte b2=(byte)0x00;
		if (frame.isMasked()) {
			b2=(byte)0x80;
		}
		int plen=frame.getPayloadLength();
		//11010000
		if (plen<=125) {
			b2=(byte)(b2 | plen);
			oStream.write(b2);
		}else if (plen<=65535) {
			//write 126
			oStream.write(b2 | 126);
			//write 2 bytes of length
			oStream.write(ByteHelper.getBytesFromShort((short)plen),0,2);
		}else {
			//write 127
			oStream.write(b2 | 127);
			//write 8 bytes of length
			oStream.write(ByteHelper.getBytesFromLong((long)plen),0,8);
		}
		if (frame.isMasked()) {
			//write masking key
			byte[] key=frame.getMaskingKey();
			for (int a=0;a<key.length;a++) {
				oStream.write(key[a]);
			}
		}
		byte[] pl=frame.getPayload();
		for (int a=0;a<plen;a++) {
			byte b=pl[a];
			if (frame.isMasked()) {
				b=frame.mask(b,a);
			}
			oStream.write(b);
		}		
	}
}
