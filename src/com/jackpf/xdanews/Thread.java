package com.jackpf.xdanews;

import java.util.HashMap;

import view.UI;
import Lib.Request;
import Lib.XmlParser;
import android.os.AsyncTask;

public class Thread extends AsyncTask<String, Void, Void>
{
	HashMap<String, Object> vars = new HashMap<String, Object>();
	Exception e = null;
	
	@Override
	protected void onPreExecute()
	{
		MainActivity.getInstance().setProgressBarIndeterminateVisibility(true);
	}
	
	@Override
    protected Void doInBackground(String... params)
    {
		if (params.length == 0) {
			throw new IllegalArgumentException("Too few arguments");
		}
		
		try {
			Request request = new Request(params[0]).request().parse(new XmlParser());
			
			vars.put("feed", request);
		} catch (Exception e) {
			this.e = e;
		}
		
		return null;
    }
    
	@Override
    protected void onPostExecute(Void _void)
	{
		UI ui = new UI();
		
		ui.setVars(vars);
		
		if (e == null) {
			ui.update();
		} else {
			ui.error(e);
		}
		
		MainActivity.getInstance().setProgressBarIndeterminateVisibility(false);
	}
}