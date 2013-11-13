package FileIO;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class ReadFile {
	
	public String readTextFile(String fileName) 
    {
	    String returnValue = "";
	    FileReader file;
	    String line = "";
	    List<String> movesList = new ArrayList<String>();
	    
	    try {
	        
	    	file = new FileReader(fileName);
	        BufferedReader reader = new BufferedReader(file);
	        
	        while ((line = reader.readLine()) != null) 
	        {
	        	returnValue += line;   	
	        }
	        
	        
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException("File not found");
	        } catch (IOException e) {
	            throw new RuntimeException("IO Error occured");
	        }
	      
	    return returnValue;
    }
	
	

}
