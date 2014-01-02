package mod4TurnTaking;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerIsTextFile {
	
	public ArrayList<String> textFileCommander = new ArrayList<String>();
	public final Pattern basicMove = Pattern.compile("(?<piece>q|k|b|p|n|r+)(?<color>l|d)(?<x>\\w)(?<y>\\d) ?(?<x2>\\w)(?<y2>\\d)(?<capture>[*])?");
	String fileName = "setMovesFile";
	
	int chessBoardInteger = 9;
	protected String piece = " ";
	protected String color = " ";
	
	public String starty = "";
	public int startx;
	
	public int startY;
	public int startX;
	
	public String endy = " ";
	public int endx;
	
	public int endY;
	public int endX;
	
	ChessPiece[][] pieces = new ChessPiece[chessBoardInteger][chessBoardInteger];
	boolean isWhiteTurn = true;
	
	
	// PIECE SETUP AND PARSING
	//-----------------------------------------------------------------------------------------------------------------//
	//returns current board
	public void getCurrentBoardAndMakeMove(ChessPiece[][] currentBoard) throws IOException {
		pieces = currentBoard;
		timeToMove(fileName);
	}
	
	//looks through list of moves, finds moves that are basic, and runs them.
	public void timeToMove(String fileName) throws IOException
	{
		String line;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		
		while((line = br.readLine()) != null) 
	    {
	       textFileCommander.add(line.toLowerCase()); 
	    }
        for(String item : textFileCommander)
        {
        	parsingMoves(item);
        }
	}
	
	//parses moves from the text file.
	private void parsingMoves(String item) throws IOException {
				
		Matcher basicMoveMatcher = basicMove.matcher(item);

		if(item != null)
		{
			if(basicMoveMatcher.find())
			{
				System.out.println("\n *----------------------------next move------------------------------*\n");
				System.out.println("WhiteTurn boolean is: " + isWhiteTurn);
				getMoveItemElements(basicMoveMatcher);	
				verifyPieceIsLegit();
			}
		}
	}

	//-----------------------------------------------------------------------------------------------------------------//
	
	
	//PIECE LEGAL SYSTEM
	//-----------------------------------------------------------------------------------------------------------------//
	//checks that the string move item matches and applies to pieces on the board. 

	private void verifyPieceIsLegit() throws IOException {
		
		//verify the square has a piece
		if (pieces[startX][startY]!= null )
		{
			// verify that the piece is who the board wants to move
			if (pieces[startX][startY].getName().equals(piece) && (pieces[startX][startY].getColor().equals(color)))
			{
				//System.out.println("MATCH - text file string matches " + pieces[startX][startY].getColorEnglish() + " " + pieces[startX][startY].toString() + " at " + startY +  ", " + startX );
				verificationSystem();
			}
			else {
				System.out.println("NO MATCH ON PIECE NAME, COLOR AT THE INITIAL COORDINATES- text file string does not match a piece on the board. This is an attempt to illegally move a a piece.");
			} 	
		}
		else {
			System.out.println("NO PIECE PRESENT - text file has tried to access a piece that is not present at the file's designated coordinates. Cannot move.");
		}
	}


	private void verificationSystem() throws IOException {
			if (pieces[startX][startY].legalMove(pieces, startY, startX, endY, endX)) //checks legal moves
			{
				verifyClearPath();
			}
			else
			{
				System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
			}
		}
	
	
	private void verifyClearPath() throws IOException {
			if (pieces[startX][startY].pathIsClear(pieces, startY, startX, endY, endX))//checks for barriers
			{
				verifyTurnIsCorrect();		
			}
			else 
			{
				System.out.println("MOVE BLOCKED - another piece is blocking your route.");
			}
		}
	
		
	private void verifyTurnIsCorrect() throws IOException {
			if (turnChecking()) //check for turn
			{
				move();	
			}
			else
			{
				System.out.println("ERROR - piece is moving out of turn." );
			}
		}
	
	
	private void move() throws IOException {
		pieces[endX][endY] =  pieces[startX][startY];
		pieces[startX][startY] = null; 
				
		Board board = new Board();
		board.updateBoard(pieces);
	}	
		//-----------------------------------------------------------------------------------------------------------------//
		
		//checks turn-taking
	
	private boolean turnChecking() {
	
	boolean isValidTurn = true;		
	if (color.equals("l"))
	{
//			isValidTurn = checkWhiteTurn();
		if (isWhiteTurn)
		{
			isWhiteTurn = false;
			isValidTurn = true;
		}
		else { 
			isWhiteTurn = false;
			isValidTurn = false;
		}	
	}	
	else if (color.equals("d")) //if color is dark
	{
//			isValidTurn = checkBlackTurn();
		if (!isWhiteTurn) 
		{
			isWhiteTurn = true;
			isValidTurn = true;
		}
		else { //and it is whites turn
			isWhiteTurn = true;
			isValidTurn = false;
		}
	}
	return isValidTurn;
}
	
	
//support classes //-------------------------------------------------------------------------------------------------//
//SUPPORT
//-----------------------------------------------------------------------------------------------------------------//
//gets elements of move from the string that is read in.
	private void getMoveItemElements(Matcher basicMoveMatcher) {
		getWhichColor(basicMoveMatcher); //finds color + piece and its case
		getInitialPosition(basicMoveMatcher); //initial position (placeY, placeX) //run transfer chars //run backwards column syndrome 
		getFinalPosition(basicMoveMatcher); //final position (placeY, placeX) //run transfer chars //run backwards column syndrome 
	}
		
	// finds origin color w/ help of hashmap(pieceColor)
	private void getWhichColor(Matcher basicMoveMatcher) {
		
		color = basicMoveMatcher.group("color");
		if (color.equals("d"))
			piece = basicMoveMatcher.group("piece").toLowerCase();
		else if(color.equals("l"))
			piece = basicMoveMatcher.group("piece").toUpperCase();
		
	//		System.out.println(piece);
	//		System.out.println(color);
	}
		
	//finds initial position
	private void getInitialPosition(Matcher basicMoveMatcher) {
		starty = basicMoveMatcher.group("x");
		transferChars();
		
		startx = Integer.parseInt(basicMoveMatcher.group("y"));
		backwardsColumnNumberingSyndrome();
		
	//		System.out.println(startY);
	//		System.out.println(startX);
	}
	
	private void getFinalPosition(Matcher basicMoveMatcher) {
		endy = basicMoveMatcher.group("x2");
		transferChars2();
		
		
		endx = Integer.parseInt(basicMoveMatcher.group("y2"));
		backwardsColumnNumberingSyndrome2();
	//		
	//		System.out.println(endY);
	//		System.out.println(endX);
	}
	
	// Y Coordinates: Translates letters into numbers // initial position
	public void transferChars()
	{
		switch (starty.charAt(0)) {
		case 'a':
			startY = 1;
			break;
		case 'b':
			startY = 2;
			break;
		case 'c':
			startY = 3;
			break;
		case 'd':
			startY = 4;
			break;
		case 'e':
			startY = 5;
			break;
		case 'f':
			startY = 6;
			break;
		case 'g':
			startY = 7;
			break;
		case 'h':
			startY = 8;
			break;
		default:
			break;
		}
	}
	//final position
	public void transferChars2()
	{
		switch (endy.charAt(0)) {
		case 'a':
			endY = 1;
			break;
		case 'b':
			endY = 2;
			break;
		case 'c':
			endY = 3;
			break;
		case 'd':
			endY = 4;
			break;
		case 'e':
			endY = 5;
			break;
		case 'f':
			endY = 6;
			break;
		case 'g':
			endY = 7;
			break;
		case 'h':
			endY = 8;
			break;
		default:
			break;
		}
	}
	
	//X Coordinates: deals with the backwards column numbering 
	private void backwardsColumnNumberingSyndrome()
	{
		switch (startx) {
		case 1:
			startX = 8;
			break;
		case 2:
			startX = 7;
			break;
		case 3:
			startX = 6;
			break;
		case 4:
			startX = 5;
			break;
		case 5:
			startX = 4;
			break;
		case 6:
			startX = 3;
			break;
		case 7:
			startX = 2;
			break;
		case 8:
			startX = 1;
			break;
		default:
			break;
		}
	}
	
	private void backwardsColumnNumberingSyndrome2()
{
	switch (endx) {
	case 1:
		endX = 8;
		break;
	case 2:
		endX = 7;
		break;
	case 3:
		endX = 6;
		break;
	case 4:
		endX = 5;
		break;
	case 5:
		endX = 4;
		break;
	case 6:
		endX = 3;
		break;
	case 7:
		endX = 2;
		break;
	case 8:
		endX = 1;
		break;
	default:
		break;
	}
}

//-----------------------------------------------------------------------------------------------------------------//
}

