/*
 * This code is part of JLW Warehouse Wonder
 * Copyright (c) 2016-  Julie Kenny @ Softly Inspired  All rights reserved. 
 *
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.

 */

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