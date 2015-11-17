
import java.util.Scanner;

import Object.*;
import Util.DBConnection;

public class main {
	public static void main(String[] args) throws Exception{
		while(true){
			System.out.println("**********************************************************************************************************************");
			System.out.println("Please enter the number of command to proceed:");
			System.out.println("1.Scrap all site information(First step of all process)");
			System.out.println("2.Scrap all user information on a site on a given number of pages(Each page has 100 users)");
			System.out.println("3.Scrap one user information on a site(Require user name)");
			System.out.println("4.Display top X authoritative users information on a given site(X is a number entered by you)");
			System.out.println("5.Display user information on a given site(Require user name)");
			
			System.out.println("0.Exit system");		
		
			Scanner scan = new Scanner(System.in);
			String command =  scan.nextLine();
			if(command.equals("1")){
				SiteCollection siteCollection = new SiteCollection();
				siteCollection.ScrapAllSiteInformation();
				siteCollection.WriteSiteInformationToDB();
			} 
			else if(command.equals("2")){
				System.out.println("Please enter the site's name. For Example: Stack Overflow");
				String siteName =  scan.nextLine();
				System.out.println("Please enter the start page number. For Example: 1");
				int pageStart =  Integer.parseInt(scan.nextLine());
				System.out.println("Please enter the end page number. For Example: 40");
				int pageEnd =  Integer.parseInt(scan.nextLine());
				
				UserCollection userCollection = new UserCollection();
				userCollection.GetIndexedUserInformationOnSite(siteName,pageStart,pageEnd);
				userCollection.WriteUserInformationToDB();
			}
			else if(command.equals("3")){
				System.out.println("Please enter the site's name. For Example: Stack Overflow");
				String siteName =  scan.nextLine();
				System.out.println("Please enter the user's name. For Example: Amber");
				String userName =  scan.nextLine();
				User user = new User();
				user.GetInformationOfOneUser(userName, siteName);
				user.InsertIntoDB();
			}
			else if(command.equals("4")){
				System.out.println("Please enter the site's name. For Example: Stack Overflow");
				String siteName =  scan.nextLine();
				System.out.println("Please enter the amount of users. For Example: 3");
				int userAmount =  Integer.parseInt(scan.nextLine());
				DBConnection conn = new DBConnection();
				conn.DisplayTopXUserOnSite(siteName, userAmount);
			}
			else if(command.equals("5")){
				System.out.println("Please enter the site's name. For Example: Stack Overflow");
				String siteName =  scan.nextLine();
				System.out.println("Please enter the user's name. For Example: Amber");
				String userName =  scan.nextLine();
				DBConnection conn = new DBConnection();
				conn.DisplayUserOnSite(siteName, userName);
			}
			else if(command.equals("0")){
				scan.close();
				System.exit(0);
			}
			else{
				System.out.println(command);
				System.out.println("Invalid input, please try again.");
			}
		}
	}
}
