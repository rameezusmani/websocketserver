package com.usmani.websocket;

/**
 *
 * @author Rameez Usmani
 */
public class Log {
	
	public static final int LOG_DEBUG=1;
	public static final int LOG_WARNING=2;
	public static final int LOG_INFO=3;
	public static final int LOG_ERROR=4;
	
	private static ILogListener _listener=null;
	
	public static void setLogListener(ILogListener listener) {
		_listener=listener;
	}
	
    public static void write(String tag,String log,int severity){
    	String date=new java.util.Date().toString();
        String fullLog=date+"::"+tag+":: "+log;
        System.out.println(fullLog);
        if (_listener!=null) {
        	_listener.onLog(date, tag, log,severity);
        }
    }
    
    public static void d(String tag,String log){
        write(tag,log,LOG_DEBUG);
    }
    
    public static void i(String tag,String log){
        write(tag,log,LOG_INFO);
    }
    
    public static void w(String tag,String log){
        write(tag,log,LOG_WARNING);
    }
    
    public static void e(String tag,String log){
        write(tag,log,LOG_ERROR);
    }
}
