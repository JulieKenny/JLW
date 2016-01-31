import javax.swing.JOptionPane;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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

import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

public class validationScript {
	
	int scriptCount;
	int currentScriptId;
	String currentScriptText;
	String currentScriptCategory;
	String currentScriptTitle;
	String currentConnection;

	
	public String[][] listall(){
		String scriptText = new String();
		String scriptList[][] = new String[100][2];
		String tempId = new String();
		String tempText = new String();
		String tempTitle = new String();

		try {
			MongoClient mongoClient = new MongoClient("localhost",27017);
		    DB db = mongoClient.getDB("local");
		    
		    DBCollection coll = db.getCollection("customScripts");
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
		        	 tempText = doc.get("scriptText").toString();
		        	 try {
		        	 tempTitle = doc.get("scriptTitle").toString();
		        	 }
		        	 catch (Exception e){
		        		 tempTitle = "";
		        	 }

		        	 scriptList[scriptCount][0] = tempId;
		        	 scriptList[scriptCount][1] = tempTitle;
		        	 //scriptMessage(scriptList[scriptCount][1],true);		        	 
	        	 }       	

		    }
	        catch (Exception e){
	        	scriptText = "There was nothing there";
	        }

	        mongoClient.close();
		}
		catch (Exception e) {
			scriptMessage(e.toString(),true);
			
		}
		return scriptList;
		    
	}
	
	public void fetchScriptFromMenu(int MenuId){
		try{
		int ScriptId = -1;
		int scriptStatus = 0;
		String scriptValue = null;

		ScriptId = fetchMenuScriptId(MenuId);
		currentScriptId = ScriptId;

     	if (ScriptId != -1 )
     	 {
		   scriptStatus = fetchScriptValue(ScriptId);
		   currentScriptText = scriptValue;
     	 }
		}catch (Exception ex){
			scriptMessage("Error fetching menu script",true);
		}
	}

	public int fetchMenuScriptId(int MenuId) {
		int ScriptId = -1;
		try {
			ScriptId = readMongoMenuScriptId(MenuId);			
			if (ScriptId == -1 )
			{scriptMessage("No Script Found",true);
			}	
		}
        catch (Exception e) {
        	  scriptMessage("Error fetching script",true);  }		
		 return ScriptId;
	}
	
	public int readMongoMenuScriptId(int MenuId) throws UnknownHostException {
	    int scriptId =-1;
		MongoClient mongoClient = new MongoClient("localhost",27017);
	    DB db = mongoClient.getDB("local");

	    DBCollection coll = db.getCollection("customMenus");
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
			 scriptMessage("Error fetching mongo script",true);        	
        }

        mongoClient.close();
        return scriptId;
	}
	
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
	
	public int readMongoScript(int ScriptId) throws UnknownHostException {
	    //String scriptText = new String();
	    int errorId = 0;
		MongoClient mongoClient = new MongoClient("localhost",27017);
	    DB db = mongoClient.getDB("local");

	    DBCollection coll = db.getCollection("customScripts");
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
	
        mongoClient.close();
        return errorId; 
	}

	public void delete(int scriptId){
		try {
			deleteScript(scriptId);
		}catch (UnknownHostException e){
			scriptMessage("Error attempting delete",true);
		}
	}
	
	public void deleteScript(int ScriptId) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost",27017);
	    DB db = mongoClient.getDB("local");
	    DBCollection thisCollection = db.getCollection("customScripts");

	    try {
	       BasicDBObject query = new BasicDBObject();
           query.put("scriptId",ScriptId);
	       thisCollection.remove(query);
	    }
        catch (Exception e){
        	scriptMessage("Error deleting script",true);
        }

        mongoClient.close();
	}	
	
	public void create(int scriptId,String scriptValue, String scriptCategory, String scriptTitle, String scriptConnection) {
		  
			 try {
				 writeNewScript(scriptId,scriptValue,scriptCategory,scriptTitle,scriptConnection);
				 scriptMessage("Successfully saved script",false);
			 } catch (UnknownHostException e){
				 scriptMessage("Unknown Host",true);
			 }		  
		  
	}
	
	public void updateScript(int scriptId,String scriptValue, String scriptCategory, String scriptTitle,
			          String scriptConnection) {
		  
		 try {
			 writeExistingScript(scriptId,scriptValue,scriptCategory,scriptTitle,scriptConnection);
			 scriptMessage("Successfully updated script",false);
		 } catch (UnknownHostException e){
			 scriptMessage("Unknown Host",true);
		 }		  

    }	

	public void writeExistingScript(int scriptId, String scriptText, String scriptCategory, 
			                            String scriptTitle, String scriptConnection) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost",27017);
	    DB db = mongoClient.getDB("local");

	    DBCollection coll = db.getCollection("customScripts");
	    BasicDBObject updateQuery = new BasicDBObject();

	    BasicDBObject searchQuery = new BasicDBObject().append("scriptId", scriptId);
	    
	    DBObject changes = new BasicDBObject().append("scriptText",scriptText)
                .append("ScriptCategory",scriptCategory)
                .append("scriptTitle",scriptTitle)
                .append("scriptConnection",scriptConnection);
	    updateQuery.append("$set", changes);	    
            
        try {
	       coll.update(searchQuery,updateQuery);
	       //scriptText = customQuery.toString();   
	    }
        catch (Exception e){
        	scriptMessage("Error creating script",true);
        }

        mongoClient.close();
	}
	
	public void writeNewScript(int scriptId, String scriptText, String scriptCategory, String scriptTitle, 
			                       String scriptConnection) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost",27017);
	    DB db = mongoClient.getDB("local");

	    DBCollection coll = db.getCollection("customScripts");
	    BasicDBObject query = new BasicDBObject();
	    //BasicDBObject fields = new BasicDBObject();
        query.put("scriptId",scriptId);
        query.put("scriptText",scriptText);
        query.put("scriptTitle",scriptTitle);        
        query.put("ScriptCategory",scriptCategory);
        query.put("scriptConnection",scriptConnection);
            
        try {
	       coll.insert(query);
	       //scriptText = customQuery.toString();   
	    }
        catch (Exception e){
        	scriptMessage("Error creating script",true);
        }

        mongoClient.close();
	}
	
	public void runScript(int scriptId) {
		/** This will run a script and output the result to a file as csv **/
		 // JDBC driver name and database URL
//		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
//		final String DB_URL = "jdbc:mysql://localhost:3306/DW1";
//		final String USER = "testuser";
//		final String PASS = "password";		//String scriptText = new String();
		
		try {
		    int scriptResult = fetchScriptValue(scriptId);	
		} catch (Exception e){
			System.out.println("error");
		};
		
	    try {    	
	      String fileName = new String();
	      fileName = Integer.toString(scriptId) +  "_myfile.csv";
	      fileName = "C:\\Users\\Julie\\Documents\\temp\\" + fileName;
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
		  //currentScriptText = "Select * from customers";
		  
	      ResultSet rs = stmt.executeQuery(currentScriptText);	
	      rs.next();
	      //STEP 6: Clean-up environment
	      
	      ResultSetMetaData rsmd = rs.getMetaData();
	      	      
	      int numberOfColumns = rsmd.getColumnCount();
	      String columnName = new String("");
	      
	      // Output column names
	      for (int i = 1; i <= numberOfColumns; i++) {
	        if (i==1)	{columnName = rsmd.getColumnName(i);}
	        else {columnName = columnName + "," + rsmd.getColumnName(i);}
	        }
		  bw.write(columnName);
		  
	      int rowCount = rs.getFetchSize();
	      String rowValues = new String("");
	      rs.first();
	      for (int r = 0; r <= rowCount; r++) {
	    	  for (int i = 1; i <= numberOfColumns; i++) {
	    		  if (i==1){rowValues = rs.getObject(i).toString();}
	    		  else { rowValues = rowValues + ',' + rs.getObject(i).toString(); }
	    	  }
	    	  bw.newLine();
	    	  bw.write(rowValues);
	    	  rs.next();
	      }

		  bw.close();
	      rs.close();
	      stmt.close();
	      conn.close();	      
	      
		} catch(Exception e){
		      //Handle errors for Class.forName		
			  scriptMessage(e.toString(), true);
		      scriptMessage(" Error attempting to run script",true);
		}

	}
	
	public Connection getConnection() throws SQLException {

	    Connection conn = null;
	    Properties connectionProps = new Properties();
	    connectionProps.put("user", "root");
	    connectionProps.put("password", "Pentah0");


	        conn = DriverManager.getConnection(
	                   "jdbc:sakila://localhost:3306/",
	                   connectionProps);
	    
	    System.out.println("Connected to database");
	    return conn;
	}
		
	
	private void scriptMessage(String messageString,boolean isError)
	{
		String msgTitle = new String();
		if (isError){msgTitle = "Error";} else {msgTitle = "Info";}
		JOptionPane.showMessageDialog(null, messageString, 
                msgTitle,
                JOptionPane.PLAIN_MESSAGE); 
		
	}
		
}
