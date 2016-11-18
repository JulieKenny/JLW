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

import java.util.ArrayList;
import java.util.ListIterator;

import com.lowagie.text.Document;
import com.mongodb.*;
import com.softlyinspired.jlw.mongodb.*;

public class concern {

	int concernId ;
	public String concernTitle = new String();
	public String concernDescription = new String();
	public String concernCategory = new String();
	boolean concernExists;
	int[][] scripts; 
	int[][] reports ;
	DBCollection concernColl ;
	DBObject currentConcernDoc; 

	public int create(int newId,String newName,String newDescription) {
	/* Create a new concern */	
		JOptionPane.showMessageDialog(null, Integer.toString(newId), "New id",JOptionPane.PLAIN_MESSAGE);

		if (checkExistingId(newId)) {
			return 1;
		}
		
		concernId = newId;		
		try {
			/* Get the database collection */
			concernColl = repoConnection.getConcernCollection();
			if  (concernColl == null ){
				return -1;
			};
		     
		  // search for the id
		    BasicDBObject query = new BasicDBObject(); 
			query.put("concernId",newId);
			query.put("title", newName);
			query.put("description", newDescription);
			
	        try {
	 	       concernColl.insert(query);  
	 	    }
	         catch (Exception e){
	        	 System.out.println("Error creating concern");
	         }
		} catch (Exception e){
			    System.out.println("error in checking script during create");
		}       
		return 0;
	}
	
	/**
	 *  Update concern details (title, category and description)
	 */
	public int updateExisting(int concernId, concern newConcern) {
		
		int result = 0;
		concernColl = repoConnection.getConcernCollection();		
		checkExistingId(concernId);
		
       try {
			// search for the id
		    BasicDBObject updateQuery = new BasicDBObject(); 	    
			
		    BasicDBObject searchQuery = new BasicDBObject().append("concernId", concernId);		
		    DBObject changes = new BasicDBObject().append("title",concernTitle)
		    		                              .append("category", concernCategory)
	                                              .append("description",concernDescription);	  
		    updateQuery.append("$set", changes);	    
			concernColl.update(searchQuery, updateQuery);
 	    }
         catch (Exception e){
        	 System.out.println("Error updating concern");
         }
      
		return result;
	}
	
	/**
	 *  Add the script (passed as scriptId) to the concern into the database
	 */
		
	public int addScript(int scriptId) {
		int result = 0;	
		
		BasicDBObject scriptDetail = new BasicDBObject();
		scriptDetail.append("scriptId", scriptId);
		scriptDetail.append("scriptOrder", 99);
		BasicDBObject updateQuery = new BasicDBObject();

		DBObject listitem = new BasicDBObject ("Scripts", new BasicDBObject("scriptId",scriptId).append("scriptOrder", 99));
		updateQuery.append("$push",listitem);	
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.update(searchQuery, updateQuery);
		
		return result;
	}	
	
	/**
	 *  Add the report (passed as reportId) to the concern into the database
	 */
		
	public int addReport(int reportId) {
		int result = 0;	
		
		BasicDBObject reportDetail = new BasicDBObject();
		reportDetail.append("reportId", reportId);
		reportDetail.append("reportOrder", 99);
		BasicDBObject updateQuery = new BasicDBObject();

		DBObject listitem = new BasicDBObject ("Reports", 
				                    new BasicDBObject("reportId",reportId).append("reportOrder", 99));
		updateQuery.append("$push",listitem);	
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.update(searchQuery, updateQuery);
		
		return result;
	}	
	
	/**
	 * remove the scriptId passed from the current concern from the database
	 */
	public int removeScript(int scriptId){
        int result = 0;	
		
		BasicDBObject scriptDetail = new BasicDBObject();
		scriptDetail.append("scriptId", scriptId);
		scriptDetail.append("scriptOrder", 99);
		BasicDBObject updateQuery = new BasicDBObject();

		DBObject listitem = new BasicDBObject ("Scripts", new BasicDBObject("scriptId",scriptId).append("scriptOrder", 99));
		updateQuery.append("$pull",listitem);	
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.update(searchQuery, updateQuery);
		
		return result;
	}

