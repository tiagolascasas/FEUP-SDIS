package server;

import java.io.Serializable;
import java.util.HashSet;

public class User implements Serializable
{
	private static final long serialVersionUID = -1545896232181127798L;
	private String username;
	private String passwordHash;
	private HashSet<String> files;
	
	public User(String username, String passwordHash)
	{
		this.username = username;
		this.passwordHash = passwordHash;
		this.files = new HashSet<>();
	}

	public void addTrack(String title)
	{
		this.files.add(title);
	}
	
	public boolean hasTrack(String title)
	{
		return files.contains(title);
	}

	public String getPassword()
	{
		return passwordHash;
	}

	public void setPassword(String passwordHash)
	{
		this.passwordHash = passwordHash;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public HashSet<String> getFiles()
	{
		return files;
	}

	public void setFiles(HashSet<String> files)
	{
		this.files = files;
	}

}
