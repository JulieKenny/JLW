import javax.swing.JOptionPane;

import com.mongodb.*;

public class dbConnection {
	
	String connectionName ;
	String connectionDriver ;
	String connectionURL ;
	String connectionUser;
	String connectionPassword;
	
	DBCollection connectionColl ;
	MongoClient mongoClient;
	DBObject currentConnectionDoc;	
	private static String collectionName = new String("connections");
	
	public int create() {
		/* Create a new connection */	
			JOptionPane.showMessageDialog(null, connectionName, "New id",JOptionPane.PLAIN_MESSAGE);

			if (this.checkExisting()) {
				return 1;
			}
			
			try {
				/* Get the database collection */
				if  (!getAppCollection()){
					return -1;
				};
			     
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
		         mongoClient.close();

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
			if  (!getAppCollection()){
				return false;
			};
		  // search for the id
		    BasicDBObject query = new BasicDBObject(); 
			query.put("name",connectionName);

			DBCursor checkDoc =connectionColl.find(query).limit(1);
			currentConnectionDoc = checkDoc.next();

			if (checkDoc.count() == 0)
			 {nameExists = false;}
			 else {nameExists = true;}

			 
		} catch (Exception e){
			    System.out.println("Connection does not exist");
				nameExists = false;
			}
		return nameExists;
	}	

	public int updateExisting() {
	/* Update concern details */
		
		int result = 0;
		getAppCollection();
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
 	    }
         catch (Exception e){
        	 System.out.println("Error updating concern");
         }
         mongoClient.close();

		return result;
	}	
	
	public dbConnection fetchDetails(String chkConnectionName) {
	/* Update concern details */
		boolean checkif ;
		connectionName = chkConnectionName;
		checkif = checkExisting();
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

	private boolean getAppCollection(){
		/* Get the database collection returns true if found*/
		try {
		mongoClient = new MongoClient("localhost",27017);
		
	    DB db = mongoClient.getDB("local");
	    connectionColl = db.getCollection(collectionName);
	    return true;
	    } catch (Exception E) {
	    	return false;
	    }
	}	
	
	public int delete() {
	/* Delete current connection */
		try {
           mongoClient = new MongoClient("localhost",27017);
		
	       DB db = mongoClient.getDB("local");
	       connectionColl = db.getCollection(collectionName);
		   BasicDBObject searchQuery = new BasicDBObject().append("name", this.connectionName);
		   connectionColl.remove(searchQuery);
		   return 0;
		} catch (Exception E){
			System.out.println("Error in delete process");
			return 1;
		}
		
	}	
	
}
