package com.client.handlers;

import com.client.ClientManager;

public class ResponseHandlerTransmit extends ResponseHandler 
{
	private String track;
	private String ip;
	private int port;

	public ResponseHandlerTransmit(String track, String addr) 
	{
		super("TRANSMIT", "0");
		this.track = track;
		this.ip = addr.split(":")[0];
		this.port = Integer.parseInt(addr.split(";")[1]);
	}

	@Override
	public void run() 
	{
		ClientManager.getInstance().log("Sending a track to client");
	}

}
