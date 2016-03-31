package com.example.shredder;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private List<String> messages;
	private Socket sock;
	private BufferedReader sockIn;
	private PrintWriter sockOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		messages = new LinkedList<String>();
		ConnectTask connectTask = new ConnectTask(this);
		connectTask.execute("isc.tsu.ru", 1987);
	}
	
	public Socket getSocket() {
		return sock;
	}
	
	public void setSocket(Socket sock) {
		this.sock = sock;
	}
	
	public BufferedReader getSockIn() {
		return sockIn;
	}
	
	public void setSockIn(BufferedReader sockIn) {
		this.sockIn = sockIn;
	}
	
	public PrintWriter getSockOut() {
		return sockOut;
	}
	
	public void setSockOut(PrintWriter sockOut) {
		this.sockOut = sockOut;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void updateText() {
		TextView textView = (TextView)findViewById(R.id.chat_area);
		StringBuffer text = new StringBuffer();
		text.append(textView.getText().toString());
		synchronized(messages) {
			for (String line: messages) {
				processMessage(line);
				text.append(line);
				text.append("\n");
			}
			messages.clear();
		}
		textView.setText(text.toString());
	}
	
	public void processMessage(String msg) {
		if (msg.startsWith("\\topic")) {
			int index = msg.indexOf(' ');
			String topic = msg.substring(index + 1);
			TextView textView = (TextView)findViewById(R.id.topic_area);
			textView.setText(topic);
		}
	}
	
	public void addMessage(String msg) {
		synchronized(messages) {
			messages.add(msg);
		}
	}
	
	public boolean sendMessage(View view) {
		EditText editText = (EditText)findViewById(R.id.input_area);
		String text = editText.getText().toString();
		addMessage(text);
		new SendTask(this).execute(text);
		updateText();
		editText.setText("");
		return true;
	}

}
