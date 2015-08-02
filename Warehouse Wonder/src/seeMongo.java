import com.mongodb.AggregationOptions;
import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.Cursor;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class seeMongo {

public static void main(String[] args)  throws UnknownHostException {
	int vLength = 0;
	int vCommaCount = 0;
	String vString = "this, is a, long, array with some, commas";
	vLength = vString.length();
	System.out.println(vString);
	char[] vNewArray = vString.toCharArray();
	System.out.println("length " + vLength);
	for (int i = 0; i < vLength; i++) {
        //System.out.println(vNewArray[i]);
		if (vNewArray[i] == ',') {
			vCommaCount++;
      } 
	}
	System.out.println("Commas " + vCommaCount);
	
	 MongoClient mongoClient = new MongoClient();

     // get handle to "mydb"
     DB db = mongoClient.getDB("local");

     DBCollection coll = db.getCollection("music");
     DBObject myDoc = coll.findOne();
     //System.out.println(myDoc);
     
  // lets get all the documents in the collection and print them out
     BasicDBObject query = new BasicDBObject("artist", "Bob Dylan")
          .append("track", new BasicDBObject("$eq","Maggies Farm"));
     DBCursor cursor = coll.find(query);
     try{
         while (cursor.hasNext()) {
             System.out.println(cursor.next());
         }
     } finally {
         cursor.close();
     }
     
  // clean up
     mongoClient.close();
}		
}