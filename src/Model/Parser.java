package Model;

import java.io.InputStream;

public interface Parser
{
	public void setContent(InputStream content);
	public Parser parse() throws Exception;
	public String getValue(String key);
}