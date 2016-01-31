import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.ListSelectionModel;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class connectionEditFrame  extends JFrame 
{	
	  public static void OpenFrame(String[] args)
	  {
		  new connectionEditFrame();
	  }
	  
	private static JButton buttonSave;
	private static JButton buttonCancel;
	private static JButton buttonDelete;	
	private static JButton buttonNew;
	private static JComboBox connectList;
	
	final String frameTitle = new String("Connection Editor");
	
	private static dbConnection currentConnection;
	final JTextField driverText = new JTextField(20);
	final JTextField urlText = new JTextField(80);	
	final JTextField newName = new JTextField(10);
	final JTextField userText = new JTextField(20);
	final JTextField passwordText = new JTextField(20);
		
	final JLabel nameLabel = new JLabel("Name");	
	final JLabel driverLabel = new JLabel("Driver");	
	final JLabel urlLabel = new JLabel("Url ");	
	final JLabel userLabel = new JLabel("User");
	final JLabel passwordLabel = new JLabel("Password");
	//String newName = new String();

	
	private static boolean isNew = false;
	
    //public connectionEditFrame(String passedName, boolean isNew)
	public connectionEditFrame()
	{
	/**
	 * Create the frame and connection instance
	 */

	currentConnection = new dbConnection();	
	
	setBackground(Color.LIGHT_GRAY);
	this.setSize(1000,650);
	this.setLocationRelativeTo(null); /* Put in centre of screen */
	this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	JPanel cEditorPanel = new JPanel();

	// Set the panel and add items				

	/* 
	 * Show if editing or creating
	 */
   this.setTitle(frameTitle);		
   
   connectList = new JComboBox();  
   //connectList.addItem("New");
   //connectList.setEditable(true);
   final ArrayList<dbConnection> ac = allConnections();
   int i =0 ;
   for (i=0;i<ac.size();i++){
	   dbConnection cN = new dbConnection();
	   cN = ac.get(i);
	   connectList.addItem(cN.connectionName);
	if (i==0){
		   driverText.setText(cN.connectionDriver);
		   urlText.setText(cN.connectionURL);
		   userText.setText(cN.connectionUser);
		   passwordText.setText(cN.connectionPassword);
	   };
   }
   
   connectList.addItemListener(new ItemListener(){
	   public void itemStateChanged(ItemEvent arg0){

		   int i = connectList.getSelectedIndex();
		   currentConnection = ac.get(i);
		   String cDriver = currentConnection.connectionDriver;
		   String cURL = currentConnection.connectionURL;
		   String cUser = currentConnection.connectionUser;
		   String cPass = currentConnection.connectionPassword;
		   driverText.setText(cDriver);
		   urlText.setText(cURL);
		   userText.setText(cUser);
		   passwordText.setText(cPass);

		   revalidate();
	   }
	   
   });
   
   /* New button */
   buttonNew = new JButton("New");
   buttonNew.addMouseListener(new MouseAdapter(){
	   public void mouseClicked(MouseEvent arg0){
         isNew=true;
		 connectList.setVisible(false);
		 newName.setVisible(true);
         urlText.setText("");
         driverText.setText("");
         userText.setText("");
         passwordText.setText("");
         revalidate();
	   }
   });
   
	/* Cancel button */
	buttonCancel = new JButton("Close");
	buttonCancel.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent arg0) {
		      dispose();
		}
	});
	
	buttonDelete = new JButton("Delete");
	buttonDelete.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent arg0){
			if (!isNew)
			{ intendedAction("delete existing");
				try {
					   currentConnection.delete();					
					   showError("connection deleted");				
				    }catch (Exception e){
					   showError("error in delete process");
				     }
		    }else
		       {intendedAction("delete new");  }
			
		}
	});
		
	/*
	 * Save script details as new or changed
	 */
	buttonSave = new JButton("Save");
	buttonSave.addMouseListener(new MouseAdapter(){
		public void mouseClicked(MouseEvent arg0) {
			int errorCount = 0;

			if (isNew){
			   intendedAction("saving new");
			   if (newName.getText() == null || newName.getText().isEmpty() )
					{showError("You must enter a unique name");
				       errorCount++;  
					}
			   else {
				   currentConnection.connectionName = newName.getText();
			   };
			};
			if (driverText.getText() == null || (driverText.getText().isEmpty()))
			     {showError("Driver is required");
			       errorCount++;
			     }
			else {currentConnection.connectionDriver = driverText.getText();}
			
			if (urlText.getText() == null || (urlText.getText().isEmpty()))
			     {showError("URL is required");
			      errorCount++;
			     }
			else {currentConnection.connectionURL = urlText.getText();}
			
			if (userText.getText() == null || (userText.getText().isEmpty()))
		     {showError("user name is required");
		      errorCount++;
		     }
		    else {currentConnection.connectionUser = userText.getText();}
		
			if (passwordText.getText() == null || (passwordText.getText().isEmpty()))
		     {showError("password is required");
		      errorCount++;
		     }
		    else {currentConnection.connectionPassword = passwordText.getText();}
		
            if (errorCount == 0 ){
            	int r;
            	if (!isNew  ) 
            	     { r = currentConnection.updateExisting();
            		   JOptionPane.showMessageDialog(null, "Connection updated", "Connection",JOptionPane.PLAIN_MESSAGE);	
            	       }
            	else { r = currentConnection.create();}
            }
            isNew = false;
		}
	});
	
	// Set the panel and add items	
	SpringLayout connectionPaneLayout = new SpringLayout();		
	cEditorPanel.setLayout(connectionPaneLayout);		
	cEditorPanel.add(newName);
	newName.setVisible(false);
	cEditorPanel.add(buttonSave);
	cEditorPanel.add(buttonCancel);
	cEditorPanel.add(buttonDelete);		
	cEditorPanel.add(buttonNew);

	cEditorPanel.add(nameLabel);

	cEditorPanel.add(driverLabel);
	cEditorPanel.add(driverText);
	
	cEditorPanel.add(urlLabel);
	cEditorPanel.add(urlText);	
	
	cEditorPanel.add(userLabel);
	cEditorPanel.add(userText);
	cEditorPanel.add(passwordLabel);
	cEditorPanel.add(passwordText);
	
	cEditorPanel.add(connectList);
			
	/* Set layout */
	placeLayouts(connectionPaneLayout, cEditorPanel);
	
	// add panel to frame
	getContentPane().add(cEditorPanel);
	this.setVisible(true);		

}


