package com.softlyinspired.jlw.connections;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JComboBox;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.softlyinspired.jlw.JLWUtilities;
import com.softlyinspired.jlw.mongodb.repoConnection;


public class ConnectionSelector extends JComboBox<String> {
	private  dbConnection currentConnection;
	
	public ConnectionSelector (){
		
		final ArrayList<dbConnection> ac = allConnections();
		int i =0 ;
		for (i=0;i<ac.size();i++){
		   dbConnection cN = new dbConnection();
		   cN = ac.get(i);
		   this.addItem(cN.connectionName);
		}
		
		this.addItemListener(new ItemListener(){
		   public void itemStateChanged(ItemEvent arg0){

			   int i = ConnectionSelector.this.getSelectedIndex();
			   currentConnection = ac.get(i);
		   }
		   
	   });		
		
	}

	public dbConnection setSelection(String connectionName){
		dbConnection c = new dbConnection();
		c = c.fetchDetails(connectionName);
		return c;
	}

	private ArrayList<dbConnection> allConnections (){
		int numberFound =0 ;
		ArrayList<dbConnection> connectionList = new ArrayList<dbConnection>();
		try {

		    DBCollection coll = repoConnection.getConnectionCollection();
		    DBObject doc ;	
			
	        try {	   
	        	   DBCursor allConnections = coll.find();
	        	   numberFound = allConnections.count();

			       for(int i =0; i < (numberFound); i++){
		        	 doc = allConnections.next();
		        	 dbConnection a = new dbConnection();
		        	 a.connectionName = doc.get("name").toString();
		        	 connectionList.add(a);
	        	 }       	
		    }
	        catch (Exception e){
	        	JLWUtilities.scriptErrorMessage("Error finding connection");
	        }
		}
		catch (Exception e) {
			JLWUtilities.scriptErrorMessage("General Error in listing");
		}
		
		return connectionList;
	}


}
