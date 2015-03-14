/*------------------------------------------------------------------------------------------------------------------
-- SOURCE FILE: Message.java - a message control object for client communication.
--
-- PROGRAM: gps_server.jar
--
-- FUNCTIONS:
--		public Message(String addr);
--		public void setId(String uId);
--		public void setLatitude(String l);
--		public void setLongitude(String l);
--		public void setTime(String t);
--		public String getIP();
--		public String getId();
--		public String getLatitude();
--		public String getLongitude();
--		public String getTime();
--
-- DATE: March 3, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- NOTES: This class is a bridge between the client and the server for data transfer.
----------------------------------------------------------------------------------------------------------------------*/


public class Message {
	private String ip;
	private String id;
	private String lat;
	private String lon;
	private String time;
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: Message
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public Message(String addr);
	--
	-- PARAMETERS:
	--		addr - the IP address of the client
	--
	-- RETURNS: nothing
	--
	-- NOTES:
	--     This function creates a Message object with an IP. 
	----------------------------------------------------------------------------------------------------------------------*/
	public Message(String addr)
	{
		ip = addr;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: setId
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public void setId(String uId);
	--
	-- PARAMETERS:
	--		uId - the user id
	--
	-- RETURNS: void
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public void setId(String uId)
	{
		id = uId;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: setLatitude
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public void setLatitude(String l);
	--
	-- PARAMETERS:
	--		l - the user's latitude
	--
	-- RETURNS: void
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public void setLatitude(String l)
	{
		lat = l;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: setLongitude
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public void setLongitude(String l);
	--
	-- PARAMETERS:
	--		l - the user longitude
	--
	-- RETURNS: void
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public void setLongitude(String l)
	{
		lon = l;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: setTime
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public void setTime(String t);
	--
	-- PARAMETERS:
	--		t - the user's time
	--
	-- RETURNS: void
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public void setTime(String t)
	{
		time = t;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: getIP
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public String getIP();
	--
	-- PARAMETERS:
	--
	-- RETURNS: String - the IP.
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public String getIP()
	{
		return ip;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: getId
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public String getId();
	--
	-- PARAMETERS:
	--
	-- RETURNS: String - the Id.
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public String getId()
	{
		return id;
	}
	

	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: getLatitude
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public String getLatitude();
	--
	-- PARAMETERS:
	--
	-- RETURNS: String - the latitude.
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public String getLatitude()
	{
		return lat;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: getLongitude
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public String getLongitude();
	--
	-- PARAMETERS:
	--
	-- RETURNS: String - the longitude.
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public String getLongitude()
	{
		return lon;
	}
	
	
	/*------------------------------------------------------------------------------------------------------------------
	-- FUNCTION: getTime
	--
	-- DATE: March 3, 2015
	--
	-- REVISIONS: (Date and Description)
	--
	-- DESIGNER: Chris Klassen
	--
	-- PROGRAMMER: Chris Klassen
	--
	-- INTERFACE: public String getTime();
	--
	-- PARAMETERS:
	--
	-- RETURNS: String - the time of transfer.
	--
	-- NOTES:
	----------------------------------------------------------------------------------------------------------------------*/
	public String getTime()
	{
		return time;
	}
}
