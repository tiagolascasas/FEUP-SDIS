package com.client.handlers;

import com.client.ClientManager;

public class ResponseHandlerRegister extends ResponseHandler
{
	private String msg;

	public ResponseHandlerRegister(String status, String msg)
	{
		super("RES_REGISTER", status);
		this.msg = msg;
	}

	@Override
	public void run()
	{
		int status = Integer.parseInt(this.status);
		ClientManager.getInstance().getConsole().log(this.msg);
	}
}
