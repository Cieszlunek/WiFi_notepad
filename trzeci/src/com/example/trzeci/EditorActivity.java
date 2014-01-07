package com.example.trzeci;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;

public class EditorActivity extends Activity{
	private Button go_back;
	private EditText editText;
	private String text;
	private int cursor_start;
	private int deleted_chars;
	private CharSequence insert;
	private StringBuffer stringBuffer;
	public String fileName;
	private boolean pressed_enter = false;

	private int previous_text_length;
	
	//przy tworzeniu komponentu trzeba podaæ plik Ÿród³owy
	//public Editor(File file)
	

	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
        	fileName = extras.getString("fileName");
        }
        
        go_back = (Button) findViewById(R.id.go_back);
        editText = (EditText) findViewById(R.id.editorText);
        go_back.setOnClickListener(go_back_listener);
        text = new String();
        String abc;
        try
        {
        	//editText.setText(fileName);
        	abc = getStringFromFile(fileName);
        	editText.setText(abc);
        }
        catch(Exception ex)
        {
        	editText.setText(ex.toString());
        	//editText.setText(fileName);
        }
        int position = editText.length();
        editText.setSelection(position);
        editText.addTextChangedListener(TW);
        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
            	if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER )
            	{
            		if(!pressed_enter)
            		{
            			Log.i("Key pressed", "enter");
            			editText.append("\n");
            			pressed_enter = true;
            			return true;
            		}
            		else
            		{
            			pressed_enter = false;
            			return false;
            		}
            	}
            	
            	return false;
            }});

        stringBuffer = new StringBuffer();
        stringBuffer.append(editText.getText());
        previous_text_length = editText.getText().length();
	}
	
	
	
	@Override
	public void onBackPressed() {
		saveStringToFile(fileName, editText.getText().toString());
		super.onBackPressed();

	}
	
	private OnClickListener go_back_listener = new OnClickListener(){
		@Override
		public void onClick(View v){
			saveStringToFile(fileName, editText.getText().toString());//stringBuffer.toString());
		}
	};
	
	public void udpate_string()
	{
		//stringBuffer.insert(cursor_start, insert, 0, insert.length());
	}
	
	private TextWatcher TW = new TextWatcher(){

		@Override
		public void afterTextChanged(Editable arg0) {
			//saveStringToFile(fileName, stringBuffer.toString());
			
		}

		@Override //arg1 - start position of cursor, arg2 - number of changed characters, arg3 - length of new inserted text
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			//previous_text_length = arg3;
			// TODO Auto-generated method stub
		}

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			int temp = arg1 + arg3;
			if(temp > previous_text_length)
			{
				Log.i("key pressed", arg0.subSequence(temp - 1, temp).toString());
			}
			else if(temp == previous_text_length)
			{
				//s³owo zatwierdzone, nic nie robiæ i siê cieszyæ
			}
			else
			{
				Log.i("Key pressed", "backspace");
			}
			previous_text_length = temp;
		}
		
	};
	
	private void writeToFile(String data) {
	    try {
	    	
	        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("razdwatrzy", Context.MODE_APPEND));
	        outputStreamWriter.write(data);
	        outputStreamWriter.close();
	    }
	    catch (IOException e) {
	        Log.e("Exception", "File write failed: " + e.toString());
	    } 
	}

	
	private String getStringFromFile (String filePath)
	{
		String ret = "";
		try
		{
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filePath);
			InputStream inputStream = new FileInputStream(file);
			if(inputStream != null)
			{
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				String receiveString = "";
				StringBuilder stringBuilder = new StringBuilder();
				while((receiveString = bufferedReader.readLine()) != null)
				{
					stringBuilder.append(receiveString);
				}
				inputStream.close();
				ret = stringBuilder.toString();
			}
		}
		catch(FileNotFoundException e)
		{
			Log.e("EditorActivity", "File not found: " + e.toString());
		}
		catch(IOException e)
		{
			Log.e("EditorActivity", "Can't read file: " + e.toString());
		}
		return ret;
	}

	private int saveStringToFile(String filePath, String text)
	{
		try
		{
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + filePath);
			PrintWriter writer = new PrintWriter(file);
			writer.print("");
			writer.close();
			OutputStream os = new FileOutputStream(file);
			byte[] arra = text.getBytes();
			
			os.write(arra);
			return 1;//success
		}
		catch(Exception e)
		{
			
		}
		return 0;
	}
}
