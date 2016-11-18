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

package com.softlyinspired.jlw.script;
import javax.swing.JOptionPane;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;





import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.softlyinspired.jlw.JLWProperties;
import com.softlyinspired.jlw.JLWUtilities;
import com.softlyinspired.jlw.connections.dbConnection;
import com.softlyinspired.jlw.mongodb.repoConnection;

import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

public class validationScript {
	
	public int scriptCount;
	public int currentScriptId;
	public String currentScriptText;
	public String currentScriptCategory;
	public String currentScriptTitle;
	public String currentConnection;
	
	/**
	 * Lists all the scripts available
	 * @return  Two dimensional array
	 */
	public String[][] listall(){
		String scriptList[][] = new String[100][2];
		String tempId = new String();
		String tempTitle = new String();

		try {
			DBCollection coll = repoConnection.getScriptCollection();
			
		    DBObject doc ;
		    BasicDBObject query = new BasicDBObject();
		    BasicDBObject fields = new BasicDBObject();
		    BasicDBObject sort = new BasicDBObject();
			
		    fields.put("scriptText",1);
		    fields.put("scriptId", 1);
		    fields.put("scriptTitle",1);
			fields.put("_id",0);	
			
			sort.put("scriptId", 1);

	        try {
			       DBCursor allScripts = coll.find(query,fields); //.sort(sort);	
			       allScripts.sort(sort);
			       scriptCount = -1;
	        	   while (allScripts.hasNext()) {
		        	 doc = allScripts.next();    	 
		        	 scriptCount = scriptCount + 1;		
		        	 tempId = doc.get("scriptId").toString();
		        	 doc.get("scriptText").toString();
		        	 try {
		        	 tempTitle = doc.get("scriptTitle").toString();
		        	 }
		        	 catch (Exception e){
		        		 tempTitle = "";
		        	 }

		        	 scriptList[scriptCount][0] = tempId;
		        	 scriptList[scriptCount][1] = tempTitle;	        	 
	        	 }       	

		    }
	        catch (Exception e){
	        	System.out.println("Script Missing");
	        }

		}
		catch (Exception e) {
			JLWUtilities.scriptErrorMessage(e.toString());
			
		}
		return scriptList;
		    
	}
	
	/**
	 * Accepts a menuId and sets the currentScriptText string
	 * @param MenuId
	 */
	public void fetchScriptFromMenu(int MenuId){
		try{
		int ScriptId = -1;
		String scriptValue = null;

		ScriptId = fetchMenuScriptId(MenuId);
		currentScriptId = ScriptId;

     	if (ScriptId != -1 )
     	 {
		   fetchScriptValue(ScriptId);
		   currentScriptText = scriptValue;
     	 }
		}catch (Exception ex){
			JLWUtilities.scriptErrorMessage("Error fetching menu script");
		}
	}

	/**
	 * Accepts the menuId and returns the scriptId 
	 * @param MenuId
	 * @return
	 */
	public int fetchMenuScriptId(int MenuId) {
		int ScriptId = -1;
		try {
			ScriptId = readMongoMenuScriptId(MenuId);			
			if (ScriptId == -1 )
			{
			 JLWUtilities.scriptErrorMessage("No Script Found");
			}	
		}
        catch (Exception e) {
        	JLWUtilities.scriptErrorMessage("Error fetching script");  }		
		 return ScriptId;
	}
	
	/**
	 * returns a scriptId
	 * @param MenuId
	 * @throws UnknownHostException
	 */
	private int readMongoMenuScriptId(int MenuId) throws UnknownHostException {
	    int scriptId =-1;
	    
		DBCollection coll = repoConnection.getScriptCollection();	    

	    BasicDBObject query = new BasicDBObject();
	    BasicDBObject fields = new BasicDBObject();
        query.put("menuId",MenuId);
            
		fields.put("scriptIds",1);
		fields.put("_id",0);
	    
        try {
	       DBObject scriptIdQuery = coll.findOne(query,fields);
		   scriptId = Integer.parseInt(scriptIdQuery.get("scriptIds").toString()); 

	    }
        catch (Exception e){
        	JLWUtilities.scriptErrorMessage("Error fetching mongo script");       	
        }

        return scriptId;
	}
	
