/*------------------------------------------------------------------------------------------------------------------
-- SOURCE FILE: ListenSocket.java - This file contains all Server socket and data parsing functions
--
-- PROGRAM: gps_server.jar
--
-- FUNCTIONS:
--		public ListenSocket(int p);
--		public void run();
--		private void parseMessage(Message cData, String msg);
--		private void saveData(Message cData);
--
-- DATE: March 3, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- NOTES: All data received from clients is in UDP format. This data is then placed in an
-- XML document.
----------------------------------------------------------------------------------------------------------------------*/

import java.net.*;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;

public class ListenSocket extends Thread 
{
	static final int DATAGRAM_SIZE = 1024;
	static final int MESSAGE_FIELDS = 3;

	private DatagramSocket socket;
	private boolean done;
	private int port;
	
	private  byte[] packetData;
	
	private Message clientData;
	private String clientMessage;
	private DatagramPacket clientPacket;
	private InetAddress clientAddress;

	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: ListenSocket
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public ListenSocket(int p);
	--
	-- PARAMETERS:
	--		p - the port to listen on.
	--
	-- RETURNS: nothing
	--
	-- NOTES:
	--     This function creates the listener with a specified port. 
	----------------------------------------------------------------------------------------------------------------------*/
	public ListenSocket(int p) throws IOException 
	{
		done = false;
		port = p;
		socket = new DatagramSocket(p);
	}
	

	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: run
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public void run();
	--
	-- PARAMETERS:
	--
	-- RETURNS: void
	--
	-- NOTES:
	--     This function is the entry point for the thread. It handles the receive loop
	--     and calls the parsing and saving commands.
	----------------------------------------------------------------------------------------------------------------------*/
	public void run() {
		// Print startup message
		System.out.println("Starting up on port " + port);
		
		// Capture the SIGINT signal
		Runtime.getRuntime().addShutdownHook(new Thread() 
		{
			@Override
			public void run() {
				System.out.println("Shutting down");
				done = true;
			}
		});

		// Loop until SIGINT captured
		while (!done) 
		{
			packetData = new byte[DATAGRAM_SIZE];
			clientPacket = new DatagramPacket(packetData, DATAGRAM_SIZE);
			
			try 
			{
				// Receive a datagram from a client
				socket.receive(clientPacket);
				
				// Parse the address into a client message object
				clientAddress = clientPacket.getAddress();
				clientData = new Message(clientAddress.toString());
				
				// Retrieve the message from the packet
				clientMessage = new String(packetData, 0, clientPacket.getLength());
				
				// Retrieve the current time
				Calendar cal = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				dateFormat.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
				
				clientData.setTime(dateFormat.format(cal.getTime()));
				
				// Parse the client message
				parseMessage(clientData, clientMessage);
				
				// Save the client data to the xml file
				saveData(clientData);
			}
			catch (IOException e) 
			{
				e.printStackTrace();
				break;
			}
		}
		
		// Close socket
		socket.close();
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: parseMessage
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: private void parseMessage(Message cData, String msg);
	--
	-- PARAMETERS:
	--		cData - the control data structure containing client info
	--		msg - the message to parse into the structure
	--
	-- RETURNS: void
	--
	-- NOTES:
	--     This function parses a client control message into a data structure.
	----------------------------------------------------------------------------------------------------------------------*/
	private void parseMessage(Message cData, String msg)
	{
		// Pull the data from the string
		cData.setId(msg.substring(0, msg.indexOf('`')));
		msg = msg.substring(msg.indexOf('`') + 1, msg.length());

		cData.setLatitude(msg.substring(0, msg.indexOf('`')));
		msg = msg.substring(msg.indexOf('`') + 1, msg.length());

		cData.setLongitude(msg.substring(0, msg.length()));
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: 
	--
	-- DATE: 
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: 
	--
	-- PROGRAMMER: 
	--
	-- INTERFACE: private void saveData(Message cData);
	--
	-- PARAMETERS:
	--		
	--
	-- RETURNS: 
	--
	-- NOTES:
	--     This function 
	----------------------------------------------------------------------------------------------------------------------*/
	private void saveData(Message cData)
	{

	}
}
