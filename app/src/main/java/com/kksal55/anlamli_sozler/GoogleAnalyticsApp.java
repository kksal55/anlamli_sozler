package com.kksal55.anlamli_sozler;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class GoogleAnalyticsApp extends Application {

	// change the following line 
	private static final String PROPERTY_ID = "UA-52041656-3";

	public enum TrackerName {
		APP_TRACKER, // tracker used only in this app
		GLOBAL_TRACKER, // tracker used by all the apps from a company . eg: roll-up tracking.
		ECOMMERCE_TRACKER, // tracker used by all ecommerce transactions from a company .
	}

	public HashMap<TrackerName, Tracker> mTrackers = new HashMap<>();

	public synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker tracker = (trackerId == TrackerName.APP_TRACKER)?analytics.newTracker(PROPERTY_ID)
					: (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
					: analytics.newTracker(R.xml.ecommerce_tracker);
			mTrackers.put(trackerId , tracker);
		}
		return  mTrackers.get(trackerId);
	}


	private Activity mCurrentActivity = null;

	public void setCurrentActivity(Activity mCurrectActivity) {
		this.mCurrentActivity = mCurrectActivity;
	}

	public Activity getCurrentActivity() {
		return mCurrentActivity;
	}

	private static Context mAppContext;

	private static GoogleAnalyticsApp mInstance;

	@Override
	public void onCreate() {
		super.onCreate();
		MultiDex.install(this);
		mInstance = this;
		this.setAppContext(getApplicationContext());
	}

	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public static GoogleAnalyticsApp getInstance() {
		return mInstance;
	}

	public static Context getAppContext() {
		return mAppContext;
	}

	public void setAppContext(Context mAppContext) {
		this.mAppContext = mAppContext;
	}
}