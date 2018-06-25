package Models;

import org.json.JSONObject;

public class Weather 
{
	public String summary;
	public String icon;
	
	public Weather(String apiResponse)
	{
		JSONObject r = new JSONObject(apiResponse);
		r = r.getJSONObject("currently");	
		summary = r.getString("summary");
		icon = r.getString("icon");
	}
}
