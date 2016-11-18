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

package com.softlyinspired.jlw.script;
import javax.swing.*;

import com.softlyinspired.jlw.concerns.concernResultDialog;
import com.softlyinspired.jlw.connections.ConnectionSelector;
import com.softlyinspired.jlw.connections.connectionEditDialog;
import com.softlyinspired.jlw.connections.dbConnection;
import com.softlyinspired.jlw.mongodb.IdManager;
import com.softlyinspired.jlw.JLWUtilities;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ScriptEditDialog extends JDialog
 {
    /** 
     * Frame to edit a single script called with boolean true if new script 
     */

	private static JButton buttonSaveScript;
	private static JButton buttonCancel;
	private static JButton buttonDeleteScript;	
	private static JButton buttonConnections;
	private static JButton buttonRun;
	
	private static validationScript currentScript;
	final JTextField scriptId = new JTextField(10);	
	final JTextField scriptTitle = new JTextField(20);
	final JTextField scriptCategory = new JTextField(20);
	final JTextArea scriptArea = new JTextArea(10,60);	
	//final JTextField scriptConnection = new JTextField(15);
	private static int scriptIdNum;
	private static JComboBox<String> connectList;

	
	public ScriptEditDialog(final boolean isNewScript, int editScriptId)
	{
		/**
		 * Create the frame and script instance
		 */
	    this.setModal(true);
		setBackground(Color.LIGHT_GRAY);
		this.setSize(900,400);
		//this.setLocationRelativeTo(null); /* Put in centre of screen */
        this.setLocation(0,0);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel scriptEditorPanel = new JPanel();
		   
		currentScript = new validationScript();	
		String idLabel = new String() ;
		
		/* 
		 * Show if editing or creating
		 */
		if (isNewScript == true){
		   this.setTitle("New Script Creator");			
		   idLabel = "Enter Unique Id";
		   int n = new IdManager().getNextId("script") + 1;
		   scriptId.setText(Integer.toString(n));
		}else {
		   this.setTitle("Script Editor");
		   idLabel = "Script Id";
		}
		scriptId.setEnabled(false);

		/* Cancel button */
		buttonCancel = new JButton("Close");
		buttonCancel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
			      dispose();
			}
		});
		
		buttonDeleteScript = new JButton("Delete");
		buttonDeleteScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				try {
					scriptIdNum= Integer.parseInt(scriptId.getText());
					currentScript.delete(scriptIdNum);					
					showError("script deleted");				
				}catch (Exception e){
					showError("error in delete process");
				}

			}
		});
			
		
		buttonConnections = new JButton("Connections");
		buttonConnections.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				try {
					new connectionEditDialog();		
				}catch (Exception e){
					System.out.println("Error calling connection screen");
				}
			}
		});
		
		/* Script details */
		JLabel scriptIdLabel = new JLabel(idLabel);
		JLabel scriptCategoryLabel = new JLabel("Category");	
		JLabel scriptTitleLabel = new JLabel("Title");
		JLabel scriptAreaLabel = new JLabel("Enter script long text");
		JLabel scriptConnectionLabel = new JLabel("Connection");

		scriptArea.setText("Enter text here");
		connectList = new ConnectionSelector();  
		
		/*
		 * If editing an existing script get the details
		 */
		if (isNewScript)
		{
			scriptCategory.setText("Enter category");
			scriptCategory.setActionCommand("categoryEntry");				
		}
		else {
			scriptId.setText(Integer.toString(editScriptId));
			setValueFields(editScriptId);
		}
		
		   		
		/*
		 * Save script details as new or changed
		 */
		buttonSaveScript = new JButton("Save script");
		buttonSaveScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				int errorCount = 0;
				//validationScript newScript = new validationScript();
				int newScriptId = 0;
				String newScriptText = new String();
				String newScriptCategory = new String();
				String newScriptTitle = new String();
				String newScriptConnection = new String();
				if (scriptId.getText() == null || (scriptId.getText().isEmpty()))
						{
					       showError("You must enter an id");
					       errorCount++;  
						}
				else {
					newScriptId=Integer.parseInt(scriptId.getText());
				}
				if (scriptArea.getText() == null || (scriptArea.getText().isEmpty()))
				{
			       showError("You must enter some text");
			       errorCount++;
				}
		        else {
		        	newScriptText = scriptArea.getText();
		        }
				
				if (scriptCategory.getText() == null || (scriptCategory.getText().isEmpty()))
				{
					newScriptCategory = "Generic";
				}
				else {
					newScriptCategory = scriptCategory.getText();
				}
				
				if (scriptTitle.getText() == null || (scriptArea.getText().isEmpty()))
				{
					newScriptTitle = "Generic script";
				}
				else {
					newScriptTitle = scriptTitle.getText();
				}
				
			
				newScriptConnection = connectList.getSelectedItem().toString();
				
                if (errorCount == 0 ){
                	if (isNewScript == true)
                	{
                	currentScript.create(newScriptId, newScriptText,newScriptCategory,newScriptTitle,newScriptConnection);
                	}
                	else 
                	currentScript.updateScript(newScriptId,newScriptText,newScriptCategory,newScriptTitle,newScriptConnection);
                	;
                }
				
			}
		});
		
		JButton buttonRun = new JButton("Run");
		buttonRun.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				validationScript v = new validationScript();
				try {
				   if (v.runScript(Integer.parseInt(scriptId.getText())) == 0) {
				      new concernResultDialog(v.getOutputFile(currentScript.currentScriptId));
				   }
				} catch (Exception e) {
					e.printStackTrace();
				}					
			}
		});		
		
		// Set the panel and add items	
		SpringLayout scriptPanelayout = new SpringLayout();		
		scriptEditorPanel.setLayout(scriptPanelayout);		
		scriptEditorPanel.add(buttonSaveScript);
		scriptEditorPanel.add(buttonCancel);
		scriptEditorPanel.add(buttonDeleteScript);		
		scriptEditorPanel.add(buttonConnections);
		scriptEditorPanel.add(buttonRun);		

		scriptEditorPanel.add(scriptIdLabel);
		scriptEditorPanel.add(scriptId);

		scriptEditorPanel.add(scriptCategoryLabel);
		scriptEditorPanel.add(scriptCategory);
		scriptEditorPanel.add(scriptTitleLabel);
		scriptEditorPanel.add(scriptTitle);
		
		scriptEditorPanel.add(scriptAreaLabel);
		scriptEditorPanel.add(scriptArea);
		
		scriptEditorPanel.add(scriptConnectionLabel);
		//scriptEditorPanel.add(scriptConnection);
		scriptEditorPanel.add(connectList);
		
		/* Standardise placing */
		int labelColumn = 5;
		int detailDistance = 150; /*space between label and data */
		int[] row = {5,35,65,95,125,320};  /* Row positions */

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonConnections,row[0],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonConnections,700,SpringLayout.WEST,scriptEditorPanel);	
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonSaveScript,row[5],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonSaveScript,350,SpringLayout.WEST,scriptEditorPanel);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonCancel,row[5],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonCancel,120,SpringLayout.WEST,buttonSaveScript);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonDeleteScript,row[5],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonDeleteScript,120,SpringLayout.WEST,buttonCancel);		

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonRun,row[5],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonRun,200,SpringLayout.WEST,buttonCancel);		
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptIdLabel,row[0],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptIdLabel,labelColumn,SpringLayout.WEST,scriptEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptId,row[0],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptId,detailDistance,SpringLayout.WEST,scriptIdLabel);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptCategoryLabel,row[1],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptCategoryLabel,labelColumn,SpringLayout.WEST,scriptEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptCategory,row[1],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptCategory,detailDistance,SpringLayout.WEST,scriptCategoryLabel);	
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptTitleLabel,row[2],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptTitleLabel,labelColumn,SpringLayout.WEST,scriptEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptTitle,row[2],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptTitle,detailDistance,SpringLayout.WEST,scriptCategoryLabel);	
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptConnectionLabel,row[3],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptConnectionLabel,labelColumn,SpringLayout.WEST,scriptEditorPanel);		
		//scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptConnection,row[3],SpringLayout.NORTH,scriptEditorPanel);
		//scriptPanelayout.putConstraint(SpringLayout.WEST,scriptConnection,detailDistance,SpringLayout.WEST,scriptConnectionLabel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,connectList,row[3],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,connectList,detailDistance,SpringLayout.WEST,scriptConnectionLabel);		
				
				
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptAreaLabel,row[4],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptAreaLabel,labelColumn,SpringLayout.WEST,scriptEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptArea,row[4],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptArea,detailDistance,SpringLayout.WEST,scriptAreaLabel);	
	
		// add panel to frame
		getContentPane().add(scriptEditorPanel);
		this.setVisible(true);		
	
	};
	
	private void setValueFields(int editScript)
	{
		String sConnection = new String();
		dbConnection c = new dbConnection();
		try {
			new validationScript();
		    currentScript.fetchScriptValue(editScript);
		    scriptArea.setText(currentScript.currentScriptText);
			scriptCategory.setText(currentScript.currentScriptCategory);
			scriptTitle.setText(currentScript.currentScriptTitle);
			sConnection = currentScript.currentConnection;
			if (sConnection.length() == 0 || sConnection.isEmpty()) {
				// Do nothing
			} else {
				c = c.fetchDetails(sConnection);					
				connectList.setSelectedItem(c.connectionName);	
			}
		} catch (Exception e){
			System.out.println(e);
		}
		
	}

	private void showError(String errorMessage){
		JOptionPane.showMessageDialog(null, errorMessage, "Error",JOptionPane.PLAIN_MESSAGE);	
	};	
}
