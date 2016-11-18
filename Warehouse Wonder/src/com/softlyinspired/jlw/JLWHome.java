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
package com.softlyinspired.jlw;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;

import com.softlyinspired.jlw.concerns.*;
import com.softlyinspired.jlw.connections.connectionEditDialog;
import com.softlyinspired.jlw.script.scriptManagerFrame;

import java.io.File;
import java.io.IOException;

import com.softlyinspired.jlw.menus.mongoMenu;
import com.softlyinspired.jlw.menus.mongoMenuSet;
import com.softlyinspired.jlw.reports.*;

import java.net.URL;


public class JLWHome extends JFrame
{

   /**
   *  This loads a frame with multiple menus
      One menu loads a second frame and prompts to read a file
      Errors happen
   */   

    
	public static void main(String[] args)
	{
        try {
	       ClassicEngineBoot.getInstance().start() ;
	    } catch (Exception e) {
	       System.out.println("error in classic boot");
	      }		

        new JLWProperties().readall();
 		new JLWHome();
      
	}
	
	public void windowOpened(WindowEvent e) {
    }
	
	public JLWHome() 
	{
	    addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
            public void windowOpened(java.awt.event.WindowEvent e) {
            	try { 
            		setIconImage(ImageIO.read(new File("res/logo1.png")));
            	} catch(Exception e1) {
            		System.out.println("icon load failed");
            	}
        }
        });
	    
		this.setSize(600,300);
	    this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Warehouse Wonder");
		
		//Create the menu bar.  Make it have a green background.
        JMenuBar MenuBar1 = new JMenuBar();
        MenuBar1.setOpaque(true);
        MenuBar1.setBackground(new Color(192, 206, 239));
        MenuBar1.setPreferredSize(new Dimension(200, 20));
        this.setJMenuBar(MenuBar1);
        this.CustomMenu(MenuBar1);		

        JMenu menuAbout = new JMenu("About");
        JMenuItem menuAboutRun = new JMenuItem("About Warehouse Wonder");
        menuAboutRun.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	new JLWAboutBox();
            }
       });
        MenuBar1.add(Box.createHorizontalGlue()); 
        menuAbout.add(menuAboutRun);
        MenuBar1.add(menuAbout);
        
		SpringLayout thisPaneLayout = new SpringLayout();			
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(thisPaneLayout);
		
		/* Manage scripts button */
		final JButton buttonManagerScripts;		
		buttonManagerScripts = new JButton("Manage scripts");
		buttonManagerScripts.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				new scriptManagerFrame(JLWHome.this);
			}
		});
		
		/* Manage concerns button */
		final JButton buttonManageConcerns;		
		buttonManageConcerns = new JButton("Manage concerns");
		buttonManageConcerns.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				new concernManagerFrame();
			}
		});		
		
		/* Manage connections button */
		final JButton buttonManageConnections;		
		buttonManageConnections = new JButton("Manage connections");
		buttonManageConnections.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				new connectionEditDialog();
			}
		});		
	
		/* Manage reports button */
		final JButton buttonManageReports;		
		buttonManageReports = new JButton("Manage reports");
		buttonManageReports.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				new ReportManagerFrame(JLWHome.this);
			}
		});			

		/* Preview button */
		final JButton buttonPreview;
		buttonPreview = new JButton("Preview");
			
	    buttonPreview.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
			 try {
				 ResourceManager manager = new ResourceManager();
			     manager.registerDefaults();
			     Resource res = manager.createDirectly(new URL("file:src/ReportQueried.prpt"),MasterReport.class);
			     MasterReport report = (MasterReport) res.getResource();  
	
				 final PreviewDialog preview = new PreviewDialog(report);
				 preview.addWindowListener(new WindowAdapter(){
				 public void windowClosing (final WindowEvent event){
				      preview.setVisible(false);
	             }});
			     preview.pack();
			     preview.setVisible(true);
			   } catch (ResourceException e){
			       System.out.println(e);
			   } catch (IOException e){
			        System.out.println(e);
			   }   
			 }
			}
		);

	    // Set the panel and add items			
		panel1.add(buttonManagerScripts);			
		panel1.add(buttonManageConcerns);
		panel1.add(buttonManageConnections);
		panel1.add(buttonManageReports);
		
		thisPaneLayout.putConstraint(SpringLayout.SOUTH,buttonManagerScripts,-5,SpringLayout.SOUTH,panel1);
		thisPaneLayout.putConstraint(SpringLayout.WEST,buttonManagerScripts,5,SpringLayout.WEST,panel1);	
		thisPaneLayout.putConstraint(SpringLayout.SOUTH,buttonManageConcerns,-5,SpringLayout.SOUTH,panel1);
		thisPaneLayout.putConstraint(SpringLayout.WEST,buttonManageConcerns,145,SpringLayout.WEST,panel1);	
		thisPaneLayout.putConstraint(SpringLayout.SOUTH,buttonManageConnections,-5,SpringLayout.SOUTH,panel1);
		thisPaneLayout.putConstraint(SpringLayout.WEST,buttonManageConnections,290,SpringLayout.WEST,panel1);	
		thisPaneLayout.putConstraint(SpringLayout.SOUTH,buttonManageReports,-5,SpringLayout.SOUTH,panel1);
		thisPaneLayout.putConstraint(SpringLayout.WEST,buttonManageReports,450,SpringLayout.WEST,panel1);
		// add panel to frame
		this.add(panel1);
		this.setVisible(true);

	}
	
       /**
         * Adds the 'Custom' menu and then adds items from a mongo doc
       */    

        public void CustomMenu(JMenuBar MenuBar1){

        int menuCount;
		mongoMenuSet mymenu = new mongoMenuSet();
        menuCount = mymenu.returnAllMenus();
		
        int i;
        for ( i= 0;i<menuCount;i=i+1){
          mongoMenu m = new mongoMenu();
          m = mymenu.customMenu[i];
          addCustomMenu(MenuBar1,m);
        }
        
        // Get menus from concerns, each category becomes a menu with each concern a menu item.
        int menuCount2;
        concernSet cs = new concernSet();
        concernSet.concernMenu[] cmSet = new concernSet.concernMenu[10];        
        cmSet = cs.buildConcernMenus();
        menuCount2 = cs.menuCount;
        int m = 0;
        for ( m= 0;m<menuCount2;m=m+1){           
           addConcernMenu(MenuBar1,cmSet[m]);
        }        

        
	}	
	
    /**
      Adds custom menus and menu items.
    */
	private void addCustomMenu(JMenuBar thisMenuBar,mongoMenu thisMenuDef){
        JMenu thisMenu = new JMenu();
        thisMenu.setText(thisMenuDef.menuTitle);
        int i;
        for (i=0;i<thisMenuDef.menuItemCount;i=i+1){
           concern c = new concern();
           final String mc = new String(thisMenuDef.menuItem[i]);
           String mItemLabel = new String();
           if (c.checkExistingId(Integer.parseInt(mc))) {
        	  c.fetchConcernDetails(Integer.parseInt(mc));
        	  mItemLabel = mc + " : " + c.concernTitle;
        	  JMenuItem mi = new JMenuItem(mItemLabel);
              mi.addActionListener(new ActionListener(){
	               public void actionPerformed(ActionEvent e){
	                    new concernActionFrame(Integer.parseInt(mc));
	               }
              });
              thisMenu.add(mi);
      		               
           }
           else {
        	   JOptionPane.showMessageDialog(null,mc, "custom menu Unknown concern",JOptionPane.PLAIN_MESSAGE);
           }
        }
		thisMenuBar.add(thisMenu);
	}
			
    /**
      Adds concern menus and menu items.
    */
	private void addConcernMenu(JMenuBar thisMenuBar,concernSet.concernMenu thisMenuDef){
        JMenu thisMenu = new JMenu();
        thisMenu.setText(thisMenuDef.menuTitle);
        String currentConcernName = new String();
        int i=0;
        for (i=0;i<thisMenuDef.menuItemCount;i=i+1){
           concern c = new concern();
           final int currentConcernInt ;
           currentConcernName = thisMenuDef.menuItem[i];
           currentConcernInt = Integer.parseInt(currentConcernName);
           String mItemLabel = new String();
           if (c.checkExistingId(currentConcernInt)) {
        	  c.fetchConcernDetails(currentConcernInt);
        	  mItemLabel = currentConcernName + " : " + c.concernTitle;
        	  JMenuItem mi = new JMenuItem(mItemLabel);
              mi.addActionListener(new ActionListener(){
	               public void actionPerformed(ActionEvent e){
	                    new concernActionFrame(currentConcernInt);
	               }
              });
              thisMenu.add(mi);
           }
           else {
        	   JLWUtilities.scriptInfoMessage("concern menu Unknown concern");
           }
        }
		thisMenuBar.add(thisMenu);
	}			
	
	
}

