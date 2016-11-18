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

import javax.swing.JOptionPane;

public class JLWUtilities {
	
	private static void scriptMessage(String message, String title){
		JOptionPane.showMessageDialog(null, message, 
                title,
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