/*------------------------------------------------------------------------------------------------------------------
-- SOURCE FILE: gps.js - Handles all user data loading and displaying onto the map.
--
-- WEBSITE: GPS Website
--
-- FUNCTIONS:
--     initialize();
--     getLocation();
--     loadUsers();
--     showPosition(pos);
--     addUser(user);
--     placeMarker(id, lat, lon);
--
-- DATE: March 1, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- NOTES:
--     This file contains all functionality related to Google Map rendering,
--     user loading and displaying, and constant updating.
----------------------------------------------------------------------------------------------------------------------*/


/* The Google Map to use */
var gMap;
var markers = new Array();


/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: $(document.ready)
--
-- DATE: January 10, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- INTERFACE: $(document).ready(function {...});
--
-- PARAMETERS:
--
-- RETURNS: void
--
-- NOTES:
--     This function is called when the document is finished loading. It is
--     responsible for initializing the map and loading the first batch of users.
----------------------------------------------------------------------------------------------------------------------*/
$(document).ready(function() 
{
	initialize();
	getLocation();
	loadUsers();
});


/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: initialize
--
-- DATE: January 10, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- INTERFACE: initialize();
--
-- PARAMETERS:
--
-- RETURNS: void
--
-- NOTES:
--     This function initializes the Google Map to centre on Vancouver.
----------------------------------------------------------------------------------------------------------------------*/
function initialize() {
    var mapOptions = {
		center: { lat: 49, lng: -123},
		zoom: 9
    };

    gMap = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
}


/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: getLocation
--
-- DATE: January 10, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- INTERFACE: getLocation();
--
-- PARAMETERS:
--
-- RETURNS: void
--
-- NOTES:
--     This function retrieves the user's location.
----------------------------------------------------------------------------------------------------------------------*/
function getLocation() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(showPosition);
    } else {
        alert("Geolocation is not supported by this browser.");
    }
}


/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: loadUsers
--
-- DATE: January 10, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- INTERFACE: loadUsers();
--
-- PARAMETERS:
--
-- RETURNS: void
--
-- NOTES:
--     This function loads all users from an XML file in the root directory.
--     It then adds them one by one to the webpage and map.
--
--     This function is called repeatedly by a timer.
----------------------------------------------------------------------------------------------------------------------*/
function loadUsers()
{
	// Clear old data
	$('#users > tbody').empty();

	for (var i = 0; i < markers.length; i++)
	{
		markers[i].setMap(null);
	}

	// Open User List XML file
	if (window.XMLHttpRequest)
	{
		xhttp = new XMLHttpRequest();
	}

	xhttp.open("GET", "users.xml", false);
	xhttp.send();
	xmlDoc = xhttp.responseXML;

	// Loop through all users in the list
	var users = xmlDoc.getElementsByTagName("user");
	for (var i = 0; i < users.length; i++)
	{
		// Create a new user
		var user = {
			uId: users[i].getElementsByTagName("id")[0].childNodes[0].nodeValue,
			lat: users[i].getElementsByTagName("lat")[0].childNodes[0].nodeValue,
			lon: users[i].getElementsByTagName("long")[0].childNodes[0].nodeValue
		};

		addUser(user);
	}

	// Set a timer for the next refresh
	window.setTimeout(loadUsers, 5000);
}


/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: showPosition
--
-- DATE: January 10, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- INTERFACE: showPosition(pos);
--
-- PARAMETERS:
--     pos - a position object containing a latitude and a longitude
--
-- RETURNS: void
--
-- NOTES:
--     This function centers the map on the user's location.
----------------------------------------------------------------------------------------------------------------------*/
function showPosition(pos)
{
	// Put the current location into a LatLng object
	var currentLocation = new google.maps.LatLng(pos.coords.latitude, pos.coords.longitude);

	gMap.setCenter(currentLocation);
}


/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: addUser
--
-- DATE: January 10, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- INTERFACE: addUser(user);
--
-- PARAMETERS:
--     user - the user object to add
--
-- RETURNS: void
--
-- NOTES:
--     This function adds a user to the table and the Google Map.
----------------------------------------------------------------------------------------------------------------------*/
function addUser(user)
{
	// Add the user to the table
	$('#users > tbody:last').append('<tr><td>' + user.uId + '</td><td>' + user.lat + '</td><td>' + user.lon + '</td></tr>');

	// Place the marker
	placeMarker(user.uId, user.lat, user.lon);
}


/*------------------------------------------------------------------------------------------------------------------
-- FUNCTION: placeMarker
--
-- DATE: January 10, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Chris Klassen
--
-- PROGRAMMER: Chris Klassen
--
-- INTERFACE: placeMarker(id, lat, lon);
--
-- PARAMETERS:
--     id - the user id
--     lat - the user latitude
--     lon - the user longitude
--
-- RETURNS: void
--
-- NOTES:
--     This function places a new marker on the Google Map.
----------------------------------------------------------------------------------------------------------------------*/
function placeMarker(id, lat, lon)
{
	// Put the marker location into a LatLng object
	var markerLocation = new google.maps.LatLng(lat, lon);

	// Create a new marker
	var marker = new google.maps.Marker({
	    position: markerLocation,
	    map: gMap,
	    title: id
	});

	markers.push(marker);
}