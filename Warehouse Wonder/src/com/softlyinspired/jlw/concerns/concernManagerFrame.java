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

package com.softlyinspired.jlw.concerns;
import javax.swing.*;

import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.softlyinspired.jlw.menus.mongoMenu;
import com.softlyinspired.jlw.script.scriptManagerFrame;

import java.awt.event.*;
import java.net.UnknownHostException;
import java.util.*;


public class concernManagerFrame extends JFrame
 {
  public static void OpenFrame(String[] args)
  {
	  new concernManagerFrame();
  }
  
	private static JButton buttonNewConcern;
	private static JButton buttonEditConcern;	
	int selectedConcernId = 0;	
	final DefaultListModel<String> concernListModel = new DefaultListModel<>();
	final JList<String> concernList = new JList<>(concernListModel);

	public concernManagerFrame() 
	{
		setBackground(Color.LIGHT_GRAY);
		this.setSize(420,400);
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setTitle("Concern Manager");
		 
		JPanel concernManagerLayout = new JPanel();
		
		final JScrollPane concernListScrollPane = new JScrollPane(concernList); 
		concernList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		concernList.addListSelectionListener(new ListSelectionListener(){
		
			public void valueChanged(ListSelectionEvent ie){
				try {
				int t = concernList.getSelectedIndex();
				if (t != -1) {
				String selectedConcernName = new String();
				selectedConcernName = concernList.getSelectedValue().toString();
				selectedConcernId = Integer.parseInt(selectedConcernName.substring(0,selectedConcernName.indexOf(":")-1));
				}
				} catch(Exception e) {
					JOptionPane.showMessageDialog(null,"Invalid Concern Listed", 
							"Concern Manager",JOptionPane.PLAIN_MESSAGE);
				}				
			}
		} );
		
		setList(concernListScrollPane);		
		concernListScrollPane.setVisible(true);		

		buttonNewConcern = new JButton("Create new concern");
		buttonNewConcern.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			new concernEditDialog(-1,true);		
			setList(concernListScrollPane);		
			concernManagerFrame.this.repaint();			
			}
		});
		
		buttonEditConcern = new JButton("Edit concern");
		buttonEditConcern.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
			int editConcernId = 0;
			if (selectedConcernId != 0)
				{
				editConcernId = selectedConcernId;
				}
			new concernEditDialog(editConcernId,false);
			setList(concernListScrollPane);				
			concernManagerFrame.this.repaint();			
	
			}
		});

		
		SpringLayout scriptPanelayout = new SpringLayout();
		
		// put items on panel 
		concernManagerLayout.add(buttonNewConcern, "1, 7, left, center");
		concernManagerLayout.add(buttonEditConcern, "1, 9, left, center");		

		concernManagerLayout.add(concernListScrollPane);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonNewConcern,5,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonNewConcern,5,SpringLayout.WEST,concernManagerLayout);

		scriptPanelayout.putConstraint(SpringLayout.NORTH,buttonEditConcern,5,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,buttonEditConcern,250,SpringLayout.WEST,concernManagerLayout);	

		scriptPanelayout.putConstraint(SpringLayout.NORTH,concernListScrollPane,90,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.WEST,concernListScrollPane,5,SpringLayout.WEST,concernManagerLayout);		
		scriptPanelayout.putConstraint(SpringLayout.SOUTH,concernListScrollPane,300,SpringLayout.NORTH,concernManagerLayout);
		scriptPanelayout.putConstraint(SpringLayout.EAST,concernListScrollPane,400,SpringLayout.WEST,concernManagerLayout);		

		concernManagerLayout.setLayout(scriptPanelayout);
		
		// add panel to frame
		getContentPane().add(concernManagerLayout);
		this.setVisible(true);

	}
	
	private void setList(JScrollPane concernListScrollPane){
		concernListModel.clear();
		String[][] allConcerns = new String[100][2];	

		concernSet concernSet = new concernSet();
		
		allConcerns = concernSet.listall(); 

		for(int i =0; i <= concernSet.concernCount ; i++)
		{
			if (allConcerns[i][0] != null )
			 {	 
			   concernListModel.addElement(allConcerns[i][0] + " : " + allConcerns[i][1]);
			 }
		 }	
		}
	
}
