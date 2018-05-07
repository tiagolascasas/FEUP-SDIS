package server;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

import server.utils.Pair;
import server.utils.PairList;

public class OnlineUsers implements Serializable
{
	private static final long serialVersionUID = 8931759196661021575L;
	PairList<String, Socket> list;
	
	public OnlineUsers()
	{
		this.list = new PairList<String, Socket>();
	}
	
	public synchronized void setUserOnline(String username, Socket socket)
	{
		list.pushPair(username, socket);
	}
	
	public synchronized void setUserOffline(String username)
	{
		Socket socket = list.getSecondFromFirst(username);
		list.removePair(username, socket);
	}
	
	public synchronized void setUserOffline(Socket socket)
	{
		String username = list.getFirstFromSecond(socket);
		list.removePair(username, socket);
	}
	
	public Socket getUserSocket(String username)
	{
		return list.getSecondFromFirst(username);
	}
	
	public String getUsername(Socket socket)
	{
		return list.getFirstFromSecond(socket);
	}
}
