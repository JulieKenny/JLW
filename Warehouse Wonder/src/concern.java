import javax.swing.JOptionPane;
import javax.swing.text.Document;

import org.bson.types.*;

import static java.util.Arrays.asList;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.mongodb.*;


public class concern {

	int concernId ;
	String concernTitle = new String();
	String concernDescription = new String();
	String concernCategory = new String();
	boolean concernExists;
	int[][] scripts; 
	int[][] reports ;
	DBCollection concernColl ;
	MongoClient mongoClient;
	DBObject currentConcernDoc;
	


	public int create(int newId,String newName,String newDescription) {
	/* Create a new concern */	
		JOptionPane.showMessageDialog(null, Integer.toString(newId), "New id",JOptionPane.PLAIN_MESSAGE);

		if (checkExistingId(newId)) {
			return 1;
		}
		
		concernId = newId;		
		try {
			/* Get the database collection */
			if  (!getAppCollection("concerns")){
				return -1;
			};
		     
		  // search for the id
		    BasicDBObject query = new BasicDBObject(); 
			query.put("concernId",newId);
			query.put("title", newName);
			query.put("description", newDescription);
			
	        try {
	 	       concernColl.insert(query);  
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
	
	/**
	 *  Update concern details (title, category and description)
	 */
	public int updateExisting(int concernId, concern newConcern) {
		
		int result = 0;
		getAppCollection("concerns");
		checkExistingId(concernId);
		
       try {
			// search for the id
		    BasicDBObject updateQuery = new BasicDBObject(); 	    
			
		    BasicDBObject searchQuery = new BasicDBObject().append("concernId", concernId);		
		    DBObject changes = new BasicDBObject().append("title",concernTitle)
		    		                              .append("category", concernCategory)
	                                              .append("description",concernDescription);	  
		    updateQuery.append("$set", changes);	    
			concernColl.update(searchQuery, updateQuery);
 	    }
         catch (Exception e){
        	 System.out.println("Error updating concern");
         }
         mongoClient.close();
      
		return result;
	}
	
	/**
	 *  Add the script (passed as scriptId) to the concern into the database
	 */
		
	public int addScript(int scriptId) {
		int result = 0;	
		
		BasicDBObject scriptDetail = new BasicDBObject();
		scriptDetail.append("scriptId", scriptId);
		scriptDetail.append("scriptOrder", 99);
		BasicDBObject updateQuery = new BasicDBObject();

		DBObject listitem = new BasicDBObject ("Scripts", new BasicDBObject("scriptId",scriptId).append("scriptOrder", 99));
		updateQuery.append("$push",listitem);	
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.update(searchQuery, updateQuery);
		
		return result;
	}	
	
	/**
	 * remove the scriptId passed from the current concern from the database
	 */
	public int removeScript(int scriptId){
        int result = 0;	
		
		BasicDBObject scriptDetail = new BasicDBObject();
		scriptDetail.append("scriptId", scriptId);
		scriptDetail.append("scriptOrder", 99);
		BasicDBObject updateQuery = new BasicDBObject();

		DBObject listitem = new BasicDBObject ("Scripts", new BasicDBObject("scriptId",scriptId).append("scriptOrder", 99));
		updateQuery.append("$pull",listitem);	
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.update(searchQuery, updateQuery);
		
		return result;
	}
	
	/** 
	 * delete the concern passed as a concernId 
	 * */
	public int delete(int concernId) {
		int result = 0;
		BasicDBObject searchQuery = new BasicDBObject().append("concernId", this.concernId);
		concernColl.remove(searchQuery);
		return result;
	}
	
	/**
	 *  set description, title and category for current concern - passed as concernId 
	 **/
	public concern fetchConcernDetails(int concernCheckId) {
		checkExistingId(concernCheckId);
		concernId = concernCheckId;

		try {
		concernDescription = currentConcernDoc.get("description").toString();
		} catch (Exception e){concernDescription = "";};
		
		try {
		concernTitle = currentConcernDoc.get("title").toString();
		} catch (Exception e){concernTitle = "";}
		
		try {
		concernCategory = currentConcernDoc.get("category").toString();
		} catch (Exception e){concernCategory = "";}		
		return this;

	}	
	
	/**
	 * Return a list of the scripts associated with the concern 
	 **  each script will have an id and a order 
	 */	
	public int getScripts(int concernId){


		int scriptCount ;
		ArrayList<String[]> scriptList = new ArrayList<String[]>();
		checkExistingId(concernId);
		
		try {
			currentConcernDoc.get("scripts");

			ListIterator<Object> scriptObjects = ((BasicDBList) currentConcernDoc.get("Scripts")).listIterator();
			while(scriptObjects.hasNext()){
				String[] t = new String[2];
	            BasicDBObject nextItem = (BasicDBObject) scriptObjects.next();
	            t[0] = nextItem.getString("scriptId");
	            t[1] = nextItem.getString("scriptOrder");          
	            scriptList.add(t);
	
	        }	
			if (scriptList.isEmpty()) {
				return 0;
			} else {
				scriptCount = scriptList.size();
				scripts = new int[scriptCount][2];
				int i;	
				for (i=0;i<scriptCount;i++)	{
					String[] t = new String[2];
					t = scriptList.get(i);
					scripts[i][0] = Integer.parseInt(t[0]);
					scripts[i][1] = Integer.parseInt(t[1]);
				}	
			}
		} catch (Exception e){
			return 0;
		}
		
		return scriptCount;			
	}
	
	
	/**
	 * Checks if the concernId already exists and returns boolean
	 */	
	public boolean checkExistingId(int CheckId){

		try {
			/* Get the database collection */
			if  (!getAppCollection("concerns")){
				return false;
			};
		     
		  // search for the id
		    BasicDBObject query = new BasicDBObject(); 
			query.put("concernId",CheckId);
			DBCursor checkDoc =concernColl.find(query).limit(1);
			currentConcernDoc = checkDoc.next();
			
			 if (checkDoc.count() == 0)
			 {concernExists = false;}
			 else {concernExists = true;}
			 
		} catch (Exception e){
			    System.out.println("error finding concern");
				concernExists = false;
			}
		return concernExists;
	}	
	
	/**
	 *  Get the database collection for concerns returns true if found
	 **/
	private boolean getAppCollection(String CollectionName){
		try {
		mongoClient = new MongoClient("localhost",27017);
		
	    DB db = mongoClient.getDB("local");
	    concernColl = db.getCollection("concerns");
	    return true;
	    } catch (Exception E) {
	    	return false;
	    }
	    
	}
	
}
