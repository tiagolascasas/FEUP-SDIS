package com.client.handlers;

import java.util.Base64;

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
		String res = new String(Base64.getDecoder().decode(this.msg));
		ClientManager.getInstance().log(res);
	}
}
