package Mod3TextFilePlayer;

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
	
	ChessPiece[][] pieces = new ChessPiece[9][9];
	boolean whiteTurn = true;
	
	
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
	
	//parsese moves from the text file.
	private void parsingMoves(String item) throws IOException {
				
		Matcher basicMoveMatcher = basicMove.matcher(item);

		if(item != null)
		{
			
			if(basicMoveMatcher.find())
			{
				System.out.println("WhiteTurn boolean is: " + whiteTurn);
				getMoveItemElements(basicMoveMatcher);	
				turnChecking(); //determines whoes turn it is
			}
		}
	}

	//checks turn-taking
	private void turnChecking() throws IOException {
		if(whiteTurn && color.equals("l")) //if its whites turn, and the piece is white
		{
			applyMovesToBoard();
			whiteTurn = false;
		}
		else if (!whiteTurn && color.equals("d")) //if its blacks turn, and the piece is black
		{
			applyMovesToBoard();
			whiteTurn = true;
		}
		else if (whiteTurn && color.equals("d")) // if its whites turn and the piece is black
		{
			System.out.println("MOVING OUT OF ORDER - Black piece attempted to move out of turn. It is currently WHITE's turn.");
			whiteTurn = true;
		}
		else if(!whiteTurn && color.equals("l")) {
			System.out.println("MOVING OUT OF ORDER - White piece attempted to move out of turn. It is currently BLACKS's turn.");
			whiteTurn = false;
		}
	}

	//checks that the string move item matches and applies to pieces on the board. 
	private void applyMovesToBoard() throws IOException {
		if (pieces[startX][startY]!= null )
		{
			if (pieces[startX][startY].getName().equals(piece) && (pieces[startX][startY].getColor().equals(color)))
			{
				System.out.println("MATCH - text file string matches " + pieces[startX][startY].getColorEnglish() + " " + pieces[startX][startY].toString() + " at " + startY +  ", " + startX );
				move(); //actually moves them.
			}
			else {
				System.out.println("NO MATCH ON PIECE NAME, COLOR AT THE INITIAL COORDINATES- text file string does not match a piece on the board. This is an attempt to illegally move a a piece.");
			} 	
		}
		else {
			System.out.println("NO PIECE PRESENT - text file has tried to access a piece that is not present at the file's designated coordinates. Cannot move.");
		}
	}

	//gets elements of move from the string that is read in.
	private void getMoveItemElements(Matcher basicMoveMatcher) {
		//getWhichPiece(basicMoveMatcher); //finds piece
		getWhichColor(basicMoveMatcher); //finds color + piece and its case
		getInitialPosition(basicMoveMatcher); //initial position (placeY, placeX) //run transfer chars //run backwards column syndrome 
		getFinalPosition(basicMoveMatcher); //final position (placeY, placeX) //run transfer chars //run backwards column syndrome 
		
	}
	
	//moves the piece.
	public void move() throws IOException
	{
		if (pieces[startX][startY] != null)
		{
			if (pieces[startX][startY].legalMove(pieces, startY, startX, endY, endX)) //checks legal moves
			{
				if (pieces[startX][startY].pathIsClear(pieces, startY, startX, endY, endX))//checks for barriers
				{
					pieces[endX][endY] =  pieces[startX][startY];
					pieces[startX][startY] = null; //set old spot to "| - |"
							
	
					Board board = new Board();
					board.updateBoard(pieces);				
				}
				else 
					System.out.println("MOVE BLOCKED - another piece is blocking your route.");
			}
			else
			{
				System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
				//answer to recursive. 
//				Board board = new Board();
//				board.inCaseOfIllegalMove(pieces); 
			}	
		}
		else
			System.out.println("ERROR - could not locate piece you designated to move.");
//			Board board = new Board();
//			board.inCaseOfIllegalMove(pieces); 
	}
	
		
	// finds origin color w/ help of hashmap(pieceColor)
	private void getWhichColor(Matcher basicMoveMatcher) {
		
		color = basicMoveMatcher.group("color");
		//piece = basicMoveMatcher.group("piece");
	
		if (color.equals("d"))
			piece = basicMoveMatcher.group("piece").toLowerCase();
		else if(color.equals("l"))
			piece = basicMoveMatcher.group("piece").toUpperCase();
		
		System.out.println(piece);
		System.out.println(color);
	}
		
	//finds initial position
	private void getInitialPosition(Matcher basicMoveMatcher) {
		starty = basicMoveMatcher.group("x");
		transferChars();
		
		startx = Integer.parseInt(basicMoveMatcher.group("y"));
		backwardsColumnNumberingSyndrome();
		
		System.out.println(startY);
		System.out.println(startX);
	}
	
	private void getFinalPosition(Matcher basicMoveMatcher) {
		endy = basicMoveMatcher.group("x2");
		transferChars2();
		
		
		endx = Integer.parseInt(basicMoveMatcher.group("y2"));
		backwardsColumnNumberingSyndrome2();
		
		System.out.println(endY);
		System.out.println(endX);
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
	public void backwardsColumnNumberingSyndrome()
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

	public void backwardsColumnNumberingSyndrome2()
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
		
}
