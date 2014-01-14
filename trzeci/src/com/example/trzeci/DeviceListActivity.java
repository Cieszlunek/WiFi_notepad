package com.example.trzeci;

import java.util.ArrayList;
import java.util.List;

import android.net.wifi.p2p.WifiP2pDevice;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class DeviceListActivity extends Activity {

	private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    ProgressDialog progressDialog = null;
    View mContentView = null;
    ListView lw;
    private WifiP2pDevice device;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device_list, menu);
		return true;
	}
	
	private static String getDeviceStatus(int deviceStatus) {
	       // Log.d(WiFiDirectActivity.TAG, "Peer status :" + deviceStatus);
	        switch (deviceStatus) {
	            case WifiP2pDevice.AVAILABLE:
	                return "Available";
	            case WifiP2pDevice.INVITED:
	                return "Invited";
	            case WifiP2pDevice.CONNECTED:
	                return "Connected";
	            case WifiP2pDevice.FAILED:
	                return "Failed";
	            case WifiP2pDevice.UNAVAILABLE:
	                return "Unavailable";
	            default:
	                return "Unknown";

	        }
	    }
	
	public WifiP2pDevice getDevice() {
        return device;
    }
}
