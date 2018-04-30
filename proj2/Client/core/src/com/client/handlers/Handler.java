package com.client.handlers;

public abstract class Handler implements Runnable
{
	private String type;

	public Handler(String type)
	{
		this.type = type;
	}

	@Override
	public abstract void run();
}
