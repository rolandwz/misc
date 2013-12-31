package com.roland.rwtool;

import java.util.ArrayList;
import java.util.List;

import com.roland.rwtool.data.MarketFetcher;

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

public class MarketFragment extends Fragment {
	private ArrayAdapter<String> marketDataAdapter = null;
	private Handler mainHandler = null;

    public MarketFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message, container, false);

        ListView marketListView = (ListView) rootView.findViewById(R.id.message_list);
        List<String> loadingList = new ArrayList<String>();
        loadingList.add("loading");
        marketDataAdapter = new ArrayAdapter<String>(getActivity(), 
                android.R.layout.simple_list_item_1, loadingList);
        marketListView.setAdapter(marketDataAdapter);
		
		Button freshBtn = (Button) rootView.findViewById(R.id.btn_fresh_message); 
		freshBtn.setOnClickListener(new OnClickListener() {
	        @Override
	        public void onClick(View view) {
	        	fetchMarketList();
	        }
	    });	

		mainHandler = new MainHandler(Looper.myLooper());
		fetchMarketList();
        return rootView;
    }
    
    private void fetchMarketList() {
    	new Thread(new Runnable() {
    		@Override
    		public void run() {
    			Utils.updateLog("market fetch thread running..");
    			Message msg = Message.obtain();
    			msg.obj = MarketFetcher.getMarketDataList();
    			mainHandler.sendMessage(msg);
    		}
    	}).start();
    }
    
    class MainHandler extends Handler {
        public MainHandler(Looper looper) {
            super(looper);
        }
        
        @Override
        public void handleMessage(Message msg) {
        	List<String> marketDataList = (List<String>) msg.obj;
        	marketDataAdapter.clear();
			marketDataAdapter.addAll(marketDataList);
        	marketDataAdapter.notifyDataSetChanged();
        }
    }
}
