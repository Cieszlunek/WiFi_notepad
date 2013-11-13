package com.example.trzeci;

import java.io.File;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ClientActivity extends Activity {
	 
  
    private Button search;
    
    private Button create;
    
    private Button connect;
 
    private String serverIpAddress;
 
    private boolean connected = false;
    
    private String str;
 
    private Handler handler = new Handler();
    
    private File mPath;
    
    private FileDialog fileDialog;
    
    private NewFileDialog newFileDialog;
    
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

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
    }
    private OnClickListener searchListener = new OnClickListener() {
    	@Override
    	public void onClick(View v){
    		fileDialog.createFileDialog();
    	}
    };
    private OnClickListener connectListener = new OnClickListener() {
 
        @Override
        public void onClick(View v) {
            if (!connected) {
                serverIpAddress = "127.0.0.1";
                if (!serverIpAddress.equals("")) {
                	
                    Thread cThread = new Thread(new ClientThread());
                    cThread.start();
                }
                
            }
            
        }
    };
    
    private OnClickListener createListener = new OnClickListener(){
    	@Override
    	public void onClick(View v){
    		newFileDialog.createNewFileDialog();
    	}
    }
    
    
    ;
 
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
    

    
}
