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
	private  int listCount;
	
	public ConnectionSelector (){
		
		final ArrayList<dbConnection> ac = allConnections();
		int i =0 ;
		for (i=0;i<ac.size();i++){
		   dbConnection cN = new dbConnection();
		   cN = ac.get(i);
		   this.addItem(cN.connectionName);
		}
		listCount = i;
		this.addItemListener(new ItemListener(){
		   public void itemStateChanged(ItemEvent arg0){

			   int i = ConnectionSelector.this.getSelectedIndex();
			   currentConnection = ac.get(i);
		   }
		   
	   });		
		

	}
	
    public int itemCount (){
    	return listCount;
    };

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
