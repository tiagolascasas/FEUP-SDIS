import java.io.IOException;
import java.util.Hashtable;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class SSLServer 
{

	private String[] cypherSuite;
	private int port;
	private Hashtable <String,String> plates;
	private SSLServerSocket socket;

	public SSLServer(int port, String[] cypherSuite)
	{
		this.port = port;
		this.cypherSuite = cypherSuite;
		this.plates = new Hashtable<String,String>();
		
		SSLServerSocket s = null;  
		SSLServerSocketFactory ssf = null;  
		 
		ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();  
		 
		try 
		{  
		    s = (SSLServerSocket) ssf.createServerSocket(port);  
		}  
		catch( IOException e) 
		{  
		    System.out.println("Server - Failed to create SSLServerSocket");  
		    e.getMessage();  
		    System.exit(-1);
		}
		s.setNeedClientAuth(true);
		this.socket = s;
	}
	
	public static void main(String[] args) 
	{
		
		if (args.length < 1)
		{
			System.out.println("Usage:\tjava SSLServer <port> <cypher-suite>*\n");
			System.exit(-1);
		}
		
		int port = Integer.parseInt(args[0]);
		String[] cs = new String[args.length - 1];
		System.arraycopy(args, 1, cs, 0, args.length - 1);
		
		SSLServer server = new SSLServer(port, cs);
		server.run();
	}

	private void run() 
	{
		System.out.println("aaaa");
	}
	
	private String processRequest(String request) 
	{
		System.out.println("Request: " + request);
		request = request.trim();
		String[] tokens = request.split(" ");
		String answer = "";
		
		if (tokens[0].equals("REGISTER"))
		{
			answer = processRegister(tokens);
		}
		else if (tokens[0].equals("LOOKUP"))
		{
			answer = processLookup(tokens);
		}
		else
			answer = "Unable to parse request";
		
		return answer;
	}


	private String processLookup(String[] tokens) 
	{
		String plateName = tokens[1];
		String answer = "-1 NOT_FOUND";
		if (plates.get(plateName) != null)
		{
			answer = plates.size() + "\n";
			answer += plateName + " " + plates.get(plateName);
			
		}
		return answer;
	}

	private String processRegister(String[] tokens) 
	{
		String plateName = tokens[1];
		String ownerName = tokens[2].replace("-", " ");
		
		String answer = "-1";
		if (plates.get(plateName) == null)
		{
			plates.put(plateName, ownerName);
			answer = plates.size() + "\n";
			answer += plateName + " " + ownerName;
			
		}
		return answer;
	}
}
