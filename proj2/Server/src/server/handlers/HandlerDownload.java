package server.handlers;

import java.io.IOException;
import java.net.Socket;
import java.util.Base64;

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
		String message = "";
		log("Starting Download of file from server");
		
		if (ownerIsOnline())
		{
			log("Owner of track " + Utils.decode(track) + " is online; forwarded request to him instead.");
			return;
		}

		ServerManager manager = ServerManager.getInstance();
		StringBuilder build = new StringBuilder();
		build.append("RES_DOWNLOAD ");
				
		byte data[] = manager.getTrack(this.track);
		if (data == null) {
			message = Utils.encode("Error: File is not on server");
			build.append(1);
		}
		else if(data.length == 1) {
			message = Utils.encode("Error: Couldn't download file from server");
			build.append(1);
		} else {
			
	        String encodedName = new String(Base64.getEncoder().encode(this.track.getBytes()));
	        String encodedFile = new String(Base64.getEncoder().encode(data));
	        
			message = Utils.encode(encodedName + " " + encodedFile);
			build.append(1);
		}
		
		build.append(" ").append(message).append('\0');
		String s = build.toString();
		send(s);
		
		log("Exiting");
	}

	private boolean ownerIsOnline() 
	{
		String decoded = Utils.decode(track);
		String user = ServerManager.getInstance().getOwnerOfTrack(decoded);
		if (user == null)
			return false;

		if (user == this.username)
			return false;
		
		Socket s = ServerManager.getInstance().getSocketOfOnlineUser(user);
		if (s == null)
			return false;
		System.out.println("User is online on " + s.getRemoteSocketAddress());
		
		int peerPort = ServerManager.getInstance().getPeerPort(this.username);
		
		String strMsg = "TRANSMIT " + peerPort + " " + this.track + " " + socket.getRemoteSocketAddress();
		byte[] message = Utils.byteArrayAppend(strMsg.getBytes(), new byte[] {'\0'});
		try 
		{
			s.getOutputStream().write(message);
		} 
		catch (IOException e) 
		{
			return false;
		}
		return true;
	}

}
