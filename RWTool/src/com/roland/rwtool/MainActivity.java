package com.roland.rwtool;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Notification;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

public class MainActivity extends FragmentActivity implements
		ActionBar.OnNavigationListener {

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.title_messages),
								getString(R.string.title_informations),
								getString(R.string.title_market),
								getString(R.string.title_log), }), this);

		Utils.updateLog("start binding...");
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				"42A8cSLYwIZnugnNS0WHQUAR");
		List<String> tags = new ArrayList<String>();
		tags.add("RWTOOL");
		PushManager.setTags(getApplicationContext(), tags);

		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
        		getApplicationContext(),
        		resource.getIdentifier("notification_custom_builder", "layout", pkgName), 
        		resource.getIdentifier("notification_icon", "id", pkgName), 
        		resource.getIdentifier("notification_title", "id", pkgName), 
        		resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
		PushManager.setNotificationBuilder(this, 1, cBuilder);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		if (0 == position) {
			Fragment msgFragment = new MessageFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, msgFragment).commit();
			return true;
		}
		
		if (1 == position) {
			Fragment informationFragment = new InformationFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, informationFragment).commit();
			return true;
		}
		
		if (2 == position) {
			Fragment marketFragment = new MarketFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, marketFragment).commit();
			return true;
		}
		
		if (3 == position) {
			Fragment logsFragment = new LogsFragment();
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.container, logsFragment).commit();
			return true;
		}

		return true;
	}


}
