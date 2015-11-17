package Object;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Util.*;

public class UserCollection {
	List<User> userCollection = new ArrayList<User>();
	
	UserCollection(JSONArray jsonArray, String site){
		int length = jsonArray.length();
		for(int i=0;i<length;i++){
			this.userCollection.add(new User(jsonArray.getJSONObject(i),site));
		}
	}
	
	public UserCollection() {
		// TODO Auto-generated constructor stub
	}

	//Get all the user information
	public void GetIndexedUserInformationOnSite(String siteName, int pageStart, int pageEnd) throws Exception{
		System.out.println("Scraping all sites information from " + siteName);
		
		//Get the api_parameter for the site
		DBConnection conn = new DBConnection();
		String apiParameter = conn.GetApiParameterOfSite(siteName);
		if(apiParameter==null){
			return;
		}
		
		boolean hasMore = true;
		int page = pageStart-1;
		while(hasMore&&page<pageEnd){		
			page++;
			String url = "https://api.stackexchange.com/2.1/users?pagesize=100&key=t0y0NakqjbvlQty14JLzug((&site=" + apiParameter + "&page=" + page;
			JSONObject json = HttpOperation.httpGet(url);
			userCollection.addAll(new UserCollection(json.getJSONArray("items"), siteName).userCollection);
			hasMore = json.getBoolean("has_more");
		}
		
		System.out.println("Now scraping all activity summaries");
		int count=1;
		for(User user : userCollection){
			String url = "https://api.stackexchange.com/2.2/users/" + user.accountId + "/network-activity?pagesize=100&key=t0y0NakqjbvlQty14JLzug((&fromdate=" + TimeOperation.GetEpochTimeOfThreeMonthAgo();
			JSONObject json = HttpOperation.httpGet(url);
			//Progress bar of some kind
			System.out.println("Scraping activity: "+(count++) + "/" + (pageEnd-pageStart+1)*100);
			
			//API would not work on some of the sites, usually small sites, if that happens, store -1 to indicate api malfunction 
			if(json==null){
				user.lastThreeMonthActivities = -1;
				continue;
			}
			user.lastThreeMonthActivities = json.has("items")?json.getJSONArray("items").length():0;
			if(count==270){
				System.out.println("adqdqw");
			}
			
		}
		
		System.out.println("Now scraping top 10 popular tags");
		count=1;
		for(User user : userCollection){
			String url = "https://api.stackexchange.com/2.1/users/" + user.userId + "/tags?pagesize=100&order=desc&key=t0y0NakqjbvlQty14JLzug((&sort=popular&site=" + apiParameter;
			JSONObject json = HttpOperation.httpGet(url);
			StringBuilder sb = new StringBuilder();
			//API would not work on some of the sites, usually small sites, if that happens, store "N/A" to indicate api malfunction 
			if(json==null){
				user.tags = "N/A";
				continue;
			}
			for(int i=0;i<Math.min(10, json.getJSONArray("items").length());i++){
				if(i>0){
					sb.append(",");
				}
				sb.append(json.getJSONArray("items").getJSONObject(i).getString("name"));
			}
			System.out.println("Scraping tags: "+(count++) + "/" + (pageEnd-pageStart+1)*100);
			user.tags = sb.toString();
		}		
	}
	
	//write all user information to DB
	public void WriteUserInformationToDB() throws Exception{
		System.out.println("Writing sites information to DB");
		for(User user : userCollection){
			user.InsertIntoDB();
		}
		System.out.println("All user information stored in DB\n");
	}
	
}
