package com.usmani.websocket;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class WebSocketServerDemo {
	
	private static final String TAG = WebSocketServerDemo.class.getSimpleName();
	
	public static void main(String[] args) {
    	WebSocketServerDemo s=new WebSocketServerDemo();
    	s.demo();
        Log.d(TAG,"Done!");
    }
	
	public void demo() {
		
		HashMap<String,WebSocketClient> clientsMap=new HashMap<>();
//		long timeoutMilliseconds=1000*10; //10 seconds timeout
//		Timer timeoutCheckTimer=new Timer();
//		timeoutCheckTimer.scheduleAtFixedRate(new TimerTask() {	
//			@Override
//			public void run() {
//				clientsMap.forEach((key,client)->{
//					if (client.hasNotReceivedFor(timeoutMilliseconds)) {
//						//this client instance has not received any data for timeoutMilliseconds
//						//close it
//						Log.d(TAG,client.getId()+" timed out");
//						client.timeout();
//					}
//				});
//			}
//		},5000,5000); //5 seconds timer to check the clients timeout
		
		try {
        	WebSocketServer server=new WebSocketServer();
        	server.setWebSocketEventListener(new WebSocketEventListener() {
				@Override
				public void onOpen(WebSocketClient client) {
					Log.d(TAG, "onOpen: "+client.getId());
					clientsMap.put(client.getId(), client);
				}
				
				@Override
				public boolean onHandshakeRequest(WebSocketClient client, HandshakeRequest request) {
					Log.d(TAG, "onHandshakeRequest: "+client.getId()+"::"+request.uri);
					if (request.uri.startsWith("/wrong")) {
						return false;
					}
					return true;
				}

				@Override
				public void onHandshake(WebSocketClient client, HandshakeRequest request,HandshakeResponse response) {
					Log.d(TAG, "onHandshake: "+client.getId());
				}

				@Override
				public void onMessage(WebSocketClient client, WebSocketFrame frame) {
					Log.d(TAG, "onMessage: "+client.getId());
					Log.d(TAG, "message type: "+frame.getFrameTypeByte()+"::payload length: "+frame.getPayloadLength());
				}

				@Override
				public void onError(WebSocketClient client, String err) {
					Log.d(TAG, "onError: "+client.getId()+"::"+err);					
				}

				@Override
				public void onClose(WebSocketClient client) {
					Log.d(TAG, "onClose: "+client.getId());	
					clientsMap.remove(client.getId());
				}
        	});
        	server.start();
        	System.in.read();
        	server.stop();
        }catch(Exception ex) {
        	ex.printStackTrace();
        	Log.e(TAG,"Exception: "+ex.getMessage());
        }
//		timeoutCheckTimer.cancel();
		clientsMap.clear();
	}
}