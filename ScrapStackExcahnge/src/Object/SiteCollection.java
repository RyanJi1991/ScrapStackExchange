package Object;
import java.util.ArrayList;
import java.util.List;

import Util.*;
import org.json.*;

public class SiteCollection {
	//Class that store a collection of Site Objects
	List<Site> siteCollection = new ArrayList<Site>();
	
	SiteCollection(JSONArray jsonArray){
		int length = jsonArray.length();
		for(int i=0;i<length;i++){
			this.siteCollection.add(new Site(jsonArray.getJSONObject(i)));
		}
	}
	
	public SiteCollection() {
		// TODO Auto-generated constructor stub
	}

	//Scrap the information of all sites and store it in the object
	public void ScrapAllSiteInformation()throws Exception{
		siteCollection.clear();
		System.out.println("Scraping all sites information");
		
		boolean hasMore = true;
		int page = 0;
		while(hasMore){		
			page++;
			String url = "https://api.stackexchange.com/2.1/sites?pagesize=100&key=t0y0NakqjbvlQty14JLzug((&page=" + page;
			JSONObject json = HttpOperation.httpGet(url);		
			siteCollection.addAll(new SiteCollection(json.getJSONArray("items")).siteCollection);
			hasMore = json.getBoolean("has_more");
		}
		
		System.out.println("All sites information scraped");
	}
	
	//write all site information to DB
	public void WriteSiteInformationToDB() throws Exception{
		DBConnection conn = new DBConnection();
		conn.PerformTruncate("sys.site");
		System.out.println("Writing sites information to DB");
		for(Site site : siteCollection){
			site.InsertIntoDB();
		}
		System.out.println("All sites information stored in DB\n");
	}
}