	/**
	 * Gets the value of the script given the ID
	 * @param ScriptId
	 * @return
	 */
	public int fetchScriptValue(int ScriptId) {
	/** this method accepts a script id and sets all the values on the script 
	 *  Int value returned, 0 if all fine , 1 if not
	 **/
		  int scriptStatus = 0;
			 try {
				 scriptStatus = readMongoScript(ScriptId);
			 } catch (Exception e){

				 System.out.println("error in fetchScriptValue");
				 return 1;
			 }	
		  if (scriptStatus != 0){
			  return 1;
		  }
		  return scriptStatus;	   
	}
	
	/**
	 * Gets the full details of the script using the script id
	 * @param ScriptId
	 * @return
	 * @throws UnknownHostException
	 */
	private int readMongoScript(int ScriptId) throws UnknownHostException {	    //String scriptText = new String();
	    int errorId = 0;

		DBCollection coll = repoConnection.getScriptCollection();	
	    BasicDBObject query = new BasicDBObject();
	    BasicDBObject fields = new BasicDBObject();
	    
        query.put("scriptId",ScriptId);
            
		fields.put("scriptText",1);
		fields.put("ScriptCategory",1);
		fields.put("scriptTitle",1);
		fields.put("scriptConnection",1);
		fields.put("_id",0);
	    
        try {
	        DBObject customQuery = coll.findOne(query,fields);
	 	     
			try {currentScriptText = customQuery.get("scriptText").toString(); } 
			   catch (Exception e){currentScriptText = "unknown";}
			try {currentScriptCategory = customQuery.get("ScriptCategory").toString();} 
			   catch (Exception e){currentScriptCategory = "unknown";}
			try {currentScriptTitle = customQuery.get("scriptTitle").toString();} 
			   catch (Exception e){currentScriptTitle = "unknown";}
			try {currentConnection = customQuery.get("scriptConnection").toString();} 
			   catch (Exception e){currentConnection = "unknown";}
        }
        finally {
          }
	
        return errorId; 
	}

	/**
	 * delete the script given the scriptid
	 * @param scriptId
	 */
	public void delete(int scriptId){
		try {
			deleteScript(scriptId);
		}catch (UnknownHostException e){
			JLWUtilities.scriptErrorMessage("Error attempting delete");
		}
	}
	
	/**
	 * delete the script from the mongodb
	 * @param ScriptId
	 * @throws UnknownHostException
	 */
	public void deleteScript(int ScriptId) throws UnknownHostException {
		DBCollection coll = repoConnection.getScriptCollection();	

	    try {
	       BasicDBObject query = new BasicDBObject();
           query.put("scriptId",ScriptId);
           coll.remove(query);
	    }
        catch (Exception e){
        	JLWUtilities.scriptErrorMessage("Error deleting script");
        }
	}	
	
	/**
	 * create script with given details
	 * @param scriptId
	 * @param scriptValue
	 * @param scriptCategory
	 * @param scriptTitle
	 * @param scriptConnection
	 */
	public void create(int scriptId,String scriptValue, String scriptCategory, String scriptTitle, String scriptConnection) {
		  
			 try {
				 writeNewScript(scriptId,scriptValue,scriptCategory,scriptTitle,scriptConnection);
				 JLWUtilities.scriptInfoMessage("Successfully saved script");
			 } catch (UnknownHostException e){
				 JLWUtilities.scriptErrorMessage("Unknown Host");
			 }		    
	}
	
	/**
	 * Update an existing script, all details passed.
	 * @param scriptId
	 * @param scriptValue
	 * @param scriptCategory
	 * @param scriptTitle
	 * @param scriptConnection
	 */
	public void updateScript(int scriptId,String scriptValue, String scriptCategory, String scriptTitle,
			          String scriptConnection) { 
		 try {
			 writeExistingScript(scriptId,scriptValue,scriptCategory,scriptTitle,scriptConnection);
			 JLWUtilities.scriptInfoMessage("Successfully updated script");
		 } catch (UnknownHostException e){
			 JLWUtilities.scriptErrorMessage("Unknown Host");
		 }		  
    }	

