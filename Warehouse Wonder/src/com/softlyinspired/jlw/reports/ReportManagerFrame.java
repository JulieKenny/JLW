package com.softlyinspired.jlw.reports;
import javax.swing.*;

import java.awt.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.org.apache.xerces.internal.dom.ParentNode;

import java.awt.event.*;

public class ReportManagerFrame extends JFrame
 {
  public static void OpenFrame(String[] args, JFrame parent)
  {
	  new ReportManagerFrame(parent);
  }
  
	private static JButton buttonNew;
	private static JButton buttonEdit;	
	int selectedReportId = 0;
	private ReportReference reportList = new ReportReference();
	private DefaultListModel<String> reportListModel = new DefaultListModel<>();

	public ReportManagerFrame(JFrame parent) 
	{
		setBackground(Color.LIGHT_GRAY);
		this.setSize(420,400);
		//this.setLocationRelativeTo(null); /* Put in centre of screen */
		Point parentloc = new Point(parent.getLocation());
		this.setLocation(parentloc.x + 200,parentloc.y + 60);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Report Manager");
		 
		JPanel reportManagerLayout = new JPanel();
		
		final JList<String> reportList = new JList<>(reportListModel);
		final JScrollPane reportListScrollPane = new JScrollPane(reportList); 
		reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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

		buttonNew = new JButton("Create new");
		buttonNew.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			new ReportEditDialog(true,0);	
			showReportList(reportListScrollPane);
			ReportManagerFrame.this.repaint();
			}
		});
		
		buttonEdit = new JButton("Edit");
		buttonEdit.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			int editReportId = 0;
			if (selectedReportId != 0)
			  {
			    editReportId = selectedReportId;
			    new ReportEditDialog(false,editReportId);
			  }
				showReportList(reportListScrollPane);
				ReportManagerFrame.this.repaint();				    
		}});


		SpringLayout reportPaneLayout = new SpringLayout();
		
		// put items on panel 
		reportManagerLayout.add(buttonNew, "1, 7, left, center");
		reportManagerLayout.add(buttonEdit, "1, 9, left, center");		

		reportManagerLayout.add(reportListScrollPane);

		reportPaneLayout.putConstraint(SpringLayout.NORTH,buttonNew,5,SpringLayout.NORTH,reportManagerLayout);
		reportPaneLayout.putConstraint(SpringLayout.WEST,buttonNew,5,SpringLayout.WEST,reportManagerLayout);

		reportPaneLayout.putConstraint(SpringLayout.NORTH,buttonEdit,5,SpringLayout.NORTH,reportManagerLayout);
		reportPaneLayout.putConstraint(SpringLayout.WEST,buttonEdit,150,SpringLayout.WEST,reportManagerLayout);	

		reportPaneLayout.putConstraint(SpringLayout.NORTH,reportListScrollPane,90,SpringLayout.NORTH,reportManagerLayout);
		reportPaneLayout.putConstraint(SpringLayout.WEST,reportListScrollPane,5,SpringLayout.WEST,reportManagerLayout);		
		reportPaneLayout.putConstraint(SpringLayout.SOUTH,reportListScrollPane,300,SpringLayout.NORTH,reportManagerLayout);
		reportPaneLayout.putConstraint(SpringLayout.EAST,reportListScrollPane,400,SpringLayout.WEST,reportManagerLayout);		

		reportManagerLayout.setLayout(reportPaneLayout);
		
		// add panel to frame
		getContentPane().add(reportManagerLayout);
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
