package com.client.handlers;

public class ResponseHandlerUpload extends ResponseHandler
{
	private String message;

	public ResponseHandlerUpload(String status, String message)
	{
		super("Upload", status);
		this.message = message;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
