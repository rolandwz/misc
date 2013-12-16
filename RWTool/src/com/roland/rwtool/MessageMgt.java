package com.roland.rwtool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

public class MessageMgt {
	public static List<String> messageList;
	
	public static void addMessage(Context context, String title, String message) {
		if (title == null && message == null)
			return;
		
		getMessageList(context);
		String msg = message;
		if (title != null && title.length() > 0)
			msg = title + ":" + msg;
		
		messageList.add(msg);
		storeMessages(context);
	}
	
	public static void deleteMessage(Context context, int index) {
  	  	Utils.updateLog("deleting message, index:" + index);
		getMessageList(context);
		if (index < 0 || index >= messageList.size())
			return;
		messageList.remove(index);
		storeMessages(context);
	}

	
	public static List<String> getMessageList(Context context) {
    	messageList = new ArrayList<String>();
    	
    	try {
        	File msgFile = new File(context.getExternalFilesDir(null), "messages.txt");
    		InputStream  in = new FileInputStream(msgFile);
        	JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        	reader.beginArray();     
        	while (reader.hasNext()) {
        	     reader.beginObject();
        	     String[] msgArr = new String[2];
        	     while (reader.hasNext()) {
        	    	 String name = reader.nextName();
        	    	 if (name.equals("id")) {
        	    		 msgArr[0] = Long.valueOf(reader.nextLong()).toString();
        	         } else if (name.equals("content")) {
        	    		 msgArr[1] = reader.nextString();
        	         } else {
        	             reader.skipValue();
        	         } // if
        	     } // while
        	     reader.endObject();
        	     messageList.add(msgArr[1]);
            } // while
        	
            reader.endArray();
            reader.close();
            in.close();
    		
		} catch (FileNotFoundException e) {
			messageList.add("Empty!");
			return messageList;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			messageList.add("Encoding Exception:" + e.getMessage());
			return messageList;
		} catch (IOException e) {
			e.printStackTrace();
			messageList.add("IO Exception:" + e.getMessage());
			return messageList;
		}
    	
    	return messageList;
    }
    
	public static void storeMessages(Context context) {
		try {
	    	File msgFile = new File(context.getExternalFilesDir(null), "messages.txt");
	    	msgFile.delete();
	    	OutputStream out = new FileOutputStream(msgFile);
	    	JsonWriter writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
	    	writer.setIndent("  ");
	    	writer.beginArray();
	    	long id = 0;
	    	for (String message : messageList) {
	    		writer.beginObject();
	    		writer.name("id").value(id ++);
	    		writer.name("content").value(message);
	    		writer.endObject();
	    	}
	    	writer.endArray();
	    	writer.close();
	    	out.close();
		} catch (IOException e) {
			e.printStackTrace();
			Utils.updateLog("Saving Error!" + e.getMessage());
		} 
    }
}
