package com.example.trzeci;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//import com.example.trzeci.DeviceListFragment.DeviceActionListener;
//import com.example.trzeci.DeviceListFragment.WiFiPeerListAdapter;

import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.InputType;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ClientActivity extends Activity implements ConnectionInfoListener, PeerListListener {
	 
    private Button search;  
    private Button create;  
    private Button connect;
    
    private Button tryWifiButton;

 
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
	private BroadcastReceiver mReceiver = null;
	public ListView listView;
	public static Activity act;
	public String fileName;
	public boolean fileIsSelected;
	public Resources res;
	public List<String> List_opened_files;
	private Activity thisActivity = this;
	private final List<String> items = new ArrayList<String>();
	private DeviceListFragment deviceListFragment;
	public static final String TAG = "wifidirectdemo";
	private WifiP2pDevice device;

	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        
        
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        listView = (ListView)findViewById(R.id.list_of_opened_files);
		//List_opened_files.getResources().getStringArray(R.array.pliki_otwarte_name);
        //AddNewItemToLastOpenedList();

        //czyszczenie listy
        //
        //SharedPreferences sharedPref = thisActivity.getPreferences(Context.MODE_PRIVATE);
		//SharedPreferences.Editor editor = sharedPref.edit();
		//editor.remove("pliki_otwarte_name");
		//editor.commit();
        
        RefreshList();
       
        
        act = this;
        fileName = null;
        fileIsSelected = false;
        
        mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
        fileDialog.setFileEndsWith(".txt");
        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
            public void fileSelected(File file) {
            	
        		fileName = file.getName();
        		GoToEditorActivity();
        		
            }
        });
        newFileDialog = new NewFileDialog(this);
        
        
        
        search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(openFileListener);
        create = (Button) findViewById(R.id.create_button);
        create.setOnClickListener(createListener);
        connect = (Button) findViewById(R.id.connect_button);
        connect.setOnClickListener(searchListener);

        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel,
                this);
        //registerReceiver(mReceiver, mIntentFilter);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        
        tryWifiButton = (Button) findViewById(R.id.GoToWifiActivity);
        tryWifiButton.setOnClickListener(tryWifiListener);
    }
	
	
	
	private void GoToEditorActivity()
	{
		Intent i = new Intent(getApplicationContext(), EditorActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		i.putExtra("fileName", fileName);
		startActivity(i);
        Log.d(getClass().getName(), "selected file " + fileName);
	}
	
	private void AddNewItemToLastOpenedList(String name, String path)
	{
		SharedPreferences sharedPref = thisActivity.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		Set<String> s = sharedPref.getStringSet("pliki_otwarte_name", null);
		Set<String> set = new HashSet<String>();
		set.addAll(s);
		if(!set.contains(name))
		{
			set.add(name);
			editor.putStringSet("pliki_otwarte_name", set);
			editor.commit();
			Set<String> ss = sharedPref.getStringSet("pliki_otwarte_path", null);
			Set<String> sset = new HashSet<String>();
			sset.addAll(ss);
			sset.add(path);
			editor.putStringSet("pliki_otwarte_path", sset);
			editor.commit();
		}
		//editor.putStringSet("pliki_otwarte_name", s);
		//editor.commit();
		
	}
	
	
    public void RefreshList()
    {
    	//res = getResources();
        //String[] itemsa = res.getStringArray(R.array.pliki_otwarte_name);
    	SharedPreferences sharedPref = thisActivity.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		Set<String> s = sharedPref.getStringSet("pliki_otwarte_name", null);
		List<String> items = new ArrayList<String>();
        if(s!= null)
        {
        	if(s.size() > 0)
        	{
        		for(String str : s)
        		{
        			items.add(str);
        		}
        	}
        }
		
        
        StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.row, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
            public void onItemClick(AdapterView<?> parent, final View view,
                int position, long id) {
				String[] patches = res.getStringArray(R.array.pliki_otwarte_path);
				fileName = patches[position];
				GoToEditorActivity();
            }

          });
        
        
    }
	
	@Override
	public void onResume() {
		super.onResume();
		mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
		registerReceiver(mReceiver, mIntentFilter);
	}
		
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);		
	}
    
	private OnClickListener tryWifiListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent i = new Intent(getApplicationContext(), Chapter9Activity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		}
	};
	
	private OnClickListener openFileListener = new OnClickListener(){
		@Override
		public void onClick(View v) {
			fileDialog.createFileDialog();
		}
	};
	
    private OnClickListener searchListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {

    		deviceListFragment = new DeviceListFragment();
    		v = deviceListFragment.getView();
    		deviceListFragment.onInitiateDiscovery();	/* ta linijka wywala mi b��d aplikacji  */
    		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
				
				@Override
				public void onSuccess() {
					Toast.makeText(ClientActivity.this, "Discovery Initiated", 
							Toast.LENGTH_SHORT).show();
					
				}
				
				@Override
				public void onFailure(int reason) {
					Toast.makeText(ClientActivity.this, "Discovery Failed: " + reason,
							Toast.LENGTH_SHORT).show();
					
				}
			});

    		//deviceListFragment.
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
    	public void onClick(View v){
    		//newFileDialog.createNewFileDialog();
    		
    		AlertDialog.Builder builder = new AlertDialog.Builder(act);
    		builder.setTitle("Enter file name");
    		final EditText input = new EditText(act);
    		input.setInputType(InputType.TYPE_CLASS_TEXT);
    		builder.setView(input);
    		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which){
    				String t = input.getText().toString();
    				//check if file exists / create file
    				File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + t + ".txt");
    				if(!file.exists())
    				{
    					//et.setText(t);
    				}
    				try
    				{
    					file.createNewFile();
    					
    					//od�wie�a list� plik�w do przeszukiwania. bez tego od razu po stworzeniu pliku nie by�o go wida� w dialogu szukania, trzeba by�o albo reset aplikacji albo wej�� i wyj�� do innego katalogu
    					mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
    			        fileDialog = new FileDialog(thisActivity, mPath);
    			        fileDialog.setFileEndsWith(".txt");
    			        fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
    			            public void fileSelected(File file) {
    			            	Intent i = new Intent(getApplicationContext(), EditorActivity.class);
    			        		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    			        		String temp = file.getName();
    			        		i.putExtra("fileName", temp);
    			    			startActivity(i);
    			                Log.d(getClass().getName(), "selected file " + file.toString());
    			            }
    			        });
    			        newFileDialog = new NewFileDialog(thisActivity);
    					Intent i = new Intent(getApplicationContext(), EditorActivity.class);
    					fileName = t;
    	        		i.putExtra(fileName, fileName);
    					startActivity(i);
    					
    				}
    				catch(Exception ex)
    				{
    				
    				}
    			}
    		});
    		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
    			@Override
    			public void onClick(DialogInterface dialog, int which){
    				dialog.cancel();
    			}
    		});
    		Dialog dialog = builder.create();
    		dialog.show();
    	}
    };
    	
    	public class StableArrayAdapter extends ArrayAdapter<String> {

            HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

            public StableArrayAdapter(Context context, int textViewResourceId,
                List<String> objects) {
              super(context, textViewResourceId, objects);
              for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
              }
            }

            @Override
            public long getItemId(int position) {
              String item = getItem(position);
              return mIdMap.get(item);
            }

            @Override
            public boolean hasStableIds() {
              return true;
            }

          }