//if(isWhiteTurn && color.equals("l")) //if its whites turn, and the piece is white
//	{	
//		isWhiteTurn = false;
//		isValidTurn = true;
//	}
//	if (!isWhiteTurn && color.equals("d")) //if its blacks turn, and the piece is black
//	{
//		isWhiteTurn = true;
//		isValidTurn = true;
//	}
//	
//	if (isWhiteTurn && color.equals("d")) // if its whites turn and the piece is black
//	{
//		System.out.println("MOVING OUT OF ORDER - Black piece attempted to move out of turn. It is currently WHITE's turn.");
//		isWhiteTurn = true;
//		isValidTurn = false;
//	}
//	 if (!isWhiteTurn && color.equals("l")) {
//		System.out.println("MOVING OUT OF ORDER - White piece attempted to move out of turn. It is currently BLACKS's turn.");
//		isWhiteTurn = false;
//		isValidTurn = false;
//	}



//private boolean checkBlackTurn() {
//boolean isValidTurn;
//if (!isWhiteTurn) 
//{
//	isWhiteTurn = true;
//	isValidTurn = true;
//}
//else { //and it is whites turn
//	isWhiteTurn = true;
//	isValidTurn = false;
//}
//return isValidTurn;
//}
//
//private boolean checkWhiteTurn() {
//boolean isValidTurn;
//if (isWhiteTurn)
//{
//	isWhiteTurn = false;
//	isValidTurn = true;
//}
//else { 
//	isWhiteTurn = false;
//	isValidTurn = false;
//}
//return isValidTurn;
//}