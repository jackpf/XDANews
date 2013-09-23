package view;

import java.util.HashMap;

import Lib.Lib;
import Lib.Request;
import Model.View;
import android.app.Activity;
import android.widget.TextView;

import com.jackpf.xdanews.MainActivity;
import com.jackpf.xdanews.R;

public class UI implements View
{
	private HashMap<String, Object> vars;
	
	/**
	 * Set vars
	 */
	public void setVars(HashMap<String, Object> vars)
	{
		this.vars = vars;
	}
	
	/**
	 * Update UI
	 */
	public void update()
	{
		final Activity context = MainActivity.getInstance();
		
		Request feed = (Request) vars.get("feed");
		
		TextView tv = (TextView) context.findViewById(R.id.hello);
		
		tv.setText(feed.toString());
	}
	
	/**
	 * Render errors thrown by backend
	 * 
	 * @param e
	 */
	public void error(Exception e)
	{
		e.printStackTrace();
		
		Lib.error(MainActivity.getInstance(), e.getMessage());
	}
}