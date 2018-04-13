import java.io.IOException;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLClient 
{

	private String[] cypherSuite;
	private int port;
	private SSLSocket socket;

	public SSLClient(String host, int port, String[] cypherSuite)
	{
		this.port = port;
		this.cypherSuite = cypherSuite;
		
		try 
		{
			SSLSocketFactory sslsocketfactory = (SSLSocketFactory)SSLSocketFactory.getDefault();
			SSLSocket sslSocket = (SSLSocket)sslsocketfactory.createSocket(host, port);
			this.socket = sslSocket;
		} 
		catch (IOException e) 
		{

			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		
		if (args.length < 1)
		{
			System.out.println("Usage:\tjava SSLServer <port> <cypher-suite>*\n");
			System.exit(-1);
		}
	
		int port = Integer.parseInt(args[1]);
		
		String[] op = null;
		int next = 0;
		
		if (args[2].equals("LOOKUP"))
		{
			op = new String[2];
			op[0] = "LOOKUP";
			op[1] = args[3];
			next = 4;
		}
		else if (args[2].equals("REGISTER"))
		{
			op = new String[2];
			op[0] = "REGISTER";
			op[1] = args[3];
			op[2] = args[4];
			next = 5;
		}
		else
		{
			System.out.println("Usage:\tjava SSLClient <host> <port> <oper> <opnd>* <cypher-suite>*\n");
			System.exit(-1);
		}
		
		String[] cs = new String[args.length - next];
		System.arraycopy(args, 1, cs, 0, args.length - next);
		
		SSLClient server = new SSLClient(args[0], port, cs);
		server.run(op);
	}

	private void run(String[] operands) 
	{
		String message = String.join(" ", operands);
		//socket.
		
	}
}