	/**
	 * remove the reportId passed from the current concern from the database
	 */
	public int removeReport(int reportId){
        int result = 0;	
		
		BasicDBObject reportDetail = new BasicDBObject();
		reportDetail.append("reportId", reportId);
		reportDetail.append("reportOrder", 99);
		BasicDBObject updateQuery = new BasicDBObject();

		DBObject listitem = new BasicDBObject ("Reports", new BasicDBObject("reportId",reportId).append("reportOrder", 99));
		updateQuery.append("$pull",listitem);	
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.update(searchQuery, updateQuery);
		
		return result;
	}	
	
	/** 
	 * delete the concern passed as a concernId 
	 * */
	public int delete(int concernId) {
		int result = 0;
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.remove(searchQuery);
		return result;
	}
	
	/**
	 *  set description, title and category for current concern - passed as concernId 
	 **/
	public concern fetchConcernDetails(int concernCheckId) {
		checkExistingId(concernCheckId);
		concernId = concernCheckId;

		try {
		concernDescription = currentConcernDoc.get("description").toString();
		} catch (Exception e){concernDescription = "";};
		
		try {
		concernTitle = currentConcernDoc.get("title").toString();
		} catch (Exception e){concernTitle = "";}
		
		try {
		concernCategory = currentConcernDoc.get("category").toString();
		} catch (Exception e){concernCategory = "";}		
		return this;

	}	

	/**
	 * Return a list of the reports associated with the concern 
	 **  each report will have an id and a order 
	 */	
	public int getReports(int concernId){
		int reportCount ;
		ArrayList<String[]> reportList = new ArrayList<String[]>();
		checkExistingId(concernId);
		
		try {
			currentConcernDoc.get("Reports");
			
			ListIterator<Object> scriptObjects = 
					((BasicDBList) currentConcernDoc.get("Reports")).listIterator();
			while(scriptObjects.hasNext()){
				String[] t = new String[2];
	            BasicDBObject nextItem = (BasicDBObject) scriptObjects.next();
	            t[0] = nextItem.getString("reportId");
	            t[1] = nextItem.getString("reportOrder");          
	            reportList.add(t);
	
	        }	
			if (reportList.isEmpty()) {
				return 0;
			} else {
				reportCount = reportList.size();
				reports = new int[reportCount][2];
				int i;	
				for (i=0;i<reportCount;i++)	{
					String[] t = new String[2];
					t = reportList.get(i);
					reports[i][0] = Integer.parseInt(t[0]);
					reports[i][1] = Integer.parseInt(t[1]);
				}	
			}
		} catch (Exception e){
			return 0;
		}
		
		return reportCount;			
	}	
	
	/**
	 * Return a list of the scripts associated with the concern 
	 **  each script will have an id and a order 
	 */	
	public int getScripts(int concernId){


		int scriptCount ;
		ArrayList<String[]> scriptList = new ArrayList<String[]>();
		checkExistingId(concernId);
		
		try {
			currentConcernDoc.get("scripts");

			ListIterator<Object> scriptObjects = ((BasicDBList) currentConcernDoc.get("Scripts")).listIterator();
			while(scriptObjects.hasNext()){
				String[] t = new String[2];
	            BasicDBObject nextItem = (BasicDBObject) scriptObjects.next();
	            t[0] = nextItem.getString("scriptId");
	            t[1] = nextItem.getString("scriptOrder");          
	            scriptList.add(t);
	
	        }	
			if (scriptList.isEmpty()) {
				return 0;
			} else {
				scriptCount = scriptList.size();
				scripts = new int[scriptCount][2];
				int i;	
				for (i=0;i<scriptCount;i++)	{
					String[] t = new String[2];
					t = scriptList.get(i);
					scripts[i][0] = Integer.parseInt(t[0]);
					scripts[i][1] = Integer.parseInt(t[1]);
				}	
			}
		} catch (Exception e){
			return 0;
		}
		
		return scriptCount;			
	}
	
	
	/**
	 * Checks if the concernId already exists and returns boolean
	 */	
	public boolean checkExistingId(int CheckId){

		try {
			/* Get the database collection */
			concernColl = repoConnection.getConcernCollection();
			if  (concernColl == null ){			
				return false;
			};
		     
		  // search for the id
		    BasicDBObject query = new BasicDBObject(); 
			query.put("concernId",CheckId);
			DBCursor checkDoc =concernColl.find(query).limit(1);

			 if (checkDoc.count() == 0)
			 {concernExists = false;}
			 else {currentConcernDoc = checkDoc.next();
				 concernExists = true;}			 
			 
		} catch (Exception e){
			    System.out.println("error finding concern");
				concernExists = false;
			}
		return concernExists;
	}	
	

	
}
