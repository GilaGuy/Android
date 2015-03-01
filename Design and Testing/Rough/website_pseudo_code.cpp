Onload Function
{
	Query user for password

	if password is invalid
	{
		display error message
		redirect away from page
	}

	call Setup Map Function
	call Display Coordinates Function
}

Setup Map Function
{
	Create Google Map

	if Google Map fails to be created
	{
		display error message
		return
	}

	Centre map on current location
}

Display Coordinates Function
{
	Load coordinates file

	if file not found
	{
		display error message
		return
	}

	for each line in coordinates file
	{
		Create new map pin at specified coordinates
		set map pin text to specified id
	}
	
	Set a timer to call the Display Coordinates Function
}