package com.softlyinspired.jlw.menus;
import com.mongodb.*;
import com.softlyinspired.jlw.mongodb.repoConnection;

import java.net.UnknownHostException;


public class mongoMenu {
	
	 int maxMenus = 15;
	 int menuId ;
	 public String menuTitle  = new String();
	 public String menuItem[] = new String[15];
	 public int menuItemCount ;
	
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

		DBCollection coll = repoConnection.getScriptCollection();	     
	     
	  // get all the documents in the collection and print them out
	     BasicDBObject query = new BasicDBObject();
	     BasicDBObject fields = new BasicDBObject();
		 fields.put("menuTitle",1);	   
		 fields.put("menuId", 1);
		 fields.put("concernId",1);
		 fields.put("_id", 0);

		 try {
		    DBObject myDoc = coll.findOne(query);	    
		    String menuIdString = null;
		     
		     menuIdString = myDoc.get("menuId").toString();
		     menuId = Integer.parseInt(menuIdString);
			 menuTitle = myDoc.get("menuTitle").toString(); 
		 } catch (Exception e){
			 return 1;
		 }

	   return 0;    

}		

	  

		 
	    
}