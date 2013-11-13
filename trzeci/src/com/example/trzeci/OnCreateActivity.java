package com.example.trzeci;



import java.io.File;

import java.util.ArrayList;

import java.util.List;

import android.R.bool;
import android.R.integer;
import android.app.AlertDialog;
import android.app.DialogFragment;

import android.app.ListActivity;

import android.content.DialogInterface;

import android.os.Bundle;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.TextView;



public class OnCreateActivity extends ListActivity {
	
	private List<String> item = null;
	private List<String> path = null;
	private String root="/";
	private TextView myPath;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_start);
        myPath = (TextView)findViewById(R.id.path);
        getDir(root);
    }

    private void getDir(String dirPath) {
    	String fullPath;
    	int dot;
    	String ext;
    	myPath.setText("  Location: " + dirPath);
    	item = new ArrayList<String>();
    	path = new ArrayList<String>();
    	File f = new File(dirPath);
    	File[] files = f.listFiles();
    	if(!dirPath.equals(root))
    	{
    		item.add(root);
    		path.add(root);
    		item.add("../");
    		path.add(f.getParent());
    	}
    	for(int i=0; i < files.length; i++)
    	{
    		File file = files[i];
    		path.add(file.getPath());
    		if(file.isDirectory())
    		{
    			item.add(file.getName() + "/");
    		}
    		else 
    		{
    			fullPath = file.getName();
    			dot = fullPath.lastIndexOf(".");
    			ext = fullPath.substring(dot + 1);
    			if(ext.equals("txt"))
    				item.add(file.getName());
    		}
    	}
    	ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.row, item);
    	setListAdapter(fileList);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	File file = new File(path.get(position));
    	if (file.isDirectory())
    	{
    		if(file.canRead())
    			getDir(path.get(position));
    		else
    		{
    			new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle("[" + file.getName() + "] folder can't be read!").setPositiveButton("OK", new DialogInterface.OnClickListener() 
    				{
    					@Override
    					public void onClick(DialogInterface dialog, int which) {
    						// TODO Auto-generated method stub
    					}
    				}).show();
    		}
    	}
    	else
    	{
    		new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher).setTitle("[" + file.getName() + "]").setPositiveButton("OK", new DialogInterface.OnClickListener() 
    			{
    				@Override
    				public void onClick(DialogInterface dialog, int which) {
    					// TODO Auto-generated method stub
    				}
    			}).show();
    	}
    }

}
