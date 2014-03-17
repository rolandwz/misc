package com.roland.rwtool;

import java.util.ArrayList;
import java.util.List;

import com.roland.rwtool.data.InformationFetcher;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class InformationFragment extends Fragment {
	private ArrayAdapter<String> informationDataAdapter = null;
	private Handler mainHandler = null;

    public InformationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        ListView informationListView = (ListView) rootView.findViewById(R.id.message_list);
        List<String> loadingList = new ArrayList<String>();
        loadingList.add("loading");
        informationDataAdapter = new ArrayAdapter<String>(getActivity(), 
                android.R.layout.simple_list_item_1, loadingList);
        informationListView.setAdapter(informationDataAdapter);
		
		Button freshBtn = (Button) rootView.findViewById(R.id.btn_fresh_message); 
		freshBtn.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	fetchInformation();
	        }
	    });	

		mainHandler = new MainHandler(Looper.myLooper());
		fetchInformation();
        return rootView;
    } // onCreateView
    
    private void fetchInformation() {
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			Utils.updateLog("information fetch thread running..");
    			Message msg = Message.obtain();
    			msg.obj = InformationFetcher.getInformationList();
    			mainHandler.sendMessage(msg);
    		}
    	}).start();
    } // fetchInformation
    
    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }
        
        @Override
        public void handleMessage(Message msg) {
        	List<String> informationList = (List<String>) msg.obj;
        	informationDataAdapter.clear();
			informationDataAdapter.addAll(informationList);
        	informationDataAdapter.notifyDataSetChanged();
        }
    } // MainHandler

}
