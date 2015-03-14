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
import android.view.Menu;
import android.view.MenuItem;
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

    public String createMessage(Location l)
    {
        String msg;

        msg =
                MyID
                + '`'
                + l.getLatitude()
                + '`'
                + l.getLongitude();

        return msg;
    }

    private final class MyLocationListener implements LocationListener {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startLocationServices(View v) {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    public void onConnect(View v) {
        isConnected = !isConnected;

        if (isConnected) {
            // Get the values from the editTexts
            ServerIP = ((EditText)findViewById(R.id.input_serverIP)).getText().toString();
            String strPort = ((EditText)findViewById(R.id.input_serverPort)).getText().toString();
            if (strPort.isEmpty())
                strPort = ((EditText)findViewById(R.id.input_serverPort)).getHint().toString();

            ServerPort = Integer.valueOf(strPort);

            MyID = ((EditText)(findViewById(R.id.input_myID))).getText().toString();
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, UPDATE_INTERVAL, 10, locationListener);

            //Disable fields
            findViewById(R.id.input_serverIP).setEnabled(false);
            findViewById(R.id.input_serverPort).setEnabled(false);
            findViewById(R.id.input_myID).setEnabled(false);

            ((TextView)findViewById(R.id.btn_connect)).setText("Disconnect");
        }
        else
        {
            // Close the socket
            ClientSocket.close();

            // Stop updating my location
            locationManager.removeUpdates(locationListener);

            //Enable fields
            findViewById(R.id.input_serverIP).setEnabled(true);
            findViewById(R.id.input_serverPort).setEnabled(true);
            findViewById(R.id.input_myID).setEnabled(true);

            ((TextView)findViewById(R.id.btn_connect)).setText("Connect");
        }
    }
}
