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
package com.softlyinspired.jlw.reports;
import javax.swing.*;

import java.io.*;

import com.softlyinspired.jlw.connections.ConnectionSelector;
import com.softlyinspired.jlw.connections.connectionEditDialog;
import com.softlyinspired.jlw.connections.dbConnection;
import com.softlyinspired.jlw.mongodb.IdManager;
import com.softlyinspired.jlw.JLWUtilities;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ReportEditDialog extends JDialog
 {
    /** 
     * Frame to edit a single script called with boolean true if new script 
     */

	private static JButton buttonSave;
	private static JButton buttonCancel;
	private static JButton buttonDelete;	
	private static JButton buttonRun;
	private static JButton buttonBrowse;
	
	private static ReportReference currentReport;
	final JTextField reportId = new JTextField(10);	
	final JTextField reportName = new JTextField(20);
	final JTextField reportCategory = new JTextField(20);
	final JTextField reportLocation = new JTextField(60);		
	final JTextArea reportDescription = new JTextArea(10,60);
	
	public ReportEditDialog(final boolean isNew, int editreportId)
	{
		
		currentReport = new ReportReference();	
		
		/**
		 * Create the frame and script instance
		 */
	    this.setModal(true);
		setBackground(Color.LIGHT_GRAY);
		this.setSize(900,400);
		//this.setLocationRelativeTo(null); /* Put in centre of screen */
        this.setLocation(0,0);

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel reportEditorPanel = new JPanel();
		   
		
		/* 
		 * Show if editing or creating
		 */
		if (isNew == true){
		   this.setTitle("New Report Creator");			
		   int n = new IdManager().getNextId("report") + 1;
		   reportId.setText(Integer.toString(n));
		}else {
		   this.setTitle("Report Editor");
		}
		
		
		/* Report details */
		JLabel reportIdLabel = new JLabel("Report Id");
		JLabel reportCategoryLabel = new JLabel("Category");	
		JLabel reportNameLabel = new JLabel("Name");
		JLabel reportLocationLabel = new JLabel("Location"); 
		JLabel reportDescriptionLabel = new JLabel("Description");		
		
		//Create a file chooser
		final JFileChooser fc = new JFileChooser(System.getProperty("user.home") + System.getProperty("file.separator")+ "JLWFiles");
		reportId.setEnabled(false);

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
				try {
					Object[] options = {"Yes","Cancel"};
					int n = JOptionPane.showOptionDialog(ReportEditDialog.this,
							 "Do you really want to delete?", 
							 "Delete confirmation",
							 JOptionPane.YES_NO_OPTION,
							 JOptionPane.QUESTION_MESSAGE,
							 null,
							 options,
							 options[1]
							 );
					if (n == 0){
					   currentReport.delete();
					}
					JLWUtilities.scriptErrorMessage("definition deleted");				
				}catch (Exception e){
					JLWUtilities.scriptErrorMessage("error in delete process");
				}
			}
		});
				
		buttonRun = new JButton("Run");
		buttonRun.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				try {
					ReportRunner r = new ReportRunner();
					r.execute(reportLocation.getText());
				}catch (Exception e){
					System.out.println(e);
				}
			}
		});

		buttonBrowse= new JButton("Browse");
		buttonBrowse.addMouseListener(new MouseAdapter(){
		  public void mouseClicked(MouseEvent e){
		    //Handle open button action.
		    if (e.getSource() == buttonBrowse) {
		        int returnVal = fc.showOpenDialog(ReportEditDialog.this);

		        if (returnVal == JFileChooser.APPROVE_OPTION) {		        
		            File file = fc.getSelectedFile();
		            reportLocation.setText(file.getPath());
		        } else {
		            // DO NOTHING - they cancelled
		        }
		   }
		}
		});
					
		/*
		 * If editing an existing script get the details
		 */
		if (isNew)
		{
			reportCategory.setText("Enter category");
			reportCategory.setActionCommand("categoryEntry");				
		}
		else {
			reportId.setText(Integer.toString(editreportId));
			setValueFields(editreportId);
		}
		
		   		
		/*
		 * Save report details as new or changed
		 */
		buttonSave = new JButton("Save");
		buttonSave.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				int errorCount = 0;

				if (reportId.getText() == null || (reportId.getText().isEmpty()))
						{
					       JLWUtilities.scriptErrorMessage("Invalid id");
					       errorCount++;  
						}
				else {
					currentReport.reportId =Integer.parseInt(reportId.getText());
				}
				if (reportLocation.getText() == null || (reportLocation.getText().isEmpty()))
				{
			       JLWUtilities.scriptErrorMessage("You must provide a report location");
			       errorCount++;
				}
		        else {
		        	currentReport.setLocation(reportLocation.getText());
		        }
				
				if (reportCategory.getText() == null || (reportCategory.getText().isEmpty()))
				{
					currentReport.setCategory("Generic Report");
				}
				else {
					currentReport.setCategory(reportCategory.getText());
				}
				
				if (reportDescription.getText() == null || (reportDescription.getText().isEmpty()))
				{
					currentReport.setDescription("Report");
				}
				else {
					currentReport.setDescription(reportDescription.getText());
				}
				
				currentReport.setName(reportName.getText());

                if (errorCount == 0 ){
                	if (isNew == true)
                	{
                	currentReport.create();
                	}
                	else 
                	currentReport.updateExisting();
                	;
                }
				
			}
		});
		
		// Set the panel and add items	
		SpringLayout scriptPanelayout = new SpringLayout();		
		reportEditorPanel.setLayout(scriptPanelayout);		
		reportEditorPanel.add(buttonSave);
		reportEditorPanel.add(buttonCancel);
		reportEditorPanel.add(buttonDelete);		
		reportEditorPanel.add(buttonRun);
		reportEditorPanel.add(buttonBrowse);
		
		reportEditorPanel.add(reportIdLabel);
		reportEditorPanel.add(reportId);

		reportEditorPanel.add(reportCategoryLabel);
		reportEditorPanel.add(reportCategory);
		reportEditorPanel.add(reportNameLabel);
		reportEditorPanel.add(reportName);
		
		reportEditorPanel.add(reportLocationLabel);
		reportEditorPanel.add(reportLocation);
		
		reportEditorPanel.add(reportDescriptionLabel);
		reportEditorPanel.add(reportDescription);
		
		/* Standardise placing */
		int labelColumn = 5;
		int detailDistance = 150; /*space between label and data */
		int[] row = {5,35,65,95,125,320};  /* Row positions */

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonSave,row[5],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonSave,350,SpringLayout.WEST,reportEditorPanel);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonCancel,row[5],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonCancel,120,SpringLayout.WEST,buttonSave);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonDelete,row[5],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonDelete,120,SpringLayout.WEST,buttonCancel);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonRun,row[5],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonRun,120,SpringLayout.WEST,buttonDelete);	
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonBrowse,row[2],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonBrowse,120,SpringLayout.WEST,buttonDelete);	

		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportIdLabel,row[0],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportIdLabel,labelColumn,SpringLayout.WEST,reportEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportId,row[0],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportId,detailDistance,SpringLayout.WEST,reportIdLabel);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportCategoryLabel,row[1],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportCategoryLabel,labelColumn,SpringLayout.WEST,reportEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportCategory,row[1],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportCategory,detailDistance,SpringLayout.WEST,reportCategoryLabel);	
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportNameLabel,row[2],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportNameLabel,labelColumn,SpringLayout.WEST,reportEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportName,row[2],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportName,detailDistance,SpringLayout.WEST,reportCategoryLabel);	
						
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportLocationLabel,row[3],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportLocationLabel,labelColumn,SpringLayout.WEST,reportEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportLocation,row[3],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportLocation,detailDistance,SpringLayout.WEST,reportLocationLabel);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportDescriptionLabel,row[4],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportDescriptionLabel,labelColumn,SpringLayout.WEST,reportEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,reportDescription,row[4],SpringLayout.NORTH,reportEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,reportDescription,detailDistance,SpringLayout.WEST,reportDescriptionLabel);	
		
		// add panel to frame
		getContentPane().add(reportEditorPanel);
		this.setVisible(true);		
	
	};
	
	private void setValueFields(int editReportId)
	{
		try {
			currentReport.reportId = editReportId;
		    currentReport.fetchDetails(editReportId);
			reportName.setText(currentReport.getName());
		    reportLocation.setText(currentReport.getLocation());
			reportCategory.setText(currentReport.getCategory());
			reportDescription.setText(currentReport.getDescription());
	
		} catch (Exception e){
			System.out.println(e);
		}
		
	}

	
}
