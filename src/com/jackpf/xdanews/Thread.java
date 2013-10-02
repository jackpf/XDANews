package com.jackpf.xdanews;

import java.util.Date;
import java.util.HashMap;

import Lib.Request;
import Lib.XmlParser;
import Model.UIView;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class Thread extends AsyncTask<String, Void, Void>
{
	HashMap<String, Object> vars = new HashMap<String, Object>();
	Exception e = null;
	UIView ui;
	Context context;
	
	public Thread()
	{
		this.context = MainActivity.getInstance();
		this.ui = new UI();
	}
	
	public Thread(Context context, UIView ui)
	{
		this.context = context;
		this.ui = ui;
	}
	
	@Override
	protected void onPreExecute()
	{
		//MainActivity.getInstance().setProgressBarIndeterminateVisibility(true);
		try {
			/*context*/ MainActivity.getInstance().refreshAttacher.setRefreshing(true);
		} catch (NullPointerException e) {
			
		}
	}
	
	@Override
    protected Void doInBackground(String... params)
    {
		if (params.length == 0) {
			throw new IllegalArgumentException("Too few arguments");
		}
		
		try {
			Request request = new Request(params[0]).request().parse(new XmlParser());
			
			// Save request time
			Editor prefsEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
			prefsEditor.putLong("update_time", new Date().getTime());              
			prefsEditor.commit();
			
			vars.put("feed", request);
		} catch (Exception e) {
			this.e = e;
		}
		
		return null;
    }
    
	@Override
    protected void onPostExecute(Void _void)
	{
		ui.setVars(vars);
		
		if (e == null) {
			ui.update();
		} else {
			ui.error(e);
		}
		
		//MainActivity.getInstance().setProgressBarIndeterminateVisibility(false);
		try {
			/*context*/ MainActivity.getInstance().refreshAttacher.setRefreshComplete();
		} catch (NullPointerException e) {
			
		}
	}
}