import javax.swing.*;

import java.awt.*;

import java.awt.event.*;

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
	
	public FrontScreen() 
	{
	    
		this.setSize(1200,700);
	    this.setLocationRelativeTo(null); /* Put in centre of screen */
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Julies Little Wonder");
		
		//Create the menu bar.  Make it have a green background.
        JMenuBar MenuBar1 = new JMenuBar();
        MenuBar1.setOpaque(true);
        MenuBar1.setBackground(new Color(226, 230, 110));
        MenuBar1.setPreferredSize(new Dimension(200, 20));
        this.setJMenuBar(MenuBar1);
        
        String[] fixMenu = new String[3];
        fixMenu[0] = "Run Test";
        fixMenu[1] = "Run Report";
        fixMenu[2] = "Run Fix";
		this.stdMenu(MenuBar1,"Fixes",fixMenu);
        
        this.CustomMenu(MenuBar1);			 	        
        
		SpringLayout thisPaneLayout = new SpringLayout();			
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(thisPaneLayout);
		
		/* Manage scripts button */
		final JButton buttonManagerScripts;		
		buttonManagerScripts = new JButton("Manage scripts");
		buttonManagerScripts.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent arg0){
				new scriptManagerFrame();
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
				new connectionEditFrame();
			}
		});			
		
	    // Set the panel and add items			
		panel1.add(buttonManagerScripts);			
		panel1.add(buttonManageConcerns);
		panel1.add(buttonManageConnections);
		
		thisPaneLayout.putConstraint(SpringLayout.SOUTH,buttonManagerScripts,-5,SpringLayout.SOUTH,panel1);
		thisPaneLayout.putConstraint(SpringLayout.WEST,buttonManagerScripts,5,SpringLayout.WEST,panel1);	
		thisPaneLayout.putConstraint(SpringLayout.SOUTH,buttonManageConcerns,-5,SpringLayout.SOUTH,panel1);
		thisPaneLayout.putConstraint(SpringLayout.WEST,buttonManageConcerns,145,SpringLayout.WEST,panel1);	
		thisPaneLayout.putConstraint(SpringLayout.SOUTH,buttonManageConnections,-5,SpringLayout.SOUTH,panel1);
		thisPaneLayout.putConstraint(SpringLayout.WEST,buttonManageConnections,290,SpringLayout.WEST,panel1);	

		// add panel to frame
		this.add(panel1);
		this.setVisible(true);

	}
	
	/** 
	  * Creates a standard menu that has 3 items on it
	  * Enhance by changing the set 3 items to an array or list
	*/	
	public void stdMenu(JMenuBar MenuBar1, String menuTitle, String[] mItems) {

	    JMenu menu1 =  new JMenu(menuTitle);
        MenuBar1.add(menu1);
        
        int i;
        int max = mItems.length;
        for (i = 0;i<max;++i) {
         if (!mItems[i].isEmpty() && mItems[i] != null) {
	 		JMenuItem m = new JMenuItem(mItems[i]);
	        menu1.add(m);
	        ReadListener m1 = new ReadListener();
	        m.addActionListener(m1);   
	     }       
        }
	};
	
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
        	   JOptionPane.showMessageDialog(null,mc, "Unknown concern",JOptionPane.PLAIN_MESSAGE);
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
        	   JOptionPane.showMessageDialog(null,currentConcernName, "Unknown concern",JOptionPane.PLAIN_MESSAGE);
           }
        }
		thisMenuBar.add(thisMenu);
	}			
	
    /**
    * Action events
    */
	private class ReadListener
	 implements ActionListener
	 {
		
		public void actionPerformed(ActionEvent e)
		{
			String commandName = e.getActionCommand();
			switch(commandName)
			{ 
			case "Run Test" : validationScript fixScript = new validationScript();
            				  fixScript.runScript(101);
            				  break;
            						
            default : JOptionPane.showMessageDialog(null,commandName, "Unknown Command",JOptionPane.PLAIN_MESSAGE);
	                         break;
	                       				        
			};
		}
	 }
	

		
	
}

