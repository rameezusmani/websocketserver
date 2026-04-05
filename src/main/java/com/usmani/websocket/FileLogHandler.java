package com.usmani.websocket;

import java.io.FileOutputStream;

public class FileLogHandler implements ILogListener {
	
	private static final String DEFAULT_LOG_FILE="websocketserver.log";
	private FileOutputStream _fos;
	private String fileName="";
	
	public FileLogHandler() 
	throws Exception {
		this(DEFAULT_LOG_FILE);
	}
	
	public FileLogHandler(String fName) 
	throws Exception {
		fileName=fName;
		_fos=new FileOutputStream(fileName,true);
	}
	
	public void close() {
		if (_fos!=null) {
			try {
				_fos.close();
			}catch(Exception ex) {}
		}
	}
	
	private static final long LOG_FILE_MAX_BYTES=1024*1024;

	@Override
	public void onLog(String date, String tag, String log, int severity) {
		String logLine=date+"::"+tag+"::"+log+"\r\n";
		java.io.File fd=new java.io.File(fileName);
		if (fd.exists() && fd.length()>=LOG_FILE_MAX_BYTES) {
			try {
				fd.delete();
			}catch(Exception ex) {}
		}
		if (!fd.exists()) {
			close();
			_fos=null;
			try {
				if (fd.createNewFile()) {
					_fos=new FileOutputStream(fileName,true);
				}
			}catch(Exception ex) {}
		}
		if (_fos!=null) {
			try {
				_fos.write(logLine.getBytes());
			}catch(Exception ex) {
				System.out.println("Error in writing log to file: "+ex.getMessage());
			}
		}
	}
}
