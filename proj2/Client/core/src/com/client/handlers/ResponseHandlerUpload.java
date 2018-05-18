package com.client.handlers;

import java.util.Base64;

import com.client.ClientManager;

public class ResponseHandlerUpload extends ResponseHandler {
	private String message;

	public ResponseHandlerUpload(String status, String message) {
		super("RES_UPLOAD", status);
		this.message = message;
	}

	@Override
	public void run() 
	{		
		String res = new String(Base64.getDecoder().decode(this.message));
		ClientManager.getInstance().log(res);
	}

}
