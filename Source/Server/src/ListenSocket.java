import java.net.*;
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

	public ListenSocket(int p) throws IOException 
	{
		done = false;
		port = p;
		socket = new DatagramSocket(p);
	}

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
				System.out.println("Message received: " + clientMessage);
				
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
	
	
	private void parseMessage(Message cData, String msg)
	{
		// Pull the data from the string
		cData.setId(msg.substring(0, msg.indexOf('`')));
		msg = msg.substring(msg.indexOf('`') + 1, msg.length());

		cData.setLatitude(msg.substring(0, msg.indexOf('`')));
		msg = msg.substring(msg.indexOf('`') + 1, msg.length());

		cData.setLongitude(msg.substring(0, msg.indexOf('`')));
		msg = msg.substring(msg.indexOf('`') + 1, msg.length());

		cData.setTime(msg.substring(0, msg.length()));
	}
	
	
	private void saveData(Message cData)
	{
		
	}
}
