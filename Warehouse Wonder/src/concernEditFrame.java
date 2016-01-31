import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.ListSelectionModel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class concernEditFrame extends JFrame
 {
    /** 
     * Frame to edit a single script called with boolean true if new script 
     */
	private static JButton buttonSave;
	private static JButton buttonCancel;
	private static JButton buttonDelete;	
	private static JButton buttonShowScript;
	private static JButton buttonAddScript;
	private static JButton buttonRemoveScript;
	
	final String frameTitle = new String("Concern Editor");
	
	private static concern currentConcern;
	final JTextField concernId = new JTextField(10);	
	final JTextField concernTitle = new JTextField(20);
	final JTextField concernCategory = new JTextField(20);	
	final JTextArea concernDescription = new JTextArea(10,70);
	final JLabel concernScriptsLabel = new JLabel("Concern Scripts");
	final JLabel reportSetLabel = new JLabel("Available Reports");
	final JLabel concernIdLabel = new JLabel("Enter concern Id");		
	final JLabel concernCategoryLabel = new JLabel("Category");	
	final JLabel concernTitleLabel = new JLabel("Title");	
	final JLabel concernDescriptionLabel = new JLabel("Description");

	private static int concernIdNum;
	
    String selectedDeleteScript = null;		
    String selectedAddScript = null;	
    
	private ArrayList scriptSet = new ArrayList();
	private ArrayList reportSet = new ArrayList();
	

	public concernEditFrame(int passedConcernId, boolean isNewScript)
	{
		/**
		 * Create the frame and concern instance
		 */
 
		currentConcern = new concern();	
		
		int editconcernId = 0;		
		if (isNewScript) {
			editconcernId = 0;
		} else {
			editconcernId = passedConcernId;
			concernIdNum = passedConcernId;
			currentConcern.fetchConcernDetails(passedConcernId);
		};
		
		setBackground(Color.LIGHT_GRAY);
		this.setSize(1000,650);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel cEditorPanel = new JPanel();
		
		String[] columnNames 	= {"Id","Category","Title", "Detail"}; /* ColumnNames used for available and used scripts */
		
		/**
		 * Get current scripts in the concern
		 */
		int concernScriptCount=100;
		Object[][] concernScriptData = new Object[concernScriptCount][4];
		
		final JTable concernScriptTable = new JTable(concernScriptData,columnNames);	
		final JScrollPane concernScriptScrollPane = new JScrollPane(concernScriptTable);	
		ListSelectionModel cellSelectionModelc = concernScriptTable.getSelectionModel();		
	    cellSelectionModelc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);			
		TableColumnModel columnModelc = concernScriptTable.getColumnModel();
		//concernScriptTable.setCellSelectionEnabled(false);
		//concernScriptTable.setColumnSelectionAllowed(false);	
		concernScriptTable.setRowSelectionAllowed(true);
	    cellSelectionModelc.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		
		/** 
		 * Get all available scripts to show possible additions to the concern
		 */
		String[][] scriptList = new String[100][2];	
		validationScript v = new validationScript();
		scriptList = v.listall(); 
		
		int arraySize;
		arraySize = v.scriptCount +1;
		Object[][] availableScriptData = new Object[arraySize][4];	
		
		final JTable scriptTable = new JTable(availableScriptData,columnNames);	
		final JScrollPane scriptScrollPane = new JScrollPane(scriptTable);		
		ListSelectionModel cellSelectionModel = scriptTable.getSelectionModel();		
	    cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		TableColumnModel columnModel = scriptTable.getColumnModel();
		scriptTable.setRowSelectionAllowed(true);
	    
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

	    cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent e) {

	          int[] selectedCRow = scriptTable.getSelectedRows();
              selectedAddScript = scriptTable.getValueAt(selectedCRow[0],0).toString();

	        }

	      });
	    
	    cellSelectionModelc.addListSelectionListener(new ListSelectionListener() {
	        public void valueChanged(ListSelectionEvent e) {

	          int[] selectedARow = concernScriptTable.getSelectedRows();
              selectedDeleteScript = concernScriptTable.getValueAt(selectedARow[0],0).toString();

	        }

	      });	    
		
		columnModel.getColumn(0).setPreferredWidth(3);
		columnModel.getColumn(1).setPreferredWidth(100);
		columnModel.getColumn(2).setPreferredWidth(40);
		columnModel.getColumn(3).setPreferredWidth(40);
		
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
					showError("Please select a concern to delete first");
				}else {
				try {
					concernIdNum= Integer.parseInt(concernId.getText());
					currentConcern.delete(concernIdNum);					
					showError("concern deleted");				
				}catch (Exception e){
					showError("error in delete process");
				}

			}}
		});
		
		/* Add script button */
		buttonAddScript = new JButton("Add selected script");
		buttonAddScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				currentConcern.addScript(Integer.parseInt(selectedAddScript));
			}
		});
		
		/* Remove script button */
		buttonRemoveScript = new JButton("Remove selected script from concern");
		buttonRemoveScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				try{
				if (selectedDeleteScript.isEmpty()) {
					intendedAction ("Please make a selection");
				    JOptionPane.showMessageDialog(null, "Please make a selection", "Concern updating",
				    		                      JOptionPane.PLAIN_MESSAGE);
				}else {
					currentConcern.removeScript(Integer.parseInt(selectedDeleteScript));
				}} catch (Exception e){
				    JOptionPane.showMessageDialog(null, "Failed to remove script", "Concern updating",
		                      JOptionPane.PLAIN_MESSAGE);					
				}
				
			}
		});		
		
		/* Show script button */
		buttonShowScript = new JButton("Show Available Scripts");
		scriptScrollPane.setVisible(false);
		buttonShowScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				  scriptScrollPane.setVisible(!scriptScrollPane.isVisible());	
				  if (scriptScrollPane.isVisible()) {
					   buttonShowScript.setText("Hide Scripts");
					   buttonAddScript.setVisible(true);
				  }else
				  {
					  buttonShowScript.setText("Show Available Scripts");
					  buttonAddScript.setVisible(false);
				  }
			}
		});		
		
		/* Concern details */
		concernDescription.setText("Enter descriptive text here");
		
		/*
		 * If editing an existing concern get the details
		 */
		if (isNewScript)
		{
			concernTitle.setText("Enter title");
			concernTitle.setActionCommand("titleEntry");				
			concernCategory.setText("Enter category");
			concernCategory.setActionCommand("categoryEntry");	
		}
		else {
			concernId.setText(Integer.toString(editconcernId));
			setValueFields(editconcernId, concernScriptData);
			
			int allScripts[][] = new int[100][];
			allScripts = currentConcern.scripts;
			String m = new String();
			}
		/*
		 * Save script details as new or changed
		 */
		buttonSave = new JButton("Save Changes");
		buttonSave.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				int errorCount = 0;
				//validationScript newScript = new validationScript();
				int newconcernId = 0;
				String newConcernText = new String();
				String newconcernCategory = new String();
				String newconcernTitle = new String();
				if (concernId.getText() == null || (concernId.getText().isEmpty()))
						{
					       showError("You must enter an id");
					       errorCount++;  
						}
				else {newconcernId=Integer.parseInt(concernId.getText());}
				
				if (concernDescription.getText() == null || (concernDescription.getText().isEmpty()))
				{showError("You must enter some text");
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
                	int r;

                	if (currentConcern.concernId != 0  ) 
                	{			
                		r = currentConcern.updateExisting(currentConcern.concernId, currentConcern);
                	}else {	
                 	 r = currentConcern.create(newconcernId, newConcernText,newconcernCategory);
                	}
                }	
			}
		});
		
		// Set the panel and add items	
		SpringLayout concernPaneLayout = new SpringLayout();		
		cEditorPanel.setLayout(concernPaneLayout);		
		cEditorPanel.add(buttonSave);
		cEditorPanel.add(buttonCancel);
		cEditorPanel.add(buttonDelete);		
		cEditorPanel.add(buttonAddScript);

		cEditorPanel.add(concernIdLabel);
		cEditorPanel.add(concernId);

		cEditorPanel.add(concernCategoryLabel);
		cEditorPanel.add(concernCategory);
		
		cEditorPanel.add(concernTitleLabel);
		cEditorPanel.add(concernTitle);	
		
		cEditorPanel.add(concernDescriptionLabel);
		cEditorPanel.add(concernDescription);

		cEditorPanel.add(concernScriptsLabel);	
		
		cEditorPanel.add(buttonShowScript);
		buttonAddScript.setVisible(false);
		
		cEditorPanel.add(buttonRemoveScript);
		//buttonRemoveScript.setVisible(false);
		
		scriptTable.setFillsViewportHeight(true);
		cEditorPanel.add(scriptScrollPane);
		cEditorPanel.add(concernScriptScrollPane);
				
		/* Set layout */
		placeLayouts(concernPaneLayout, cEditorPanel,concernScriptScrollPane,scriptScrollPane);
		
		// add panel to frame
		getContentPane().add(cEditorPanel);
		this.setVisible(true);		
	
	};
	
	
	private void setValueFields(int editConcernId,Object[][] concernScriptData)
	{
		/**
		 * return the details of the current concern
		 */
		currentConcern.checkExistingId(editConcernId);

	    int scriptCount = currentConcern.getScripts(concernIdNum);
        int i = 0;
        int s;

        validationScript[] currentSet = new validationScript[scriptCount];
	    if (scriptCount >0){
		    for (i=0;i<scriptCount;i++){
		    	validationScript v = new validationScript();
		    	s= currentConcern.scripts[i][0];
		    	int isFound = 1;
		    	try {
		    		isFound = v.fetchScriptValue(s);
		    		//currentSet[i] = v;
					concernScriptData[i][0] = s;
					concernScriptData[i][1] = v.currentScriptCategory;
					concernScriptData[i][2] = v.currentScriptTitle;
					concernScriptData[i][3] = v.currentScriptText;		

		    	} catch (Exception e){
		    		JOptionPane.showMessageDialog(null, "error finding script ", "Script Listing",
		    				JOptionPane.PLAIN_MESSAGE);		    		
		    	}	
		    	
		    	//JOptionPane.showMessageDialog(null, isFound, "Script Found",JOptionPane.PLAIN_MESSAGE);
		    	
	    		if (isFound == 1){		    	
		    		concernScriptData[i][0] = s;
		    		concernScriptData[i][1] = "Script Not found";
		    		concernScriptData[i][2] = "";
		    		concernScriptData[i][3] = "";		    
		    	}
		    }
	    }
	    
	    concernDescription.setText(currentConcern.concernDescription);
		concernCategory.setText(currentConcern.concernCategory);
		concernTitle.setText(currentConcern.concernTitle);
	}
	
	private void placeLayouts(SpringLayout concernPaneLayout, JPanel cEditorPanel, JScrollPane concernScriptScrollPane
			,JScrollPane scriptScrollPane){
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

		
        concernPaneLayout.putConstraint(SpringLayout.NORTH,concernScriptsLabel,300,SpringLayout.NORTH,cEditorPanel);
        concernPaneLayout.putConstraint(SpringLayout.WEST,concernScriptsLabel,5,SpringLayout.WEST,cEditorPanel);		
        concernPaneLayout.putConstraint(SpringLayout.NORTH,concernScriptScrollPane,300,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.SOUTH,concernScriptScrollPane,120,SpringLayout.NORTH,concernScriptScrollPane);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernScriptScrollPane,150,SpringLayout.WEST,cEditorPanel);	
		concernPaneLayout.putConstraint(SpringLayout.EAST,concernScriptScrollPane,950,SpringLayout.WEST,cEditorPanel);		
		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonShowScript,450,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonShowScript,5,SpringLayout.WEST,cEditorPanel);
        concernPaneLayout.putConstraint(SpringLayout.NORTH,scriptScrollPane,450,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.SOUTH,scriptScrollPane,120,SpringLayout.NORTH,scriptScrollPane);
		concernPaneLayout.putConstraint(SpringLayout.WEST,scriptScrollPane,150,SpringLayout.WEST,cEditorPanel);	
		concernPaneLayout.putConstraint(SpringLayout.EAST,scriptScrollPane,950,SpringLayout.WEST,cEditorPanel);			
		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonAddScript,580,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonAddScript,450,SpringLayout.WEST,cEditorPanel);
		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonRemoveScript,420,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonRemoveScript,450,SpringLayout.WEST,cEditorPanel);	}


	/* Simplify messages */
	private void intendedAction(String actionName){
		JOptionPane.showMessageDialog(null, actionName, "Holding status",JOptionPane.PLAIN_MESSAGE);	
	};

	private void showError(String errorMessage){
		JOptionPane.showMessageDialog(null, errorMessage, "Error",JOptionPane.PLAIN_MESSAGE);	
	};	
}
