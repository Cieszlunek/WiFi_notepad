package com.example.trzeci;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.widget.EditText;

public class NewFileDialog {
	
	private final Activity activity;
	public NewFileDialog(Activity activity) {
		this.activity = activity;
	}
	
	public Dialog createNewFileDialog() {		
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("Enter file name");
		final EditText input = new EditText(activity);
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which){
				//String t = input.getText().toString();
				//create file
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
