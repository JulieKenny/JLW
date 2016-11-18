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
