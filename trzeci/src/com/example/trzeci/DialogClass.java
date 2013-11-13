package com.example.trzeci;

//real time npi, bootstrap

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import com.example.trzeci.R.string;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.WindowManager;

public class DialogClass extends DialogFragment{
    private String[] mFileList;
    private File mPath = new File("/");
    private String mChosenFile;
    private static final String FTYPE = ".txt";

    private void loadFileList()
    {
    	try
    	{
    		mPath.mkdirs();
    	}
    	catch(SecurityException ex)
    	{
    		
    	}
    	if(mPath.exists())
    	{
    		FilenameFilter filter = new FilenameFilter()
    		{
    			public boolean accept(File dir, String filename)
    			{
    				 File sel = new File(dir, filename);
    	             return filename.contains(FTYPE) || sel.isDirectory();
    			}
    		};
    		mFileList = mPath.list(filter);
    	}
    	else
    	{
    		mFileList = new String[0];
    	}
    }
    
    protected Dialog onCreateDialog()
    {
    	Dialog dialog = null;
    	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    	
    	builder.setTitle("Chose file");
    	loadFileList();
    	if(mFileList == null)
    	{
    		dialog = builder.create();
    		return dialog;
    	}
    	builder.setItems(
    			mFileList, 
    			new DialogInterface.OnClickListener()
    			{
    				public void onClick(DialogInterface dialog, int which)
    				{
    					mChosenFile = mFileList[which];
    				}
    			});
    	dialog = builder.create();
    	WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
    	
    	wmlp.gravity = Gravity.TOP | Gravity.LEFT;
    	wmlp.x = 100;
    	wmlp.y = 100;
    	dialog = builder.show();
    	return dialog;
    }
}
