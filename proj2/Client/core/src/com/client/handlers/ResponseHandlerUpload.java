package com.client.handlers;

public class ResponseHandlerUpload extends ResponseHandler
{
	private String message;

	public ResponseHandlerUpload(String status, String message)
	{
		super("RES_UPLOAD", status);
		this.message = message;
	}

	@Override
	public void run()
	{
		//show server message
	}

}
