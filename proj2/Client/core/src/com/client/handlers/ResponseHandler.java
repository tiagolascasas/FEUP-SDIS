package com.client.handlers;

public abstract class ResponseHandler implements Runnable
{
	@SuppressWarnings("unused")
	private String type;
	protected int status;

	public ResponseHandler(String type, String status)
	{
		this.type = type;
		this.status = Integer.parseInt(status);
	}

	@Override
	public abstract void run();
}
