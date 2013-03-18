package com.dimond.nmea2udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import android.app.Activity;
import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class nmea2udp extends Activity {
	//var
	Button start;
	Button exit;
	TextView textGGA;
	TextView textVTG;
	TextView textRMC;
	EditText editIP;
	EditText editPort;
    String message;
    int server_port;
    DatagramSocket socket;
    InetAddress local;
    int msg_length;
    byte[] messageB;
    DatagramPacket nmeapacket;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        start = (Button) findViewById(R.id.button1);
        exit = (Button) findViewById(R.id.button2);
        textGGA = (TextView)findViewById(R.id.textGGA);
        textVTG = (TextView)findViewById(R.id.textVTG);
        textRMC = (TextView)findViewById(R.id.textRMC);
        editIP = (EditText)findViewById(R.id.editTextIP);
        editPort = (EditText)findViewById(R.id.editTextPort);

        

        
        //Start
        start.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) 
            		{
            			try{
            		        server_port = Integer.valueOf(editPort.getText().toString());
            		        socket = new DatagramSocket();
            		        local = InetAddress.getByName(editIP.getText().toString());
            			} catch(Exception e){}
            			
					}                          
            	}); 
        //stop
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View arg0) 
            		{
            			Close();
            		}                          
            	}); 
        //NMEA listener
        LocationManager LM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ((LocationManager)getSystemService(Context.LOCATION_SERVICE)).requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,new LocationListener(){
			@Override
			public void onLocationChanged(Location loc) {}
			@Override
			public void onProviderDisabled(String provider) {}
			@Override
			public void onProviderEnabled(String provider) {}
			@Override
			public void onStatusChanged(String provider, int status,Bundle extras) {} });

        LM.addNmeaListener(new GpsStatus.NmeaListener() {
        public void onNmeaReceived(long timestamp, String nmea) {
        	SendNmea2UDP(nmea);
        }});        
    }
    


    
    public void SendNmea2UDP(String nmeastring)
    {
    	message = nmeastring;
        msg_length=message.length();
        messageB = message.getBytes();
        nmeapacket = new DatagramPacket(messageB, msg_length,local,server_port);
        try
        {
        	socket.send(nmeapacket);
        }catch(Exception e) {}
        
    }
    
    public void Close()
    {
    	android.os.Process.killProcess(android.os.Process.myPid());
    }
    
}