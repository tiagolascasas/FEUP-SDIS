package server.handlers;

import java.net.Socket;

import server.Notifier;
import server.ServerManager;
import server.Utils;

public class HandlerDownload extends Handler
{

	private String track;

	public HandlerDownload(Socket socket, String username, String password, String track)
	{
		super("DOWNLOAD", username, password, socket);
		this.track = track;

	}

	@Override
	public void run()
	{
		log("Starting Download of file from server");

		ServerManager manager = ServerManager.getInstance();
		StringBuilder build = new StringBuilder();
		build.append("RES_DOWNLOAD ");
		
		byte data[] = manager.getTrack(this.track);
		if (data == null || data.length == 0 ) {
			String message = Utils.encode("Error: Counldn't download file from server");
			build.append(0).append(" ").append(message);
		} else {
			String message = Utils.encode("Successfully downloaded file from server");
			build.append(1).append(" ").append(message);

			String notify = "File " + track + " is now downloaded from server";
			Notifier notif = new Notifier(username, notify);
			notif.start();
		}
		build.append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}

}
