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

package com.softlyinspired.jlw.concerns;
import javax.swing.JOptionPane;

import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
//import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoException;
import com.softlyinspired.jlw.mongodb.repoConnection;

import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

public class concernSet {
	
	public int concernCount;
	public int menuCount;
	
	public String[][] listall(){
		String concernText = new String();
		String concernList[][] = new String[100][4];

		try {

		    DBCollection coll = repoConnection.getConcernCollection();
		    DBObject doc ;	
			
			String [] scriptHeader = new String[3];
	        try {	   
	        	   DBCursor allConcerns = coll.find();
	        	   concernCount = allConcerns.count();

			       for(int i =0; i < (concernCount); i++){
		        	 doc = allConcerns.next();
		        	 scriptHeader = getDetails(doc);
		        	 concernList[i][0] = scriptHeader[0];
		        	 concernList[i][1] = scriptHeader[1];	   
		        	 concernList[i][2] = scriptHeader[2];
		        	 concernList[i][3] = scriptHeader[3];
	        	 }       	

		    }
	        catch (Exception e){
	        	concernText = "No concerns have yet been defined " + coll.getFullName();
	        	JOptionPane.showMessageDialog(null, concernText, "Error",JOptionPane.PLAIN_MESSAGE); 
	        }

		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.toString(), "Error",JOptionPane.PLAIN_MESSAGE); 
			
		}
		return concernList;
		    
	}
	
	public concernMenu[] buildConcernMenus(){
	   int catCount = 0;	  
	   String [] catList = new String[10];
	   String [][] concernList = this.listall();
	   int listCount = this.concernCount;	   
	   try {
		   if (listCount == 0) {return null; };
	
		   int i;
		   for (i=0;i<listCount;i=i+1){
			   String titleLabel = new String(concernList[i][3]);			   
			   int c = 0;
			   boolean catFound = false;
			   for (c=0;c<catCount;c=c+1){
				   if (titleLabel.equals(catList[c])) {
					   catFound = true;
					   }
			   }
			   if (!catFound) {
			       catList[catCount] = titleLabel;
			       catCount= catCount + 1;
			   } 		   
		   }
	  } catch (Exception e){
		  System.out.println("Error finding categories");
	  }

	  //JOptionPane.showMessageDialog(null, catCount, "Category",JOptionPane.PLAIN_MESSAGE); 		   
	  concernMenu[] cM = new concernMenu[catCount];	   //set same number of menus as categories
	  try {
		  int i=0;
		   for (i=0;i<catCount;i=i+1){            //set up the menu for each category
			  concernMenu currentMenu = new concernMenu(); 
		      currentMenu.menuTitle = catList[i]; 
		      int currentMenuItemCount = 0;
		      int m =0;
		      for (m=0;m<listCount;m=m+1){
		    	  if (concernList[m][3].equals(currentMenu.menuTitle)){
		    		  currentMenu.menuItem[currentMenuItemCount] = concernList[m][0];
		    		  currentMenuItemCount = currentMenuItemCount + 1;
		    	  }
		      }
		      currentMenu.menuItemCount = currentMenuItemCount;
		      cM[i] = currentMenu;
		   }
	  } catch (Exception e){
		  System.out.println("Error building menus");
	  }

	   menuCount = catCount;
	   return cM;
	   
	}
	
	public class concernMenu {
		public int menuItemCount;
		public String menuTitle = new String();
		public String [] menuItem = new String[20];
		{
			// Nothing yet to say
		}
	}
	
	private String[] getDetails(DBObject doc)
	{
		String[] details = new String[4];
		try {details[0] = doc.get("concernId").toString();	 }
   	    catch (Exception e){ details[0] = "";}
		
	   	try {details[1] = doc.get("title").toString();}
	   	catch (Exception e){details[1] = "";}
	   	
	   	try {details[2] = doc.get("description").toString();}
	   	catch (Exception e){details[2] = "no description";}

	   	try {details[3] = doc.get("category").toString();}
	   	catch (Exception e){details[3] = "Concern";}
	   	
	   	return details;
   	 
	}
	
		
}
