import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class ScriptEditFrame extends JFrame
 {
    /** 
     * Frame to edit a single script called with boolean true if new script 
     */
	private static JButton buttonSaveScript;
	private static JButton buttonCancel;
	private static JButton buttonDeleteScript;	
	
	private static validationScript currentScript;
	final JTextField scriptId = new JTextField(10);	
	final JTextField scriptTitle = new JTextField(20);
	final JTextField scriptCategory = new JTextField(20);
	final JTextArea scriptArea = new JTextArea(10,60);	
	final JTextField scriptConnection = new JTextField(15);
	private static int scriptIdNum;
	
	public ScriptEditFrame(final boolean isNewScript, int editScriptId)
	{
		/**
		 * Create the frame and script instance
		 */
		setBackground(Color.LIGHT_GRAY);
		this.setSize(900,400);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel scriptEditorPanel = new JPanel();
		   
		currentScript = new validationScript();	
		/* 
		 * Show if editing or creating
		 */
		if (isNewScript == true){
		   this.setTitle("New Script Creator");			
		}else {
		   this.setTitle("Script Editor");	
		}

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
			
		
		/* Script details */
		JLabel scriptIdLabel = new JLabel("Enter numeric script Id");		
		JLabel scriptCategoryLabel = new JLabel("Category");	
		JLabel scriptTitleLabel = new JLabel("Title");
		JLabel scriptAreaLabel = new JLabel("Enter script long text");
		JLabel scriptConnectionLabel = new JLabel("Connection");

		scriptArea.setText("Enter text here");
		
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
		if (isNewScript)
		{	showError(Integer.toString(editScriptId));}
		
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
				
				if (scriptConnection.getText() == null || (scriptConnection.getText().isEmpty()))
				{
					newScriptTitle = "connection unknown";
				}
				else {
					newScriptConnection = scriptConnection.getText();
				}
				
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
		
		// Set the panel and add items	
		SpringLayout scriptPanelayout = new SpringLayout();		
		scriptEditorPanel.setLayout(scriptPanelayout);		
		scriptEditorPanel.add(buttonSaveScript);
		scriptEditorPanel.add(buttonCancel);
		scriptEditorPanel.add(buttonDeleteScript);		

		scriptEditorPanel.add(scriptIdLabel);
		scriptEditorPanel.add(scriptId);

		scriptEditorPanel.add(scriptCategoryLabel);
		scriptEditorPanel.add(scriptCategory);
		scriptEditorPanel.add(scriptTitleLabel);
		scriptEditorPanel.add(scriptTitle);
		
		scriptEditorPanel.add(scriptAreaLabel);
		scriptEditorPanel.add(scriptArea);
		
		scriptEditorPanel.add(scriptConnectionLabel);
		scriptEditorPanel.add(scriptConnection);
		
		/* Standardise placing */
		int labelColumn = 5;
		int detailDistance = 150; /*space between label and data */
		int[] row = {5,35,65,95,125,320};  /* Row positions */
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonSaveScript,row[5],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonSaveScript,350,SpringLayout.WEST,scriptEditorPanel);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonCancel,row[5],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonCancel,120,SpringLayout.WEST,buttonSaveScript);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonDeleteScript,row[5],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonDeleteScript,120,SpringLayout.WEST,buttonCancel);		
		
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
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptConnection,row[3],SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptConnection,detailDistance,SpringLayout.WEST,scriptConnectionLabel);		
				
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
		validationScript a = new validationScript();
	    int scriptError = currentScript.fetchScriptValue(editScript);
	    scriptArea.setText(currentScript.currentScriptText);
		scriptCategory.setText(currentScript.currentScriptCategory);
		scriptTitle.setText(currentScript.currentScriptTitle);
		scriptConnection.setText(currentScript.currentConnection);
	}

	/* Simplify messages */
	private void intendedAction(String actionName){
		JOptionPane.showMessageDialog(null, actionName, "Holding status",JOptionPane.PLAIN_MESSAGE);	
	};

	private void showError(String errorMessage){
		JOptionPane.showMessageDialog(null, errorMessage, "Error",JOptionPane.PLAIN_MESSAGE);	
	};	
}
