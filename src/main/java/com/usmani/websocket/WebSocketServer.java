package com.usmani.websocket;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Rameez Usmani
 */
public class WebSocketServer {
    
    private static final String TAG = WebSocketServer.class.getSimpleName();
    
    private WebSocketClientFactory factory=new DefaultClientFactory();
    private int port=8765;
    private ServerSocket serverSocket;
    private boolean active=false;
    private WebSocketEventListener eventListener;
    
    public WebSocketServer() {
    }
    
    public WebSocketServer(int p) {
    	setPort(p);
    }
    
    public void setWebSocketEventListener(WebSocketEventListener l) {
    	eventListener=l;
    }
    
    public void setClientFactory(WebSocketClientFactory f) {
    	this.factory=f;
    }
    
    public int getPort() {
    	return port;
    }
    
    public void setPort(int p) {
    	port=p;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public void start() 
    	throws Exception {
    	serverSocket=new ServerSocket(port);
        Log.d(TAG,"Server listening on: "+port);
        active=true;
    	Thread thr=new Thread(()->{
            while(active){
                try{
                    Socket socket=serverSocket.accept();
                    Log.d(TAG,"Client connected: "+socket.getRemoteSocketAddress().toString());
                    WebSocketClient client=factory.create(socket);
                    client.setWebSocketEventListener(eventListener);
                    try {
                    	client.open();
                    	Log.d(TAG, "client opened: "+client.getId());
                    }catch(Exception ex) {
                    	ex.printStackTrace();
                    	Log.e(TAG, "Exception in client.open: "+ex.getMessage());
                    	client.close();
                    }
                }catch(Exception ex){
                    ex.printStackTrace();
                    Log.e(TAG,"Exception in serverSocket.accept: "+ex.getMessage());
                }
            }
        });
        thr.start();
    }
    
    public void stop() {
    	active=false;
    	try {
            serverSocket.close();
        }catch(Exception ex){
            ex.printStackTrace();
            Log.e(TAG,"Exception in serverSocket.close: "+ex.getMessage());
        }
    }
}
