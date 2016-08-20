package com.softlyinspired.jlw.connections;
import javax.swing.JOptionPane;

import com.mongodb.*;
import com.softlyinspired.jlw.JLWUtilities;
import com.softlyinspired.jlw.mongodb.repoConnection;

public class dbConnection {
	
	public String connectionName ;
	public String connectionDriver ;
	public String connectionURL ;
	public String connectionUser;
	public String connectionPassword;
	
	DBCollection connectionColl ;
	MongoClient mongoClient;
	DBObject currentConnectionDoc;	
	private static String collectionName = new String("connections");
	
	public int create() {
		    /* Create a new connection */	
			if (this.checkExisting()) {
				return 1;
			}
			
			try {
				/* Get the database collection */
				DBCollection connectionColl = repoConnection.getConnectionCollection();	
		        if (connectionColl == null) {
		        	 return -1;
		        } 
			     
			  // search for the id
			    BasicDBObject query = new BasicDBObject(); 
				query.put("name",connectionName);
				query.put("driver", connectionDriver);
				query.put("url", connectionURL);
				query.put("password", connectionPassword);
				query.put("user", connectionUser);
				
		        try {
		 	       connectionColl.insert(query);  
		 	    }
		         catch (Exception e){
		        	 System.out.println("Error creating concern");
		         }

			} catch (Exception e){
				    System.out.println("error in checking script during create");
			}       
			return 0;
		}
	
	
	public boolean checkExisting(){
		/**
		 * sets value of concernExists;
		 */
		boolean nameExists;
		try {
			/* Get the database collection */
			DBCollection connectionColl = repoConnection.getConnectionCollection();	
	        if (connectionColl == null) {
				return false;
			};
		    // search for the id
		    BasicDBObject query = new BasicDBObject(); 
			query.put("name",connectionName);

			DBCursor checkDoc =connectionColl.find(query).limit(1);

			if (checkDoc.count() == 0)
			 {nameExists = false;}
			 else {nameExists = true;
			       currentConnectionDoc = checkDoc.next();}

			 
		} catch (Exception e){
			    System.out.println("Connection does not exist");
				nameExists = false;
			}
		return nameExists;
	}	

	public int updateExisting() {
	/* Update connection details */
		
		int result = 0;
		DBCollection connectionColl = repoConnection.getConnectionCollection();	

        checkExisting();
 
       try {
			// search for the id
		    BasicDBObject updateQuery = new BasicDBObject(); 	    
			
		    BasicDBObject searchQuery = new BasicDBObject().append("name", connectionName);		
		    DBObject changes = new BasicDBObject().append("driver",connectionDriver)
		    		                              .append("url", connectionURL)
		    		                              .append("user", connectionUser)
		    		                              .append("password", connectionPassword);	  
		    updateQuery.append("$set", changes);	    
		    connectionColl.update(searchQuery, updateQuery);
		    JLWUtilities.scriptInfoMessage("Existing Connection Saved");
 	    }
         catch (Exception e){
        	 System.out.println("Error updating connection");
         }

		return result;
	}	
	
	public dbConnection fetchDetails(String chkConnectionName) {
	/* Update concern details */
		connectionName = chkConnectionName;
		checkExisting();
		connectionName = chkConnectionName;

		try {
		connectionDriver = currentConnectionDoc.get("driver").toString();
		} catch (Exception e){connectionDriver = "";};
		
		try {
		connectionURL = currentConnectionDoc.get("url").toString();
		} catch (Exception e){connectionURL = "";}
		
		try {
			connectionUser = currentConnectionDoc.get("user").toString();
		} catch (Exception e){connectionUser = "";}
		
		try {
			connectionPassword = currentConnectionDoc.get("password").toString();
		} catch (Exception e){connectionPassword = "";}

		return this;

	}
		
	public int delete() {
	/* Delete current connection */
		try {
	       connectionColl = repoConnection.getConnectionCollection();
		   BasicDBObject searchQuery = new BasicDBObject().append("name", this.connectionName);
		   connectionColl.remove(searchQuery);
		   return 0;
		} catch (Exception E){
			System.out.println("Error in delete process");
			return 1;
		}
		
	}	
	
}
