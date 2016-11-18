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

package com.softlyinspired.jlw.menus;
import javax.swing.*;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.softlyinspired.jlw.script.validationScript;

import java.awt.event.*;
import java.net.UnknownHostException;
import java.util.*;


public class menuEditFrame extends JFrame
{
	private static JButton buttonAddScript;

	int selectedScriptId = 0;	
	boolean visibleList ;
	
	public menuEditFrame() {
		setBackground(Color.LIGHT_GRAY);
		this.setSize(800,600);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Menu Manager");
		
		JPanel menuManagerLayout = new JPanel();
		JLabel labelMenus = new JLabel("Menus");
		JLabel labelScripts = new JLabel("Scripts");
		SpringLayout scriptPanelayout = new SpringLayout();
		menuManagerLayout.setLayout(scriptPanelayout);
		
		final DefaultListModel scriptListModel = new DefaultListModel();
		final JList scriptList = new JList(scriptListModel);
		final JScrollPane scriptListScrollPane = new JScrollPane(scriptList); 
		scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		scriptList.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent ie){
				int inx = scriptList.getSelectedIndex();

				String selectedScriptName = new String();
				selectedScriptName = scriptList.getSelectedValue().toString();
				selectedScriptId = Integer.parseInt(selectedScriptName.substring(0,selectedScriptName.indexOf(":")));
			}
		} );	
		
		final DefaultListModel menuListModel = new DefaultListModel();
		final JList menuList = new JList(menuListModel);
		final JScrollPane menuListScrollPane = new JScrollPane(menuList); 
		scriptList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		menuList.addListSelectionListener(new ListSelectionListener(){
		
			public void valueChanged(ListSelectionEvent ie){
				int inx = scriptList.getSelectedIndex();

				String selectedScriptName = new String();
				selectedScriptName = scriptList.getSelectedValue().toString();
				selectedScriptId = Integer.parseInt(selectedScriptName.substring(0,selectedScriptName.indexOf(":")));
			}
		} );		
		
		// add panel to frame
		getContentPane().add(menuManagerLayout);
		this.setVisible(true);	
		
		String allScripts[][] = new String[100][2];
		validationScript scriptListSet = new validationScript();
		allScripts = scriptListSet.listall(); 
		scriptListModel.clear();

		//scriptListScrollPane.setVisible(true);
		for(int i =0; i < scriptListSet.scriptCount; i++)
		  {
			 if (allScripts[i] != null )
			 {
		       scriptListModel.addElement(allScripts[i][1]);
			 }
		  }
		int menuCount;
		int foundMenuId;
		String foundMenuTitle = new String();
		String foundMenuDisplay = new String();
		mongoMenuSet menuListSet = new mongoMenuSet();
		menuCount = menuListSet.returnAllMenus();		
        int i;
        for ( i= 0;i<menuCount;i=i+1){
        	foundMenuId = menuListSet.menuId[i];
        	foundMenuTitle = menuListSet.menuTitle[i];	
        	foundMenuDisplay = String.valueOf(foundMenuId) + " : "+ foundMenuTitle;
        	menuListModel.addElement(foundMenuDisplay);
        }
		
		buttonAddScript = new JButton("Add script to menu");
				
	// put items on panel 		
	menuManagerLayout.add(scriptListScrollPane);
	menuManagerLayout.add(menuListScrollPane);
	menuManagerLayout.add(labelMenus);
	menuManagerLayout.add(labelScripts);
	menuManagerLayout.add(buttonAddScript);


	scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonAddScript,5,SpringLayout.NORTH,menuManagerLayout);
	scriptPanelayout.putConstraint(SpringLayout.WEST,buttonAddScript,150,SpringLayout.WEST,menuManagerLayout);	

	scriptPanelayout.putConstraint(SpringLayout.NORTH,labelMenus,20,SpringLayout.NORTH,menuManagerLayout);
	scriptPanelayout.putConstraint(SpringLayout.WEST,labelMenus,5,SpringLayout.WEST,menuManagerLayout);	
	
	scriptPanelayout.putConstraint(SpringLayout.NORTH,labelScripts,20,SpringLayout.NORTH,menuManagerLayout);
	scriptPanelayout.putConstraint(SpringLayout.WEST,labelScripts,410,SpringLayout.WEST,menuManagerLayout);	
	
	scriptPanelayout.putConstraint(SpringLayout.NORTH,scriptListScrollPane,50,SpringLayout.NORTH,menuManagerLayout);
	scriptPanelayout.putConstraint(SpringLayout.WEST,scriptListScrollPane,5,SpringLayout.WEST,menuManagerLayout);		
	scriptPanelayout.putConstraint(SpringLayout.SOUTH,scriptListScrollPane,400,SpringLayout.NORTH,menuManagerLayout);
	scriptPanelayout.putConstraint(SpringLayout.EAST,scriptListScrollPane,400,SpringLayout.WEST,menuManagerLayout);		

	scriptPanelayout.putConstraint(SpringLayout.NORTH,menuListScrollPane,50,SpringLayout.NORTH,menuManagerLayout);
	scriptPanelayout.putConstraint(SpringLayout.WEST,menuListScrollPane,400,SpringLayout.WEST,menuManagerLayout);		
	scriptPanelayout.putConstraint(SpringLayout.SOUTH,menuListScrollPane,400,SpringLayout.NORTH,menuManagerLayout);
	scriptPanelayout.putConstraint(SpringLayout.EAST,menuListScrollPane,800,SpringLayout.WEST,menuManagerLayout);
	
	menuManagerLayout.setLayout(scriptPanelayout);
	
	//scriptListScrollPane.setVisible(false);
	
	// add panel to frame
	//getContentPane().add(scriptManagerLayout, BorderLayout.NORTH);
	getContentPane().add(menuManagerLayout);
	this.setVisible(true);
}
}
