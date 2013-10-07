package com.jackpf.xdanews;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class ViewActivity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_view);

		WebView webView = (WebView) findViewById(R.id.webview);
		
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.getSettings().setUseWideViewPort(true);

		webView.loadData(getIntent().getStringExtra("article"), "text/html", "UTF-8");
	}
}
