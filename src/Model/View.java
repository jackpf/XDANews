package Model;

import java.util.HashMap;

public interface View
{
	public void setVars(HashMap<String, Object> vars);
	public void update();
	public void error(Exception e);
}