/*
    private OnClickListener createListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		newFileDialog.createNewFileDialog();
    	}
    };
 */
    
    //
    //context.getPackageName() wywala b��d, context nie istnieje, okomentowa�em ;)
    //

    public class ClientThread implements Runnable {
        public void run() {        	
            try {
                //InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                //Log.d("ClientActivity", "C: Connecting...");
                //Socket socket = new Socket(serverAddr, 8080);
                //connected = true;
            	ServerSocket serverSocket = new ServerSocket(8888);
            	Socket client = serverSocket.accept();
            	
            	/*
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
                 * */
                 
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

	@Override
	public void onConnectionInfoAvailable(WifiP2pInfo info) {
        String infoname = info.groupOwnerAddress.toString();
		
	}

	@Override
	public void onPeersAvailable(WifiP2pDeviceList peers) {
		for (WifiP2pDevice device : peers.getDeviceList()) {
            this.device = device;
            break;
        }
	}
	
	public static class DeviceListFragment extends ListFragment implements PeerListListener {

	    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
	    ProgressDialog progressDialog = null;
	    View mContentView = null;
	    //ListView lw;
	    private WifiP2pDevice device;

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        //lw = (ListView) findViewById(R.id.paired_devices);
	        this.setListAdapter(new WiFiPeerListAdapter(act, R.layout.row_devices, peers));

	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	        mContentView = inflater.inflate(R.layout.device_list, null);
	        return mContentView;
	    }

	    /**
	     * @return this device
	     */
	    public WifiP2pDevice getDevice() {
	        return device;
	    }

	    private static String getDeviceStatus(int deviceStatus) {
	    	Log.d(ClientActivity.TAG, "Peer status :" + deviceStatus);
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

	    /**
	     * Initiate a connection with the peer.
	     */
	    @Override
	    public void onListItemClick(ListView l, View v, int position, long id) {
	        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
	        ((DeviceActionListener) getActivity()).showDetails(device);
	    }

	    /**
	     * Array adapter for ListFragment that maintains WifiP2pDevice list.
	     */
	    private class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {

	        private List<WifiP2pDevice> items;

	        /**
	         * @param context
	         * @param textViewResourceId
	         * @param objects
	         */
	        public WiFiPeerListAdapter(Context context, int textViewResourceId,
	                List<WifiP2pDevice> objects) {
	            super(context, textViewResourceId, objects);
	            items = objects;

	        }

	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	            View v = convertView;
	            if (v == null) {
	                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(
	                        Context.LAYOUT_INFLATER_SERVICE);
	                v = vi.inflate(R.layout.row_devices, null);
	            }
	            WifiP2pDevice device = items.get(position);
	            if (device != null) {
	                TextView top = (TextView) v.findViewById(R.id.device_name);
	                TextView bottom = (TextView) v.findViewById(R.id.device_details);
	                if (top != null) {
	                    top.setText(device.deviceName);
	                }
	                if (bottom != null) {
	                   bottom.setText(getDeviceStatus(device.status));
	                }
	            }
	            return v;
	        }
	    }

	    /**
	     * Update UI for this device.
	     * 
	     * @param device WifiP2pDevice object
	     */
	    public void updateThisDevice(WifiP2pDevice device) {
	        this.device = device;
	        TextView view = (TextView) mContentView.findViewById(R.id.list_of_opened_files);
	        view.setText(device.deviceName);
	        view = (TextView) mContentView.findViewById(R.id.list_of_opened_files);
	        view.setText(getDeviceStatus(device.status));
	    }

	    @Override
	    public void onPeersAvailable(WifiP2pDeviceList peerList) {
	        if (progressDialog != null) {
	        	if (progressDialog.isShowing()) {
	        		progressDialog.dismiss();
	        	}
	        }
	        peers.clear();
	        peers.addAll(peerList.getDeviceList());
	        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
	        if (peers.size() == 0) {
	            Log.d(ClientActivity.TAG, "No devices found");
	            return;
	        }

	    }

	    public void clearPeers() {
	        peers.clear();
	        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
	    }

	    public void onInitiateDiscovery() {
	        if (progressDialog != null) {
	        	if (progressDialog.isShowing()) {
	        		progressDialog.dismiss();
	        	}
	        }
	        //Activity act = act;
	        if (act != null) {
	        	progressDialog = ProgressDialog.show(act, "Press back to cancel", "finding peers", true,
	                true, new DialogInterface.OnCancelListener() {

	                    @Override
	                    public void onCancel(DialogInterface dialog) {
	                        
	                    }
	                });
	        }
	    }

	    /**
	     * An interface-callback for the activity to listen to fragment interaction
	     * events.
	     */
	    public interface DeviceActionListener {

	        void showDetails(WifiP2pDevice device);

	        void cancelDisconnect();

	        void connect(WifiP2pConfig config);

	        void disconnect();
	    }

	}

}

