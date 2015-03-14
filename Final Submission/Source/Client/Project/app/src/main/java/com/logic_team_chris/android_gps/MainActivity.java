/*--------------------------------------------------------------------------------------------------
-- SOURCE FILE: MainActivity.java - This file contains all Client functionality
--
-- PROGRAM: Android-GPS.apk
--
-- CLASSES:
--      MainActivity
--      MyLocationListener
--
-- FUNCTIONS:
--		Class MainActivity:
--          public String createMessage(Location l);
--		    protected void onCreate(Bundle savedInstanceState);
--		    public void startLocationServices(View v);
--		    public void onConnect(View v);
--
--		Class MyLocationListener:
--          public void onLocationChanged(Location l);
--
-- DATE: March 14, 2015
--
-- REVISIONS: (Date and Description)
--
-- DESIGNER: Melvin Loho
--
-- PROGRAMMER: Melvin Loho
--
-- NOTES: The client sends UDP datagrams to inform the server of its new location. The client's
-- location is updated every 5 seconds or less.
--------------------------------------------------------------------------------------------------*/

package com.logic_team_chris.android_gps;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends ActionBarActivity {
    static final int DATAGRAM_SIZE = 1024;
    static final int UPDATE_INTERVAL = 5 * 1000;

    InetAddress Addr;
    DatagramSocket ClientSocket;
    boolean isConnected = false;

    String ClientString;
    byte[] PacketData;
    DatagramPacket dgram;

    String ServerIP;
    int ServerPort;
    String MyID;

    LocationListener locationListener = new MyLocationListener();
    LocationManager locationManager;


    /*----------------------------------------------------------------------------------------------
    -- FUNCTION: createMessage
    --
    -- DATE: March 14, 2015
    --
    -- REVISIONS: (Date and Description)
    --
    -- DESIGNER: Melvin Loho
    --
    -- PROGRAMMER: Melvin Loho
    --
    -- INTERFACE: public String createMessage(Location l);
    --
    -- PARAMETERS:
    --		l - the new location.
    --
    -- RETURNS: the message in String format.
    --
    -- NOTES: This function creates a message that contains the client's ID, Latitude and Longitude.
    ----------------------------------------------------------------------------------------------*/
    public String createMessage(Location l) {
        String msg;

        msg =
                MyID
                        + '`'
                        + l.getLatitude()
                        + '`'
                        + l.getLongitude()
                        + '`';

        return msg;
    }

    /*----------------------------------------------------------------------------------------------
    -- FUNCTION: onCreate
    --
    -- DATE: March 14, 2015
    --
    -- REVISIONS: (Date and Description)
    --
    -- DESIGNER: Melvin Loho
    --
    -- PROGRAMMER: Melvin Loho
    --
    -- INTERFACE: protected void onCreate(Bundle savedInstanceState);
    --
    -- PARAMETERS:
    --		savedInstanceState - The saved instance state of the activity.
    --
    -- RETURNS: void.
    --
    -- NOTES: This function handles what happens when the activity is created.
    ----------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    /*----------------------------------------------------------------------------------------------
    -- FUNCTION: startLocationServices
    --
    -- DATE: March 14, 2015
    --
    -- REVISIONS: (Date and Description)
    --
    -- DESIGNER: Melvin Loho
    --
    -- PROGRAMMER: Melvin Loho
    --
    -- INTERFACE: public void startLocationServices(View v);
    --
    -- PARAMETERS:
    --		v - the view that called this function.
    --
    -- RETURNS: void.
    --
    -- NOTES: This function starts the location services activity.
    ----------------------------------------------------------------------------------------------*/
    public void startLocationServices(View v) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    /*----------------------------------------------------------------------------------------------
    -- FUNCTION: onConnect
    --
    -- DATE: March 14, 2015
    --
    -- REVISIONS: (Date and Description)
    --
    -- DESIGNER: Melvin Loho
    --
    -- PROGRAMMER: Melvin Loho
    --
    -- INTERFACE: public void onConnect(View v);
    --
    -- PARAMETERS:
    --		v - the view that called this function.
    --
    -- RETURNS: void.
    --
    -- NOTES: This function handles the things that need to be done
    -- when the client connects to and disconnects from the server.
    ----------------------------------------------------------------------------------------------*/
    public void onConnect(View v) {
        isConnected = !isConnected;

        if (isConnected) {
            // Get the values from the editTexts

            ServerIP = ((EditText) findViewById(R.id.input_serverIP)).getText().toString();
            String strPort = ((EditText) findViewById(R.id.input_serverPort)).getText().toString();
            if (strPort.isEmpty())
                strPort = ((EditText) findViewById(R.id.input_serverPort)).getHint().toString();

            ServerPort = Integer.valueOf(strPort);

            MyID = ((EditText) (findViewById(R.id.input_myID))).getText().toString();
            if (MyID.isEmpty()) {
                Toast.makeText(this, "Enter your ID.", Toast.LENGTH_LONG).show();
                isConnected = false;
                return;
            }

            // Get the InetAddress object from the Server IP
            try {
                Addr = InetAddress.getByName(ServerIP);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                isConnected = false;
                return;
            }

            // Create the socket
            try {
                ClientSocket = new DatagramSocket();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                isConnected = false;
                return;
            }

            // Start updating my location
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 10, locationListener);

            //Disable fields
            findViewById(R.id.input_serverIP).setEnabled(false);
            findViewById(R.id.input_serverPort).setEnabled(false);
            findViewById(R.id.input_myID).setEnabled(false);

            ((TextView) findViewById(R.id.btn_connect)).setText("Disconnect");
        } else {
            // Close the socket
            ClientSocket.close();

            // Stop updating my location
            locationManager.removeUpdates(locationListener);

            //Enable fields
            findViewById(R.id.input_serverIP).setEnabled(true);
            findViewById(R.id.input_serverPort).setEnabled(true);
            findViewById(R.id.input_myID).setEnabled(true);

            ((TextView) findViewById(R.id.btn_connect)).setText("Connect");
        }
    }

    private final class MyLocationListener implements LocationListener {
        /*------------------------------------------------------------------------------------------
        -- FUNCTION: onLocationChanged
        --
        -- DATE: March 14, 2015
        --
        -- REVISIONS: (Date and Description)
        --
        -- DESIGNER: Melvin Loho
        --
        -- PROGRAMMER: Melvin Loho
        --
        -- INTERFACE: public void onLocationChanged(Location l);
        --
        -- PARAMETERS:
        --		l - the new location
        --
        -- RETURNS: void.
        --
        -- NOTES: This function occurs when the location of the client has changed.
        -- It sends a message to the server containing new information about the client.
        ------------------------------------------------------------------------------------------*/
        @Override
        public void onLocationChanged(Location l) {
            // Create message string
            ClientString = createMessage(l);

            // Create packet data
            PacketData = new byte[DATAGRAM_SIZE];
            System.arraycopy(ClientString.getBytes(), 0, PacketData, 0, ClientString.length());

            // Create datagram packet
            dgram = new DatagramPacket(PacketData, PacketData.length, Addr, ServerPort);

            // Send the packet
            new AsyncTask<Void, String, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    try {
                        ClientSocket.send(dgram);
                    } catch (Exception e) {
                        publishProgress(e.getMessage());
                    }
                    return null;
                }

                @Override
                protected void onProgressUpdate(String... progress) {
                    Toast.makeText(MainActivity.this, progress[0], Toast.LENGTH_LONG).show();
                }
            }.execute();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    }
}
