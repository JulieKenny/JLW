import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.ListSelectionModel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class concernActionFrame extends JFrame
 {
    /** 
     * Frame to edit a single script called with boolean true if new script 
     */
	private static JButton buttonClose;
	
	final String frameTitle = new String("Concern Action");
	
	private static concern currentConcern;	
	final JTextArea concernDescription = new JTextArea(6,85);
	final JLabel concernScriptsLabel = new JLabel("Scripts");
	final JLabel reportSetLabel = new JLabel("Available Reports");		
	final JLabel concernTitleLabel = new JLabel("Title");	

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
		
		/**
		 * Get current scripts in the concern
		 */
		int concernScriptCount=100;
		Object[][] concernScriptData = new Object[concernScriptCount][4];
		
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
		cEditorPanel.add(concernScriptsLabel);	
				
		/* Set layout */
		placeLayouts(concernPaneLayout, cEditorPanel);
		
		int scriptRow = 200;
		int i;
		for (i=0;i<currentScriptCount;i++){
			final int sRow = i; 
			JButton sjl = new JButton();
			sjl.setText(scriptArray[i][1]);
			cEditorPanel.add(sjl);
			concernPaneLayout.putConstraint(SpringLayout.NORTH,sjl,scriptRow,SpringLayout.NORTH,cEditorPanel);
			concernPaneLayout.putConstraint(SpringLayout.WEST,sjl,80,SpringLayout.WEST,cEditorPanel);
            sjl.setName(Integer.toString(i));
            sjl.enableInputMethods(false);
            
            JButton sView = new JButton();
            sView.setText("Preview");
			cEditorPanel.add(sView);
			concernPaneLayout.putConstraint(SpringLayout.NORTH,sView,scriptRow,SpringLayout.NORTH,cEditorPanel);
			concernPaneLayout.putConstraint(SpringLayout.WEST,sView,250,SpringLayout.WEST,cEditorPanel);
			sView.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent arg0) {
					validationScript v = new validationScript();
					v.fetchScriptValue(Integer.parseInt(scriptArray[sRow][0]));
					JOptionPane.showMessageDialog(null, v.currentScriptText, "Preview",JOptionPane.PLAIN_MESSAGE);
				}
			});
            
			JButton sRun = new JButton();
			sRun.setText("Run");
			cEditorPanel.add(sRun);
			concernPaneLayout.putConstraint(SpringLayout.NORTH,sRun,scriptRow,SpringLayout.NORTH,cEditorPanel);
			concernPaneLayout.putConstraint(SpringLayout.WEST,sRun,350,SpringLayout.WEST,cEditorPanel);			
			sRun.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent arg0) {
					validationScript v = new validationScript();
					v.runScript(Integer.parseInt(scriptArray[sRow][0]));
				}
			});
             
			scriptRow = scriptRow + 40;
		}
		
		// add panel to frame
		getContentPane().add(cEditorPanel);
		this.setVisible(true);		
	
	};
	
	
	private void setValueFields(int editConcernId,Object[][] concernScriptData,String[][] scriptArray)
	{
		/**
		 * return the details of the current concern
		 */
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
	    
	}
	
	private void placeLayouts(SpringLayout concernPaneLayout, JPanel cEditorPanel)
	{	
		Font titleFont = new Font("Serif", Font.BOLD, 16);
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonClose,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,buttonClose,850,SpringLayout.WEST,cEditorPanel);			
		
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernTitleLabel,5,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernTitleLabel,5,SpringLayout.WEST,cEditorPanel);	
		concernTitleLabel.setFont(titleFont);
						
		concernPaneLayout.putConstraint(SpringLayout.NORTH,concernDescription,40,SpringLayout.NORTH,cEditorPanel);
		concernPaneLayout.putConstraint(SpringLayout.WEST,concernDescription,15,SpringLayout.WEST,cEditorPanel);	
		concernDescription.setEditable(false);
		concernScriptsLabel.setFont(titleFont);
		concernDescription.setOpaque(true);
		
        concernPaneLayout.putConstraint(SpringLayout.NORTH,concernScriptsLabel,150,SpringLayout.NORTH,cEditorPanel);
        concernPaneLayout.putConstraint(SpringLayout.WEST,concernScriptsLabel,5,SpringLayout.WEST,cEditorPanel);				
				
	}


	/* Simplify messages */
	private void intendedAction(String actionName){
		JOptionPane.showMessageDialog(null, actionName, "Holding status",JOptionPane.PLAIN_MESSAGE);	
	};

	private void showError(String errorMessage){
		JOptionPane.showMessageDialog(null, errorMessage, "Error",JOptionPane.PLAIN_MESSAGE);	
	};	
}
