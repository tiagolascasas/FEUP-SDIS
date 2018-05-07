package com.client.handlers;

public abstract class ResponseHandler implements Runnable
{
	private String type;
	protected String status;

	public ResponseHandler(String type, String status)
	{
		this.type = type;
		this.status = status;
	}

	@Override
	public abstract void run();
}
