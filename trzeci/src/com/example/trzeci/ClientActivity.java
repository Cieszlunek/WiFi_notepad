package com.example.trzeci;

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.InputType;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;



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
	public ListView listView;
	public Activity act;
	public String fileName;
	public boolean fileIsSelected;
	public Resources res;
	public List<String> List_opened_files;
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        
        /*
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
*/
        listView = (ListView)findViewById(R.id.list_of_opened_files);
		//List_opened_files.getResources().getStringArray(R.array.pliki_otwarte_name);
        final List<String> items = new ArrayList<String>();
        items.add("a");
        items.add("b");
        AddItemToList("plii_otwarte_name", "b");
        RefreshList();
       
        
        act = this;
        fileName = null;
        fileIsSelected = false;
        
        mPath = new File(Environment.getExternalStorageDirectory() + "//DIR//");
        fileDialog = new FileDialog(this, mPath);
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
        newFileDialog = new NewFileDialog(this);
        
        search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(searchListener);
        create = (Button) findViewById(R.id.create_button);
        create.setOnClickListener(createListener);
        connect = (Button) findViewById(R.id.connect_button);
        connect.setOnClickListener(connectListener);

    }
    public void RefreshList()
    {
    	res = getResources();
        String[] itemsa = res.getStringArray(R.array.pliki_otwarte_name);
        List<String> items = new ArrayList<String>();
        int i = itemsa.length;
        for(int j = 0; j < i; ++j)
        {
        	items.add(itemsa[j]);
        }
        
        StableArrayAdapter adapter = new StableArrayAdapter(this, R.layout.row, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
            public void onItemClick(AdapterView<?> parent, final View view,
                int position, long id) {
              
            }

          });
        /*
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
        */
    }
/*	
	@Override
	public void onResume() {
		super.onResume();
		//mReceiver = new WifiDirectBroadcastReceiver(mManager, mChannel, this);
		registerReceiver(mReceiver, mIntentFilter);
	}
	*/
/*	
	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);		
	}
*/    
    private OnClickListener searchListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
    		//DeviceListFragment devList = new DeviceListFragment();
    		//v = devList.getView();
    		fileDialog.createFileDialog();
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
    
    private OnClickListener createListener = new OnClickListener(){
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
    					Intent i = new Intent(getApplicationContext(), EditorActivity.class);
    					String temp = file.getPath();
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
    public class ClientThread implements Runnable {
        public void run() {        	
            try {
                InetAddress serverAddr = InetAddress.getByName(serverIpAddress);
                Log.d("ClientActivity", "C: Connecting...");
                Socket socket = new Socket(serverAddr, 8080);
                connected = true;
                while (connected) {
                    try {
                        Log.d("ClientActivity", "C: Sending command.");
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                        // where you issue the commands
                        
                        out.println("Hey Server!");
                        Log.d("ClientActivity", "C: Sent.");
                    } catch (Exception e) {
                        Log.e("ClientActivity", "S: Error", e);
                    }
                }
                socket.close();
                Log.d("ClientActivity", "C: Closed.");
            } catch (Exception e) {
            	//textViewClient.post(new Runnable(){public void run(){textViewClient.setText("Cant connect to server!");}});
                Log.e("ClientActivity", "C: Error", e);
                connected = false;
            }
        }
    }
    
    
    
    public void AddItemToList(String name, String path)
    {
    	//SharedPreferences.Editor editor = (Editor) getSharedPreferences(path, R.array.pliki_otwarte_name);
    	
    	//Set<String> ss = null;
    	//ss.add("a");
    	//ss.add("b");
    	//editor.putStringSet("pliki_otwarte_name", ss);
    	
    }


    public boolean isWifiP2pEnabled() {
		return isWifiP2pEnabled;
	}

	public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled) {
		this.isWifiP2pEnabled = isWifiP2pEnabled;
	}
}

