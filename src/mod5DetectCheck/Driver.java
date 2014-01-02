package mod5DetectCheck;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class Driver {

	/*
	DRIVER MAP
 	-readFromFile - get a update of successful read-parse / correct amount of pieces
	 -Piece - update of successful generation for pieces
	 -Board - update of board created - pieces set?
	 -SetPieces - update of pieces set?
	 */
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		
		ReadParse readIn = new ReadParse();
	
		try {
		
			readIn.parseFromArray(args[0]); //reads in the origin board and sets the pieces
		} 
		
		catch (IOException e) 
		{
			System.out.println("File error - file not found");
		}
	}
}