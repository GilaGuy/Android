
public class Message {
	private String ip;
	private String id;
	private String lat;
	private String lon;
	private String time;
	
	public Message(String addr)
	{
		ip = addr;
	}
	
	public void setId(String uId)
	{
		id = uId;
	}
	
	public void setLatitude(String l)
	{
		lat = l;
	}
	
	public void setLongitude(String l)
	{
		lon = l;
	}
	
	public void setTime(String t)
	{
		time = t;
	}
	
	public String getIP()
	{
		return ip;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getLatitude()
	{
		return lat;
	}
	
	public String getLongitude()
	{
		return lon;
	}
	
	public String getTime()
	{
		return time;
	}
}
