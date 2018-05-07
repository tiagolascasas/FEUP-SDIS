package com.client.handlers;

import java.net.Socket;

import com.strongjoshua.console.Console;

public class ResponseHandlerDownload extends ResponseHandler
{
	private String body;
	private String title;

	public ResponseHandlerDownload(String status, String title, String body)
	{
		super("RES_DOWNLOAD", status);
		this.title = title;
		this.body = body;
	}

	@Override
	public void run()
	{
		// TODO Auto-generated method stub

	}

}
