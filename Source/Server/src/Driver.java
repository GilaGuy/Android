/*------------------------------------------------------------------------------------------------------------------
-- SOURCE FILE: Driver.java - The entry-point for the server that launches the ListenSocket thread. 
--
-- PROGRAM: gps_server.jar
--
-- FUNCTIONS:
--		public static void main(String[] args);
--
-- DATE: March 3, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- NOTES: This program is a simple server accepting UDP data from any number of 
-- mobile clients. It puts the data into an XML file for a website to use.
--
-- This server must be placed in the same directory as the website's index.html page.
----------------------------------------------------------------------------------------------------------------------*/


import java.io.IOException;

public class Driver 
{
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: main
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public static void main(String[] args);
	--
	-- PARAMETERS:
	--		args - an array of string command line arguments.
	--
	-- RETURNS: void
	--
	-- NOTES:
	--     This function validates command line arguments and launches the Socket thread.
	----------------------------------------------------------------------------------------------------------------------*/
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
