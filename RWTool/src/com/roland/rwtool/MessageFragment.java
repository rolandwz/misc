/**
 * 
 */
package com.roland.rwtool;

import com.roland.rwtool.data.MessageMgt;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * @author kfzx-weizhuo
 *
 */
public class MessageFragment extends Fragment {
	private ArrayAdapter<String> messageAdapter = null;

    public MessageFragment() {
    }

    // Create a message handling object as an anonymous class.
   private OnItemLongClickListener messageLongClickedHandler = new OnItemLongClickListener() {
       public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

           AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
           final int index = position;
           builder.setMessage(MessageMgt.messageList.get(index))
                  .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                    	  deleteMessage(index);
                      }
                  })
                  .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                      public void onClick(DialogInterface dialog, int id) {
                          // User cancelled the dialog
                      }
                  });
           AlertDialog dialog = builder.create();
           dialog.show();

    	   return true;
       }
   };
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);
        
        ListView msgListView = (ListView) rootView.findViewById(R.id.message_list); 
        messageAdapter = new ArrayAdapter<String>(getActivity(), 
                android.R.layout.simple_list_item_1, MessageMgt.getFormatedMessageList(this.getActivity()));
        msgListView.setAdapter(messageAdapter);
		msgListView.setOnItemLongClickListener(messageLongClickedHandler);
		
		Button freshBtn = (Button) rootView.findViewById(R.id.btn_fresh_message); 
		freshBtn.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	refreshMessageList();
	        }
	    });		
       
        return rootView;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	refreshMessageList();
    }
    
    private void deleteMessage(int index) {
	  MessageMgt.deleteMessage(this.getActivity(), index);
	  refreshMessageList();
    }
    
    private void refreshMessageList() {
    	messageAdapter.clear();
    	messageAdapter.addAll(MessageMgt.getFormatedMessageList(this.getActivity()));
    	messageAdapter.notifyDataSetChanged();
    }
} // class
