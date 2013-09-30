package Model;

import java.util.HashMap;

public interface UIView
{
	public void setVars(HashMap<String, Object> vars);
	public void update();
	public void error(Exception e);
}
