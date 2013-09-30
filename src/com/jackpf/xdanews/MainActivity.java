package com.jackpf.xdanews;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.Window;

public class MainActivity extends Activity
{
	private static MainActivity instance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setInstance();
		
		setContentView(R.layout.activity_main);
		
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
}
