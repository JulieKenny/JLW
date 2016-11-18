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

package com.softlyinspired.jlw.reports;

import com.mongodb.*;
import com.softlyinspired.jlw.JLWUtilities;
import com.softlyinspired.jlw.mongodb.repoConnection;

public class ReportReference {
	
	public int reportId;
	private String reportName ;
	private String reportType ;
	private String reportDescription ;
	private String reportCategory;
	private String reportLocation;
	public int reportCount;
	
	DBCollection coll ;
	MongoClient mongoClient;
	DBObject currentReportDoc;	
	
	/**
	 * Lists all the reports available
	 * @return  Two dimensional array
	 */
	public String[][] listall(){
		String reportList[][] = new String[100][2];
		String tempId = new String();
		String tempTitle = new String();

		try {
			DBCollection coll = repoConnection.getReportsCollection();
			
		    DBObject doc ;
		    BasicDBObject query = new BasicDBObject();
		    BasicDBObject fields = new BasicDBObject();
		    BasicDBObject sort = new BasicDBObject();
			
		    fields.put("name",1);
		    fields.put("reportId", 1);
			fields.put("_id",0);	
			
			sort.put("reportId", 1);

	        try {
			       DBCursor allReports = coll.find(query,fields); 	
			       allReports.sort(sort);
			       reportCount = -1;
	        	   while (allReports.hasNext()) {
		        	 doc = allReports.next();    	 
		        	 reportCount = reportCount + 1;		
		        	 tempId = doc.get("reportId").toString();
		        	 try {tempTitle = doc.get("name").toString();}
		        	 catch (Exception e){tempTitle = "";}

		        	 reportList[reportCount][0] = tempId;
		        	 reportList[reportCount][1] = tempTitle;	        	 
	        	 }       	
		    }
	        catch (Exception e){
	        	System.out.println("Report Missing");
	        }
		}
		catch (Exception e) {
			JLWUtilities.scriptErrorMessage(e.toString());
		}
		return reportList;
	}	
	
	public int create() {
		/* Create a new report reference */	
		
			if (this.checkExisting()) {
				return 1;
			}
			
			try {
				/* Get the database collection */
				DBCollection reportColl = repoConnection.getReportsCollection();	
		        if (reportColl == null) {
		        	 return -1;
		        } 
			     
			  // search for the id
			    BasicDBObject query = new BasicDBObject(); 

				query.put("reportId", reportId);
				query.put("name",reportName);
				query.put("type", reportType);
				query.put("description", reportDescription);
				query.put("category", reportCategory);
				query.put("reportLocation", reportLocation);
		        try {
		        	reportColl.insert(query);  		        	
		 	    }
		         catch (Exception e){
		        	 System.out.println("Error defining report");
		         }

			} catch (Exception e){
				    System.out.println("error during report reference create");
			}       
			return 0;
		}
	
	/**
	 * 
	 * @return boolean - true if report id exists
	 */
	public boolean checkExisting(){
		/**
		 * Returns if report reference exists or not;
		 */
		boolean IdExists;
		try {
			/* Get the database collection */
			DBCollection coll = repoConnection.getReportsCollection();	
	        if (coll == null) {return false;};
		    
		    BasicDBObject query = new BasicDBObject(); 
			query.put("reportId",reportId);
			
			DBCursor checkDoc = coll.find(query).limit(1);
			if (checkDoc.count() == 0)
			   { IdExists = false;} 
			else {IdExists = true;
			      currentReportDoc = checkDoc.next();
			      reportName = currentReportDoc.get("name").toString();
			      reportType = currentReportDoc.get("type").toString();
			  	  reportDescription = currentReportDoc.get("description").toString();
			  	  reportCategory = currentReportDoc.get("category").toString();
			  	  reportLocation = currentReportDoc.get("reportLocation").toString();	  	  
			}
			 
		} catch (Exception e){
			    System.out.println("Report does not exist");
			    IdExists = false;
			}
		return IdExists;
	}	

	public int updateExisting() {
	/* Update report details */
		
		int result = 0;
		DBCollection coll = repoConnection.getReportsCollection();	
        checkExisting();

       try {
			// search for the id
		    BasicDBObject updateQuery = new BasicDBObject(); 	   
			
		    BasicDBObject searchQuery = new BasicDBObject().append("reportId", reportId);		
		    DBObject changes = new BasicDBObject().append("name",reportName)
		    		                              .append("type", reportType)
		    		                              .append("description", reportDescription)
		    		                              .append("category", reportCategory)
		    		                              .append("reportLocation",reportLocation);	  
		    updateQuery.append("$set", changes);	    
		    coll.update(searchQuery, updateQuery);
 	    }
         catch (Exception e){
        	 System.out.println("Error updating report");
         }
		return result;
	}	
	
	public int delete() {
	/* Delete current report */
		try {
	       coll = repoConnection.getReportsCollection();
		   BasicDBObject searchQuery = new BasicDBObject().append("reportId", this.reportId);
		   coll.remove(searchQuery);
		   return 0;
		} catch (Exception E){
			System.out.println("Error in delete process");
			return 1;
		}
		
	}	
	
	public int fetchDetails(int editReportId) {
	/* Update report details */
		int result = 0;
		this.reportId = editReportId;
		if (checkExisting()){
			try {
				reportName = currentReportDoc.get("name").toString();
			} catch (Exception e){
				 reportName = "error name";
			     result = result + 1;}
			try {
			reportType = currentReportDoc.get("type").toString();
			} catch (Exception e){reportType = "";}
				 
			try {
				reportCategory = currentReportDoc.get("category").toString();
			} catch (Exception e){reportCategory = ""; }
			
			try {
				reportDescription = currentReportDoc.get("description").toString();
			} catch (Exception e){reportDescription = "";}
			
			try {
				reportLocation = currentReportDoc.get("reportLocation").toString();
			} catch (Exception e){reportLocation = "";}
		 }else { result = 1;}
	
 		return result;
	}
	
	public String getName(){	
		return reportName;
	}
		
	public String getType(){
        return reportType;
	}
	
	public String getCategory(){
		return reportCategory;
	}

	public String getDescription(){
		return reportDescription;
	}	
	
	public String getLocation(){
		return reportLocation;
	}
	
	
	
	public void setLocation(String newlocation){
		reportLocation = newlocation;
	}
	
	public void setName(String name){
		reportName = name;
	}

	public void setCategory(String category){
		reportCategory = category;
	}
	
	public void setType(String type){
		reportType = type;
	}

	public void setDescription(String desc){
		if (desc == null){ desc="";};
		reportDescription = desc;
	}

	
	
	
	
	
}
