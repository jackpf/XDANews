package com.jackpf.xdanews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(final Context c, Intent i)
	{
		AlarmReceiver.create(c);
		
		Log.d("Debug", "Boot received");
	}
}
