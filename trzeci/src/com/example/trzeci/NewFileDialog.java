package com.example.trzeci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.InputType;
import android.widget.EditText;

public class NewFileDialog extends Activity{
	
	private final Activity activity;
	public NewFileDialog(Activity activity) {
		this.activity = activity;
	}
	
<<<<<<< HEAD
	
	public Dialog createNewFileDialog(){
=======
	public Dialog createNewFileDialog() {		
>>>>>>> f7d7a452b30c3fc18d1a9c7911609a04b6124842
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Enter file name");
		final EditText input = new EditText(activity);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
<<<<<<< HEAD
		
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				String t = input.getText().toString();
				//check if file exists / create file
				File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + t);
				if(!file.exists())
				{
					//et.setText(t);
				}
				
				
=======
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				//String t = input.getText().toString();
				//create file
>>>>>>> f7d7a452b30c3fc18d1a9c7911609a04b6124842
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		dialog = builder.show();
		return dialog;
	}
}
