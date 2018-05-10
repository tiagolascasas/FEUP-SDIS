package com.client.handlers;

import java.util.Base64;

import com.client.ClientManager;

public class ResponseHandlerLogin extends ResponseHandler
{
	private String msg;

	public ResponseHandlerLogin(String status, String msg)
	{
		super("RES_LOGIN", status);
		this.msg = msg;
	}

	@Override
	public void run()
	{
		if (status == 0)
			ClientManager.getInstance().setLoggedIn(true);
		String res = new String(Base64.getDecoder().decode(this.msg));
		ClientManager.getInstance().getConsole().log(res);
	}
}
