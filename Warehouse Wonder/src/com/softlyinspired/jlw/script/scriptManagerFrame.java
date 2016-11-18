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

package com.softlyinspired.jlw.script;
import javax.swing.*;

import java.awt.*;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.org.apache.xerces.internal.dom.ParentNode;

import java.awt.event.*;

public class scriptManagerFrame extends JFrame
 {
  public static void OpenFrame(String[] args, JFrame parent)
  {
	  new scriptManagerFrame(parent);
  }
  
	private static JButton buttonNewScript;
	private static JButton buttonEditScript;	
	int selectedScriptId = 0;
	private validationScript scriptList = new validationScript();
	private DefaultListModel<String> scriptListModel = new DefaultListModel<>();

	public scriptManagerFrame(JFrame parent) 
	{
		setBackground(Color.LIGHT_GRAY);
		this.setSize(420,400);
		//this.setLocationRelativeTo(null); /* Put in centre of screen */
		Point parentloc = new Point(parent.getLocation());
		this.setLocation(parentloc.x + 200,parentloc.y + 60);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Script Manager");
		 
		JPanel scriptManagerLayout = new JPanel();
		
		final JList<String> scriptList = new JList<>(scriptListModel);
		final JScrollPane scriptListScrollPane = new JScrollPane(scriptList); 
		scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scriptList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent ie){
				try {
				scriptList.getSelectedIndex();
				String selectedScriptName = new String();
				selectedScriptName = scriptList.getSelectedValue().toString();
				selectedScriptId = Integer.parseInt(selectedScriptName.substring(0,selectedScriptName.indexOf(":")-1));
				} catch (Exception e){
					selectedScriptId = 0;
				}
			}
		} );

		showScriptList(scriptListScrollPane);

		buttonNewScript = new JButton("Create new script");
		buttonNewScript.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			new ScriptEditDialog(true,0);	
			showScriptList(scriptListScrollPane);
			scriptManagerFrame.this.repaint();
			}
		});
		
		buttonEditScript = new JButton("Edit script");
		buttonEditScript.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			int editScriptId = 0;
			if (selectedScriptId != 0)
			  {
			    editScriptId = selectedScriptId;
			    new ScriptEditDialog(false,editScriptId);
			  }
				showScriptList(scriptListScrollPane);
				scriptManagerFrame.this.repaint();				    
		}});


		SpringLayout scriptPanelayout = new SpringLayout();
		
		// put items on panel 
		scriptManagerLayout.add(buttonNewScript, "1, 7, left, center");
		scriptManagerLayout.add(buttonEditScript, "1, 9, left, center");		

		scriptManagerLayout.add(scriptListScrollPane);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonNewScript,5,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonNewScript,5,SpringLayout.WEST,scriptManagerLayout);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonEditScript,5,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonEditScript,150,SpringLayout.WEST,scriptManagerLayout);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptListScrollPane,90,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,scriptListScrollPane,5,SpringLayout.WEST,scriptManagerLayout);		
		scriptPanelayout.putConstraint(SpringLayout.SOUTH,scriptListScrollPane,300,SpringLayout.NORTH,scriptManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.EAST,scriptListScrollPane,400,SpringLayout.WEST,scriptManagerLayout);		

		scriptManagerLayout.setLayout(scriptPanelayout);
		
		// add panel to frame
		getContentPane().add(scriptManagerLayout);
		this.setVisible(true);
		
	}

	//private void showScriptList(DefaultListModel<String> scriptListModel,JScrollPane scriptListScrollPane) {
	private void showScriptList(JScrollPane scriptListScrollPane) {
		String[][] allScripts = new String[100][2];	
		allScripts = scriptList.listall(); 
		scriptListModel.clear();
		for(int i =0; i <= scriptList.scriptCount ; i++)
		  {
			 if (allScripts[i][0] != null )
			 {	 
			   scriptListModel.addElement(allScripts[i][0] + " : " + allScripts[i][1]);
			 }
		  }		
	}
	


	
}
