package com.client.handlers;

public abstract class ResponseHandler implements Runnable
{
	private String type;

	public ResponseHandler(String type)
	{
		this.type = type;
	}

	@Override
	public abstract void run();
}
