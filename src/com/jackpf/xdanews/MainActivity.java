package com.jackpf.xdanews;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher.OnRefreshListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity implements OnRefreshListener
{
	private static MainActivity instance;
	
	public PullToRefreshAttacher refreshAttacher;
	
	ActionBarDrawerToggle drawerToggle;
	
	private static String url = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
		setInstance();
		
		// Request action bar
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		
		setContentView(R.layout.activity_main);
		
		// Init pull to refresh
		refreshAttacher = PullToRefreshAttacher.get(this);
		refreshAttacher.addRefreshableView(findViewById(R.id.articles), this);
		
		// Init navigation drawer
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        drawerList.setAdapter(new ArrayAdapter<String>(
    		this,
            R.layout._drawer_list_item,
            getResources().getStringArray(R.array.drawer_list_titles))
        );
        
        drawerList.setOnItemClickListener(
	        new ListView.OnItemClickListener()
	        {
	            @Override
	            public void onItemClick(AdapterView parent, View view, int position, long id)
	            {
	            	url = MainActivity.getInstance().getResources().getStringArray(R.array.drawer_list_values)[position];
	                MainActivity.getInstance().refresh();
	                
	                // Not gonna do this until I can find how to select first item on default -_-
	                //view.setSelected(true);
	                
	                drawerLayout.closeDrawer(drawerList);
	            }
	        }
        );
        
        drawerToggle = new ActionBarDrawerToggle(
            this,                   /* Host Activity */
            drawerLayout,          /* DrawerLayout object */
            R.drawable.ic_launcher, /* Nav drawer icon to replace 'Up' caret */
            R.string.drawer_open,   /* "Open drawer" description */
            R.string.drawer_close   /* "Close drawer" description */
        );

        drawerLayout.setDrawerListener(drawerToggle);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
	}
	
	/**
	 * Refresh on start
	 */
	@Override
	public void onStart()
	{
		super.onStart();
		
		refresh();
	}
	
	/**
	 * Refresh on resume
	 */
	@Override
	public void onResume()
	{
		super.onResume();
		
		refresh();
	}

	/**
	 * Handle action bar click
	 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (drawerToggle.onOptionsItemSelected(item)) {
        	return true;
        }
        
        return super.onOptionsItemSelected(item);
    }

    /**
     * Handle pull to refresh action
     */
	@Override
    public void onRefreshStarted(View view)
	{
		refresh();
	}
	
	/**
	 * Refresh ArrayList with default url
	 */
	private void refresh()
	{
		if (url == null) {
			url = getString(R.string.xda_news_url);
		}
		
		new Thread().execute(url);
	}

	/**
	 * Create options menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		//getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	/**
	 * Set instance
	 */
	private void setInstance()
	{
		instance = this;
	}
	
	/**
	 * Get instance
	 * 
	 * @return MainActivity
	 */
	public static MainActivity getInstance()
	{
		return instance;
	}
}
