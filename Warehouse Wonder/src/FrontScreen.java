import javax.swing.*;

import java.awt.*;

import javax.swing.BoxLayout;

import java.awt.event.*;
import java.util.*;

public class FrontScreen extends JFrame
{

   /**
   *  This loads a frame with multiple menus
      One menu loads a second frame and prompts to read a file
      Errors happen
   */   
	public static void main(String[] args)
	{
		new FrontScreen();
	}
	
	private static JButton buttonReadFile;

	
	public FrontScreen() 
	{
		this.setSize(800,500);
		
		this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Julies Little Wonder");
		
		//Create the menu bar.  Make it have a green background.
        JMenuBar MenuBar1 = new JMenuBar();
        MenuBar1.setOpaque(true);
        MenuBar1.setBackground(new Color(226, 230, 110));
        MenuBar1.setPreferredSize(new Dimension(200, 20));
        this.setJMenuBar(MenuBar1);
        
        this.stdMenu(MenuBar1,"Admin","Manage Scripts","Manage Menus","Manage Connections");
		this.stdMenu(MenuBar1,"Fixes","Run Test","Run Report","Run Fix");
        this.CustomMenu(MenuBar1);
        
		ReadListener rl = new ReadListener();
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
		JLabel label1 = new JLabel("Click this button to read the file");
		
		buttonReadFile = new JButton("ReadFile");
		buttonReadFile.addActionListener(rl);

		// put items on panel 
		panel1.add(label1,BorderLayout.CENTER);
		//panel1.add(label2,BorderLayout.SOUTH);
		panel1.add(buttonReadFile);

		// add panel to frame
		this.add(panel1);
		this.setVisible(true);

		
	}
	
	public void stdMenu(JMenuBar MenuBar1, String menuTitle, String mItem1, String mItem2, String mItem3) {
	/** 
	  * Creates a standard menu that has 3 items on it
	  * Enhance by changing the set 3 items to an array or list
	*/
	    JMenu menu1 =  new JMenu(menuTitle);
        MenuBar1.add(menu1);
        JMenuItem menuItemCDef = new JMenuItem(mItem1);
        menu1.add(menuItemCDef);
        ReadListener m1 = new ReadListener();
        menuItemCDef.addActionListener(m1);
		 
        JMenuItem menuItemCAmend = new JMenuItem(mItem2);
        menu1.add(menuItemCAmend);
        menuItemCAmend.addActionListener(m1);
		
        JMenuItem menuItemCSelect = new JMenuItem(mItem3);
        menu1.add(menuItemCSelect);
        menuItemCSelect.addActionListener(m1);
	};
	
	
     public void CustomMenu(JMenuBar MenuBar1){
      /** Adds the 'Custom' menu and then adds items from a mongo doc
       */
		ReadListener m2 = new ReadListener();

		JMenu menu1 =  new JMenu("Custom");
        MenuBar1.add(menu1);

        int menuCount;
		mongoMenuSet mymenu = new mongoMenuSet();
		menuCount = mymenu.returnAllMenus();
		
		String countString = new String();
	    countString = Integer.toString(menuCount);
		
        int i;
        for ( i= 0;i<menuCount;i=i+1){
          applyCustomMenuItem(menu1,MenuBar1,mymenu.menuTitle[i]);
        }
        
	}	
	
	private void applyCustomMenuItem (JMenu menu1, JMenuBar MenuBar1,String menuName){
      /**
         Adds a new menu item 
       */	 
        ReadListener m2 = new ReadListener();
	    customMenu m = new customMenu();
	    String a = Integer.toString(m.result()) + " : " + menuName;
	    JMenuItem menuItemNew = new JMenuItem(a);
	    menuItemNew.addActionListener(m2);
        menu1.add(menuItemNew);
       
	}

	private class ReadListener
	 implements ActionListener
	 {
		
		public void actionPerformed(ActionEvent e)
		{
			String commandName = e.getActionCommand();
			String menuIdString;
			int menuIdClicked;
			//JOptionPane.showMessageDialog(null, commandName, "Note",JOptionPane.PLAIN_MESSAGE);
			String IdName = null;
			switch(commandName)
			{ case "Manage Scripts" : IdName = "newDef";
			                         scriptManagerFrame scriptManager = new scriptManagerFrame();
			                         break;
			case "Manage Menus" : IdName = "amendDef";
			                         menuEditFrame menuManager = new menuEditFrame();
					                 break;
			case "Select Definition" : IdName = "selectDef";
			                         myNumberClass numberSet = new myNumberClass();
			                         numberSet.a = 12;
			                         numberSet.b = 2;
			                         numberSet.result();
			                         break;
			                         
			case "Run Test" : IdName = "runReport1";	
			                        //JOptionPane.showMessageDialog(null, commandName, "Note",JOptionPane.PLAIN_MESSAGE);
            						validationScript fixScript = new validationScript();
            						fixScript.runScript(101);
            						break;
            						
			case "Run Report" : IdName = "runReport";
			                        validationReport myReport = new validationReport();
			                        myReport.run();
            						break;
            						
			case "Run Fix" : IdName = "runFix";
            						break;
            						
            default :  menuIdString = commandName.substring(0,commandName.indexOf(":")-1);
                       menuIdClicked =  Integer.parseInt(menuIdString);
                       validationScript k = new validationScript();
                       k.fetchScriptFromMenu(menuIdClicked);					
           
			};
			
			
			if (e.getSource()== buttonReadFile)
			{
				ArrayList <String> errorsFound = new ArrayList <String>();
				ReadFile.main(errorsFound);
				buttonReadFile.setText("The file has been read");
				Iterator <String> currentError = errorsFound.iterator();
			    while(currentError.hasNext()){
			    	commandName += currentError.next();
			        
			    }
			}

		}
	 }
	
	
	
}

