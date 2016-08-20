import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
//import java.util.Arrays;
//import java.util.List;

import javax.swing.JOptionPane;

public class mongoMenuSet {
	
	 int menuItemCount = 0;
	 int menuLength = 10;
	 int menuId [] = new int[menuLength];
	 String menuTitle [] = new String[menuLength];
	 mongoMenu customMenu[] = new mongoMenu[5];
	 int menuCount = 0;
	
	 int returnAllMenus(){
		 int result = 0;
		 try {
		     result = readAllCustomMenus();
		 } catch (UnknownHostException e){
			 System.out.println("Host not found");
		 }
		 return result;
	 }
	  
	 int readAllCustomMenus() throws UnknownHostException {

		 MongoClient mongoClient = new MongoClient("localhost",27017);

	
	     // get handle to "mydb"
	     DB db = mongoClient.getDB("local");

	     DBCollection coll = db.getCollection("customMenus");
	     
	  // get all the documents in the collection and print them out
	     BasicDBObject query = new BasicDBObject();
	     BasicDBObject fields = new BasicDBObject();
		 fields.put("menuTitle",1);	   
		 fields.put("menuId", 1);
		 fields.put("concernId",1);
		 fields.put("_id", 0);

	    DBCursor cursor = coll.find(query,fields);
	    int menuIdFound  ;
	    String menuTitleFound = null;
	    String menuConcernFound = null;
	    DBObject doc ;
     
	     try{

	         while (cursor.hasNext()) {

	        	 doc = cursor.next();
	        	 menuIdFound = Integer.parseInt(doc.get("menuId").toString());
	        	 menuTitleFound = doc.get("menuTitle").toString();
	        	 menuConcernFound = doc.get("concernId").toString();
	        	 boolean menuFound = false;
	        	 
	        	 int i = 0;
	        	 while ( (i < menuCount) && (menuFound == false)){
	        		 if (customMenu[i].menuId == menuIdFound ){
	        			 menuFound = true;
	        			 mongoMenu currentMenu = new mongoMenu();
	        			 currentMenu = customMenu[i] ;
	        			 currentMenu.menuItem[currentMenu.menuItemCount] = menuConcernFound;
	        			 currentMenu.menuItemCount += 1;
                         customMenu[i] = currentMenu;	      
	        		 }
	        	     i += 1;
	        	 }	        	 
	        	 if (! menuFound) {	        		 
	        		 mongoMenu currentMenu = new mongoMenu();
		        	 currentMenu.menuId = menuIdFound;
		        	 currentMenu.menuTitle = menuTitleFound;
		        	 currentMenu.menuItem[0] = menuConcernFound;
		        	 currentMenu.menuItemCount = 1;	
		        	 customMenu[menuCount] = currentMenu;
		        	 menuCount = menuCount +1;	
		        	                             
	        	 }
	         }
	     } catch (Exception e){
	    	System.out.println(e); 
	     } finally {
	         cursor.close();
	     } 

	     mongoClient.close();

	   return menuCount;  

}		
	  

}