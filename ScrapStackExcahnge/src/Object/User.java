package Object;

import org.json.*;
import Util.*;

public class User {
	public String userName = "";
	public int accountId = 0;
	public int userId = 0;
	public String site = "";
	
	//reputation 
	public int reputation = 0;
	public int repGold = 0;
	public int repSilver = 0;
	public int repBronze = 0;
	
	//activity summary
	public String lastAccess = "";
	public int lastThreeMonthActivities = 0;
	
	//tags
	public String tags = "";
			
	User(JSONObject json, String site){
		this.userName = json.getString("display_name");
		this.accountId = json.has("account_id")?json.getInt("account_id"):-1;
		this.userId = json.getInt("user_id");
		this.site = site;
		
		this.reputation = json.getInt("reputation");
		this.repGold = json.getJSONObject("badge_counts").getInt("gold");
		this.repSilver = json.getJSONObject("badge_counts").getInt("silver");
		this.repBronze = json.getJSONObject("badge_counts").getInt("bronze");
		
		this.lastAccess = TimeOperation.GetTimeFromEpoch(json.getLong("last_access_date"));
		this.lastThreeMonthActivities = 0;
		this.tags = "";
		
	}
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	public void GetInformationOfOneUser(String userName, String siteName)throws Exception{
		//Get the api_parameter for the site
		DBConnection conn = new DBConnection();
		String apiParameter = conn.GetApiParameterOfSite(siteName);
		if(apiParameter==null){
			return;
		}
		
		System.out.println("Scraping the basic information of " + userName + " on " + siteName);
		String url = "https://api.stackexchange.com/2.1/users?pagesize=100&key=t0y0NakqjbvlQty14JLzug((&site=" + apiParameter + "&inname=" + userName.replace(" ", "%20");
		JSONObject rawJson = HttpOperation.httpGet(url);
		if(rawJson==null){
			System.out.println("No such user on that site, please try again");
			return;
		}
		JSONObject json = rawJson.getJSONArray("items").getJSONObject(0);		
		this.userName = json.getString("display_name");
		accountId = json.has("account_id")?json.getInt("account_id"):-1;
		userId = json.getInt("user_id");
		site = siteName;
		
		reputation = json.getInt("reputation");
		repGold = json.getJSONObject("badge_counts").getInt("gold");
		repSilver = json.getJSONObject("badge_counts").getInt("silver");
		repBronze = json.getJSONObject("badge_counts").getInt("bronze");
		
		lastAccess = TimeOperation.GetTimeFromEpoch(json.getLong("last_access_date"));
		lastThreeMonthActivities = 0;
		tags = "";
		
		System.out.println("Scraping the activity information of " + userName + " on " + siteName);
		url = "https://api.stackexchange.com/2.2/users/" + accountId + "/network-activity?pagesize=100&key=t0y0NakqjbvlQty14JLzug((&fromdate=" + TimeOperation.GetEpochTimeOfThreeMonthAgo();
		json = HttpOperation.httpGet(url);
		//API would not work on some of the sites, usually small sites, if that happens, store -1 to indicate api malfunction
		if(json==null){
			lastThreeMonthActivities = -1;
		}
		lastThreeMonthActivities = json.has("items")?json.getJSONArray("items").length():0;
		
		System.out.println("Scraping the tags information of " + userName + " on " + siteName);
		url = "https://api.stackexchange.com/2.1/users/" + userId + "/tags?pagesize=100&order=desc&key=t0y0NakqjbvlQty14JLzug((&sort=popular&site=" + apiParameter;
		json = HttpOperation.httpGet(url);
		//API would not work on some of the sites, usually small sites, if that happens, store "N/A" to indicate api malfunction
		if(json==null){
			tags = "N/A";
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<Math.min(10, json.getJSONArray("items").length());i++){
			if(i>0){
				sb.append(",");
			}
			sb.append(json.getJSONArray("items").getJSONObject(i).getString("name"));
		}
		tags = sb.toString();
		
	}
	
	public void InsertIntoDB() throws Exception{
		DBConnection conn = new DBConnection();

		if(!conn.CheckIfExsist("sys.user", "account_id=" + accountId)){
			String from = "sys.user";
			String values = "(" + accountId + ",\"" + userName + "\"," + lastThreeMonthActivities + ")";
			conn.PerformInsert(from, values);
		}
		if(!conn.CheckIfExsist("sys.site_user", "user_id=" + userId + " and site=\"" + site +"\"")){
			String from = "sys.site_user";
			String values = "(\"" + site + "\"," + userId + "," + accountId + "," + reputation + "," + repGold + "," + repSilver + "," +repBronze + ",\""  + lastAccess + "\",\"" + tags +"\")";
			conn.PerformInsert(from, values);
		}
	}
}
