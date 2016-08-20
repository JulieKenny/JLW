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
