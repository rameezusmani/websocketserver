package com.usmani.websocket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WebSocketClient {
	
    private static final String TAG = WebSocketClient.class.getSimpleName();
    
    private Socket socket;
    private InputStream iStream;
    private OutputStream oStream;
    private ClientState state;
    private String clientId = "";
    
    private Map<String,Object> properties=new HashMap<>();
    
    private WebSocketEventListener listener;

    public WebSocketClient(Socket s) {
        this.socket = s;
        this.state = ClientState.INITIALIZED;
        this.clientId=UUID.randomUUID().toString();
    }
    
    public void setWebSocketEventListener(WebSocketEventListener l) {
    	this.listener=l;
    }
    
    public boolean isOpened() {
    	return state!=ClientState.CLOSED;
    }
    
    public Socket getSocket() {
    	return this.socket;
    }
    
    public String getId() {
    	return this.clientId;
    }
    
    public ClientState getState() {
    	return this.state;
    }
    
    public Object get(String key) {
    	return properties.get(key);
    }
    
    public void set(String key,Object value) {
    	properties.put(key, value);
    }
   
    public void remove(String key) {
    	properties.remove(key);
    }
    
    public boolean has(String key) {
    	return properties.containsKey(key);
    }

    public void open() 
    	throws Exception {
    	iStream = socket.getInputStream();
		oStream = socket.getOutputStream();
		if (listener!=null) {
			listener.onOpen(this);
		}
    	Thread thr=new Thread(()->{
    		this.state=ClientState.HANDSHAKE;
    		try {
    			performHandshake();
    		}catch(Exception ex) {
    			ex.printStackTrace();
    			onError("Error in performHandshake: "+ex.getMessage());
    			close();
    			return;
    		}
    		//handshake done...ready to send/receive websocket frames
    		this.state=ClientState.READY;
    		while(true) {
    			try {
    				WebSocketFrame frame=WebSocketFrameReader.read(iStream);
    				onFrame(frame);
    			}catch(Exception ex) {
    				ex.printStackTrace();
    				onError("Exception in WebSocketFrameReader.read: "+ex.getMessage());
    				close();
    				return;
    			}
    		}
    	});
    	thr.start();
    }
    
    private void performHandshake() 
    		throws Exception {
    	String requestStr="";
    	byte[] buff=new byte[1024];
    	while(true) {
    		int bRead=read(buff,0,1024);
    		if (bRead==-1) {
    			throw new Exception("stream end");
    		}
    		if (bRead>0) {
    			requestStr+=new String(buff,0,bRead);
    		}
    		if (requestStr.endsWith("\r\n\r\n")) {
    			break;
    		}
    	}
    	HandshakeRequest request=HandshakeRequest.create(requestStr);
    	Log.d(TAG, "handshake request created");
    	HandshakeResponse response=HandshakeResponse.create(request);
		String str=response.toHttpFormat();
		write(str.getBytes());
		Log.d(TAG, "handshake response sent");
		if (listener!=null) {
			listener.onHandshake(this,request,response);
		}
    }
    
    protected void onFrame(WebSocketFrame frame) {
    	Log.d(TAG, "onFrame");
    	if (listener!=null) {
    		listener.onMessage(this, frame);
    	}
    }
    
    protected void onError(String msg) {
    	Log.e(TAG, msg);
    	if (listener!=null) {
    		listener.onError(this, msg);
    	}
    }
    
    public int read(byte[] buff,int off,int len) 
    		throws Exception {
    	return iStream.read(buff,off,len);
    }
    
    public void write(byte[] buff) 
    		throws Exception {
    	write(buff,0,buff.length);
    }
    
    public void write(byte[] buff,int off,int len)
            throws Exception {
        oStream.write(buff,off,len);
    }

    public void close() {
    	try {
    		iStream.close();
    	}catch(Exception ex) {}
    	try {
    		oStream.close();
    	}catch(Exception ex) {}
        try {
            socket.close();
        } catch (Exception ex) {
        }
        properties.clear();
        this.state = ClientState.CLOSED;
        if (listener!=null) {
        	listener.onClose(this);
        }
    }
}
