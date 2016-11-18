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

package com.softlyinspired.jlw.concerns;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import com.softlyinspired.jlw.JLWUtilities;
import com.softlyinspired.jlw.mongodb.IdManager;
import com.softlyinspired.jlw.reports.ReportReference;
import com.softlyinspired.jlw.reports.ReportSelectorDialog;
import com.softlyinspired.jlw.script.ScriptSelectorDialog;
import com.softlyinspired.jlw.script.validationScript;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class concernEditDialog extends JDialog
 {
    /** 
     * Frame to edit a single script called with boolean true if new script 
     */
	private static JButton buttonSave;
	private static JButton buttonCancel;
	private static JButton buttonDelete;	
	private static JButton buttonAddScript;
	private static JButton buttonAddReport;
	private static JButton buttonRemoveReport;
	private static JButton buttonRemoveScript;

	String frameTitle = new String("Concern Editor Dialog");
	
	private static concern currentConcern;
	private final JTextField concernId = new JTextField(10);	
	private final JTextField concernTitle = new JTextField(20);
	private final JTextField concernCategory = new JTextField(20);	
	private final JTextArea concernDescription = new JTextArea(10,70);
	private final JLabel concernIdLabel = new JLabel("Concern Id");		
	private final JLabel concernCategoryLabel = new JLabel("Category");	
	private final JLabel concernTitleLabel = new JLabel("Title");	
	private final JLabel concernDescriptionLabel = new JLabel("Description");
	private final String[][] concernScriptData = new String[100][4];
	private final String[][] concernReportData = new String[100][4];
	private final JCheckBox[] scriptDel = new JCheckBox[100];
	private final JCheckBox[] reportDel = new JCheckBox[100];
	private final JPanel reportPanel = new JPanel();
	private final JPanel scriptPanel = new JPanel();
    private SpringLayout scriptsLayout = new SpringLayout();
    private SpringLayout reportsLayout = new SpringLayout();
	private static int concernIdNum;
	private int currentReportCount;
	private int currentScriptCount;
	private String[] columnNames = {"Id","Category","Title"}; 

	
    String selectedDeleteScript = null;		
    String selectedAddScript = null;	
    
	public concernEditDialog(int passedConcernId, final boolean isNewScript)
	{
		/**
		 * Create the frame and concern instance
		 */
 
	    this.setModal(true);
		currentConcern = new concern();	
				
		if (isNewScript) {
			concernIdNum = new IdManager().getNextId("concern") + 1;			
		} else {
			concernIdNum = passedConcernId;
			currentConcern.fetchConcernDetails(passedConcernId);
		};
		// Set the panel and add items	
	    SpringLayout concernPaneLayout = new SpringLayout();	


		setBackground(Color.LIGHT_GRAY);
		this.setSize(1100,650);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel cEditorPanel = new JPanel();
		
	    reportPanel.setLayout(reportsLayout);
	    scriptPanel.setLayout(scriptsLayout);			
		Dimension scrollerSize = new Dimension(450, 200);
		Dimension viewSize = new Dimension(410,300);
		Dimension splitPaneSize = new Dimension(900,200);
		JScrollPane sps = new JScrollPane(scriptPanel);		
		JScrollPane rps = new JScrollPane(reportPanel);	

		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sps, rps);
		splitPane.setResizeWeight(0.5);
        sps.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        scriptPanel.setPreferredSize(viewSize);
        reportPanel.setPreferredSize(viewSize); 
        sps.setPreferredSize(scrollerSize);	
        rps.setPreferredSize(scrollerSize);
        splitPane.setPreferredSize(splitPaneSize);

		Border b = BorderFactory.createLineBorder(Color.black);
		
		/** 
		 * Get all available scripts to show possible additions to the concern
		 */
		String[][] scriptList = new String[100][2];	
		validationScript v = new validationScript();
		scriptList = v.listall(); 
		
		int arraySize;
		arraySize = v.scriptCount +1;
		Object[][] availableScriptData = new Object[arraySize][4];	
	
		TitledBorder tbScripts;				
		TitledBorder tbReports;				
		tbScripts = BorderFactory.createTitledBorder(b, "Scripts");
		tbReports = BorderFactory.createTitledBorder(b, "Reports");
		sps.setBorder(tbScripts);
		rps.setBorder(tbReports);
		

	    /*
	     * This fills out the available scripts table
	     */
		String tId = new String();
		for(int i =0; i < (arraySize); i++)
		  {
			 if (scriptList[i][0] != null )
			 {
			   tId = scriptList[i][0];			
			   if (v.fetchScriptValue(Integer.parseInt(tId)) == 0 ){
				   availableScriptData[i][0] = tId;
				   availableScriptData[i][1] = v.currentScriptCategory;
				   availableScriptData[i][2] = scriptList[i][1];
				   availableScriptData[i][3] = v.currentScriptText;					   
			   };
			 }
		  }

		// Set the panel and add items				
		/* 
		 * Show if editing or creating
		 */
	   this.setTitle(frameTitle);			
	   
		/* Cancel button */
		buttonCancel = new JButton("Close");
		buttonCancel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
			      dispose();
			}
		});
		
		buttonDelete = new JButton("Delete");
		buttonDelete.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				if (concernId.getText().isEmpty()){
					JLWUtilities.scriptErrorMessage("Please select a concern to delete first");
				}else {
				try {
					if (JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this concern",
							        "Delete Confirmation", 
							        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
							concernIdNum= Integer.parseInt(concernId.getText());
							currentConcern.delete(concernIdNum);			
							JOptionPane.showMessageDialog(concernEditDialog.this,"Concern has been deleted" ,
									 "Confirmation", 1);
							
					} else {
						JOptionPane.showMessageDialog(concernEditDialog.this, "Confirmation","Delete Aborted", 1);
					}
					
				}catch (Exception e){
					JLWUtilities.scriptErrorMessage("error in delete process");
				}

			}}
		});
		
		/* Add script button */
		buttonAddScript = new JButton("Add script");
		buttonAddScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				int scriptToAdd;
				ScriptSelectorDialog ssd  = new ScriptSelectorDialog(concernEditDialog.this);
				scriptToAdd = ssd.selectedScriptId;
				if (scriptToAdd != 0 ) {
					currentConcern.addScript(scriptToAdd);
				}				
			setConcernDataList(concernScriptData,2,"Scripts",scriptDel);					
			scriptPanel.revalidate();
			scriptPanel.repaint();
			}
		});
		
		buttonRemoveScript = new JButton("Disconnect Script");
		buttonRemoveScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				JLWUtilities.scriptInfoMessage("Deleting a script");
				int i=0;
				for (i=0;i<currentScriptCount;i++){
					if (scriptDel[i].isSelected()){
						currentConcern.removeScript(Integer.parseInt(concernScriptData[i][0]));
					};
				}
				setConcernDataList(concernScriptData,2,"Scripts",scriptDel);
				scriptPanel.revalidate();
                scriptPanel.repaint();			
			}
		});


		buttonAddReport = new JButton("Add Report");
		buttonAddReport.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				int reportToAdd;
				ReportSelectorDialog rsd  = new ReportSelectorDialog(concernEditDialog.this);	
				reportToAdd = rsd.selectedReportId;
				if (reportToAdd != 0) {
					currentConcern.addReport(reportToAdd);
					setConcernDataList(concernReportData,2,"Reports",reportDel);
				}
				reportPanel.revalidate();
				reportPanel.repaint();
			}
		});
			
		buttonRemoveReport = new JButton("Disconnect Report");
		buttonRemoveReport.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){					
				int i=0;
				for (i=0;i<currentReportCount;i++){
					if (reportDel[i].isSelected()){
						currentConcern.removeReport(Integer.parseInt(concernReportData[i][0]));
					};
				}
				setConcernDataList(concernReportData,2,"Reports",reportDel);
				reportPanel.revalidate();
				reportPanel.repaint();				
			}
		});		
		
		
		/*
		 * Save script details as new or changed
		 */
		buttonSave = new JButton("Save Changes");
		buttonSave.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				int errorCount = 0;
				int newconcernId = 0;
				String newConcernText = new String();
				String newconcernCategory = new String();
				String newconcernTitle = new String();
				if (concernId.getText() == null || (concernId.getText().isEmpty()))
						{
					JLWUtilities.scriptErrorMessage("Error setting id");
					       errorCount++;  
						}
				else {newconcernId=Integer.parseInt(concernId.getText());}
				
				if (concernDescription.getText() == null || (concernDescription.getText().isEmpty()))
				{JLWUtilities.scriptErrorMessage("You must enter some text");
			       errorCount++;}
		        else {newConcernText = concernDescription.getText();
		              currentConcern.concernDescription = newConcernText;
		              }
				
				if (concernCategory.getText() == null || (concernCategory.getText().isEmpty()))
				{newconcernCategory = "Generic";}
				else {newconcernCategory = concernCategory.getText();
				      currentConcern.concernCategory = newconcernCategory;}
				
				if (concernTitle.getText() == null || (concernTitle.getText().isEmpty()))
				     {newconcernTitle = "Generic";}
				else {newconcernTitle = concernTitle.getText();
				      currentConcern.concernTitle = concernTitle.getText();}
				
                if (errorCount == 0 ){
                   if (!isNewScript)                		
                	{	currentConcern.updateExisting(currentConcern.concernId, currentConcern);
                	}else {	
                 	 currentConcern.create(newconcernId, newConcernText,newconcernCategory);
                	}
                }	
			}
		});		
		
		/* Concern details */
		concernDescription.setText("Enter descriptive text here");
		
		/*
		 * If editing an existing concern get the details
		 */
		concernId.setText(Integer.toString(concernIdNum));
		if (isNewScript)
		{
			concernTitle.setText("Enter title");
			concernTitle.setActionCommand("titleEntry");				
			concernCategory.setText("Enter category");
			concernCategory.setActionCommand("categoryEntry");	
		}
		else {
			setDetailFields();
			}
		/* Set concern and report lists */
		setConcernDataList(concernScriptData,0,"Scripts",scriptDel);
		setConcernDataList(concernReportData,0,"Reports",reportDel);	
		
		cEditorPanel.setLayout(concernPaneLayout);		
		cEditorPanel.add(buttonSave);
		cEditorPanel.add(buttonCancel);
		cEditorPanel.add(buttonDelete);		
		cEditorPanel.add(buttonAddScript);
		cEditorPanel.add(buttonAddReport);
		cEditorPanel.add(buttonRemoveReport);
		cEditorPanel.add(buttonRemoveScript);

		cEditorPanel.add(concernIdLabel);
		cEditorPanel.add(concernId);
		concernId.setEnabled(false);

		cEditorPanel.add(concernCategoryLabel);
		cEditorPanel.add(concernCategory);
		
		cEditorPanel.add(concernTitleLabel);
		cEditorPanel.add(concernTitle);	
		
		cEditorPanel.add(concernDescriptionLabel);
		cEditorPanel.add(concernDescription);
		
		cEditorPanel.add(splitPane);
				
		/* Set layout */
		placeLayouts(concernPaneLayout, cEditorPanel,splitPane);
		
		// add panel to frame
		getContentPane().add(cEditorPanel);
		this.setVisible(true);		
	
	};
	
	private void updateRowList (JPanel currentPanel,SpringLayout currentLayout, int RowId, JComponent[] rowObject)
	{
		/*
		 * Places items in the script and reports boxes. Row 0 are headers.  
		 */
	    int rowplace = 0;	
		int [] col = {2,25,70,170,250};
		
        currentPanel.add(rowObject[1]);
        currentPanel.add(rowObject[2]);
        currentPanel.add(rowObject[3]);		
		if (RowId > 0){
           currentPanel.add(rowObject[0]);
		   rowplace = 35 + ((RowId-1)*30);
		   currentLayout.putConstraint(SpringLayout.NORTH,rowObject[0],rowplace,SpringLayout.NORTH,currentPanel);
		   currentLayout.putConstraint(SpringLayout.WEST,rowObject[0],col[0],SpringLayout.WEST,currentPanel);
		}
		currentLayout.putConstraint(SpringLayout.NORTH,rowObject[1],rowplace,SpringLayout.NORTH,currentPanel);
		currentLayout.putConstraint(SpringLayout.WEST,rowObject[1],col[1],SpringLayout.WEST,currentPanel);
		currentLayout.putConstraint(SpringLayout.NORTH,rowObject[2],rowplace,SpringLayout.NORTH,currentPanel);
		currentLayout.putConstraint(SpringLayout.WEST,rowObject[2],col[2],SpringLayout.WEST,currentPanel);
		currentLayout.putConstraint(SpringLayout.NORTH,rowObject[3],rowplace,SpringLayout.NORTH,currentPanel);
		currentLayout.putConstraint(SpringLayout.WEST,rowObject[3],col[3],SpringLayout.WEST,currentPanel);		
	}
	
	private int setConcernDataList(String[][] concernDataSet, int stateChange, String setType, JCheckBox[] delSet)
	{
		int currentCount = 0;
		/**
		 * return the details of the current concern
		 */
		currentConcern.checkExistingId(concernIdNum);
		if (setType == "Reports") {
			currentCount = currentConcern.getReports(concernIdNum);
			currentReportCount = currentCount;
		} else {
			currentCount = currentConcern.getScripts(concernIdNum);
			currentScriptCount = currentCount;
		}
        int i = 0;
        //int s;
        int rowId = 0;
        if (stateChange == 2){
        	for (i=0;i<currentCount+1;i++){
        		concernDataSet[i][0] = "";
        		concernDataSet[i][1] = "";
        		concernDataSet[i][2] = "";
        		concernDataSet[i][3] = "";	  
        		if (setType == "Reports"){
        			reportPanel.removeAll();
        		} else {
        			scriptPanel.removeAll();
        		}	
        	}
        }
        
		JComponent [] currentRow = new JComponent[4];		        
		currentRow[1] = new JLabel(columnNames[0]);
		currentRow[2] = new JLabel(columnNames[1]);
		currentRow[3] = new JLabel(columnNames[2]);				
		
		if (setType == "Reports"){
			updateRowList(reportPanel,reportsLayout,0,currentRow);
		} else {
			updateRowList(scriptPanel,scriptsLayout,0,currentRow);
		}	
		
	    if (currentCount >0){
		    for (i=0;i<currentCount;i++){
		    	int isFound = 1;

		    	try {
		    		if (setType == "Reports"){
			    		ReportReference r = new ReportReference();
			    		rowId = currentConcern.reports[i][0];			    			
			    		isFound = r.fetchDetails(rowId);
			    		concernDataSet[i][0] = Integer.toString(rowId);
			    		concernDataSet[i][1] = r.getCategory();
			    		concernDataSet[i][2] = r.getName(); 		    		
			    		concernDataSet[i][3] = r.getDescription();
			    		} 
			    	else {
			    		validationScript v = new validationScript();
				    	rowId= currentConcern.scripts[i][0];
			    		isFound = v.fetchScriptValue(rowId);
						concernDataSet[i][0] = Integer.toString(rowId);
						concernDataSet[i][1] = v.currentScriptCategory;
						concernDataSet[i][2] = v.currentScriptTitle;
						concernDataSet[i][3] = v.currentScriptText;			    		
			    	}		    		
		    		
					JLabel jid = new JLabel(concernDataSet[i][0]);
					JLabel jcategory = new JLabel(concernDataSet[i][1]);
					JLabel jname = new JLabel(concernDataSet[i][2]);
					delSet[i] = new JCheckBox();
					
					currentRow[0] = delSet[i];
					currentRow[1] = jid;
					currentRow[2] = jcategory;
					currentRow[3] = jname;
						
					if (setType == "Reports"){
						updateRowList(reportPanel,reportsLayout,i+1,currentRow);
					} else {
						updateRowList(scriptPanel,scriptsLayout,i+1,currentRow);
					}					
		    		
		    	} catch (Exception e){
		    		JLWUtilities.scriptErrorMessage("error finding reports in concern");	    		
		    	}	
		    	
	    		if (isFound == 1){		    	
	    			concernDataSet[i][0] = Integer.toString(rowId);
	    			concernDataSet[i][1] = "";
	    			concernDataSet[i][2] = "";
	    			concernDataSet[i][3] = "";		    
		    	}
		    }
	    }

        return currentCount;
	}	
		
	private void setDetailFields(){
		currentConcern.checkExistingId(concernIdNum);
	    concernDescription.setText(currentConcern.concernDescription);
		concernCategory.setText(currentConcern.concernCategory);
		concernTitle.setText(currentConcern.concernTitle);	
	}
	

	private void placeLayouts(SpringLayout concernPaneLayout, JPanel cEditorPanel, JSplitPane splitPanel){
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonSave,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonSave,700,SpringLayout.WEST,cEditorPanel);	
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonCancel,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonCancel,110,SpringLayout.WEST,buttonSave);			
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonDelete,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonDelete,80,SpringLayout.WEST,buttonCancel);		
		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernIdLabel,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernIdLabel,5,SpringLayout.WEST,cEditorPanel);		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernId,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernId,150,SpringLayout.WEST,concernIdLabel);	

		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernTitleLabel,35,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernTitleLabel,5,SpringLayout.WEST,cEditorPanel);		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernTitle,35,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernTitle,150,SpringLayout.WEST,concernTitleLabel);	
				
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernCategoryLabel,60,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernCategoryLabel,5,SpringLayout.WEST,cEditorPanel);		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernCategory,60,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernCategory,150,SpringLayout.WEST,concernCategoryLabel);	
				
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernDescriptionLabel,100,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernDescriptionLabel,5,SpringLayout.WEST,cEditorPanel);		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernDescription,100,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernDescription,150,SpringLayout.WEST,concernDescriptionLabel);	

		concernPaneLayout.putConstraint(SpringLayout.NORTH,splitPanel,270,SpringLayout.NORTH,cEditorPanel);
	    concernPaneLayout.putConstraint(SpringLayout.WEST,splitPanel,60,SpringLayout.WEST,cEditorPanel);	
				  
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonAddScript,500,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonAddScript,150,SpringLayout.WEST,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonRemoveScript,500,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonRemoveScript,250,SpringLayout.WEST,cEditorPanel);

		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonAddReport,500,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonAddReport,700,SpringLayout.WEST,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonRemoveReport,500,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonRemoveReport,800,SpringLayout.WEST,cEditorPanel);
 
	}

	
}
