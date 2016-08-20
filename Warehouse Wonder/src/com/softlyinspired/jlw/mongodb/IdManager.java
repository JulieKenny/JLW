package com.softlyinspired.jlw.mongodb;

import javax.swing.JOptionPane;

import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class IdManager {


    public int getNextId(String collectionType){
    	String n = new String();    	
    	DBCollection coll ;
    	String collkey = new String();
    	
    	switch (collectionType) {
        case "concern":  coll = repoConnection.getConcernCollection();
                         collkey = "concernId";
                break;
        case "script":  coll = repoConnection.getScriptCollection();
                        collkey = "scriptId";
                 break;
        case "connection" :  coll = repoConnection.getConnectionCollection();
                             collkey = "connectionId";
                 break;
        case "menu" : coll = repoConnection.getCustomMenusCollection();
                             collkey = "scriptId";
                 break;
        case "report" : coll = repoConnection.getReportsCollection();      
                             collkey = "reportId";
                             break;
        default: coll = null;
                 break;
    }

	    DBObject sort = new BasicDBObject();
	    // -1 sorts descending
	    sort.put(collkey, -1);
	    
	    DBCursor obj = coll.find().sort(sort).limit(1);
	    
	    System.out.println(obj.count());
	    if (obj.count() > 0){
	        DBObject doc ;
	        doc = obj.next();
	        n = doc.get(collkey).toString();
	        return Integer.parseInt(n);	    	
	    }else {
        	return 0	;    	
	    }


    }
	

}
