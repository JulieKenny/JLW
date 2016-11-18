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

public class ScriptSelectorDialog extends JDialog
 {

	private static JButton buttonSelect;	
	private static JButton buttonCancel;
	public int selectedScriptId = 0;
	private validationScript scriptList = new validationScript();
	private DefaultListModel<String> scriptListModel = new DefaultListModel<>();

	public ScriptSelectorDialog(JDialog parent)
	{
	    this.setModal(true);
		setBackground(Color.LIGHT_GRAY);
		this.setSize(420,400);
		Point parentloc = new Point(parent.getLocation());
		this.setLocation(parentloc.x + 200,parentloc.y + 60);
		
		this.setTitle("Script Selector");
		 
		JPanel scriptSelectorLayout = new JPanel();
		
		final JList<String> scriptList = new JList<>(scriptListModel);
		final JScrollPane scriptListScrollPane = new JScrollPane(scriptList); 
		scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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
				  selectedScriptId = 0;
			      dispose();
			}
		});		
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
	
		SpringLayout scriptPaneLayout = new SpringLayout();
		
		// put items on panel 		
		scriptSelectorLayout.add(buttonSelect);		
        scriptSelectorLayout.add(buttonCancel);
		 
		scriptSelectorLayout.add(scriptListScrollPane);

		scriptPaneLayout.putConstraint(SpringLayout.NORTH,buttonSelect,5,SpringLayout.NORTH,scriptSelectorLayout);
		scriptPaneLayout.putConstraint(SpringLayout.WEST,buttonSelect,60,SpringLayout.WEST,scriptSelectorLayout);	
		scriptPaneLayout.putConstraint(SpringLayout.NORTH,buttonCancel,5,SpringLayout.NORTH,scriptSelectorLayout);
		scriptPaneLayout.putConstraint(SpringLayout.WEST,buttonCancel,150,SpringLayout.WEST,scriptSelectorLayout);	

		scriptPaneLayout.putConstraint(SpringLayout.NORTH,scriptListScrollPane,90,SpringLayout.NORTH,scriptSelectorLayout);
		scriptPaneLayout.putConstraint(SpringLayout.WEST,scriptListScrollPane,5,SpringLayout.WEST,scriptSelectorLayout);		
		scriptPaneLayout.putConstraint(SpringLayout.SOUTH,scriptListScrollPane,300,SpringLayout.NORTH,scriptSelectorLayout);
		scriptPaneLayout.putConstraint(SpringLayout.EAST,scriptListScrollPane,400,SpringLayout.WEST,scriptSelectorLayout);		

		scriptSelectorLayout.setLayout(scriptPaneLayout);
		
		// add panel to frame
		getContentPane().add(scriptSelectorLayout);
		this.setVisible(true);
		
	}

	//private void showscriptList(DefaultListModel<String> scriptListModel,JScrollPane scriptListScrollPane) {
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
