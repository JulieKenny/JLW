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
	final JTextField scriptCategory = new JTextField(20);
	final JTextArea scriptArea = new JTextArea(10,60);	
	private static int scriptIdNum;
	

	public ScriptEditFrame(boolean isNewScript, int editScriptId)
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
		
        /* button to fetch script used for edit */
		JButton buttonFetchScript = new JButton("Get Script");	
		
		/* Script details */
		JLabel scriptIdLabel = new JLabel("Enter numeric script Id");		
		JLabel scriptCategoryLabel = new JLabel("Category");	
		JLabel scriptAreaLabel = new JLabel("Enter script long text");

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
		buttonSaveScript.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
				int errorCount = 0;
				//validationScript newScript = new validationScript();
				int newScriptId = 0;
				String newScriptText = new String();
				String newScriptCategory = new String();
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
				
				if (scriptCategory.getText() == null || (scriptArea.getText().isEmpty()))
				{
					newScriptCategory = "Generic";
				}
				else {
					newScriptCategory = scriptCategory.getText();
				}
				
                if (errorCount == 0 ){
                	currentScript.create(newScriptId, newScriptText,newScriptCategory);
                }
				
			}
		});
		
		// Set the panel and add items	
		SpringLayout scriptPanelayout = new SpringLayout();		
		scriptEditorPanel.setLayout(scriptPanelayout);		
		scriptEditorPanel.add(buttonSaveScript);
		scriptEditorPanel.add(buttonCancel);
		scriptEditorPanel.add(buttonDeleteScript);		
		
		// fetch button only used for script changes
		if (! isNewScript) {
			scriptEditorPanel.add(buttonFetchScript);
			buttonFetchScript.addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent me){
					int scriptIdNum;
				    scriptIdNum = Integer.parseInt(scriptId.getText());
				    setValueFields(scriptIdNum);
				//	validationScript a = new validationScript();
				//   int scriptError = currentScript.fetchScriptValue(scriptIdNum);
				//    scriptArea.setText(currentScript.currentScriptText);
				//	scriptCategory.setText(currentScript.currentScriptCategory);

				}
			});
		};
		
		scriptEditorPanel.add(scriptIdLabel);
		scriptEditorPanel.add(scriptId);

		scriptEditorPanel.add(scriptCategoryLabel);
		scriptEditorPanel.add(scriptCategory);
		
		scriptEditorPanel.add(scriptAreaLabel);
		scriptEditorPanel.add(scriptArea);
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonSaveScript,300,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonSaveScript,350,SpringLayout.WEST,scriptEditorPanel);	
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonCancel,300,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonCancel,120,SpringLayout.WEST,buttonSaveScript);	
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonDeleteScript,300,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonDeleteScript,120,SpringLayout.WEST,buttonCancel);		
		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptIdLabel,5,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptIdLabel,5,SpringLayout.WEST,scriptEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptId,5,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptId,150,SpringLayout.WEST,scriptIdLabel);	
		
		if (! isNewScript){
		   scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonFetchScript,5,SpringLayout.NORTH,scriptEditorPanel);
		   scriptPanelayout.putConstraint(SpringLayout.WEST,buttonFetchScript,150,SpringLayout.WEST,scriptId);	
		}

		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptCategoryLabel,35,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptCategoryLabel,5,SpringLayout.WEST,scriptEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptCategory,35,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptCategory,150,SpringLayout.WEST,scriptCategoryLabel);	
				
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptAreaLabel,100,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptAreaLabel,5,SpringLayout.WEST,scriptEditorPanel);		
		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptArea,100,SpringLayout.NORTH,scriptEditorPanel);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptArea,150,SpringLayout.WEST,scriptAreaLabel);	
	
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
	}


	/* Simplify messages */
	private void intendedAction(String actionName){
		JOptionPane.showMessageDialog(null, actionName, "Holding status",JOptionPane.PLAIN_MESSAGE);	
	};

	private void showError(String errorMessage){
		JOptionPane.showMessageDialog(null, errorMessage, "Error",JOptionPane.PLAIN_MESSAGE);	
	};	
}
