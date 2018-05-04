package server;

import java.net.Socket;
import java.util.ArrayList;

public class OnlineUsers 
{
	ArrayList<String> usernames;
	ArrayList<Socket> sockets;
	
	public OnlineUsers()
	{
		this.usernames = new ArrayList<>();
		this.sockets = new ArrayList<>();
	}
	
	public synchronized void setUserOnline(String username, Socket socket)
	{
		usernames.add(username);
		sockets.add(socket);
	}
	
	public synchronized void setUserOffline(String username)
	{
		int index = usernames.indexOf(username);
		usernames.remove(index);
		sockets.remove(index);
	}
	
	public synchronized void setUserOffline(Socket socket)
	{
		int index = sockets.indexOf(socket);
		usernames.remove(index);
		sockets.remove(index);
	}
	
	public Socket getUserSocket(String username)
	{
		int index = usernames.indexOf(username);
		return sockets.get(index);
	}
	
	public String getUsername(Socket socket)
	{
		int index = sockets.indexOf(sockets);
		return usernames.get(index);
	}
}
