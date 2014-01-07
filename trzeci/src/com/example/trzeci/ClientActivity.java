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
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.InputType;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
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
	private Activity thisActivity = this;
	private final List<String> items = new ArrayList<String>();

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
        search.setOnClickListener(searchListener);
        create = (Button) findViewById(R.id.create_button);
        create.setOnClickListener(createListener);
        connect = (Button) findViewById(R.id.connect_button);
        connect.setOnClickListener(connectListener);

    }
	
	private void GoToEditorActivity()
	{
		Intent i = new Intent(getApplicationContext(), EditorActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		i.putExtra("fileName", fileName);
		startActivity(i);
        Log.d(getClass().getName(), "selected file " + fileName);
	}
	
	private void AddNewItemToLastOpenedList()
	{
		//SharedPreferences sharedPref = thisActivity.getPreferences(Context.MODE_PRIVATE);
		//SharedPreferences.Editor editor = sharedPref.edit();
		//editor.putStringSet("pliki_otwarte_path", items);
	}
	
    public void RefreshList()
    {
    	res = getResources();
//    	SharedPreferences sharedPref = thisActivity.getPreferences(Context.MODE_PRIVATE);
//    	int nazwa_id = getResources().getInteger(R.array.pliki_otwarte_name);
//    	Set<String> SS = sharedPref.getStringSet("pliki_otwarte_name", null); 
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
				String[] patches = res.getStringArray(R.array.pliki_otwarte_path);
				fileName = patches[position];
				GoToEditorActivity();
            }

          });
        
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
    
	//
	//deviceListFragment wywala b³¹d - nie rozpoznaje mi co to jest, nie ma do³¹czonej biblioteki? tradycyjnie okomentowa³em :D
	//
    private OnClickListener searchListener = new OnClickListener() {
    	@Override
    	public void onClick(View v) {
/*
    		deviceListFragment = new DeviceListFragment();
    		v = deviceListFragment.getView();
*/
    		//deviceListFragment.
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
    					
    					//odœwie¿a listê plików do przeszukiwania. bez tego od razu po stworzeniu pliku nie by³o go widaæ w dialogu szukania, trzeba by³o albo reset aplikacji albo wejœæ i wyjœæ do innego katalogu
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
    //context.getPackageName() wywala b³¹d, context nie istnieje, okomentowa³em ;)
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
}

