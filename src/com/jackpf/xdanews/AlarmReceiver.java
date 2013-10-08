package com.jackpf.xdanews;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import Lib.Request;
import Model.UIView;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver
{
	public static void create(Context c)
	{
		PendingIntent pi = PendingIntent.getBroadcast(c, 0, new Intent("com.jackpf.xdanews.ALARM_INTENT"), 0);
		AlarmManager am = (AlarmManager) (c.getSystemService(Context.ALARM_SERVICE));
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
		Long refreshInterval = prefs.getLong("refresh_interval", Integer.parseInt(c.getString(R.string.pref_refresh_interval_default)));
		Long refreshDelay = prefs.getLong("refresh_delay", Integer.parseInt(c.getString(R.string.pref_refresh_delay_default)));
		
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + refreshDelay, refreshInterval, pi);
		
		Log.d("Debug", String.format("Alarm created (r_intvl=%d, r_del=%d)", refreshInterval, refreshDelay));
	}
	
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
					Notification notification = new Notification(R.drawable.ic_launcher, c.getString(R.string.notification_title), System.currentTimeMillis());
		            PendingIntent contentIntent = PendingIntent.getActivity(c, 0, new Intent(c, MainActivity.class), 0);
		            notification.setLatestEventInfo(c, c.getString(R.string.notification_title), c.getResources().getQuantityString(R.plurals.notification_text, newArticles, newArticles), contentIntent);
		            notification.flags |= Notification.FLAG_AUTO_CANCEL;
		            mNotificationManager.notify(0, notification);
				}
			}
			public void error(Exception e) { e.printStackTrace(); }
		}).execute(c.getString(R.string.xda_news_url));

		Log.d("Debug", "Alarm received");
	}
}
