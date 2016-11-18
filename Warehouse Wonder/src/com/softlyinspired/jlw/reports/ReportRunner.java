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

package com.softlyinspired.jlw.reports;

import java.awt.event.WindowAdapter;
import java.io.IOException;
import java.net.URL;

import org.pentaho.reporting.engine.classic.core.ClassicEngineBoot;
import org.pentaho.reporting.engine.classic.core.MasterReport;
import org.pentaho.reporting.engine.classic.core.modules.gui.base.PreviewDialog;
import org.pentaho.reporting.libraries.resourceloader.Resource;
import org.pentaho.reporting.libraries.resourceloader.ResourceException;
import org.pentaho.reporting.libraries.resourceloader.ResourceManager;

import com.softlyinspired.jlw.JLWUtilities;


public class ReportRunner {
	
	public void execute(String reporturl){
		
        try {
	       ClassicEngineBoot.getInstance().start() ;
	    } catch (Exception e) {
	       System.out.println("error in classic boot");
	      }
            
		  try {
			 ResourceManager manager = new ResourceManager();
		     manager.registerDefaults();
		     Resource res = manager.createDirectly(new URL("file:" + reporturl),MasterReport.class);
		     MasterReport report = (MasterReport) res.getResource();  

			 final PreviewDialog preview = new PreviewDialog(report);	
			 //preview.setAlwaysOnTop(true);
		     preview.pack();
			 preview.toFront();
		     preview.setVisible(true);
		   } catch (ResourceException e){
		       System.out.println(e);
		   } catch (IOException e){
		        System.out.println(e);
		   }       
    
	}
}
