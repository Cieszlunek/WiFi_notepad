package com.example.trzeci;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ClientActivity extends Activity {
	 
    private Button search;  
    private Button create;  
    private Button connect;
    private String serverIpAddress; 
    private boolean connected = false;   
    //private String str;
    //private Handler handler = new Handler();    
    private File mPath;   
    private FileDialog fileDialog;    
    private NewFileDialog newFileDialog;
    private IntentFilter mIntentFilter;
    private Channel mChannel;
    private WifiP2pManager mManager;
    private boolean isWifiP2pEnabled;
	private BroadcastReceiver mReceiver;
	private Context context;
	private TextView statusText;
	private DeviceListFragment deviceListFragment;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".txt");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
                Log.d(getClass().getName(), "selected file " + file.toString());
            }
        });
        newFileDialog = new NewFileDialog(this);
        
        search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(searchListener);
        create = (Button) findViewById(R.id.create_button);
        create.setOnClickListener(createListener);
        connect = (Button) findViewById(R.id.connect_button);
        connect.setOnClickListener(connectListener);
        
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        //mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
    }
	
	@Override
	public void onResume() {
		super.onResume();
		//mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
		//registerReceiver(mReceiver, mIntentFilter);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		//unregisterReceiver(mReceiver);		
	}
    
    private OnClickListener searchListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		deviceListFragment = new DeviceListFragment();
    		v = deviceListFragment.getView();
    		deviceListFragment.
    		//fileDialog.createFileDialog();
    	}
    };
    
    private OnClickListener connectListener = new OnClickListener() { 
        @Override
        public void onClick(View v) {
            if (!connected) {
                serverIpAddress = "127.0.0.1";
                if (!("").equals(serverIpAddress)) {              	
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }              
            }         
        }
    };
    
    private OnClickListener createListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		newFileDialog.createNewFileDialog();
    	}
    };
 
    public class ClientThread implements Runnable {
        public void run() {        	
            try {
                //InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                //Log.d("ClientActivity", "C: Connecting...");
                //Socket socket = new Socket(serverAddr, 8080);
                //connected = true;
            	ServerSocket serverSocket = new ServerSocket(8888);
            	Socket client = serverSocket.accept();
            	
            	final File f = new File(Environment.getExternalStorageDirectory() + "/" + context.getPackageName() + 
            			"/wifip2pchared-" + System.currentTimeMillis() + ".jpg");
            	
            	File dirs = new File(f.getParent());
            	if (!dirs.exists()) {
            		dirs.mkdirs();
            	}
            	f.createNewFile();
            	InputStream inputStream = client.getInputStream();
            	//copyFile(inputStream, new FileOutputStream(f));
            	serverSocket.close();
            	//return f.getAbsolutePath();
            	
                //while (connected) {
                    //try {
                        //Log.d("ClientActivity", "C: Sending command.");
                        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        // where you issue the commands
                        
                        //out.println("Hey Server!");
                        //Log.d("ClientActivity", "C: Sent.");
                    //} catch (Exception e) {
                        //Log.e("ClientActivity", "S: Error", e);
                    //}
                //}
                //socket.close();
                //Log.d("ClientActivity", "C: Closed.");
            } catch (IOException e) {
            	//textViewClient.post(new Runnable(){public void run(){textViewClient.setText("Cant connect to server!");}});
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }


    public boolean isWifiP2pEnabled() {
		return isWifiP2pEnabled;
	}

	public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
		this.isWifiP2pEnabled = isWifiP2pEnabled;
	}
}
