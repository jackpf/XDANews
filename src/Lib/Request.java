package Lib;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import Model.Parser;


/**
 * Steam Stats
 */
public class Request
{
    /**
     * Url
     */
    private String url;
    
    /**
     * Parser
     */
    private Parser parser;
    
    /**
     * Response
     */
    private HttpResponse response;

    /**
     * Construct
     *
     * @param user
     * @param url
     */
    public Request(String url)
    {
        this.url = url;
    }
    
    /**
     * Request given url
     * 
     * @return this
     */
    public Request request() throws Exception
    {
    	HttpParams params = new BasicHttpParams();
    	HttpConnectionParams.setConnectionTimeout(params, 10000);
    	DefaultHttpClient client = new DefaultHttpClient(params);

        HttpGet request = new HttpGet(url);

        response = client.execute(request);

        int responseCode = response.getStatusLine().getStatusCode();

        if (responseCode != 200) {
            throw new IOException(String.format("Server returned status code: %d", responseCode));
        }
        
        return this;
    }

    /**
     * Parse content
     */
    public Request parse(Parser parser) throws Exception
    {
    	this.parser = parser;
    	
    	parser.setContent(response.getEntity().getContent());
        parser.parse();
        
        return this;
    }
    
    /**
     * Get response
     * 
     * @return HttpResponse
     */
    public HttpResponse getResponse()
    {
    	return response;
    }
    
    /**
     * Get key from the parsed response
     * 
     * @param key
     * @return
     */
    public String get(String key)
    {
    	return parser.getValue(key);
    }

    /**
     * Replace url var
     *
     * @param string
     * @param name
     * @param value
     * @return string
     */
    private String replaceVar(String string, String name, String value)
    {
        return string.replaceAll("\\{" + name + "\\}", URLEncoder.encode(value));
    }
    
    /**
     * Replace url vars
     * Accepts arrays
     * 
     * @param string
     * @param name
     * @param value
     * @return
     */
    private String replaceVars(String string, String[] name, String[] value)
    {
    	for (int i = 0; i < name.length; i++) {
    		string = replaceVar(string, name[i], value[i]);
    	}
    	
    	return string;
    }
}