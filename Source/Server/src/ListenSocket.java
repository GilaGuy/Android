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
import java.util.TimeZone;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ListenSocket extends Thread 
{
	static final int DATAGRAM_SIZE = 1024;
	static final int MESSAGE_FIELDS = 3;
	static final String OUTPUT_FILE = "users.xml";

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
			
			/*try 
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
				parseMessage(clientData, clientMessage); */
				
				clientData = new Message("127.0.0.1");
				clientData.setId("5");
				clientData.setLatitude("49");
				clientData.setLongitude("-123");
				clientData.setTime("March 13");
				
				// Save the client data to the xml file
				saveData(clientData);
				
				done = true;
			/*}
			catch (IOException e) 
			{
				e.printStackTrace();
				break;
			}*/
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
	-- FUNCTION: saveData
	--
	-- DATE: March 13, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: private void saveData(Message cData);
	--
	-- PARAMETERS:
	--		cData - the control data structure to save
	--
	-- RETURNS: void
	--
	-- NOTES:
	--     This function updates the xml document to add/change a user.
	----------------------------------------------------------------------------------------------------------------------*/
	private void saveData(Message cData)
	{
		try
		{
			// Open the file
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(OUTPUT_FILE);
			
			// Get a list of users			
			NodeList users = doc.getElementsByTagName("user");
			
			// Loop through the users and see if our user already exists
			for (int i = 0; i < users.getLength(); i++)
			{
				Element user = (Element) users.item(i);
				
				// If this user already exists
				if (user.getElementsByTagName("id").item(0).getTextContent().equals(cData.getId()))
				{
					user.getElementsByTagName("lat").item(0).setTextContent(cData.getLatitude());
					user.getElementsByTagName("long").item(0).setTextContent(cData.getLongitude());
					user.getElementsByTagName("time").item(0).setTextContent(cData.getTime());

					// Save the updated file
					TransformerFactory transformerFactory = TransformerFactory.newInstance();
					Transformer transformer = transformerFactory.newTransformer();
					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(new File(OUTPUT_FILE));
					transformer.transform(source, result);
					return;
				}
			}
			
			// If our user does not exist yet, create one
			Node newUser = doc.createElement("user");
			Element newId = doc.createElement("id");
			newId.appendChild(doc.createTextNode(cData.getId()));
			Element newIP = doc.createElement("ip");
			newIP.appendChild(doc.createTextNode(cData.getIP()));
			Element newLat = doc.createElement("lat");
			newLat.appendChild(doc.createTextNode(cData.getLatitude()));
			Element newLong = doc.createElement("long");
			newLong.appendChild(doc.createTextNode(cData.getLongitude()));
			Element newTime = doc.createElement("time");
			newTime.appendChild(doc.createTextNode(cData.getTime()));
			
			newUser.appendChild(newId);
			newUser.appendChild(newIP);
			newUser.appendChild(newLat);
			newUser.appendChild(newLong);
			newUser.appendChild(newTime);

			// Add the new user to the document
			doc.getElementsByTagName("users").item(0).appendChild(newUser);
			
			// Save the updated file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(OUTPUT_FILE));
			transformer.transform(source, result);
		}
		catch (Exception e)
		{
			System.out.println("Failed to save to file.");
			return;
		}
		
	}
}
