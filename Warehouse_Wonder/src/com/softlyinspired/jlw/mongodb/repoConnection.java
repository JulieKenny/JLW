package com.softlyinspired.jlw.mongodb;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.softlyinspired.jlw.JLWProperties;


public class repoConnection {
	private static repoConnection instance = new repoConnection();
    
	MongoClient mongoClient;
	public static DB repoDB;
		
    private DB createConnection() {
        try {
    		mongoClient = new MongoClient(JLWProperties.mongoHost,JLWProperties.mongoPort);
    		repoDB = mongoClient.getDB(JLWProperties.mongoDB);
        } catch (Exception E) {
            System.out.println("Connection Error");
          }
        return repoDB;
    }   
     
    public static DB getConnection() {
        return instance.createConnection();
    }    
    
    
    private DBCollection createCollection(String collectionName) {
    	DBCollection dbc = null;
    	try {
    	   dbc = repoDB.getCollection(collectionName);
    	} catch (Exception e) {
             System.out.println(collectionName + " collection is missing");    	
    	}
    	return dbc;    	
    }
    
    public static DBCollection getConcernCollection() {
    	return instance.createCollection("concerns");	
    }
    
    public static DBCollection getScriptCollection() {
    	return instance.createCollection("customScripts");
    }
    
    public static DBCollection getConnectionCollection() {
    	return instance.createCollection("connections");
    }
    
    public static DBCollection getCustomMenusCollection() {
    	return instance.createCollection("customMenus");
    }

    public static DBCollection getReportsCollection() {
    	return instance.createCollection("reports");
    }
    
}

