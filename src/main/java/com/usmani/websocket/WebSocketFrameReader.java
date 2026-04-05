package com.usmani.websocket;

import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;

public class WebSocketFrameReader {
	
	private static String TAG = WebSocketFrameReader.class.getSimpleName();
	
	private static byte FIN_BYTE=(byte)0x80;
	private static byte OPCODE_BYTE=(byte)0x0F;
	private static byte MASK_BYTE=(byte)0x80;
	private static byte PAYLOAD_LENGTH_BYTE=(byte)0x7F;
	
	public static WebSocketFrame read(InputStream iStream) 
			throws Exception {
		WebSocketFrame frame=new WebSocketFrame();
		readUptoPayloadLength(iStream, frame);
		//check for masking
		if (frame.isMasked()) {
			readMaskingKey(iStream,frame);
		}
		//now read the payload
		byte[] buff=new byte[1024*8]; //8K buffer
		int bytesToRead=frame.getPayloadLength();
		ByteBuffer buffer=ByteBuffer.allocate(bytesToRead);
		int bytesRead=0;
		while(true) {
			int bRead = iStream.read(buff,0,buff.length);
			if (bRead==-1) {
				throw new Exception("stream end");
			}
			if (bRead>0) {
				buffer.put(buff,0,bRead);
			}
			bytesRead+=bRead;
			if (bytesRead==bytesToRead){
				break;
			}
		}
		buff=null;
		buff=new byte[buffer.position()];
		buffer.position(0);
		buffer.get(buff,0,buff.length);
		byte[] mask=frame.getMaskingKey();
		unmask(buff,mask);
		frame.setPayload(buff);
		buffer.clear();
		return frame;
	}
	
	private static void readUptoPayloadLength(InputStream iStream,WebSocketFrame frame) 
			throws Exception {
		byte[] buff=new byte[10]; //maximum 10 bytes to read
		int bytesToRead=2;
		int bytesRead=0;
		//first read 2 bytes
		while(true) {
			int bRead=iStream.read(buff,bytesRead,bytesToRead-bytesRead);
			if (bRead==-1) {
				throw new Exception("stream end");
			}
			bytesRead+=bRead;
			if (bytesRead==bytesToRead) {
				break;
			}
		}
		//1st bit of 1st byte is FIN
		byte fin = (byte)(buff[0] & FIN_BYTE);
		frame.setFin(fin==FIN_BYTE);
		//last 4 bits of 1st byte are OPCODE
        byte opcode=(byte)(buff[0] & OPCODE_BYTE);
        WebSocketFrameType type=WebSocketFrame.getFrameTypeFromByte(opcode);
        if (type==WebSocketFrameType.UNKNOWN) {
        	throw new Exception("Unknown opcode: "+opcode);
        }
		frame.setFrameType(type);
		//1st bit of 2nd byte is mask
        byte mask=(byte)(buff[1] & MASK_BYTE);
        frame.setMasked(mask==MASK_BYTE);
        //last 7 bits of 2nd byte is first part of payload length
        byte plen1=(byte)(buff[1] & PAYLOAD_LENGTH_BYTE);
        if (plen1<0) {
        	throw new Exception("Invalid payload length: "+plen1);
        }
        if (plen1<=125) {
        	frame.setPayloadLength(Byte.toUnsignedInt(plen1));
        	return;
        }
        //plen1 is not <=125 ...read extended payload length
        readExtendedPayloadLength(iStream,frame,plen1);
	}
	
	private static void readExtendedPayloadLength(InputStream iStream,WebSocketFrame frame,byte plen1) 
			throws Exception {
		int bytesToRead=0;
		if (plen1==126) {
        	bytesToRead=2; //16 bits
        }else if (plen1==127) {
        	bytesToRead=8; //64 bits
        }else {
        	throw new Exception("Invalid payload length: "+plen1);
        }
		byte[] buff=new byte[bytesToRead];
		int bytesRead=0;
		while(true) {
			int bRead=iStream.read(buff,bytesRead,bytesToRead-bytesRead);
			if (bRead==-1) {
				throw new Exception("stream end");
			}
			bytesRead+=bRead;
			if (bytesRead==bytesToRead) {
				break;
			}
		}
		int plen=(ByteHelper.getShortFromBytes(buff,0,buff.length) & 0xFFFF);
		if (plen1==127) {
			BigInteger bg=new BigInteger(1,buff);
			plen=bg.intValue();
		}
		frame.setPayloadLength(plen);
	}
	
	private static void readMaskingKey(InputStream iStream,WebSocketFrame frame) 
			throws Exception {
		byte[] buff=new byte[4];
		int bytesToRead=4;
		int bytesRead=0;
		while(true) {
			int bRead=iStream.read(buff,bytesRead,bytesToRead-bytesRead);
			if (bRead==-1) {
				throw new Exception("stream end");
			}
			bytesRead+=bRead;
			if (bytesRead==bytesToRead) {
				break;
			}
		}
		frame.setMaskingKey(buff);
	}
	
	private static void unmask(byte[] buff,byte[] mask) {
		for (int i = 0; i < buff.length; i++) {
		    buff[i] = (byte) (buff[i] ^ mask[i % 4]);
		}
	}
}
