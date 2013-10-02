package com.jackpf.xdanews;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import Lib.Request;
import Model.UIView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.Window;

public class MainActivity extends Activity implements OnRefreshListener
{
	private static MainActivity instance;
	
	public PullToRefreshAttacher refreshAttacher;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setInstance();
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);
		
		refreshAttacher = PullToRefreshAttacher.get(this);
		refreshAttacher.addRefreshableView(findViewById(R.id.articles), this);
		
		alarmSetup();
		
		new Thread().execute(getString(R.string.xda_news_url));
	}

	@Override
    public void onRefreshStarted(View view)
	{
		new Thread().execute(getString(R.string.xda_news_url));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	private void setInstance()
	{
		instance = this;
	}
	
	public static MainActivity getInstance()
	{
		return instance;
	}
	
	private final static int ONE_HOUR = 1000 * 60 * 60;
	
	private void alarmSetup()
	{
		// TODO: Start on boot
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent("com.jackpf.xdanews.ALARM_INTENT"), 0);
		AlarmManager am = (AlarmManager) (getSystemService(Context.ALARM_SERVICE));
		
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), ONE_HOUR, pi);
		System.err.println("setup");
	}
	
	public static class AlarmReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(final Context c, Intent i)
		{
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
			final Long updateTime = prefs.getLong("update_time", new Date().getTime());
			new Thread(c, new UIView()
			{
				private HashMap<String, Object> vars;
				public void setVars(HashMap<String, Object> vars) { this.vars = vars; }
				public void update()
				{
					Request feed = (Request) vars.get("feed");         
					
					int newArticles = 0;
					
					for (int i = 0; true; i++) {
			        	if (feed.get("channel.item." + (i + 1) + ".title") == null)
			        		break;
			        	
						String pubDate = feed.get("channel.item." + (i + 1) + ".pubDate");
				    	try {
					    	Date date = new SimpleDateFormat(c.getString(R.string.date_format), Locale.ENGLISH).parse(pubDate);
					    	
					    	if (date.getTime() > updateTime) {
								newArticles++;
					    	}
				    	} catch (ParseException e) {
				    		e.printStackTrace();
				    		continue;
				    	}
					}
					
					if (newArticles > 0) {
						NotificationManager mNotificationManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
						
						Notification notification = new Notification(R.drawable.ic_launcher, "Articles", System.currentTimeMillis());
	
			            PendingIntent contentIntent = PendingIntent.getActivity(c, 0, new Intent(c, MainActivity.class), 0);
	
			            notification.setLatestEventInfo(c, "New Articles", "There are " + newArticles + " articles for you to read!", contentIntent);
	
			            mNotificationManager.notify(0, notification);
					}
				}
				public void error(Exception e) { e.printStackTrace(); }
			}).execute(c.getString(R.string.xda_news_url));
		}
	}
}
