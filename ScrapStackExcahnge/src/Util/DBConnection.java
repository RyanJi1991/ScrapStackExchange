package Util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConnection {
	  private Connection connect = null;
	  private Statement statement = null;
	  private ResultSet resultSet = null;
	  
	  //Check if a data entry exists in DB, if it does, abort insertion of that data entry
	  public boolean CheckIfExsist(String from, String where)throws Exception{
		  try{
			  connect = DriverManager.getConnection("jdbc:mysql://localhost/sys?" + "user=root&password=1s22s22p2");
		      statement = connect.createStatement();
			  String command = "select * from " + from + " where "+ where;
			  ResultSet resultSet= statement.executeQuery(command);
			  return resultSet.next();
		  }catch(Exception e){
			  throw e;
		  }
		  finally {
	      close();
	    }
	  }
	  
	  //Get the api parameter of a site, used in getting a user's information of a certain site
	  public String GetApiParameterOfSite(String siteName)throws Exception{
		  try{
			  connect = DriverManager.getConnection("jdbc:mysql://localhost/sys?" + "user=root&password=1s22s22p2");
		      statement = connect.createStatement();
			  String command = "select api_site from sys.site where name=\""+ siteName + "\"";
			  ResultSet resultSet= statement.executeQuery(command);
			  if(!resultSet.next()){
					System.out.println("Sorry, No site with that name, please try again\n\n");
					return null;
				}
			  String result = resultSet.getString("api_site");
			  return result;
		  }catch(Exception e){
			  throw e;
		  }
		  finally {
	      close();
	    }
	  }
	  
	  //insert an entry into DB
	  public void PerformInsert(String from, String values)throws Exception{
		  try{
			  // This will load the MySQL driver, each DB has its own driver
		      //Class.forName("com.mysql.jdbc.Driver");
		      // Setup the connection with the DB
		      connect = DriverManager.getConnection("jdbc:mysql://localhost/sys?" + "user=root&password=1s22s22p2");
		      // Statements allow to issue SQL queries to the database
		      statement = connect.createStatement();
			  //create sql command
			  String command = "insert into "+ from + " values "+ values;
			  statement.executeUpdate(command);
		  }catch(Exception e){
			  throw e;
		  }
		  finally {
	      close();
	    }
	  }
	  
	  //Perform SQL command of truncate table
	  public void PerformTruncate(String table)throws Exception{
		  try{
			  connect = DriverManager.getConnection("jdbc:mysql://localhost/sys?" + "user=root&password=1s22s22p2");
		      statement = connect.createStatement();
			  String command = "truncate table "+ table;
			  statement.executeUpdate(command);
		  }catch(Exception e){
			  throw e;
		  }
		  finally {
	      close();
	    }
	  }
	  
	  //Display top X authoritative users' information on a site
	  public  void DisplayTopXUserOnSite(String siteName, int userAmount)throws Exception{
		  try{
			  connect = DriverManager.getConnection("jdbc:mysql://localhost/sys?" + "user=root&password=1s22s22p2");
		      statement = connect.createStatement();
			  String command = "select name from sys.site_user,sys.user where site_user.account_id=user.account_id and site=\"" + siteName + "\" order by reputation desc limit " + userAmount ;
			  ResultSet resultSet= statement.executeQuery(command);
			  if(!resultSet.next()){
					System.out.println("Sorry, No site with that name, please try again\n\n");
					return;
				}
			  resultSet.beforeFirst();
			  while(resultSet.next()){
				  String userName = resultSet.getString("name");
				  DBConnection conn = new DBConnection();
				  conn.DisplayUserOnSite(siteName, userName);
			  };
		  }catch(Exception e){
			  throw e;
		  }
		  finally {
	      close();
	    }
	  }
	  
	  //Display one user's information on a site
	  public void DisplayUserOnSite(String siteName, String userName)throws Exception{
		  try{
			  connect = DriverManager.getConnection("jdbc:mysql://localhost/sys?" + "user=root&password=1s22s22p2");
		      statement = connect.createStatement();
			  String command = "select name,user.account_id,user_id,reputation,rep_gold,rep_silver,rep_bronze,last_accessed, 3_month_activities, tags from sys.site_user,sys.user where site_user.account_id=user.account_id and site=\""+ siteName + "\" and name= \"" + userName + "\"";
			  ResultSet resultSet= statement.executeQuery(command);
			  if(!resultSet.next()){
					System.out.println("Sorry, No user found with that name, please try again\n\n");
					return;
				}
			  String name = resultSet.getString("name");
			  int accountId = resultSet.getInt("account_id");
			  int userId = resultSet.getInt("user_id");
			  
			  int reputation = resultSet.getInt("reputation");
			  int repGold = resultSet.getInt("rep_gold");
			  int repSilver = resultSet.getInt("rep_silver");
			  int repBronze = resultSet.getInt("rep_bronze");
			  
			  String lastAccessed = resultSet.getString("last_accessed");
			  int ThreeMonthActivity = resultSet.getInt("3_month_activities");
			  String tags = resultSet.getString("tags");
			  
			  System.out.println("**********************************************************************************************************************");
			  System.out.println("Name: " + name + "     Account id: " + accountId + "     User id: " + userId);
			  System.out.println("Reputation score: " + reputation + "     Gold badge: " + repGold + "  Silver badge: " + repSilver + "  Bronze badge: " + repBronze);
			  System.out.println("Last Login time: " + lastAccessed + "     Number of activities in last three months: " + ThreeMonthActivity);
			  System.out.println("Top ten experties(tags): " + tags);
		  }catch(Exception e){
			  throw e;
		  }
		  finally {
	      close();
	    }
	  }

	  //Need to close the resultSet
	  private void close() {
	    try {
	      if (resultSet != null) {
	        resultSet.close();
	      }

	      if (statement != null) {
	        statement.close();
	      }

	      if (connect != null) {
	        connect.close();
	      }
	    } catch (Exception e) {

	    }
	}
}
