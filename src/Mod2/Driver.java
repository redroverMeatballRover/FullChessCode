package Mod2;

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
		Piece piece = new Piece();
		Board board = new Board();
		
		try {
			readIn.parseFromArray(args[0]);
		} 
		
		catch (IOException e) 
		{
			System.out.println("File error - file not found");
		}
	}
}
