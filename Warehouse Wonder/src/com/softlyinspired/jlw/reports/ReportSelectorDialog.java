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

import java.awt.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.org.apache.xerces.internal.dom.ParentNode;

import java.awt.event.*;

public class ReportSelectorDialog extends JDialog
 {

	private static JButton buttonSelect;	
	private static JButton buttonCancel;
	public int selectedReportId = 0;
	private ReportReference reportList = new ReportReference();
	private DefaultListModel<String> reportListModel = new DefaultListModel<>();

	public ReportSelectorDialog(JDialog parent)
	{
	    this.setModal(true);
		setBackground(Color.LIGHT_GRAY);
		this.setSize(420,400);
		Point parentloc = new Point(parent.getLocation());
		this.setLocation(parentloc.x + 200,parentloc.y + 60);
		
		this.setTitle("Report Selector");
		 
		JPanel reportSelectorLayout = new JPanel();
		
		final JList<String> reportList = new JList<>(reportListModel);
		final JScrollPane reportListScrollPane = new JScrollPane(reportList); 
		reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		buttonSelect = new JButton("Select");
		buttonSelect.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				dispose();
			}
		});
		
		/* Cancel button */
		buttonCancel = new JButton("Cancel");
		buttonCancel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				  selectedReportId = 0;
			      dispose();
			}
		});		
		reportList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent ie){
				try {
				reportList.getSelectedIndex();
				String selectedReportName = new String();
				selectedReportName = reportList.getSelectedValue().toString();
				selectedReportId = Integer.parseInt(selectedReportName.substring(0,selectedReportName.indexOf(":")-1));
				} catch (Exception e){
					selectedReportId = 0;
				}
			}
		} );

		showReportList(reportListScrollPane);
	
		SpringLayout reportPaneLayout = new SpringLayout();
		
		// put items on panel 		
		reportSelectorLayout.add(buttonSelect);		
        reportSelectorLayout.add(buttonCancel);
		 
		reportSelectorLayout.add(reportListScrollPane);

		reportPaneLayout.putConstraint(SpringLayout.NORTH,buttonSelect,5,SpringLayout.NORTH,reportSelectorLayout);
		reportPaneLayout.putConstraint(SpringLayout.WEST,buttonSelect,60,SpringLayout.WEST,reportSelectorLayout);	
		reportPaneLayout.putConstraint(SpringLayout.NORTH,buttonCancel,5,SpringLayout.NORTH,reportSelectorLayout);
		reportPaneLayout.putConstraint(SpringLayout.WEST,buttonCancel,150,SpringLayout.WEST,reportSelectorLayout);	

		reportPaneLayout.putConstraint(SpringLayout.NORTH,reportListScrollPane,90,SpringLayout.NORTH,reportSelectorLayout);
		reportPaneLayout.putConstraint(SpringLayout.WEST,reportListScrollPane,5,SpringLayout.WEST,reportSelectorLayout);		
		reportPaneLayout.putConstraint(SpringLayout.SOUTH,reportListScrollPane,300,SpringLayout.NORTH,reportSelectorLayout);
		reportPaneLayout.putConstraint(SpringLayout.EAST,reportListScrollPane,400,SpringLayout.WEST,reportSelectorLayout);		

		reportSelectorLayout.setLayout(reportPaneLayout);
		
		// add panel to frame
		getContentPane().add(reportSelectorLayout);
		this.setVisible(true);
		
	}

	//private void showreportList(DefaultListModel<String> reportListModel,JScrollPane reportListScrollPane) {
	private void showReportList(JScrollPane reportListScrollPane) {
		String[][] allReports = new String[100][2];	
		allReports = reportList.listall(); 
		reportListModel.clear();
		for(int i =0; i <= reportList.reportCount ; i++)
		  {
			 if (allReports[i][0] != null )
			 {	 
			   reportListModel.addElement(allReports[i][0] + " : " + allReports[i][1]);
			 }
		  }		
	}
	


	
}