	/**
	 * Write details of existing script to mongodb
	 * @param scriptId
	 * @param scriptText
	 * @param scriptCategory
	 * @param scriptTitle
	 * @param scriptConnection
	 * @throws UnknownHostException
	 */
	private void writeExistingScript(int scriptId, String scriptText, String scriptCategory, 
			                            String scriptTitle, String scriptConnection) throws UnknownHostException {
		DBCollection coll = repoConnection.getScriptCollection();
	    BasicDBObject updateQuery = new BasicDBObject();

	    BasicDBObject searchQuery = new BasicDBObject().append("scriptId", scriptId);
	    
	    DBObject changes = new BasicDBObject().append("scriptText",scriptText)
                .append("ScriptCategory",scriptCategory)
                .append("scriptTitle",scriptTitle)
                .append("scriptConnection",scriptConnection);
	    updateQuery.append("$set", changes);	    
            
        try {
	       coll.update(searchQuery,updateQuery);   
	    }
        catch (Exception e){
        	JLWUtilities.scriptErrorMessage("Error creating script");
        }

	}
	
	/**
	 * write new script to mongodb given full details
	 * @param scriptId
	 * @param scriptText
	 * @param scriptCategory
	 * @param scriptTitle
	 * @param scriptConnection
	 * @throws UnknownHostException
	 */
	private void writeNewScript(int scriptId, String scriptText, String scriptCategory, String scriptTitle, 
			                       String scriptConnection) throws UnknownHostException {
		DBCollection coll = repoConnection.getScriptCollection();
		
	    BasicDBObject query = new BasicDBObject();

        query.put("scriptId",scriptId);
        query.put("scriptText",scriptText);
        query.put("scriptTitle",scriptTitle);        
        query.put("ScriptCategory",scriptCategory);
        query.put("scriptConnection",scriptConnection);
            
        try {
	       coll.insert(query);
	    }
        catch (Exception e){
        	JLWUtilities.scriptErrorMessage("Error creating script");
        }

	}
	
	public String getOutputFile(int scriptId){
		String fn = new String();
	    String fileName = new String();
	    fileName = currentScriptTitle + '_' +  Integer.toString(scriptId) +  "_output.csv";
	    fileName = JLWProperties.scriptOutput + "\\" + fileName;
		return "fileName";
	}
	
	/**
	 * run script given the id
	 * @param scriptId
	 */
	public int runScript(int scriptId) {
		/** This will run a script and output the result to a file as csv **/
		
		int result = 0;
		try {
		    fetchScriptValue(scriptId);	
		} catch (Exception e){
			result = 1;
			System.out.println("error getting script details");
		};
		
	    try {    	
	      String fileName = new String();
	      fileName = getOutputFile(scriptId);
	      File file = new File(fileName);

		  // if file doesn't exists, then create it
		  if (!file.exists()) {file.createNewFile();}

		  FileWriter fw = new FileWriter(file.getAbsoluteFile());
		  BufferedWriter bw = new BufferedWriter(fw);	
		
		  dbConnection cn = new dbConnection();

		  cn.fetchDetails(currentConnection);
		  
		  Class.forName(cn.connectionDriver);

		  // Database credentials
		  Connection conn = null;
		  Statement stmt = null;

		  conn = DriverManager.getConnection(cn.connectionURL,cn.connectionUser,cn.connectionPassword);	
	      stmt = conn.createStatement();
	      		  
	      ResultSet rs = stmt.executeQuery(currentScriptText);	
	      ResultSetMetaData rsmd = rs.getMetaData();
	      	      
	      int numberOfColumns = rsmd.getColumnCount();
	      String columnName = new String("");
	      
	      // Output column names
	      for (int i = 1; i <= numberOfColumns; i++) {
	        if (i==1)	{columnName = rsmd.getColumnName(i);}
	        else {columnName = columnName + "," + rsmd.getColumnName(i);}
	        }
		  bw.write(columnName);
		  
	      String rowValues = new String("");
	      while (rs.next()) {
	    	  for (int i = 1; i <= numberOfColumns; i++) {
	    		  if (i==1){rowValues = rs.getObject(i).toString();}
	    		  else { rowValues = rowValues + ',' + rs.getObject(i).toString(); }
	    	  }
	    	  bw.newLine();
	    	  bw.write(rowValues);
	      }

		  bw.close();
	      rs.close();
	      stmt.close();
	      conn.close();	      
	      
		} catch(Exception e){
			result = 1;
			JLWUtilities.scriptErrorMessage(" Error attempting to run script");
		}

	  return result;
	}
	
	
		
}
