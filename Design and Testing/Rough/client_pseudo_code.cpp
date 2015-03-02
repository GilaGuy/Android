// Settings Page

Tap Start Function
{
	retrieve the IP
	retrieve the port
	
	start the communication page
}


// Communication Page

OnLoad Function
{
	Call the Send Data Function
}

Send Data Function
{
	Get the current location
	if the current location cannot be retrieved
	{
		display an error message
		return false
	}
	
	Open a UDP port with the given IP and port
	if the UDP port cannot be opened
	{
		display an error message
		return false
	}
	
	Send the current location through the UDP port
	
	Set a timer for the Send Data Function
}