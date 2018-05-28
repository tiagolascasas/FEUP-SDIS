package com.client.handlers;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.client.ClientManager;
import com.client.Utils;

public class ResponseHandlerTransmit extends ResponseHandler 
{
	private String track;
	private String ip;
	private int port;

	public ResponseHandlerTransmit(String port, String track, String addr) 
	{
		super("TRANSMIT", "0");
		this.track = Utils.decode(track);
		this.ip = addr.split(":")[0].replaceAll("/", "");
		this.port = Integer.parseInt(port);
		System.out.println("track = " + this.track + ", ip = " + this.ip + ", port = " + this.port);
	}

	@Override
	public void run() 
	{
		ClientManager.getInstance().log("Sending a track to a client");
		try 
		{
			@SuppressWarnings("resource")
			Socket sk = new Socket(this.ip, this.port);
			String track = getTrack();
			if (track == null)
			{
				ClientManager.getInstance().log("Track not found, cannot forward to client");
				return;
			}
			String message = "TRACK " + track + "\0";
			sk.getOutputStream().write(message.getBytes());	
		} 
		catch (IOException e) 
		{
			ClientManager.getInstance().log("There was a connection error forwarding a track to a client");
		}
	}

	private String getTrack() 
	{
		byte[] data = null;
		
		try 
		{
			File file = new File("local_library/" + this.track);	
			if(!file.exists())
				return null;
			else if(!file.isFile())
				return null;
			data = Files.readAllBytes(file.toPath());
		} 
		catch (IOException e) 
		{
			return null;
		}
		String title = Utils.encode(this.track);
		String body = Utils.encode(new String(data, StandardCharsets.US_ASCII));
		return title + " " + body;
	}
}
