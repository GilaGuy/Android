import java.io.IOException;

public class Driver 
{
	public static void main(String[] args)
	{
		// Validate the command line arguments
		if (args.length != 1)
		{
			System.err.println("Invalid command line arguments.");
			System.err.println("USAGE: java gps_server [port]");
			
			System.exit(1);
		}
		
		// Parse the port
		int port = Integer.parseInt(args[0]);
		
		// Create a thread for listening for clients
		try
		{
			Thread lSocket = new ListenSocket(port);
			lSocket.start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
