package server;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class OnlineUsers implements Serializable
{
	private static final long serialVersionUID = 8931759196661021575L;
	ArrayList<String> users;
	ArrayList<Socket> sockets;
	
	public OnlineUsers()
	{
		this.users = new ArrayList<>();
		this.sockets = new ArrayList<>();
	}
	
	public synchronized void setUserOnline(String username, Socket socket)
	{
		users.add(username);
		sockets.add(socket);
	}
	
	public synchronized void setUserOffline(String username)
	{
		int index = users.indexOf(username);
		users.remove(index);
		sockets.remove(index);	
	}
	
	public synchronized void setUserOffline(Socket socket)
	{
		int index = sockets.indexOf(socket);
		users.remove(index);
		sockets.remove(index);
	}
	
	public Socket getUserSocket(String username)
	{
		int index = users.indexOf(username);
		return sockets.get(index);
	}
	
	public String getUsername(Socket socket)
	{
		int index = sockets.indexOf(socket);
		return users.get(index);
	}

	public boolean isOnline(String username)
	{
		return users.indexOf(username) > -1;
	}
	
	public ArrayList<Socket> getAllUserSockets(String excludeUser)
	{
		ArrayList<Socket> res = (ArrayList<Socket>) this.sockets.clone();
		int index = users.indexOf(excludeUser);
		res.remove(index);
		return res;
	}
}
