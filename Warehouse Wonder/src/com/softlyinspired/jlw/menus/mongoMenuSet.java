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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.softlyinspired.jlw.mongodb.repoConnection;

import java.net.UnknownHostException;

public class mongoMenuSet {
	
	 int menuItemCount = 0;
	 int menuLength = 10;
	 public int menuId [] = new int[menuLength];
	 public String menuTitle [] = new String[menuLength];
	 public mongoMenu customMenu[] = new mongoMenu[5];
	 int menuCount = 0;
	
	 public int returnAllMenus(){
		 int result = 0;
		 try {
		     result = readAllCustomMenus();
		 } catch (UnknownHostException e){
			 System.out.println("Host not found");
		 }
		 return result;
	 }
	  
	 int readAllCustomMenus() throws UnknownHostException {
	     // get handle to "mydb"
		 DB db = repoConnection.getConnection();
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
	        	 String menuIdText = doc.get("menuId").toString();
	        	 try {
	        	    menuIdFound = Integer.parseInt(menuIdText);	        		 
	        	 } catch (Exception e){
	        		menuIdFound = 0;
	        	 }
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

	   return menuCount;  

}		
	  

}