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

public class mongoMenuSet {
	
	 int menuItemCount = 0;
	 int menuLength = 10;
	 int menuId [] = new int[menuLength];
	 String menuTitle [] = new String[menuLength];
	
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
		 int menuCount = 0;
	
	     // get handle to "mydb"
	     DB db = mongoClient.getDB("local");

	     DBCollection coll = db.getCollection("customMenus");
	     //DBObject myDoc = coll.findOne();
	     
	  // get all the documents in the collection and print them out
	     BasicDBObject query = new BasicDBObject();
	     BasicDBObject fields = new BasicDBObject();
		 fields.put("menuTitle",1);	   
		 fields.put("menuId", 1);
		 fields.put("_id", 0);

	    DBCursor cursor = coll.find(query,fields);
	    String menuDetails = new String();
	    String menuIdFound  ;
	    String menuTitleFound = null;
	    DBObject doc ;
	     
	     try{

	         while (cursor.hasNext()) {
	        	 doc = cursor.next();
	        	 menuIdFound = doc.get("menuId").toString();
	        	 menuTitleFound = doc.get("menuTitle").toString();
	        	 
	        	 menuId[menuCount] = Integer.parseInt(menuIdFound);
	        	 menuTitle[menuCount] = menuTitleFound;
	        	 //System.out.println("Id is " + menuIdFound + " Title is " + menuTitleFound);
	             menuCount = menuCount +1;
	         }
	     } finally {
	         cursor.close();
	     } 
	     
	     
	     
	     //clean up
	     
	     mongoClient.close();
	   
	   return menuCount;  

}		
	  

		 
	    
}