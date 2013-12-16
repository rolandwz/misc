package com.roland.rwtool;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class LogsFragment extends Fragment {
	private View rooView = null;
	private TextView logText = null;
	private ScrollView scrollView = null;
	
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	rooView = inflater.inflate(R.layout.fragment_logs, container, false);
		logText = (TextView) rooView.findViewById(R.id.text_log);
		//scrollView = (ScrollView) rooView.findViewById(R.id.scroll_text);
		logText.setText(Utils.logStringCache);
		
        //onResume();
        return rooView;
    }
    
}
