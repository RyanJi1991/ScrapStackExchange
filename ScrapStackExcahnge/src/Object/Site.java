package Object;
import org.json.*;

import Util.DBConnection;;

public class Site {
	//Class that store the information of one site
	public String siteName = "";
	public String apiSite = "";
	public boolean scraped = false;
	
	Site(JSONObject json){
		this.siteName = json.getString("name");
		this.apiSite = json.getString("api_site_parameter");
	}
	
	public void InsertIntoDB() throws Exception{
		DBConnection conn = new DBConnection();
		String from = "sys.site";
		String values = "(\"" + siteName + "\",\"" + apiSite + "\",0)";
		conn.PerformInsert(from, values);
	}
}
