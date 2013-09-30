package view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Lib.Lib;
import Lib.Request;
import Model.UIView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jackpf.xdanews.MainActivity;
import com.jackpf.xdanews.R;
import com.squareup.picasso.Picasso;

public class UI implements UIView
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
		
		final ArrayList<HashMap<String, String>> articles = new ArrayList<HashMap<String, String>>();
		
		Pattern imgPattern = Pattern.compile("src=\"(http://www.xda-developers.com/wp-content/uploads/.*\\.(jpg|png))\"");

        for (int i = 0; i < 10; i++) {
	    	HashMap<String, String> article = new HashMap<String, String>();
	    	
	    	article.put("title", feed.get("channel.item." + (i + 1) + ".title"));
	    	article.put("date", feed.get("channel.item." + (i + 1) + ".pubDate"));
	    	article.put("url", feed.get("channel.item." + (i + 1) + ".guid"));
	    	
	    	Matcher m = imgPattern.matcher(feed.get("channel.item." + (i + 1) + ".description"));
	        String img;
	        
	    	if (m.find()) {
	        	img = m.group(1);
	        } else {
	        	img = "blah";
	        }
	    	
	    	article.put("image", img);
	    	
	    	articles.add(article);
	    }
	    
	    final ArrayAdapter adapter = new ArrayAdapter(context, articles);
	    ListView articlesList = (ListView) context.findViewById(R.id.articles);
	    
	    articlesList.setAdapter(adapter);
	    articlesList.setOnItemClickListener(
	    	new OnItemClickListener()
	    	{
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{
					String url = ((HashMap<String, String>) adapter.getItem(position)).get("url");
					
					Intent i = new Intent();
					i.setAction(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
					context.startActivity(i);
				}
	    	}
	    );
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
	
	private class ArrayAdapter extends BaseAdapter
	{
	    private final Context context;
	    private final ArrayList<HashMap<String, String>> objects;
	    private final LayoutInflater inflater;

	    public ArrayAdapter(Context context, ArrayList<HashMap<String, String>> objects)
	    {
	        this.context = context;
	        this.objects = objects;

	        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    }
	    
	    public Object getItem(int position)
	    {
	        return objects.get(position);
	    }
	 
	    public long getItemId(int position)
	    {
	        return position;
	    }
	    
	    public int getCount()
	    {
	    	return objects.size();
	    }

	    @Override
	    public View getView(int position, View convertView, ViewGroup parent)
	    {
	    	View row;
	    	
	    	//if (convertView == null) {
		    	row = inflater.inflate(R.layout._list_article, parent, false);
	    	//} else {
	    	//	row = convertView;
	    	//}

	    	TextView title = (TextView) row.findViewById(R.id.title);
	    	title.setText(objects.get(position).get("title"));
	    	
	    	TextView description = (TextView) row.findViewById(R.id.description);
	    	description.setText(objects.get(position).get("date"));
	    	System.err.println(objects.get(position).get("date"));

    	    ImageView img = (ImageView) row.findViewById(R.id.image);
    	    Picasso.with(context).load(objects.get(position).get("image")).into(img);

    	    return row;
	    }
	}
}