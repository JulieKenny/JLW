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

import java.net.UnknownHostException;
import java.sql.*;
import java.util.*;

public class validationScript {
	
	int scriptCount;
	int currentScriptId;
	String currentScriptText;
	String currentScriptCategory;
	
	public String[] listall(){
		String scriptText = new String();
		String scriptList[] = new String[100];
		//int scriptCount = -1;
		try {
			MongoClient mongoClient = new MongoClient("localhost",27017);
		    DB db = mongoClient.getDB("local");
		    
		    DBCollection coll = db.getCollection("customScripts");
		    DBObject doc ;
		    BasicDBObject query = new BasicDBObject();
		    BasicDBObject fields = new BasicDBObject();
			
		    fields.put("scriptText",1);
		    fields.put("scriptId", 1);
			fields.put("_id",0);	

	        try {
			       DBCursor allScripts = coll.find(query,fields);	        	
	        	   while (allScripts.hasNext()) {
		        	 doc = allScripts.next();
		        	 scriptCount = scriptCount + 1;
		        	 scriptList[scriptCount] = doc.get("scriptId").toString()+ ": " 
		        	                         + doc.get("scriptText").toString() ;
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
		int ScriptId = -1;
		int scriptStatus = 0;
		String scriptValue = null;

		ScriptId = fetchMenuScriptId(MenuId);
		currentScriptId = ScriptId;

     	if (ScriptId != -1 )
     	 {
		   scriptStatus = fetchScriptValue(ScriptId);
		   currentScriptText = scriptValue;
		   JOptionPane.showMessageDialog(null, scriptValue, 
	                "Requested Detail",
	                JOptionPane.PLAIN_MESSAGE);
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
		  int scriptStatus = 0;
		  
			 try {
				 scriptStatus = readMongoScript(ScriptId);
			 } catch (Exception e){
				 scriptStatus = 1;
				 System.out.println(e);
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
		fields.put("_id",0);
	    
        try {
	       DBObject customQuery = coll.findOne(query,fields);
	       errorId = 1;
	       currentScriptText = customQuery.get("scriptText").toString();  
	       errorId = 2;
	       currentScriptCategory = customQuery.get("ScriptCategory").toString();
	       errorId = 0;
	    }
        catch (Exception e){
        	System.out.println(e);
        	if (errorId == 0) {
        		errorId = -1;
        	}

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
	
	public void create(int scriptId,String scriptValue, String scriptCategory) {
		  
			 try {
				 createScript(scriptId,scriptValue,scriptCategory);
				 scriptMessage("Successfully saved script",false);
			 } catch (UnknownHostException e){
				 scriptMessage("Unknown Host",true);
			 }		  
		  
	}
	
	public void createScript(int ScriptId, String ScriptText, String ScriptCategory) throws UnknownHostException {
		MongoClient mongoClient = new MongoClient("localhost",27017);
	    DB db = mongoClient.getDB("local");

	    DBCollection coll = db.getCollection("customScripts");
	    BasicDBObject query = new BasicDBObject();
	    //BasicDBObject fields = new BasicDBObject();
        query.put("scriptId",ScriptId);
        query.put("scriptText",ScriptText);
        query.put("ScriptCategory", ScriptCategory);
            
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
		/** This will run a script **/
		 // JDBC driver name and database URL
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
		final String DB_URL = "jdbc:mysql://localhost:3306/sakila";
		String scriptText = new String();
		int scriptResult = 0;
		
	    try {    	
			Class.forName("com.mysql.jdbc.Driver");

		//  Database credentials
		  final String USER = "root";
		  final String PASS = "Pentah0";

		  Connection conn = null;
		  Statement stmt = null;

		  conn = DriverManager.getConnection(DB_URL,USER,PASS);	
	  
	      stmt = conn.createStatement();
		  scriptResult = fetchScriptValue(scriptId);	  
		  
	      ResultSet rs = stmt.executeQuery(currentScriptText);	
	      rs.next();
	      int aCount  = rs.getInt("actorCount");	
	      scriptMessage(Integer.toString(aCount),true);
	      scriptMessage(rs.toString(),true);
	      //STEP 6: Clean-up environment
	      rs.close();
	      stmt.close();
	      conn.close();	      
		} catch(Exception e){
		      //Handle errors for Class.forName
		      e.printStackTrace();		
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
		
//	private void scriptError(String errorString)
//	{
//		JOptionPane.showMessageDialog(null, errorString, 
//                "Note",
//                JOptionPane.PLAIN_MESSAGE); 
//	}	
	private void scriptMessage(String messageString,boolean isError)
	{
		String msgTitle = new String();
		if (isError){msgTitle = "Error";} else {msgTitle = "Info";}
		JOptionPane.showMessageDialog(null, messageString, 
                msgTitle,
                JOptionPane.PLAIN_MESSAGE); 
		
	}
		
}
