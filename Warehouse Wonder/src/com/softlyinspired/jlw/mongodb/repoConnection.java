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

