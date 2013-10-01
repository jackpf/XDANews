package com.jackpf.xdanews;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.app.Activity;
import android.os.Bundle;
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
}
