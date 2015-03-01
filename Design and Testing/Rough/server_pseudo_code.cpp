// Initialization

Main Function
{
	open a UDP socket
	
	call the Listen Function

	close the UDP socket
}


// Read Data

Listen Function
{
	while not done
	{
		read data from the UDP socket
		
		call the Parse Data Function
		
		call the Save To File Function
	}
}


Parse Data Function
{
	Create a new Message object
	set the message id to the first part of the data string
	set the message latitude to the second part of the data string
	set the message longitude to the third part of the data string
	
	return the message
}


// Save Data

Save To File Function
{
	Open the output file
	
	search the file for the message id
	
	if the message id already exists
	{
		Update the line with the new latitude and longitude
	}
	else
	{
		Add a line to the file with the ID, latitude and longitude
	}
	
	close the output file
}


// Signal Handling

Signal Handler Function
{
	set done to true
}