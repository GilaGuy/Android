---------------------------------------------
| Data Communications Assignment 3 - README |
---------------------------------------------

This document intends to identify all modules of the assignment, and to explain how they need to be set up in order to have a working project.

This document is broken into three parts; one for each module.


-------
WEBSITE
-------

In order to set up the website, place it in the directory that your Apache Web Server points to. Make sure that Apache is running and that any virtual hosting that is required is set up properly.

Once all other modules are set up, run the website in any browser and you will see pins on the map for each connected client.


------
SERVER
------

In order to set up the server, place it in the same directory as your website, ensuring that the Java application is in the same folder as your index.html file.

Run the server using the following command:

	java application_name [port]

Make sure that the port you use is forwarded to the router. The server will run until you kill it, reading data from clients automatically.


------
CLIENT
------

In order to set up the client, install the .apk file on your device of choice. Run the application, and fill in the fields to correspond with the server's IP address and open port.

Once you tap Connect, you will start sending location updates to the server. These will continue for as long as you remain connected.
