package com.softlyinspired.jlw.concerns;
import javax.swing.*;

import com.softlyinspired.jlw.JLWUtilities;
import com.softlyinspired.jlw.reports.ReportReference;
import com.softlyinspired.jlw.reports.ReportRunner;
import com.softlyinspired.jlw.script.validationScript;

import java.awt.*;
import java.awt.event.*;

import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class concernActionFrame extends JFrame
 {
    /** 
     * Frame to edit a single script called with boolean true if new script 
     */
	private static JButton buttonClose;
	
	final String frameTitle = new String("Concern Action");
	
	private static concern currentConcern;	
	final JTextArea concernDescription = new JTextArea(6,85);
	final JLabel reportSetLabel = new JLabel("Available Reports");		
	final JLabel concernTitleLabel = new JLabel("Title");	
	private final String[][] concernScriptData = new String[100][4];
	private final String[][] concernReportData = new String[100][4];	
	private final JButton[] scriptRun = new JButton[100];
	private final JButton[] reportRun = new JButton[100];
	private final JButton[] scriptView = new JButton[100];
	private final JButton[] reportView = new JButton[100];	
	private final JPanel reportPanel = new JPanel();
	private final JPanel scriptPanel = new JPanel();
    private SpringLayout scriptsLayout = new SpringLayout();
    private SpringLayout reportsLayout = new SpringLayout();	
    private String[] columnNames = {"Id","Title"}; 
	private static int concernIdNum;
	
    String selectedDeleteScript = null;		    

	public concernActionFrame(int passedConcernId)
	{
		/**
		 * Create the frame and concern instance
		 */
 
		currentConcern = new concern();	
		
		int editconcernId = 0;		
			editconcernId = passedConcernId;
			concernIdNum = passedConcernId;
			currentConcern.fetchConcernDetails(passedConcernId);
		
		setBackground(Color.LIGHT_GRAY);
		this.setSize(1000,650);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel cEditorPanel = new JPanel();
		JPanel detailPanel = new JPanel();

	    reportPanel.setLayout(reportsLayout);
	    scriptPanel.setLayout(scriptsLayout);			
		Dimension scrollerSize = new Dimension(450, 200);
		Dimension viewSize = new Dimension(410,300); //thing with rows
		Dimension splitPaneSize = new Dimension(900,400);
		JScrollPane sps = new JScrollPane(scriptPanel);		
		JScrollPane rps = new JScrollPane(reportPanel);	

		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,sps, rps);
		splitPane.setResizeWeight(0.5);
        sps.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        scriptPanel.setPreferredSize(viewSize);
        reportPanel.setPreferredSize(viewSize); 
        sps.setPreferredSize(scrollerSize);	
        rps.setPreferredSize(scrollerSize);
        splitPane.setPreferredSize(splitPaneSize);

		Border b = BorderFactory.createLineBorder(Color.black);		
		
		/**
		 * Get current scripts in the concern
		 */
		
		TitledBorder tbScripts;				
		TitledBorder tbReports;				
		tbScripts = BorderFactory.createTitledBorder(b, "Scripts");
		tbReports = BorderFactory.createTitledBorder(b, "Reports");
		sps.setBorder(tbScripts);
		rps.setBorder(tbReports);		
		
		// Set the panel and add items				
		/* 
		 * Show if editing or creating
		 */
	   this.setTitle(frameTitle);			
		/* Cancel button */
		buttonClose = new JButton("Close");
		buttonClose.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
			      dispose();
			}
		});
			
		/* Concern details */
		concernDescription.setText("");
		
		/*
		 * Get the scripts
		 */
		int currentScriptCount = currentConcern.getScripts(concernIdNum);
		final String [][] scriptArray = new String[currentScriptCount][3];
		setValueFields(editconcernId, concernScriptData,scriptArray);
		
		// Set the panel and add items	
		SpringLayout concernPaneLayout = new SpringLayout();
		
		cEditorPanel.setLayout(concernPaneLayout);		
		cEditorPanel.add(buttonClose);	
		cEditorPanel.add(concernTitleLabel);
		cEditorPanel.add(concernDescription);
		cEditorPanel.add(splitPane);		
		detailPanel.setLayout(new GridLayout(0, 3,10,10));
		
		/* Set layout */
		placeLayouts(concernPaneLayout, cEditorPanel, detailPanel,splitPane); //changed for layout

		/* Set concern and report lists */
		setConcernDataList(concernScriptData,0,"Scripts",scriptRun,scriptView);
		setConcernDataList(concernReportData,0,"Reports",reportRun,reportView);		
		cEditorPanel.add(detailPanel);

		// add panel to frame
		getContentPane().add(cEditorPanel);
		this.setVisible(true);			

	};
	
	private void setValueFields(int editConcernId,Object[][] concernScriptData,String[][] scriptArray)
	{
		/**
		 * return the details of the current concern
		 */
		try {
			
		currentConcern.checkExistingId(editConcernId);
	    concernDescription.setText(currentConcern.concernDescription);
		concernTitleLabel.setText("Concern:  " + currentConcern.concernTitle);		

	    int scriptCount = currentConcern.getScripts(concernIdNum);
        int i = 0;
        int s;

	    if (scriptCount >0){
		    for (i=0;i<scriptCount;i++){
		    	validationScript v = new validationScript();
		    	s = currentConcern.scripts[i][0];
				scriptArray[i][0] = Integer.toString(s);

		    	int isFound = 1;
		    	try {
		    		isFound = v.fetchScriptValue(s);	
	    		    scriptArray[i][1] = v.currentScriptTitle;
				
		    	} catch (Exception e){
		    		JOptionPane.showMessageDialog(null, "error finding script ", "Script Listing",JOptionPane.PLAIN_MESSAGE);		    		
		    	}	
		    	
	    		if (isFound == 1){		    		
		    		scriptArray[i][1] = "Script " + scriptArray[i][0] + " (Error)";
		    	  }			    
	    		}
	    }
		}catch (Exception e) {
			JLWUtilities.scriptErrorMessage("Error listing concern functions");
		}
	}
	
	private void placeLayouts(SpringLayout concernPaneLayout, JPanel cEditorPanel, JPanel detailPanel, JSplitPane splitPanel)
	{	
		/*
		 * layout major components
		 */
		Font titleFont = new Font("Serif", Font.BOLD, 16);
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonClose,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonClose,850,SpringLayout.WEST,cEditorPanel);			
		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernTitleLabel,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernTitleLabel,5,SpringLayout.WEST,cEditorPanel);	
		concernTitleLabel.setFont(titleFont);
						
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernDescription,40,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernDescription,15,SpringLayout.WEST,cEditorPanel);	
		concernDescription.setEditable(false);
		concernDescription.setOpaque(true);
		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,splitPanel,150,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,splitPanel,35, SpringLayout.WEST, cEditorPanel);		
			
	}

	private void updateRowList (JPanel currentPanel,SpringLayout currentLayout, int RowId, JComponent[] rowObject)
	{
		/*
		 * Places items in the script and reports boxes. Row 0 are headers.  
		 */
	    int rowplace = 0;	
		int [] col = {2,50,500,580,600};
		
        currentPanel.add(rowObject[0]);
        currentPanel.add(rowObject[1]);		
		if (RowId > 0){
           currentPanel.add(rowObject[2]);
           currentPanel.add(rowObject[3]);
		   rowplace = 35 + ((RowId-1)*30);
		   currentLayout.putConstraint(SpringLayout.NORTH,rowObject[2],rowplace,SpringLayout.NORTH,currentPanel);
		   currentLayout.putConstraint(SpringLayout.WEST,rowObject[2],col[2],SpringLayout.WEST,currentPanel);
		   currentLayout.putConstraint(SpringLayout.NORTH,rowObject[3],rowplace,SpringLayout.NORTH,currentPanel);
		   currentLayout.putConstraint(SpringLayout.WEST,rowObject[3],col[3],SpringLayout.WEST,currentPanel);
		}
		currentLayout.putConstraint(SpringLayout.NORTH,rowObject[0],rowplace,SpringLayout.NORTH,currentPanel);
		currentLayout.putConstraint(SpringLayout.WEST,rowObject[0],col[0],SpringLayout.WEST,currentPanel);
		currentLayout.putConstraint(SpringLayout.NORTH,rowObject[1],rowplace,SpringLayout.NORTH,currentPanel);
		currentLayout.putConstraint(SpringLayout.WEST,rowObject[1],col[1],SpringLayout.WEST,currentPanel);
	}
	
	private int setConcernDataList(String[][] concernDataSet, int stateChange, String setType, 
			JButton[] runSet, JButton[] viewSet)
	{
		int currentCount = 0;
		/**
		 * return the details of the current concern
		 */
		currentConcern.checkExistingId(concernIdNum);
		if (setType == "Reports") {
			currentCount = currentConcern.getReports(concernIdNum);
		} else {
			currentCount = currentConcern.getScripts(concernIdNum);
		}
        int i = 0;
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
		currentRow[0] = new JLabel(columnNames[0]);
		currentRow[1] = new JLabel(columnNames[1]);
		
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
			    		concernDataSet[i][1] = r.getName(); 		    		
			    		} 
			    	else {
			    		validationScript v = new validationScript();
				    	rowId= currentConcern.scripts[i][0];
			    		isFound = v.fetchScriptValue(rowId);
						concernDataSet[i][0] = Integer.toString(rowId);
						concernDataSet[i][1] = v.currentScriptTitle;
						concernDataSet[i][2] = v.currentScriptText;
			    	}		    		
					final String viewDetail = concernDataSet[i][2];
					final int actionId = rowId;
					runSet[i] = new JButton("Run");
					viewSet[i] = new JButton("View");		    		
					JLabel jid = new JLabel(concernDataSet[i][0]);
					JLabel jname = new JLabel(concernDataSet[i][1]);

					if (setType == "Scripts"){
						viewSet[i].addMouseListener(new MouseAdapter(){
						public void mouseClicked(MouseEvent arg0) {
							JOptionPane.showMessageDialog(null, viewDetail, "Preview",JOptionPane.PLAIN_MESSAGE);
						   }
					    });							
				
						runSet[i].addMouseListener(new MouseAdapter(){
							public void mouseClicked(MouseEvent arg0) {
								validationScript v = new validationScript();
								try {
								  if (v.runScript(actionId) == 0 ){
									  new concernResultDialog(v.getOutputFile(actionId));
								  }
								} catch (Exception e) {
									e.printStackTrace();
								}					
							}
						});
					}
					
					if (setType == "Reports"){
						ReportRunner r = new ReportRunner();
//						r.execute(reportLocation.getText());
					}
					
					currentRow[2] = runSet[i];
					currentRow[3] = viewSet[i];
					currentRow[0] = jid;
					currentRow[1] = jname;
						
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
		
	
	
}
