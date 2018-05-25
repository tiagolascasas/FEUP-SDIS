package server;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class OnlineUsers implements Serializable
{
	private static final long serialVersionUID = 8931759196661021575L;
	private static final Object IGNORE = "_ignore";
	private ArrayList<String> users;
	private ArrayList<Socket> sockets;
	
	public OnlineUsers()
	{
		this.users = new ArrayList<>();
		this.sockets = new ArrayList<>();
	}
	
	public synchronized void setUserOnline(String username, Socket socket)
	{
		if (users.indexOf(username) == -1)
		{
			users.add(username);
			sockets.add(socket);
		}
	}
	
	public synchronized void setUserOffline(String username)
	{
		if (username.equals(IGNORE))
			return;
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

	public boolean isOnline(String username, Socket socket)
	{
		return users.indexOf(username) > -1 && sockets.indexOf(socket) > -1;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Socket> getAllUserSockets(String excludeUser)
	{
		ArrayList<Socket> res = (ArrayList<Socket>) this.sockets.clone();
		int index = users.indexOf(excludeUser);
		if (index > -1)
			res.remove(index);
		return res;
	}
	
	@Override
	public String toString()
	{
		String s = "";
		for (String u : users)
			s += u + '\n';
		return s;
	}
}
