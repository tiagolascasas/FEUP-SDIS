package server.handlers;

import java.net.Socket;

import server.Notifier;
import server.ServerManager;
import server.Utils;

public class HandlerUpload extends Handler
{
	private String title;
	private String body;

	public HandlerUpload(Socket socket, String username, String password, String title, String body)
	{
		super("UPLOAD", username, password, socket);
		this.title = title;
		this.body = body;
	}

	@Override
	public void run()
	{
		log("Starting Upload of file");
		
		ServerManager manager = ServerManager.getInstance();
		StringBuilder build = new StringBuilder();
		build.append("RES_UPLOAD ");

		byte[] data = body.getBytes();
		if (!manager.saveTrack(username, title, data)) {
			String message = Utils.encode("Error: Couldn't upload file to server");
			build.append(0).append(" ").append(message);
		} else
		{
			String message = Utils.encode("Successfully uploaded file to server");
			build.append(1).append(" ").append(message);
			
			String notify = "File " + title + " is now saved on server";
			Notifier notif = new Notifier(username, notify);
			notif.start();
		}
		build.append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}

}