private void placeLayouts(SpringLayout connectionPaneLayout, JPanel cEditorPanel)
  {
	int[] row = {5,35,60,95,130};
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,buttonNew,row[0],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,buttonNew,280,SpringLayout.WEST,cEditorPanel);	

	connectionPaneLayout.putConstraint(SpringLayout.NORTH,buttonSave,row[0],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,buttonSave,700,SpringLayout.WEST,cEditorPanel);	
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,buttonCancel,row[0],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,buttonCancel,110,SpringLayout.WEST,buttonSave);			
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,buttonDelete,row[0],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,buttonDelete,80,SpringLayout.WEST,buttonCancel);		
	
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,nameLabel,row[0],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,nameLabel,5,SpringLayout.WEST,cEditorPanel);		
	
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,connectList,row[0],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,connectList,85,SpringLayout.WEST,cEditorPanel);	
	
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,newName,row[0],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,newName,85,SpringLayout.WEST,cEditorPanel);	
	

	connectionPaneLayout.putConstraint(SpringLayout.NORTH,driverLabel,row[1],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,driverLabel,5,SpringLayout.WEST,cEditorPanel);		
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,driverText,row[1],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,driverText,80,SpringLayout.WEST,driverLabel);	
			
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,urlLabel,row[2],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,urlLabel,5,SpringLayout.WEST,cEditorPanel);		
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,urlText,row[2],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,urlText,80,SpringLayout.WEST,urlLabel);	

	connectionPaneLayout.putConstraint(SpringLayout.NORTH,userLabel,row[3],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,userLabel,5,SpringLayout.WEST,cEditorPanel);		
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,userText,row[3],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,userText,80,SpringLayout.WEST,cEditorPanel);		

	connectionPaneLayout.putConstraint(SpringLayout.NORTH,passwordLabel,row[4],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,passwordLabel,5,SpringLayout.WEST,cEditorPanel);		
	connectionPaneLayout.putConstraint(SpringLayout.NORTH,passwordText,row[4],SpringLayout.NORTH,cEditorPanel);
	connectionPaneLayout.putConstraint(SpringLayout.WEST,passwordText,80,SpringLayout.WEST,cEditorPanel);		
}

private ArrayList allConnections (){
	int numberFound =0 ;
	
	ArrayList<dbConnection> connectionList = new ArrayList<dbConnection>();
	try {
		MongoClient mongoClient = new MongoClient("localhost",27017);
	    DB db = mongoClient.getDB("local");
	    
	    DBCollection coll = db.getCollection("connections");
	    DBObject doc ;	
		
        try {	   
        	   DBCursor allConnections = coll.find();
        	   numberFound = allConnections.count();

		       for(int i =0; i < (numberFound); i++){
	        	 doc = allConnections.next();
	        	 dbConnection a = new dbConnection();
	        	 a.connectionName = doc.get("name").toString();
	        	 a.connectionDriver = doc.get("driver").toString();
	        	 a.connectionURL = doc.get("url").toString();
	        	 try { a.connectionPassword = doc.get("password").toString();
	        	   } catch (Exception e) { a.connectionPassword = "";};
	        	 try { a.connectionUser = doc.get("user").toString();
	        	   } catch (Exception e) { a.connectionUser = "";};
	        	 
	        	 connectionList.add(a);
        	 }       	
	    }
        catch (Exception e){
        	showError("There was nothing there");
        }
        mongoClient.close();
	}
	catch (Exception e) {
		showError("General Error in listing");
	}
	
	return connectionList;
}


/* Simplify messages */
private void intendedAction(String actionName){
	JOptionPane.showMessageDialog(null, actionName, "Holding status",JOptionPane.PLAIN_MESSAGE);	
};

private void showError(String errorMessage){
	JOptionPane.showMessageDialog(null, errorMessage, "Error",JOptionPane.PLAIN_MESSAGE);	
};	
}


