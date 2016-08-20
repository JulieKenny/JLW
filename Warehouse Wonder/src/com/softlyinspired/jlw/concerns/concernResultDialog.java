package com.softlyinspired.jlw.concerns;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import com.softlyinspired.jlw.JLWUtilities;

public class concernResultDialog extends JDialog{

	private static JButton buttonClose;	
	
	public concernResultDialog(String resultFilename)
	{
	    this.setModal(true);	
		setBackground(Color.LIGHT_GRAY);
		this.setSize(700,500);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Script Result");	
		   
		JPanel cEditorPanel = new JPanel();
	    final JTextArea scriptResult = new JTextArea(20,60);

		/* Cancel button */
		buttonClose = new JButton("Close");
		buttonClose.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0) {
			      dispose();
			}
		});	

		// Set the panel and add items	
	    SpringLayout concernPaneLayout = new SpringLayout();	
		cEditorPanel.setLayout(concernPaneLayout);		
		cEditorPanel.add(buttonClose);
		cEditorPanel.add(scriptResult);
	
		try {
			FileReader reader = new FileReader(resultFilename);
			scriptResult.read(reader,null);		
		} catch(Exception e){
			JLWUtilities.scriptInfoMessage("Error reading result");
		}
	
		concernPaneLayout.putConstraint(SpringLayout.NORTH,buttonClose,5,SpringLayout.NORTH,cEditorPanel);
	    concernPaneLayout.putConstraint(SpringLayout.WEST,buttonClose,600,SpringLayout.WEST,cEditorPanel);
		
	    concernPaneLayout.putConstraint(SpringLayout.NORTH,scriptResult,40,SpringLayout.NORTH,cEditorPanel);
	    concernPaneLayout.putConstraint(SpringLayout.WEST,scriptResult,5,SpringLayout.WEST,cEditorPanel);
		
		// add panel to frame
		getContentPane().add(cEditorPanel);
		this.setVisible(true);		
	
  }
}

