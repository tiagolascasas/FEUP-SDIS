package com.client.handlers;

import java.util.Base64;

import com.client.ClientManager;

public class ResponseHandlerNotification extends ResponseHandler
{
	private String message;

	public ResponseHandlerNotification(String message)
	{
		super("NOTIF", "1");
		this.message = message;
	}

	@Override
	public void run()
	{
		this.message = new String(Base64.getDecoder().decode(this.message));
		ClientManager.getInstance().log(this.message);
	}
}
