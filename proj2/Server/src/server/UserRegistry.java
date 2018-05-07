package server;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class UserRegistry implements Serializable
{
	private static final long serialVersionUID = -4762588823262203258L;
	private ConcurrentHashMap<String, User> users;

	public UserRegistry()
	{
		this.users = new ConcurrentHashMap<>();
	}
	
	public boolean exists(String username)
	{
		return !(this.users.get(username) == null);
	}

	public boolean register(String username, String passwordHash)
	{
		if (this.users.get(username) != null)
			return false;
		
		User user = new User(username, passwordHash);
		this.users.put(username, user);
		return true;
	}

	public boolean verifyUser(String username, String passwordHash)
	{
		User user = this.users.get(username);
		if (user == null)
			return false;
		return user.getPassword().equals(passwordHash);
	}
	
	public void addTrackToUser(String username, String title)
	{
		User user = this.users.get(username);
		user.addTrack(title);
	}
}
