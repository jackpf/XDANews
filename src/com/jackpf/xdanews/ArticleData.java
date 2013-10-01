package com.jackpf.xdanews;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

import Lib.Request;

public class ArticleData implements Serializable
{
	private ArrayList<String> urls = new ArrayList<String>();
	
	private ArticleData()
	{
		// Use retrieveArticles
	}
	
	public static ArticleData retrieveArticles(String dataFile)
	{
		try {
			File f = new File(dataFile);
			
			if (!f.exists()) {
				f.createNewFile();
				
				return new ArticleData();
			}
			
			ObjectInputStream file = new ObjectInputStream(new FileInputStream(f));
			
			ArticleData articles = (ArticleData) file.readObject();
			
			file.close();
			
			return articles;
		} catch (Exception e) {
			return new ArticleData();
		}
	}
	
	public void persistArticles(String dataFile)
	{
		try {
			ObjectOutputStream file = new ObjectOutputStream(new FileOutputStream(dataFile));
			
			file.writeObject(this);
			
			file.close();
		} catch (IOException e) {
			//...
			e.printStackTrace();
		}
	}
	
	public void addArticle(String url)
	{
		urls.add(url);
	}
	
	public ArrayList<String> getArticles()
	{
		return urls;
	}
	
	public void getArticlesFromRequest(Request request)
	{
		for (int i = 0; true; i++) {
        	String url = request.get("channel.item." + (i + 1) + ".guid");
        	
        	if (url == null)
        		break;
        	else
        		addArticle(url);
		}
	}
}
