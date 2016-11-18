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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author Julie
 * This gets the system properties from default.properties file
 */
public class JLWProperties {
	   
	   public static String mongoHost;
	   public static int mongoPort;
	   public static String mongoDB;
	   public static String scriptOutput;
	   public void readall(){
		   
		try {
			//File propfile = new File("C:/Users/Julie/JLWFiles/default.properties");
		    File propfile = new File("default.properties");
			FileInputStream fileInput = new FileInputStream(propfile);
			Properties pro = new Properties();
			pro.load(fileInput);
			mongoHost = pro.getProperty("mongoHost","local");
            mongoPort = Integer.parseInt(pro.getProperty("mongoPort","00"));
            mongoDB = pro.getProperty("mongoDB","local");
            scriptOutput = pro.getProperty("scriptOutput","");
			fileInput.close();
		} catch (FileNotFoundException e) {
			System.out.println("Missing properties file");
		} catch (IOException e) {
			System.out.println("IO Exception");
		}	
	   }
}
