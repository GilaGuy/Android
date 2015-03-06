package com.logic_team_chris.android_gps;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

    String ServerName;
    int ServerPort;
    String ClientString = "Hello World!!";
    String ServerString;
    byte[] PacketData;
    InetAddress Addr;
    DatagramSocket ClientSocket;
    DatagramPacket dgram;
    boolean isConnected = false;

    static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            // Get the IP address of the Server
            try {
                Addr = InetAddress.getByName(ServerName);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                isConnected = false;
                return;
            }

            try {
                ClientSocket = new DatagramSocket();
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                isConnected = false;
                return;
            }

            PacketData = new byte[DATAGRAM_SIZE];
            System.arraycopy(ClientString.getBytes(), 0, PacketData, 0, ClientString.length());

            // Create the complete datagram
            dgram = new DatagramPacket(PacketData, PacketData.length, Addr, ServerPort);

            new SendDataPeriodically().execute();

            ((TextView)findViewById(R.id.btn_connect)).setText("Disconnect");
        }
        else
        {
            ClientSocket.disconnect();

            ((TextView)findViewById(R.id.btn_connect)).setText("Connect");

            Toast.makeText(this, "" + MainActivity.counter, Toast.LENGTH_SHORT).show();
        }
    }

    private class SendDataPeriodically extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            MainActivity.counter = 0;
            while(isConnected && MainActivity.counter < Integer.MAX_VALUE) {
                ++MainActivity.counter;
            }
            return null;
        }
    }
}
