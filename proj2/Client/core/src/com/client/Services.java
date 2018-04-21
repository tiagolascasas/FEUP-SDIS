package com.client;

import com.strongjoshua.console.CommandExecutor;
import com.strongjoshua.console.Console;
import com.strongjoshua.console.annotation.ConsoleDoc;

public class Services extends CommandExecutor 
{
	public Services(Console console)
	{
		this.console = console;
	}
	
	@ConsoleDoc(description = "Uploads a track to the server")
	public void upload()
	{
		this.console.log("You called upload");
	}
	
	public void play()
	{
		this.console.log("You called play");
	}
	
	public void search(String title)
	{
		this.console.log("You called search");
	}
}
