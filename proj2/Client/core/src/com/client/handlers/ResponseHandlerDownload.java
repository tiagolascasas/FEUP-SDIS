package com.client.handlers;

import java.util.Base64;

import com.client.ClientManager;

public class ResponseHandlerDownload extends ResponseHandler
{
	private String message;

	public ResponseHandlerDownload(String status, String title, String body)
	{
		super("RES_DOWNLOAD", status);
		this.message = title + ' ' + body;
	}

	@Override
	public void run()
	{
		if (status == 0)
			ClientManager.getInstance().setLoggedIn(true);

		String res = new String(Base64.getDecoder().decode(this.message));
		ClientManager.getInstance().log(res);
	}

}
