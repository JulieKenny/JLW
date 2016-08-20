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
