package com.usmani.websocket;

import java.nio.ByteBuffer;

public class ByteHelper {
	
	private static final String TAG = ByteHelper.class.getSimpleName();
	
	public static short getShortFromBytes(byte[] buff,int offset,int length) {
		return ByteBuffer.wrap(buff, offset, length).getShort();
	}
	
	public static int getIntFromBytes(byte[] buff,int offset,int length) {
		return ByteBuffer.wrap(buff, offset, length).getInt();
	}
	
	public static long getLongFromBytes(byte[] buff,int offset,int length) {
		return ByteBuffer.wrap(buff, offset, length).getLong();
	}
	
	public static byte[] getBytesFromShort(short value) {
		ByteBuffer buffer=ByteBuffer.allocate(2);
		buffer.putShort(value);
		buffer.position(0);
		byte[] ret=new byte[2];
		buffer.get(ret);
		return ret;
	}
	
	public static byte[] getBytesFromLong(long value) {
		ByteBuffer buffer=ByteBuffer.allocate(8);
		buffer.putLong(value);
		buffer.position(0);
		byte[] ret=new byte[8];
		buffer.get(ret);
		return ret;
	}
}
