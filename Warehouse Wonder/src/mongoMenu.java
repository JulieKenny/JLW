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

public class mongoMenu {
	
	 int menuItemCount = 0;
	 int menuLength = 10;
	 int menuId ;
	 String menuTitle  = new String();
	
	 int returnMenu(int menuId){
		 int result = 0;
		 try {
		     result = readMenu(menuId);
		 } catch (UnknownHostException e){
			 System.out.println("Host not found");
		 }
		 return result;
	 }
	  
	 int readMenu(int menuId) throws UnknownHostException {

		 MongoClient mongoClient = new MongoClient("localhost",27017);
	
	     // get handle to "mydb"
	     DB db = mongoClient.getDB("local");
	     DBCollection coll = db.getCollection("customMenus");
	     
	  // get all the documents in the collection and print them out
	     BasicDBObject query = new BasicDBObject();
	     BasicDBObject fields = new BasicDBObject();
		 fields.put("menuTitle",1);	   
		 fields.put("menuId", 1);
		 fields.put("scriptId",1);
		 fields.put("_id", 0);

		 try {
		    DBObject myDoc = coll.findOne(query);	    
		    String menuIdString = null;
		    DBObject doc ;
		     
		     menuIdString = myDoc.get("menuId").toString();
		     menuId = Integer.parseInt(menuIdString);
			 menuTitle = myDoc.get("menuTitle").toString(); 
		 } catch (Exception e){
			 return 1;
		 }
		
	     //clean up
	     mongoClient.close();
	   return 0;  

}		
	  

		 
	    
}