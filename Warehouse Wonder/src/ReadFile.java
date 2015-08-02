
import java.io.*;
import java.text.NumberFormat;
import java.util.*;


public class ReadFile {
	
 public static ArrayList<String> main (ArrayList<String> args)
 {ArrayList <errorEvent> errorList = new ArrayList <errorEvent>();
   NumberFormat cf = NumberFormat.getCurrencyInstance();
  int lineNo = 0;
  errorEvent thisError ;
  String fileName = "D:\\Program Files\\Files\\movies.txt";
  BufferedReader in = getReader(fileName);
  ArrayList <String> errorStrings = new ArrayList <String>();
  Movie movie = readMovie(in);
 	 while (movie != null)
	  {++lineNo;
 	   String msg = Integer.toString(lineNo) + " : ";
 	   msg += Integer.toString(movie.year);
	   msg += ":" + movie.title;
	   msg += "(" + cf.format(movie.price) + ")";
	   msg += " / Error status " + movie.errorStatus;
	   if (movie.errorStatus != 0)
		   {
		   thisError = new errorEvent(lineNo,movie.errorStatus,"test");
		   errorList.add(thisError);
		   }
	   //System.out.println(msg);
	   movie = readMovie(in);
	  } 

 	Iterator <errorEvent> itr = errorList.iterator();
    while(itr.hasNext()){
    	String errorLine;
    	errorEvent ce = itr.next();
    	errorLine = "Line " +  Integer.toString(ce.lineNo) + " : ";
    	errorLine += "(" + ce.errorCode + ") ";
        errorLine += ce.errorDesc;
        errorStrings.add(errorLine);
    }

    return errorStrings;

 }

 private static BufferedReader getReader(String name)
 { BufferedReader in = null;
   try
   {
	File file = new File(name);
	in = new BufferedReader(new FileReader(file));
   }
   catch (FileNotFoundException e)
   {
	   System.out.println("The file doesn't exist");
	   System.exit(0);
   }
   return in;
 }
 
 private static Movie readMovie(BufferedReader in)
 {
   String title;
   int year;
   double price;
   String line = "";
   String[] data;
   int errorStatus;
   
   try
   { line = in.readLine(); }
   catch (IOException e)
   { System.out.println("I/O Error");
     System.exit(0);
   }

   if (line== null)
	   return null;
   else
   {
	   data = line.split("\t");
	   title = data[0];
	   year = Integer.parseInt(data[1]);
	   price = Double.parseDouble(data[2]);
	   if  (data.length < 3)
	     {errorStatus = 100;}
	   else 
		   if (data.length > 3)
	   			{errorStatus = 200;}
		   else errorStatus = 0;

	   };
	   
	   return new Movie(title,year,price,errorStatus);
   }

 private static class errorEvent
 {
	 public int lineNo;
	 public int errorCode;
	 public String errorDesc;
	 
	 public errorEvent(int lineNo, int errorCode, String errorDesc)
	 {
		 this.lineNo = lineNo;
		 this.errorCode = errorCode;
		 this.errorDesc = errorDesc;
		 switch (this.errorCode) {
		 case 0 : this.errorDesc = "No Error";
		           break;
		 case 100 : this.errorDesc = "Too few";
		            break;
		 case 200 : this.errorDesc = "Too many";
		             break;
		    default :this.errorDesc = "Something else";
		 }
			
	 }
 }
 
 private static class Movie
 {
	 public String title;
	 public int year;
	 public double price;
	 public int errorStatus;
	 
	 public Movie(String title, int year, double price, int errorStatus)
	 {
		 this.title = title;
		 this.year = year;
		 this.price = price;
		 this.errorStatus = errorStatus;
	 }
 }
}
