package com.softlyinspired.jlw;

import javax.swing.JOptionPane;

public class JLWUtilities {
	
	private static void scriptMessage(String message, String title){
		JOptionPane.showMessageDialog(null, message, 
                "Error",
                JOptionPane.PLAIN_MESSAGE); 		
	}
      
	public static void scriptErrorMessage(String message){

		scriptMessage(message,"Error");

	}

	public static void scriptErrorMessage(int messageN){

		scriptMessage(Integer.toString(messageN),"Error");
 
	}	
	
	public static void scriptInfoMessage(String message){

		scriptMessage(message,"Information");		
	}

	public static void scriptInfoMessage(int messageN){

		scriptMessage(Integer.toString(messageN),"Information");		

	}

}