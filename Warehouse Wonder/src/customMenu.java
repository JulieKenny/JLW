

public class customMenu {
	
	int a;
	int b;
	static int customMenuCounter;
	
   int result(){
	   
	   customMenuCounter++;
	   if (customMenuCounter== 0)
		    customMenuCounter++;
	   return customMenuCounter;
   }
	
}
