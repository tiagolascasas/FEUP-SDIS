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
		if (status == 1)
			ClientManager.getInstance().setLoggedIn(true);
		else if (status == 0)
		{
			ClientManager.getInstance().setLoggedIn(false);
			ClientManager.getInstance().setUsername(null);
			ClientManager.getInstance().setPassword(null);
		}
		
		String res = new String(Base64.getDecoder().decode(this.msg));
		ClientManager.getInstance().log(res);
	}
}
