package mod6DetectCheckMate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;

/*
 * Reads and Parses the file
 */
public class ReadParse extends VariablesOfReadParseSuperClass {
	
	/*LOGIC MAP
	 * foreach string in filenames, create a piece out of it, when finished creating pieces, send to the board. 
	 * *shitty calling of the board via call to piece, which gets the array, and then calls board*
	 */
	
	//MAIN
	//-----------------------------------------------------------------------------------------------------//
	
	
	//FIX THIS FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  
	//FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS   FIX THIS 
	// FIX THIS  FIX THIS  FIX THIS  FIX THIS  FIX THIS  V FIX THIS  FIX THIS  FIX THIS  FIX THIS 
	//reads each line from the file to parse it
	public void parseFromArray(String fileName) throws IOException {
		readFromFile(fileName);
		hashIdentities(); 
        for(String item : fileLines)
        {
        	parsingMoves(item); //parse it, create a piece out of it, add piece to 2d array
        }  
        pieceClass.getPrototypePeiceArrayList(prototypePieceList); //sends objects to Piece Class to turn into an object       
        pieceClass.sendToBoard(); //send 2d array to board    
        
        //setOriginBoard.setPiecesInOrigin();
        //set-in-board
          // setinboard then calls user 
	}
	
	// reads from file, inserts each line into arraylist
	public void readFromFile(String fileName) throws IOException {
		String line;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		while((line = br.readLine()) != null) 
	    {
	       fileLines.add(line.toLowerCase()); 
	    }
	}
	/*FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS 
	 * FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS 
	 * FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS FIX THIS 
	 */
	
	
	//parses each line, adds the parsed elements to an object, and adds object to an arraylist. 
	private void parsingMoves(String item) {
		
		Matcher settingMatcher = settingPiece.matcher(item);
		if(item != null)
		{
			if(settingMatcher.find())
			{
				setWhichPiece(settingMatcher); //finds piece
				setWhichColor(settingMatcher); //finds color
				setInitialPosition(settingMatcher); //initial position (placeY, placeX)
				prototypePiece = new PrototypePiece(piece, color, placeY, placeX); //creates prototype piece object
				prototypePieceList.add(prototypePiece);	//adds object to array
			}
		}
	}
	//-----------------------------------------------------------------------------------------------------//
	
	
	//SUPPORT
	//-----------------------------------------------------------------------------------------------------//
	
	// finds origin piece w/ help of hashmap(pieceMatches)
	private void setWhichPiece(Matcher settingMatcher) {
		piece = pieceMatches.get(settingMatcher.group("piece"));
	}
	
	// finds origin color w/ help of hashmap(pieceColor)
	private void setWhichColor(Matcher settingMatcher) {
		color = colorMatches.get(settingMatcher.group("color"));
		pieceColor = settingMatcher.group("color");
	}
	
	//finds initial position
	private void setInitialPosition(Matcher settingMatcher) {
		placeY = settingMatcher.group("x");
		placeX = settingMatcher.group("y");
	}
	
	//english read-out
	private void setInOriginPosition(Matcher basicMoveMatcher) {
			 System.out.println("The " + color + " " + piece + " sets in " + placeY + placeX);
	}
	//-----------------------------------------------------------------------------------------------------//
}
