package mod6DetectCheckMate;

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
	
	private int throneX = 0;
	private int throneY = 0;

	String kingColorDesignation;
	String meatShieldColor;
	String enemyColor;
	
	boolean mapBlock = false;
	boolean meatShieldBlock = false;
	boolean threat = false;
	
	boolean northEscape = false;
	boolean southEscape = false;
	boolean eastEscape = false;
	boolean westEscape = false;
	boolean southEastEscape = false;
	boolean northEastEscape = false;
	boolean northWestEscape = false;
	boolean southWestEscape = false;
	boolean subEscape = false;
	
	int keepX;// = startX;
	int keepY; // = startY;

	int movementOptions = 8;
	int spotsOpenAfterChecks = 0;
	
	int blockCount = 0;
	int meatCount = 0;
	int threatCount = 0;
	
	boolean threat1 = false;
	boolean threat2 = false;
	boolean threat3 = false;
	boolean threat4 = false;
	boolean threat5 = false;
	boolean threat6 = false;
	boolean threat7 = false;
	boolean threat8 = false;
	
	int threatX;
	int threatY;
	int defenceX;
	int defenceY;
	
	int rookX;
	int rookY;
	int bishopX;
	int bishopY;
	int knightX;
	int knightY;
	int queenX;
	int queenY;
	
	// PIECE SETUP AND PARSING
	//-----------------------------------------------------------------------------------------------------------------//
	//returns current board and initiates the board movement
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
	
	//parses moves and begins verifying the pieces.
	private void parsingMoves(String item) throws IOException {
				
		Matcher basicMoveMatcher = basicMove.matcher(item);

		if(item != null)
		{
			if(basicMoveMatcher.find())
			{
				System.out.println("\n *----------------------------next move------------------------------*\n");
				//System.out.println("WhiteTurn boolean is: " + isWhiteTurn);
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
		if (pieces[startX][startY]!= null ) //verify the square has a piece
		{
			if (pieces[startX][startY].getName().equals(piece) && (pieces[startX][startY].getColor().equals(color))) // verify that the piece is who the board wants to move
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

	//checks that the piece will perform a legal move.
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
	
	//checks to make sure that the path the piece will travel is clear of obstructions
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
	
	//verifies that it is the pieces turn
	private void verifyTurnIsCorrect() throws IOException {
			if (turnChecking()) //check for turn
			{
				//CHECK FOR CHECK
				if (kingIsInCheck(pieces))
				{
					System.out.println("CHECK - " + kingColorDesignation + " King is in check!");
					move();
				}
				else 
				{
					//System.out.println("The " + kingColorDesignation + " king is safe");
					move(); //actually moves them.
				}	
			}
			else
			{
				System.out.println("ERROR - piece is moving out of turn." );
			}
		}
	//-----------------------------------------------------------------------------------------------------------------//
	
	
	
	//MOVE AND CHECK FOR THREATS//------------------------------------------------------------------------------------------------//
	private void move() throws IOException 
	{
		keepX = startX;
		keepY = startY;
		
		//moves.
		pieces[endX][endY] = pieces[startX][startY];
		pieces[startX][startY] = null;
		
		System.out.println("\n ---------------------- Move Made ---------------------- \n");	
		blockCount = 0;
		meatCount = 0;
		threatCount = 0;
		
		//checking check
		if (!kingIsInCheck(pieces)) //if king is not in check
		{
			Board board = new Board();
			board.updateBoard(pieces);
		}
		else 
		{
			System.out.println("CHOOSEN CHECK - You moved the King into check!");
			System.out.println("REPOSITIONING PIECE TO INITIAL POSITION - you cannot move king into or leave the king in check. ");
			
					
			//put piece back into the initial position
			pieces[keepX][keepY] = pieces[endX][endY]; 
			pieces[endX][endY] = null; // or 
			Board board = new Board();
			board.updateBoard(pieces);
			
			
			//re-verifies scenario 
			if (kingIsInCheck(pieces))
			{
				//if king is in check, check for checkmate
				if (!checkMate(pieces))
				{
					System.out.println("CHECKMATE = FALSE");
				}
				else {
					System.out.println("CHECKMATE = TRUE");	
				}
				//Board board1 = new Board();
				//board1.updateBoard(pieces);
			}
			pieceColorUponMisMovedCheckScenario();
		}	
	}	
	private void pieceColorUponMisMovedCheckScenario() {
		if (isWhiteTurn)
		{
			System.out.println("SWITCHING COLORS - Black previously moved the king into check. It is Black's turn. ");
			isWhiteTurn = false;
		}
		else 
		{
			System.out.println("SWITCHING COLORS - White's previously moved the king went check. It is White's turn.");
			isWhiteTurn = true;
		}
	}
	//-----------------------------------------------------------------------------------------------------------------//		
	
	
	
	
	// PRE AND POST KINGCHECK TOOLS //-----------------------------------------------------------------------------------//
	
	// Designate color of meatshields, enemy pieces, knights, and and kings that are being tracked
	private void designateKingColorAndMeatShieldColorOnNewTurn() {
		if (isWhiteTurn == false) //means that whiteTurn has passed isWhiteTurn and must complete the move for black to move. 
 		{
			kingColorDesignation = "l";
			meatShieldColor = "l";
			enemyColor = "d";
		}
		else {
			kingColorDesignation = "d";
			meatShieldColor = "d";
			enemyColor = "l";
		}
	}
	//locates the king on the board
	private void locateKingOnBoard() {
		//get king location
		for (int x = 0; x < 9; x++) 
		{
		    for (int y = 0; y < 9; y++)
		    {
		    	 if (pieces[x][y] != null) //add color check in there later
		    	 {
			    	if (pieces[x][y].getName().equals("K") && pieces[x][y].getColor().equals(kingColorDesignation) || pieces[x][y].getName().equals("k") && pieces[x][y].getColor().equals(kingColorDesignation)) //add color check in there later
			    	//if (pieces[x][y].getColor().equals(kingColorDesignation) && is a king) //add color check in there later
			    	 {
			            System.out.println("The " + kingColorDesignation + " king resides at y: " + y + ", x: " + x);
			            throneX = x;
			            throneY = y;
			    	 }
		    	 }
		    }
		}
	}
	//designates the turn-taking
	private boolean turnChecking() {
		
		boolean isValidTurn = true;		
		if (color.equals("l"))
		{
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
			if (!isWhiteTurn) 
			{
				isWhiteTurn = true;
				isValidTurn = true;
			}
			else { 
				isWhiteTurn = true;
				isValidTurn = false;
			}
		}
		return isValidTurn;
	}
	
	
	//CHECK FOR CHECK // --------------------------------------------------------------------------------------------------------------//
	public boolean kingIsInCheck(ChessPiece[][] pieces) //, int startY, int startX, int endY, int endX 
	{
		boolean kingIsInCheck = false;
		designateKingColorAndMeatShieldColorOnNewTurn(); //might need to add knights to this
		locateKingOnBoard();
		
		//TEST CHECKS SYSTEM --------------------------------------------------------------------------------//
		checkForEnemiesStraightUp(throneX, throneY); //CHECKING FOR PIECE UP
		checkForEnemiesStraightDown(throneX, throneY);	//CHECKING FOR PIECE DOWN
		checkForEnemiesStraightRight(throneX, throneY); //moving right -EAST
		checkForEnemiesStraightLeft(throneX, throneY); //moving left
		
		checkForenemiesDiagonalDownRight(throneX, throneY); //south-east
		checkForEnemiesDiagonalUpRight(throneX, throneY); // north-east
		checkForEnemiesDiagonalUpLeft(throneX, throneY); // north - west
		checkForEnemiesDiagonalDownLeft(throneX, throneY); // south-west
		allKnightChecks(throneX, throneY); //all knights
	
		//king vulnerability report
		System.out.println("\nMap Block Count: " + blockCount);
		System.out.println("Meatshield Block Count: " + meatCount);
		System.out.println("Threat Count: " + threatCount + "\n");
		spotsOpenAfterChecks = (movementOptions - (blockCount + meatCount + threatCount)); 
		
		if (northEscape || southEscape || eastEscape || westEscape || southEastEscape || northEastEscape || northWestEscape || southWestEscape )
		{
			kingIsInCheck = false;
			System.out.println("*---------------------------*\nKING SECURITY - Escape routes available.\n*---------------------------*");
			//switch and move again
		}		
		if(threatCount >= 1)
		{
			kingIsInCheck = true;
			System.out.println("*---------------------------*\nCHECK - King is in Check.\n*---------------------------------*");
			//trigger checkmate checks. 
		}
		else {
			System.out.println(kingColorDesignation + " King has " + spotsOpenAfterChecks + " spots to move to." );
		}
		
		//return all the escape booleans and decide where to go based on that.
		
		System.out.println("KING CHECK STATUS: " + kingIsInCheck);
		return kingIsInCheck;
	}

	
	// KING CHECKS
	//---------------------------------------------------------------------------------------------------------------------------------------//	
	//north
	private void checkForEnemiesStraightUp(int throneX, int throneY) 
	{	
		if (throneX - 1 <= 0)
		{
			blockCount++;
			//System.out.println("North is Map-Blocked.");
			mapBlock = true;
			northEscape = false;
		}
		for (int i = 1; i < throneX; i++)
		{
			if (pieces[throneX - i][throneY] != null)
			{	
				if (pieces[throneX - i][throneY].getColor().equals(meatShieldColor))
				{
					//System.out.println("North: King protected via meatshield by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
					if (i == 1)
						{
							meatShieldBlock = true;
							meatCount++;
							northEscape = false;
							break;
						}
						else {
							northEscape = true;
						}
					break;
				}
				else if (pieces[throneX - i][throneY].getColor().equals(enemyColor) && pieces[throneX - i][throneY].toString().equals("Rook") || pieces[throneX - i][throneY].getColor().equals(enemyColor) && pieces[throneX - i][throneY].toString().equals("Queen")) {
					//System.out.println("NORTH THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
					threat = true;
					threatCount++;
					threatX = throneX - i; 
					threatY = throneY;
					defenceX = startX - 1; 
					defenceY = startY;
					break;
				}
			}
			else 
			{
				//System.out.println("NORTH CLEAR - path " + (throneX - i) + ", " + throneY + " is clear.");
				northEscape = true;
			}
		}
	}
	
	//south
	private void checkForEnemiesStraightDown(int throneX, int throneY) //has different map-block check
	{
		int totalBoard = 8;
		int distance = (totalBoard - throneX); 
	
		if (distance == 0)
		{
			mapBlock = true;
			blockCount++;
			southEscape = false;
			//System.out.println("South is map-blocked.");
		}
		for(int i = 1; i < distance + 1; i++)
		{
			if (pieces[throneX + i][throneY] != null)
			{
				if (pieces[throneX + i][throneY].getColor().equals(meatShieldColor))
				{
					//System.out.println("SOUTH: King protected via meatshield by " + pieces[throneX + i][throneY].getColorEnglish() + " " + pieces[throneX + i][throneY].toString() + " at " + (throneX + i) + ", " + throneY);
					if (i == 1)
					{
						meatShieldBlock = true;
						meatCount++;
						southEscape = false;
						break;
					}
					else {
						southEscape = true;
					}
					break;
				}
				else if (pieces[throneX + i][throneY].getColor().equals(enemyColor) && pieces[throneX + i][throneY].toString().equals("Rook") || pieces[throneX + i][throneY].getColor().equals(enemyColor) && pieces[throneX + i][throneY].toString().equals("Queen")) {
					//System.out.println("SOUTH THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY].getColorEnglish() + " " + pieces[throneX + i][throneY].toString() + " at " + (throneX + i) + ", " + throneY);
					threat = true;
					threatCount++;
					threatX = throneX + i; 
					threatY = throneY;
					defenceX = startX + 1; 
					defenceY = startY;
					break;
				}
			}
			else {
				//System.out.println("SOUTH CLEAR - path " + (throneX + i) + ", " + throneY + " is clear.");
				southEscape = false;
			}
		}
	}	
	//left
	private void checkForEnemiesStraightLeft(int throneX, int throneY) {
		
		if (throneY == 1)
		{
			//System.out.println("West is Map-Blocked.");
			mapBlock = true;
			blockCount++;
			westEscape = false;
		}
		for (int i = 1; i < throneY; i++)
		{
			if (pieces[throneX][throneY - i] != null)
			{
				if (pieces[throneX][throneY - i].getColor().equals(meatShieldColor))
				{
					//System.out.println("King protected West via meatshield by " + pieces[throneX][throneY - i].getColorEnglish() + " " + pieces[throneX][throneY - i].toString() + " at " + throneX + ", " + (throneY - i));
					if (i == 1)
					{
						meatShieldBlock = true;
						meatCount++;
						westEscape = false;
						break;
					}
					else {
						westEscape = true;
						break;
					}
				}
				else if (pieces[throneX][throneY - i].getColor().equals(enemyColor) && pieces[throneX][throneY - i].toString().equals("Rook") || pieces[throneX][throneY - i].getColor().equals(enemyColor) && pieces[throneX][throneY - i].toString().equals("Queen")) 
				{
					//System.out.println("WEST THREAT FOUND - path intercepted by " + pieces[throneX][throneY - i].getColorEnglish() + " " + pieces[throneX][throneY - i].toString() + " at " + (throneY - i) + ", " + throneX);
					threat = true;
					threatCount++;
					threatX = throneX; 
					threatY = throneY - i;
					defenceX = startX; 
					defenceY = startY - 1 ;
					break;
				}
			}
			else 
			{
				//.out.println("WEST CLEAR - path " + throneX + ", " + (throneY - i) + " is clear.");
				westEscape = true;
			}
		}
	}
	//right - east
	private void checkForEnemiesStraightRight(int throneX, int throneY) 
	{
		int totalBoard = 8;
		int distance = (totalBoard - throneY);
		System.out.println("Whats the x,y? " + throneY + "," + throneX);
		
		if (distance <= 0)
		{
			mapBlock = true;
			blockCount++;
			eastEscape = false;
			//System.out.println("East is map-blocked.");
		}
		else {
			for(int i = 1; i < distance + 1; i++)
			{
				//System.out.println(i);
				//System.out.println("throne y,x: " +  throneY + ", " + throneX);
				if (throneY + i >= 9)
				{
					mapBlock = true;
					blockCount++;
					eastEscape = false;
					break;
				}
				else if (throneX == 9)
				{
					//System.out.println("THRONE X = 9?");
					eastEscape = false;
					break;
				}
				else if (pieces[throneX][throneY + i] != null)
				{
					if (pieces[throneX][throneY + i].getColor().equals(meatShieldColor))
					{
						//System.out.println("King protected East via meatshield by " + pieces[throneX][throneY + i].getColorEnglish() + " " + pieces[throneX][throneY + i].toString() + " at " + throneX + ", " + (throneY + i));
						if (i == 1)
						{
							meatShieldBlock = true;
							eastEscape = false;
							meatCount++;
							break;
							
						}
						else {
							eastEscape = true;
							break;
						}
					}
					else if (pieces[throneX][throneY + i].getColor().equals(enemyColor) && pieces[throneX][throneY + i].toString().equals("Rook") || pieces[throneX][throneY + i].getColor().equals(enemyColor) && pieces[throneX][throneY + i].toString().equals("Queen")) {
						//System.out.println("EAST THREAT FOUND - path intercepted by " + pieces[throneX][throneY + i].getColorEnglish() + " " + pieces[throneX][throneY + i].toString() + " at " + (throneY + i) + ", " + throneX);
						threat = true;
						threatX = throneX; 
						threatY = throneY + i;
						defenceX = startX; 
						defenceY = startY + 1;
						threatCount++;
						break;
					}
				}
				else 
				{
					//System.out.println("EAST CLEAR - path " + throneX + ", " + (throneY + i) + " is clear.");
					eastEscape = true;
				}
			}	
		}
	}
	//SOUTH-EAST
	private void checkForenemiesDiagonalDownRight(int throneX, int throneY) 
	{
		int endColumn = 8;
		int distance = endColumn - throneY;
		
		if(distance <= 0)
		{
			//System.out.println("SOUTH-EAST map block.");
			mapBlock = true;
			blockCount++;
			southEastEscape = false;
		}
		else if (distance == 1)
		{
			for (int j = 1; j < 2; j++)
			{
				if (pieces[throneX + 1][throneY + 1] != null)
				{
					if (pieces[throneX + 1][throneY + 1].getColor().equals(meatShieldColor)) // if its a friendly piece
					{
						//System.out.println("King protected SOUTH-EAST via meatshield by " + pieces[throneX + 1][throneY + 1].getColorEnglish() + " " + pieces[throneX + 1][throneY + 1].toString() + " at " + (throneX + 1) + ", " + (throneY + 1));
						meatShieldBlock = true;
						meatCount++;
						southEastEscape = false;
						break;
					}
					else if (pieces[throneX + 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY + 1].toString().equals("Bishop") || pieces[throneX + 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY + 1].toString().equals("Queen")) //else if enemy piece
					{
						//System.out.println("SOUTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX + 1][throneY + 1].getColorEnglish() + " " + pieces[throneX + 1][throneY + 1].toString() + " at " + (throneY + 1) + ", " + (throneX + 1));
						threat = true;
						threatCount++;
						break;
					}
				}
				else 
				{
					//System.out.println("SOUTH-EAST CLEAR - path " + (throneX + i) + ", " + (throneY + i) + " is clear.");	
					southEastEscape = true;
				}
			}
		}
		else 
		{	
			if (throneX == 8 && throneY > 0)
			{
				//System.out.println("SOUTH-EAST MAP BLOCK");
				mapBlock = true;
				blockCount++;
				southEastEscape = false;
			}
			else {
				for (int i = 1; i < distance + 1; i++)
				{
					if (throneX + i >= 9 || throneY + i >= 9)
					{
						//SPACE DID THE JOB
						southEastEscape = false;
						break;
					}
					else if (pieces[throneX + i][throneY + i] != null)
					{
						if (pieces[throneX + i][throneY + i].getColor().equals(meatShieldColor)) // if its a friendly piece
						{
							//System.out.println("King protected SOUTH-EAST via meatshield by " + pieces[throneX + i][throneY + i].getColorEnglish() + " " + pieces[throneX + i][throneY + i].toString() + " at " + (throneX + i) + ", " + (throneY + i));
							
							if (i == 1)
							{
								meatShieldBlock = true;
								meatCount++;
								southEastEscape = false;
								break;
							}
							else {
								southEastEscape = true;
								break;
							}
						}
						else if (pieces[throneX + i][throneY + i].getColor().equals(enemyColor) && pieces[throneX + i][throneY + i].toString().equals("Bishop") || pieces[throneX + i][throneY + i].getColor().equals(enemyColor) && pieces[throneX + i][throneY + i].toString().equals("Queen")) //else if enemy piece
						{
							//System.out.println("SOUTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY + i].getColorEnglish() + " " + pieces[throneX + i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX + i));
							threat = true;
							threatCount++;
							threatX = throneX + i; 
							threatY = throneY + i;
							defenceX = startX + 1; 
							defenceY = startY + 1;
							break;
						}
					}
					else 
					{
						//System.out.println("SOUTH-EAST CLEAR - path " + (throneX + i) + ", " + (throneY + i) + " is clear.");	
						southEastEscape = true;
					}
				}
			}
	}
}
	//NORTH-EAST
	private void checkForEnemiesDiagonalUpRight(int throneX, int throneY) {
		//check for 0 distance
		//check for 1 distance
		//		-- 
		//check for all other distance
		
		int endColumn = 8;
		int distance =  endColumn - throneY;
		
		if (distance <= 0 )
		{
			//System.out.println("NORTH EAST MAP BLOCK");
			mapBlock = true;
			blockCount++;
			northEastEscape = false;
		}
		else if (distance == 1)
		{
			for (int j = 1; j < 2; j++)
			{
				if (pieces[throneX - 1][throneY + 1] != null)
				{
					if (pieces[throneX - 1][throneY + 1].getColor().equals(meatShieldColor))
					{
						meatShieldBlock = true;
						meatCount++;
						northEastEscape = false;
						//System.out.println("King protected NORTH-EAST via meatshield by " + pieces[throneX - 1][throneY + 1].getColorEnglish() + " " + pieces[throneX - 1][throneY + 1].toString() + " at " + (throneX + 1) + ", " + (throneY - 1));
						//System.out.println("Continue Checking for threats.");
						break;
					}
					else if (pieces[throneX - 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY + 1].toString().equals("Bishop") || pieces[throneX - 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY + 1].toString().equals("Queen")) {
						//System.out.println("NORTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX - 1][throneY + 1].getColorEnglish() + " " + pieces[throneX - 1][throneY + 1].toString() + " at " + (throneY + 1) + ", " + (throneX - 1));
						threat = true;
						threatCount++;
						break;
					}
				}
				else 
				{
					northEastEscape = true;
					break;
					//System.out.println("NORTH-EAST CLEAR - path " + (throneX - i) + ", " + (throneY + i) + " is clear.");
				}	
			}
		}
		else
		{
			if (throneX == 1 && throneY > 0) //top row on box
			{
				//System.out.println("NORTH-EAST MAP BLOCK");
				mapBlock = true;
				blockCount++;
			}
			else {
				for (int i = 1; i < distance; i++)
				{
					if (throneX - i <= 0 || throneY + i > 8)
					{
						//space did the job.
						northEastEscape = false;
						break;
					}
					else if (pieces[throneX - i][throneY + i] != null)
					{
						if (pieces[throneX - i][throneY + i].getColor().equals(meatShieldColor))
						{
//							System.out.println("King protected NORTH-EAST via meatshield by " + pieces[throneX - i][throneY + i].getColorEnglish() + " " + pieces[throneX - i][throneY + i].toString() + " at " + (throneX + i) + ", " + (throneY - i));
//							System.out.println("Continue Checking for threats.");
							
							if (i == 1)
							{
								meatShieldBlock = true;
								meatCount++;
								northEastEscape = false;
								break;
							}
							else {
								northEastEscape = true;
								break;
							}
						}
						else if (pieces[throneX - i][throneY + i].getColor().equals(enemyColor) && pieces[throneX - i][throneY + i].toString().equals("Bishop") || pieces[throneX - i][throneY + i].getColor().equals(enemyColor) && pieces[throneX - i][throneY + i].toString().equals("Queen")) {
							//System.out.println("NORTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY + i].getColorEnglish() + " " + pieces[throneX - i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX - i));
							threat = true;
							threatCount++;
							threatX = throneX - i; 
							threatY = throneY + i;
							defenceX = startX - 1; 
							defenceY = startY + 1;
							break;
						}
					}
					else 
					{
						northEastEscape = true;
						//System.out.println("NORTH-EAST CLEAR - path " + (throneX - i) + ", " + (throneY + i) + " is clear.");
					}	
				}
			}
		}
	}
	//NORTH-WEST
	private void checkForEnemiesDiagonalUpLeft(int throneX, int throneY) {
		int endColumn = 1;
		int distance = throneY - endColumn;
		
		//no distance
		if (distance <= 0) //checks if piece is on the 1 y,x
		{
			//System.out.println("NORTH-WEST MAP BLOCK");
			mapBlock = true;
			blockCount++;
			northWestEscape = false;
		}
		//distance is only 1
		else if (distance == 1)
		{
			//top row on box
			if (throneX == 1 && throneY > 0)
			{
				//System.out.println("NORTH-WEST MAP BLOCK");
				mapBlock = true;
				blockCount++;
				northWestEscape = false;
			}
			else {
				for(int j = 1; j < 2; j++)
				{	
					if (pieces[throneX - 1][throneY - 1] != null)
					{
						if (pieces[throneX - 1][throneY - 1].getColor().equals(meatShieldColor))
						{
							meatShieldBlock = true;
							meatCount++;
							northWestEscape = false;
							//System.out.println("King protected NORTH-WEST via meatshield by " + pieces[throneX - 1][throneY - 1].getColorEnglish() + " " + pieces[throneX - 1][throneY - 1].toString() + " at " + (throneX - 1) + ", " + (throneY - 1));
							break;
						}
						else if (pieces[throneX - 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY - 1].toString().equals("Bishop") || pieces[throneX - 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY - 1].toString().equals("Queen")) {
							//System.out.println("NORTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX - 1][throneY - 1].getColorEnglish() + " " + pieces[throneX - 1][throneY - 1].toString() + " at " + (throneY - 1) + ", " + (throneX - 1));
							threat = true;
							threatCount++;
							break;
						}
					}
					else 
					{
						northWestEscape = true;
						break;
						//System.out.println("NORTH-WEST CLEAR - path " + (throneX - i) + ", " + (throneY - i) + " is clear.");
					}	
				}
			}
		}
		// more than one spaces
		else 
		{
			//top row on box
			if (throneX == 1 && throneY > 0)
			{
				//System.out.println("NORTH-WEST MAP BLOCK");
				mapBlock = true;
				blockCount++;
				northWestEscape = false;
			}
			else {
				for (int i = 1; i < distance + 1; i++)
				{
					if ((throneX - i) <= 0 || (throneY - i) <= 0) //checks if piece is on the 1 y,x
					{
						//space did the job.
						northWestEscape = false;
						break;
					}
					else if (pieces[throneX - i][throneY - i] != null)
					{
						if (pieces[throneX - i][throneY - i].getColor().equals(meatShieldColor))
						{
							//System.out.println("King protected NORTH-WEST via meatshield by " + pieces[throneX - i][throneY - i].getColorEnglish() + " " + pieces[throneX - i][throneY - i].toString() + " at " + (throneX - i) + ", " + (throneY - i));
							if (i == 1)
							{
								meatShieldBlock = true;
								meatCount++;
								northWestEscape = false;
								break;
							}
							else {
								northWestEscape = true;
								break;
							}
						}
						else if (pieces[throneX - i][throneY - i].getColor().equals(enemyColor) && pieces[throneX - i][throneY - i].toString().equals("Bishop") || pieces[throneX - i][throneY - i].getColor().equals(enemyColor) && pieces[throneX - i][throneY - i].toString().equals("Queen")) {
							//System.out.println("NORTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY - i].getColorEnglish() + " " + pieces[throneX - i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX - i));
							threat = true;
							threatCount++;
							threatX = throneX - i; 
							threatY = throneY - i;
							defenceX = startX - 1; 
							defenceY = startY - 1;
							break;
						}
					}
					else 
					{
						northWestEscape = true;
						//System.out.println("NORTH-WEST CLEAR - path " + (throneX - i) + ", " + (throneY - i) + " is clear.");
					}	
				}
			}
		}
	}
	//SOUTH-WEST //testing
	private void checkForEnemiesDiagonalDownLeft(int throneX, int throneY) 
	{
		int endColumn = 1;
		int distance = throneY - endColumn;
		
		if (distance <= 0)
		{
			//System.out.println("SOUTH-WEST MAP BLOCK");
			mapBlock = true;
			blockCount++;
			southWestEscape = false;
		}
		else if (throneX == 8)
		{
			mapBlock = true;
			blockCount++;
			southWestEscape = false;
		}
		else if (distance == 1)
		{
			for (int j = 1; j < 2; j++)
			{
				if (pieces[throneX + 1][throneY - 1] != null)
				{
					if (pieces[throneX + 1][throneY - 1].getColor().equals(meatShieldColor))
					{
						meatShieldBlock = true;
						meatCount++;
						southWestEscape = false;
						//System.out.println("King protected SOUTH-WEST via meatshield by " + pieces[throneX + 1][throneY - 1].getColorEnglish() + " " + pieces[throneX + 1][throneY - 1].toString() + " at " + (throneX + 1) + ", " + (throneY - 1));
						break;
					}
					else if (pieces[throneX + 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY - 1].toString().equals("Bishop") || pieces[throneX + 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY - 1].toString().equals("Queen"))
					{
						//System.out.println("SOUTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX + 1][throneY - 1].getColorEnglish() + " " + pieces[throneX + 1][throneY - 1].toString() + " at " + (throneY - 1) + ", " + (throneX + 1));
						threatCount++;
						threat = true;
						break;
					}
				}
				else 
				{
					southWestEscape = true;
					break;
					//System.out.println("SOUTH-WEST CLEAR - path " + (throneX + i) + ", " + (throneY - i) + " is clear.");
				}	
			}
		}
		else {
			//top row on box
			if (throneX == 8 && throneY > 0)
			{
				//System.out.println("SOUTH-WEST MAP BLOCK");
				mapBlock = true;
				blockCount++;
				southWestEscape = false;
			}
			else {
				for (int i = 1; i < distance + 1; i++)
				{
					if (throneX + i > 8 || throneY - i < 1)
					{
						//stuff
						southWestEscape = false;
						break;
					}
					else if (pieces[throneX + i][throneY - i] != null)
					{
						if (pieces[throneX + i][throneY - i].getColor().equals(meatShieldColor))
						{
							//System.out.println("King protected SOUTH-WEST via meatshield by " + pieces[throneX + i][throneY - i].getColorEnglish() + " " + pieces[throneX + i][throneY - i].toString() + " at " + (throneX + i) + ", " + (throneY - i));	
							if (i == 1)
							{
								meatShieldBlock = true;
								meatCount++;
								southWestEscape = false;
								break;
							}
							else {
								southWestEscape = true;
								break;
							}
						}
						else if (pieces[throneX + i][throneY - i].getColor().equals(enemyColor) && pieces[throneX + i][throneY - i].toString().equals("Bishop") || pieces[throneX + i][throneY - i].getColor().equals(enemyColor) && pieces[throneX + i][throneY - i].toString().equals("Queen"))
						{
							//System.out.println("SOUTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY - i].getColorEnglish() + " " + pieces[throneX + i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX + i));
							threatCount++;
							threat = true;
							threatX = throneX + i; 
							threatY = throneY - i;
							defenceX = startX + 1; 
							defenceY = startY - 1;
							
							southWestEscape = false;
							break;
						}
					}
					else 
					{
						southWestEscape = true;
						//System.out.println("SOUTH-WEST CLEAR - path " + (throneX + i) + ", " + (throneY - i) + " is clear.");
					}	
				}
			}
		}
	}

	// MAIN KNIGHT CHECK 
	private void allKnightChecks(int throneX, int throneY) {
		
		//checking up right
		int knightCheckUpRight1X = throneX - 2;
		int knightCheckUpRight1Y = throneY + 1;
		int knightCheckUpRight2X = throneX - 1;
		int knightCheckUpRight2Y = throneY + 2;
		
		//checking up left
		int knightCheckUpLeft3X = throneX - 2;
		int knightCheckUpLeft3Y = throneY - 1;
		int knightCheckUpLeft4X = throneX - 1;
		int knightCheckUpLeft4Y = throneY - 2;
		
		//checking up right
		int knightCheckDownRight1X = throneX + 2;
		int knightCheckDownRight1Y = throneY + 1;
		int knightCheckDownRight2X = throneX + 1;
		int knightCheckDownRight2Y = throneY + 2;
		
		//checking up left
		int knightCheckDownLeft3X = throneX + 2;
		int knightCheckDownLeft3Y = throneY - 1;
		int knightCheckDownLeft4X = throneX + 1;
		int knightCheckDownLeft4Y = throneY - 2;
		
		boolean knightCheck1 = false;
		boolean knightCheck2 = false;
		boolean knightCheck3 = false;
		boolean knightCheck4 = false;
		boolean knightCheck5 = false;
		boolean knightCheck6 = false;
		boolean knightCheck7 = false;
		boolean knightCheck8 = false;
		
		
		//checking up
		//-------------------------------------------------------------------------------------------------//
		//------------------------------------------------------------------------------------------------//
		//checking up right
		if (knightCheckUpLeft4X < 1 || knightCheckUpLeft4Y < 1)
		{
				
		}
		else if (pieces[knightCheckUpLeft4X][knightCheckUpLeft4Y] != null) 
		{
			knightCheckUpTop(knightCheckUpLeft4X, knightCheckUpLeft4Y);
			//System.out.println("knightCheck4 = " + knightCheck4);
		}
		if (knightCheckDownRight1X > 8 || knightCheckDownRight1Y > 8)
		{
			//System.out.println("PHANTOM ZONE REACHED");
			//kingIsInCheck = false;
		}
		else if (pieces[knightCheckDownRight1X][knightCheckDownRight1Y] != null)
		{
			moarKnights(knightCheckDownRight1X, knightCheckDownRight1Y);
			//System.out.println("knightCheck5 = " + knightCheck5);
		}
		if (knightCheckDownRight2X > 8 || knightCheckDownRight2Y > 8)
		{
			//System.out.println("PHANTOM ZONE");
			//kingIsInCheck = false;
		}
		else if (pieces[knightCheckDownRight2X][knightCheckDownRight2Y] != null) 
		{
			//knightCheck6 = knightCheckUpTop(kingIsInCheck, knightCheckDownRight2X, knightCheckDownRight2Y);
			//System.out.println("knightCheck6 = " + knightCheck6);
		}
		if (knightCheckDownLeft3X > 8 || knightCheckDownLeft3Y < 1)
		{
			//System.out.println("PHANTOM ZONE REACHED");
			//kingIsInCheck = false;
		}
		else if (pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y] != null) 
		{
			moKnights(knightCheckDownLeft3X, knightCheckDownLeft3Y);
			//System.out.println("knightCheck7 = " + knightCheck7);
		}
		if(knightCheckDownLeft4X > 8 || knightCheckDownLeft4Y < 1)
		{
			//System.out.println("PHANTOM ZONE REACHED");
			//kingIsInCheck = false;
		}
		else if (pieces[knightCheckDownLeft4X][knightCheckDownLeft4Y] != null) 
		{
			knightCheckUpTop(knightCheckDownLeft4X, knightCheckDownLeft4Y);
			//System.out.println("knightCheck8 = " + knightCheck8);
		}
		if (knightCheckUpRight1X < 1 || knightCheckUpRight1Y > 8)
		{
			//System.out.println("PHANTOM ZONE REACHED");
			//kingIsInCheck = false;
		}
		else if (pieces[knightCheckUpRight1X][knightCheckUpRight1Y] != null)
		{
			knightCheckUpRight(knightCheckUpRight1X, knightCheckUpRight1Y);	
			//System.out.println("knightCheck1 = " + knightCheck1);
		}
		if (knightCheckUpRight2X < 1 || knightCheckUpRight2Y > 8)
		{
			//System.out.println("PHANTOM ZONE REACHED");
			//kingIsInCheck = false;
		}
		else if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y] != null) 
		{
			knightCheckUpTop(knightCheckUpRight2X, knightCheckUpRight2Y);
			//System.out.println("knightCheck2 = " + knightCheck2);
		}
		if (knightCheckUpLeft3X < 1 || knightCheckUpLeft3Y < 1)
		{
			//System.out.println("PHANTOM ZONE REACHED");
			//kingIsInCheck = false;
		}
		else if (pieces[knightCheckUpLeft3X][knightCheckUpLeft3Y] != null) 
		{
			knightCheckUpTop(knightCheckUpLeft3X, knightCheckUpLeft3Y);
		//	System.out.println("knightCheck3 = " + knightCheck3);
		}
		else 
		{
			//System.out.println("Clear!");
		}
		
		
//		if (knightCheck1 == true || knightCheck2 == true || knightCheck3 == true || knightCheck4 == true || knightCheck5 == true || knightCheck6 == true || knightCheck7 == true || knightCheck8 == true)
//		{
//			kingIsInCheck = true;
//		}
	}
	//SUPPORT KNIGHT CHECKS
	private void moKnights(int knightCheckDownLeft3X, int knightCheckDownLeft3Y) 
	{
		if (pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y] != null )//&& pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString() != ("Knight"))
		{
			if(pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString().equals("Knight") && pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].getColor().equals(enemyColor)) //&& pieces[knightCheckDownLeft3Y][knightCheckDownLeft3Y].getColor().equals(enemyColor))
			{
				threat = true;
				threatCount++;
				threatX = knightCheckDownLeft3X;
				threatY = knightCheckDownLeft3Y;
				System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString() + " at : " + knightCheckDownLeft3Y + ", " + knightCheckDownLeft3X);	
			}
			else {
				//System.out.println("BLAH BALAB ASFADF");
				//kingIsInCheck = false;
			}	
		}
	}
	private void moarKnights(int knightCheckDownRight1X, int knightCheckDownRight1Y) {
		
		//checks knight attack up 2 right 1
		if (pieces[knightCheckDownRight1X][knightCheckDownRight1Y] != null ) //&& pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString() != ("Knight"))
		{
			if(pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString().equals("Knight") && pieces[knightCheckDownRight1X][knightCheckDownRight1Y].getColor().equals("d"))
			{
				threat = true;
				threatCount++;
				threatX = knightCheckDownRight1X;
				threatY = knightCheckDownRight1Y;
				System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString() + " at : " + knightCheckDownRight1Y + ", " + knightCheckDownRight1X);
			}
			else {
				//kingIsInCheck = false;
			}
		}
	}
	private void knightCheckUpTop(int knightCheckUpRight2X, int knightCheckUpRight2Y) 
	{
		if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y] != null)// && pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString() != ("Knight"))
		{
			if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString().equals("Knight") && pieces[knightCheckUpRight2X][knightCheckUpRight2Y].getColor().equals(enemyColor))
			{
				//kingIsInCheck = true;
				threat = true;
				threatCount++;
				threatX = knightCheckUpRight2X;
				threatY = knightCheckUpRight2Y;
				System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString() + " at : " + knightCheckUpRight2Y + ", " + knightCheckUpRight2X);
			}
			else 
			{
				//System.out.println("BLAH BLAH BLAH BLAH");
				//kingIsInCheck = false;
			}
		}
		//return kingIsInCheck;
	}
	private void knightCheckUpRight(int knightCheckUpRight1X, int knightCheckUpRight1Y) {	
		//checks knight attack up 2 right 1
		if (pieces[knightCheckUpRight1X][knightCheckUpRight1Y] != null)
		{
			if(pieces[knightCheckUpRight1X][knightCheckUpRight1Y].toString().equals("Knight") && pieces[knightCheckUpRight1X][knightCheckUpRight1Y].getColor().equals(enemyColor))
			{
				//kingIsInCheck = true;
				threat = true;
				threatCount++;
				threatX = knightCheckUpRight1X;
				threatY = knightCheckUpRight1Y;
				System.out.println("THREAT - path intercepted by " + pieces[knightCheckUpRight1X][knightCheckUpRight1Y].toString() + " at : " + knightCheckUpRight1Y + ", " + knightCheckUpRight1X);
			}
			else {
				//kingIsInCheck = false;
			}
		}
	}
	//---------------------------------------------------------------------------------------------------------------------------------------//	
		
	
	
	
	
	//CHECK FOR CHECKMATE// //-------------------------------------------------------------------------------------------------------------------//	
	public boolean checkMate(ChessPiece[][] pieces )
	{
		locateKingOnBoard();
		System.out.println("hello");
		boolean checkMate = false;
		int openOptions = 0;
		int optionsInCheck = 0;
		int travel = 1;
		
		//south
		if (southEscape)
		{
			System.out.println("THIS IS south BOOLEAN: " + southEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			southCheckMate(travel);
			if (threat1) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
		}
		//north
		if (northEscape)
		{
			System.out.println("THIS IS north BOOLEAN: " + northEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			northCheckMate(travel);
			System.out.println("The result of threat2 = " + threat2);
			
			if (threat2) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
			else {
				System.out.println("NOTHING in NORTH");
			}
		}
		//west
		if (westEscape)
		{
			System.out.println("THIS IS west BOOLEAN: " + westEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			westCheckMate(travel);
			if (threat4) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
		}
		//east
		if (eastEscape)
		{	System.out.println("\n\n*-------------------------------------*\n*-------------------------------------*");
			System.out.println("THIS IS east BOOLEAN: " + eastEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			eastCheckMate(travel);
			if (threat3) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
			System.out.println("\n*-------------------------------------*\n*-------------------------------------*\n\n");
		}
		// south east
		if (southEastEscape)
		{
			System.out.println("THIS IS southEast BOOLEAN: " + southEastEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			southEastCheckMate(travel);
			if (threat7) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
		}
		// north east
		if (northEastEscape)
		{
			System.out.println("THIS IS northEast BOOLEAN: " + northEastEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			northEastCheckMate(travel);
			if (threat5) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
		}
		//north west
		if (northWestEscape)
		{
			System.out.println("THIS IS northWest BOOLEAN: " + northWestEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			northWestCheckMate(travel);
			if (threat6) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
		}
		//south west
		if (southWestEscape)
		{
			System.out.println("THIS IS SouthWest BOOLEAN: " + southWestEscape);
			openOptions++;
			System.out.println("\n Open spaces: " + openOptions);
			southWestCheckMate(travel);
			if (threat8) {
				optionsInCheck++;
				System.out.println(optionsInCheck);
			}
		}
		
		//for every escape the initial king finds, add a point to openOptions.
		//for every "false"(in check) result of direction boolean, add 1 to options in check. 
		// if openOptions - optionsInCheck = 0, checkmate
		//if openOptions - options in check = 3 or 2 or 1, king is not in checkmate. 
		
		int safeOptions = (openOptions - optionsInCheck);
		System.out.println("\n*----------------------------------*\nOptions that are safe to move to: " + safeOptions);
		System.out.println("Spots that returned as free-to-move-to in original check: " + openOptions);
		System.out.println("Spots that, if moved to, would prove to be in check: " + optionsInCheck);
		System.out.println("*---------------------------------------*\n");
		
		
		if (safeOptions == 0)
		{
			 //activate team rescue
			if (teamRescue())
			{
				checkMate = false;
				System.out.println("Friendly pieces can act to protect king.");
			}
			else {
				checkMate = true;
				System.out.println("Friendly pieces cannot protect king.");
			}
				
		}
		else if (safeOptions > 0)
		{
			checkMate = false;
		}	
		return checkMate;
	}
	
	private void southCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		throneGhostX = startX + travel;
		throneGhostY = startY;
		
		if(throneGhostX > 8)
		{
			//block1 = true;
			//System.out.println("SOUTH BLOCKED.");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield1 = true; 
		}
		else 
		{
			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
			pieces[startX][startY] = null; 
			
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				//System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				threat1 = false;
			}
			else 
			{
				threat1 = true;
				//System.out.println("South Escape Route Compromised.");
			}
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; 
		}
	}
	
	private void northCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		throneGhostX = startX - travel;
		throneGhostY = startY;
				
		if(throneGhostX < 1)
		{
			//block2 = true;
			//System.out.println("NORTH BLOCKED");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield2 = true;
		}
		else {
			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
			pieces[startX][startY] = null; 
	
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				threat2 = false;
			}
			else {
				threat2 = true;
				System.out.println("North Escape Route Compromised.");
			}
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; // or 
		}
	}
	
	private void eastCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		
		throneGhostX = startX;
		throneGhostY = startY + travel;
			
		System.out.println("the starty,startx in eastCheckmate: " + throneGhostY + ", " + throneGhostX);
		
		if(throneGhostY > 8)
		{
			//block3 = true;
			//System.out.println("EAST BLOCKED");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield3 = true;
		}
		else {
			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
			pieces[startX][startY] = null; 
			
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				//System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				threat3 = false;
			}
			else {
				threat3 = true;
				//System.out.println("East Escape Route Compromise.");
			}
				
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; // or 
		}	
	}
	
	private void westCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		throneGhostX = startX;
		throneGhostY = startY - travel;
			
		if(throneGhostY < 1)
		{
			//block4 = true;
			//System.out.println("WEST BLOCKED");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield4 = true;
		}
		else {
			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
			pieces[startX][startY] = null; 
			
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				//System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				threat4 = false;
			}
			else {
				threat4 = true;
				//System.out.println("West Escape Route Compromise.");
			}
				
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; // or 
		}	
	}
	
	private void northEastCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		throneGhostX = startX - travel;
		throneGhostY = startY + travel;
		
		if(throneGhostX < 1 || throneGhostY > 8)
		{
			//block5 = false;
			//System.out.println("NORTH EAST BLOCKED");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield5 = false;
		}
		else {
			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
			pieces[startX][startY] = null; 
			
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				//System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				threat5 = false;
			}
			else {
				threat5 = true;
				//System.out.println("north east Escape Route Compromised.");
			}
				
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; // or 
		}
	}
	
	private void northWestCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		throneGhostX = startX - travel;
		throneGhostY = startY - travel;
		
		if(throneGhostX < 1 || throneGhostY < 1)
		{
			//block6 = true;
			//System.out.println("NORTH WEST BLOCKED");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield6 = true;
		}
		else {
			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
			pieces[startX][startY] = null; 
			
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				//System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				threat6 = false;
			}
			else {
				threat6 = true;
				//System.out.println("NorthWest Escape Route Blocked.");
			}
				
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; // or 
		}
	}

	private void southEastCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		throneGhostX = startX + travel;
		throneGhostY = startY + travel;
		
		if(throneGhostX > 8 || throneGhostY > 8)
		{
			//block7 = true;
			//System.out.println("SOUTH EAST BLOCKED");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield7 = true;
		}
		else {
			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
			pieces[startX][startY] = null; 
			
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				//System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				northWestEscape = false;
				threat7 = false;
			}
			else {
				threat7 = true;
				//System.out.println("southEast Escape Route Blocked.");
			}
				
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; // or 
		}
	}

	private void southWestCheckMate(int travel) {
		
		int throneGhostX;
		int throneGhostY;
			
		throneGhostX = startX + travel;
		throneGhostY = startY - travel;
			
		if(throneGhostX > 8 || throneGhostY < 1)
		{
			//block8 = true;
			//System.out.println("SOUTH WEST BLOCKED");
		}
		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
		{
			//System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
			//meatShield8 = true;
		}
		else {
			if(!runCheckMateChecks(throneGhostX, throneGhostY))
			{
				//System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
				southWestEscape = false;
				threat8 = false;
			}
			else {
				threat8 = true;
				//System.out.println("southWest Escape Route Blocked.");
			}
				
			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
			pieces[throneGhostX][throneGhostY] = null; // or 
		}
	}

	
	
	
	//CHECK POTENTIAL ESCAPE ROUTES FOR KING FOR THREATS/CHECK ------------------------------------------------------------------------------------------------------------------------//	
		public boolean runCheckMateChecks(int throneX, int throneY)
	{
		boolean check = false;
		designateKingColorAndMeatShieldColorOnNewTurn();
		
		blockCount = 0;
		meatCount = 0;
		threatCount = 0;
		
		//TEST CHECKS SYSTEM --------------------------------------------------------------------------------//
		northPositionSetKingThreatCheck(throneX, throneY); //CHECKING FOR PIECE UP
		southPositionSetKingThreatCheck(throneX, throneY);	//CHECKING FOR PIECE DOWN
		eastPositionSetKingThreatCheck(throneX, throneY); //moving right
		westPositionSetKingThreatCheck(throneX, throneY); //moving left
		
		southEastPositionSetKingThreatCheck(throneX, throneY); //south-east
		northEastPositionSetKingThreatCheck(throneX, throneY); // north-east //THIS
		northWestPositionSetKingThreatCheck(throneX, throneY); // north - west
		southWestPositionSetKingThreatCheck(throneX, throneY); // south-west
		allKnightSetKingChecks(throneX, throneY); //all knights
	
		//king vulnerability report
//		System.out.println("\nMap Block Count: " + blockCount);
//		System.out.println("Meatshield Block Count: " + meatCount);
//		System.out.println("Threat Count: " + threatCount + "\n");
		spotsOpenAfterChecks = (movementOptions - (blockCount + meatCount + threatCount)); 
		
		if (threatCount >= 1)
		{
			check = true;
//			System.out.println("The GHOST-KING is in CHECK.");
		}
		else if (threatCount == 0)
		{
			check = false; 
//			System.out.println("Safe Location.");
		}
		return check;
	}
		//north
		private void northPositionSetKingThreatCheck(int throneX, int throneY) 
		{	
			if (throneX - 1 <= 0)
			{
				blockCount++;
				mapBlock = true;
			}
			for (int i = 1; i < throneX; i++)
			{
				if (pieces[throneX - i][throneY] != null)
				{	
					if (pieces[throneX - i][throneY].getColor().equals(meatShieldColor))
					{
						//System.out.println("North: King protected via meatshield by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
						if (i == 1)
							{
								meatShieldBlock = true;
								meatCount++;
								break;
							}
							else {
								
							}
						break;
					}
					else if (pieces[throneX - i][throneY].getColor().equals(enemyColor) && pieces[throneX - i][throneY].toString().equals("Rook") || pieces[throneX - i][throneY].getColor().equals(enemyColor) && pieces[throneX - i][throneY].toString().equals("Queen")) {
						System.out.println("NORTH THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
						threatX = throneX - i;
						threatY = throneY;
						
						defenceX = startX - 1;
						defenceY = startY;
						
						threat = true;
						threatCount++;
						break;
					}
				}
				else 
				{
					//System.out.println("NORTH CLEAR - path " + (throneX - i) + ", " + throneY + " is clear.");
					//northEscape = true;
				}
			}
		}
		//south
		private void southPositionSetKingThreatCheck(int throneX, int throneY) //has different map-block check
		{
			int totalBoard = 8;
			int distance = (totalBoard - throneX); 
		
			if (distance == 0)
			{
				mapBlock = true;
				blockCount++;
				//southEscape = false;
				//System.out.println("South is map-blocked.");
			}
			for(int i = 1; i < distance + 1; i++)
			{
				if (pieces[throneX + i][throneY] != null)
				{
					if (pieces[throneX + i][throneY].getColor().equals(meatShieldColor))
					{
						//System.out.println("SOUTH: King protected via meatshield by " + pieces[throneX + i][throneY].getColorEnglish() + " " + pieces[throneX + i][throneY].toString() + " at " + (throneX + i) + ", " + throneY);
						if (i == 1)
						{
							meatShieldBlock = true;
							meatCount++;
							//southEscape = false;
							break;
						}
						else {
							southEscape = true;
						}
						break;
					}
					else if (pieces[throneX + i][throneY].getColor().equals(enemyColor) && pieces[throneX + i][throneY].toString().equals("Rook") || pieces[throneX + i][throneY].getColor().equals(enemyColor) && pieces[throneX + i][throneY].toString().equals("Queen")) {
						//System.out.println("SOUTH THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY].getColorEnglish() + " " + pieces[throneX + i][throneY].toString() + " at " + (throneX + i) + ", " + throneY);
						threatX = throneX + i;
						threatY = throneY;
						
						defenceX = startX + 1;
						defenceY = startY;
						
						threat = true;
						threatCount++;
						break;
					}
				}
				else {
					//System.out.println("SOUTH CLEAR - path " + (throneX + i) + ", " + throneY + " is clear.");
					//southEscape = false;
				}
			}
		}	
		//left
		private void westPositionSetKingThreatCheck(int throneX, int throneY) {
			
			if (throneY == 1)
			{
				//System.out.println("West is Map-Blocked.");
				mapBlock = true;
				blockCount++;
				//westEscape = false;
			}
			for (int i = 1; i < throneY; i++)
			{
				if (pieces[throneX][throneY - i] != null)
				{
					if (pieces[throneX][throneY - i].getColor().equals(meatShieldColor))
					{
						//System.out.println("King protected West via meatshield by " + pieces[throneX][throneY - i].getColorEnglish() + " " + pieces[throneX][throneY - i].toString() + " at " + throneX + ", " + (throneY - i));
						if (i == 1)
						{
							meatShieldBlock = true;
							meatCount++;
							//westEscape = false;
							break;
						}
						else {
							//westEscape = true;
							break;
						}
					}
					else if (pieces[throneX][throneY - i].getColor().equals(enemyColor) && pieces[throneX][throneY - i].toString().equals("Rook") || pieces[throneX][throneY - i].getColor().equals(enemyColor) && pieces[throneX][throneY - i].toString().equals("Queen")) 
					{
						//System.out.println("WEST THREAT FOUND - path intercepted by " + pieces[throneX][throneY - i].getColorEnglish() + " " + pieces[throneX][throneY - i].toString() + " at " + (throneY - i) + ", " + throneX);
						threatX = throneX;
						threatY = throneY - i;
						
						defenceX = startX;
						defenceY = startY - 1;
						
						threat = true;
						threatCount++;
						break;
					}
				}
				else 
				{
					//.out.println("WEST CLEAR - path " + throneX + ", " + (throneY - i) + " is clear.");
					//westEscape = true;
				}
			}
		}
		//right - east
		private void eastPositionSetKingThreatCheck(int throneX, int throneY) 
		{
			int totalBoard = 8;
			int distance = (totalBoard - throneY);
			System.out.println("Whats the x,y? " + throneY + "," + throneX);
			
			if (distance <= 0)
			{
				mapBlock = true;
				blockCount++;
			}
			else {
				for(int i = 1; i < distance + 1; i++)
				{
					//System.out.println(i);
					//System.out.println("throne y,x: " +  throneY + ", " + throneX);
					if (throneY + i >= 9)
					{
						mapBlock = true;
						blockCount++;
					//	eastEscape = false;
						break;
					}
					else if (throneX == 9)
					{
						//System.out.println("THRONE X = 9?");
						//eastEscape = false;
						break;
					}
					else if (pieces[throneX][throneY + i] != null)
					{
						if (pieces[throneX][throneY + i].getColor().equals(meatShieldColor))
						{
							//System.out.println("King protected East via meatshield by " + pieces[throneX][throneY + i].getColorEnglish() + " " + pieces[throneX][throneY + i].toString() + " at " + throneX + ", " + (throneY + i));
							if (i == 1)
							{
								meatShieldBlock = true;
								meatCount++;
								break;
								
							}
							else {
								//eastEscape = true;
								break;
							}
						}
						else if (pieces[throneX][throneY + i].getColor().equals(enemyColor) && pieces[throneX][throneY + i].toString().equals("Rook") || pieces[throneX][throneY + i].getColor().equals(enemyColor) && pieces[throneX][throneY + i].toString().equals("Queen")) {
							//System.out.println("EAST THREAT FOUND - path intercepted by " + pieces[throneX][throneY + i].getColorEnglish() + " " + pieces[throneX][throneY + i].toString() + " at " + (throneY + i) + ", " + throneX);
							threatX = throneX;
							threatY = throneY + i;
							
							defenceX = throneX;
							defenceY = throneY + 1;
							
							threat = true;
							threatCount++;
							break;
						}
					}
					else 
					{
						//System.out.println("EAST CLEAR - path " + throneX + ", " + (throneY + i) + " is clear.");
						//eastEscape = true;
					}
				}	
			}
		}
		//SOUTH-EAST
		private void southEastPositionSetKingThreatCheck(int throneX, int throneY) 
		{
			int endColumn = 8;
			int distance = endColumn - throneY;
			
			if(distance <= 0)
			{
				//System.out.println("SOUTH-EAST map block.");
				mapBlock = true;
				blockCount++;
				//southEastEscape = false;
			}
			else if (distance == 1)
			{
				for (int j = 1; j < 2; j++)
				{
					if (pieces[throneX + 1][throneY + 1] != null)
					{
						if (pieces[throneX + 1][throneY + 1].getColor().equals(meatShieldColor)) // if its a friendly piece
						{
							//System.out.println("King protected SOUTH-EAST via meatshield by " + pieces[throneX + 1][throneY + 1].getColorEnglish() + " " + pieces[throneX + 1][throneY + 1].toString() + " at " + (throneX + 1) + ", " + (throneY + 1));
							meatShieldBlock = true;
							meatCount++;
							//southEastEscape = false;
							break;
						}
						else if (pieces[throneX + 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY + 1].toString().equals("Bishop") || pieces[throneX + 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY + 1].toString().equals("Queen")) //else if enemy piece
						{
							//System.out.println("SOUTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX + 1][throneY + 1].getColorEnglish() + " " + pieces[throneX + 1][throneY + 1].toString() + " at " + (throneY + 1) + ", " + (throneX + 1));
							threatX = throneX + 1;
							threatY = throneY + 1;
							
							//MIGHT BE AN ISSUE?
							
							threat = true;
							threatCount++;
							break;
						}
					}
					else 
					{
						//System.out.println("SOUTH-EAST CLEAR - path " + (throneX + i) + ", " + (throneY + i) + " is clear.");	
						//southEastEscape = true;
					}
				}
			}
			else 
			{	
				if (throneX == 8 && throneY > 0)
				{
					//System.out.println("SOUTH-EAST MAP BLOCK");
					mapBlock = true;
					blockCount++;
					//southEastEscape = false;
				}
				else {
					for (int i = 1; i < distance + 1; i++)
					{
						if (throneX + i >= 9 || throneY + i >= 9)
						{
							//SPACE DID THE JOB
						//	southEastEscape = false;
							break;
						}
						else if (pieces[throneX + i][throneY + i] != null)
						{
							if (pieces[throneX + i][throneY + i].getColor().equals(meatShieldColor)) // if its a friendly piece
							{
								//System.out.println("King protected SOUTH-EAST via meatshield by " + pieces[throneX + i][throneY + i].getColorEnglish() + " " + pieces[throneX + i][throneY + i].toString() + " at " + (throneX + i) + ", " + (throneY + i));
								
								if (i == 1)
								{
									meatShieldBlock = true;
									meatCount++;
							//		southEastEscape = false;
									break;
								}
								else {
								//	southEastEscape = true;
									break;
								}
							}
							else if (pieces[throneX + i][throneY + i].getColor().equals(enemyColor) && pieces[throneX + i][throneY + i].toString().equals("Bishop") || pieces[throneX + i][throneY + i].getColor().equals(enemyColor) && pieces[throneX + i][throneY + i].toString().equals("Queen")) //else if enemy piece
							{
								//System.out.println("SOUTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY + i].getColorEnglish() + " " + pieces[throneX + i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX + i));
								threatX = throneX + i;
								threatY = throneY + i;
								
								defenceX = throneX + 1;
								defenceY = throneY + 1;
								
								threat = true;
								threatCount++;
								break;
							}
						}
						else 
						{
							//System.out.println("SOUTH-EAST CLEAR - path " + (throneX + i) + ", " + (throneY + i) + " is clear.");	
							//southEastEscape = true;
						}
					}
				}
		}
	}
		//NORTH-EAST
		private void northEastPositionSetKingThreatCheck(int throneX, int throneY) {
			//check for 0 distance
			//check for 1 distance
			//		-- 
			//check for all other distance
			
			int endColumn = 8;
			int distance =  endColumn - throneY;
			
			if (distance <= 0 )
			{
				//System.out.println("NORTH EAST MAP BLOCK");
				mapBlock = true;
				blockCount++;
				//northEastEscape = false;
			}
			else if (distance == 1)
			{
				for (int j = 1; j < 2; j++)
				{
					if (pieces[throneX - 1][throneY + 1] != null)
					{
						if (pieces[throneX - 1][throneY + 1].getColor().equals(meatShieldColor))
						{
							meatShieldBlock = true;
							meatCount++;
							//northEastEscape = false;
							//System.out.println("King protected NORTH-EAST via meatshield by " + pieces[throneX - 1][throneY + 1].getColorEnglish() + " " + pieces[throneX - 1][throneY + 1].toString() + " at " + (throneX + 1) + ", " + (throneY - 1));
							//System.out.println("Continue Checking for threats.");
							break;
						}
						else if (pieces[throneX - 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY + 1].toString().equals("Bishop") || pieces[throneX - 1][throneY + 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY + 1].toString().equals("Queen")) {
							//System.out.println("NORTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX - 1][throneY + 1].getColorEnglish() + " " + pieces[throneX - 1][throneY + 1].toString() + " at " + (throneY + 1) + ", " + (throneX - 1));
							threatX = throneX - 1;
							threatY = throneY + 1;
							threat = true;
							threatCount++;
							break;
						}
					}
					else 
					{
						//northEastEscape = true;
						break;
						//System.out.println("NORTH-EAST CLEAR - path " + (throneX - i) + ", " + (throneY + i) + " is clear.");
					}	
				}
			}
			else
			{
				if (throneX == 1 && throneY > 0) //top row on box
				{
					//System.out.println("NORTH-EAST MAP BLOCK");
					mapBlock = true;
					blockCount++;
				}
				else {
					for (int i = 1; i < distance; i++)
					{
						if (throneX - i <= 0 || throneY + i > 8)
						{
							//space did the job.
							//northEastEscape = false;
							break;
						}
						else if (pieces[throneX - i][throneY + i] != null)
						{
							if (pieces[throneX - i][throneY + i].getColor().equals(meatShieldColor))
							{
//								System.out.println("King protected NORTH-EAST via meatshield by " + pieces[throneX - i][throneY + i].getColorEnglish() + " " + pieces[throneX - i][throneY + i].toString() + " at " + (throneX + i) + ", " + (throneY - i));
//								System.out.println("Continue Checking for threats.");
								
								if (i == 1)
								{
									meatShieldBlock = true;
									meatCount++;
									//northEastEscape = false;
									break;
								}
								else {
									//northEastEscape = true;
									break;
								}
							}
							else if (pieces[throneX - i][throneY + i].getColor().equals(enemyColor) && pieces[throneX - i][throneY + i].toString().equals("Bishop") || pieces[throneX - i][throneY + i].getColor().equals(enemyColor) && pieces[throneX - i][throneY + i].toString().equals("Queen")) {
								//System.out.println("NORTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY + i].getColorEnglish() + " " + pieces[throneX - i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX - i));
								threatX = throneX - i;
								threatY = throneY + i;
								
								defenceX = throneX - 1;
								defenceY = throneY + 1;
								
								threat = true;
								threatCount++;
								break;
							}
						}
						else 
						{
							//northEastEscape = true;
							//System.out.println("NORTH-EAST CLEAR - path " + (throneX - i) + ", " + (throneY + i) + " is clear.");
						}	
					}
				}
			}
		}
		//NORTH-WEST
		private void northWestPositionSetKingThreatCheck(int throneX, int throneY) {
			int endColumn = 1;
			int distance = throneY - endColumn;
			
			//no distance
			if (distance <= 0) //checks if piece is on the 1 y,x
			{
				//System.out.println("NORTH-WEST MAP BLOCK");
				mapBlock = true;
				blockCount++;
				//northWestEscape = false;
			}
			//distance is only 1
			else if (distance == 1)
			{
				//top row on box
				if (throneX == 1 && throneY > 0)
				{
					//System.out.println("NORTH-WEST MAP BLOCK");
					mapBlock = true;
					blockCount++;
					///northWestEscape = false;
				}
				else {
					for(int j = 1; j < 2; j++)
					{	
						if (pieces[throneX - 1][throneY - 1] != null)
						{
							if (pieces[throneX - 1][throneY - 1].getColor().equals(meatShieldColor))
							{
								meatShieldBlock = true;
								meatCount++;
								//northWestEscape = false;
								//System.out.println("King protected NORTH-WEST via meatshield by " + pieces[throneX - 1][throneY - 1].getColorEnglish() + " " + pieces[throneX - 1][throneY - 1].toString() + " at " + (throneX - 1) + ", " + (throneY - 1));
								break;
							}
							else if (pieces[throneX - 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY - 1].toString().equals("Bishop") || pieces[throneX - 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX - 1][throneY - 1].toString().equals("Queen")) {
								//System.out.println("NORTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX - 1][throneY - 1].getColorEnglish() + " " + pieces[throneX - 1][throneY - 1].toString() + " at " + (throneY - 1) + ", " + (throneX - 1));
								threatX = throneX - 1;
								threatY = throneY - 1;
								threat = true;
								threatCount++;
								break;
							}
						}
						else 
						{
							//northWestEscape = true;
							break;
							//System.out.println("NORTH-WEST CLEAR - path " + (throneX - i) + ", " + (throneY - i) + " is clear.");
						}	
					}
				}
			}
			// more than one spaces
			else 
			{
				//top row on box
				if (throneX == 1 && throneY > 0)
				{
					//System.out.println("NORTH-WEST MAP BLOCK");
					mapBlock = true;
					blockCount++;
					//northWestEscape = false;
				}
				else {
					for (int i = 1; i < distance + 1; i++)
					{
						if ((throneX - i) <= 0 || (throneY - i) <= 0) //checks if piece is on the 1 y,x
						{
							//space did the job.
							//northWestEscape = false;
							break;
						}
						else if (pieces[throneX - i][throneY - i] != null)
						{
							if (pieces[throneX - i][throneY - i].getColor().equals(meatShieldColor))
							{
								//System.out.println("King protected NORTH-WEST via meatshield by " + pieces[throneX - i][throneY - i].getColorEnglish() + " " + pieces[throneX - i][throneY - i].toString() + " at " + (throneX - i) + ", " + (throneY - i));
								if (i == 1)
								{
									meatShieldBlock = true;
									meatCount++;
								//	northWestEscape = false;
									break;
								}
								else {
									//northWestEscape = true;
									break;
								}
							}
							else if (pieces[throneX - i][throneY - i].getColor().equals(enemyColor) && pieces[throneX - i][throneY - i].toString().equals("Bishop") || pieces[throneX - i][throneY - i].getColor().equals(enemyColor) && pieces[throneX - i][throneY - i].toString().equals("Queen")) {
								//System.out.println("NORTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY - i].getColorEnglish() + " " + pieces[throneX - i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX - i));
								threatX = throneX - i;
								threatY = throneY - i;
								
								defenceX = throneX - 1;
								defenceY = throneY - 1;
								
								threat = true;
								threatCount++;
								break;
							}
						}
						else 
						{
							//northWestEscape = true;
							//System.out.println("NORTH-WEST CLEAR - path " + (throneX - i) + ", " + (throneY - i) + " is clear.");
						}	
					}
				}
			}
		}
		//SOUTH-WEST //testing
		private void southWestPositionSetKingThreatCheck(int throneX, int throneY) 
		{
			int endColumn = 1;
			int distance = throneY - endColumn;
			
			if (distance <= 0)
			{
				//System.out.println("SOUTH-WEST MAP BLOCK");
				mapBlock = true;
				blockCount++;
				//southWestEscape = false;
			}
			else if (throneX == 8)
			{
				mapBlock = true;
				blockCount++;
				//southWestEscape = false;
			}
			else if (distance == 1)
			{
				for (int j = 1; j < 2; j++)
				{
					if (pieces[throneX + 1][throneY - 1] != null)
					{
						if (pieces[throneX + 1][throneY - 1].getColor().equals(meatShieldColor))
						{
							meatShieldBlock = true;
							meatCount++;
					//		southWestEscape = false;
							//System.out.println("King protected SOUTH-WEST via meatshield by " + pieces[throneX + 1][throneY - 1].getColorEnglish() + " " + pieces[throneX + 1][throneY - 1].toString() + " at " + (throneX + 1) + ", " + (throneY - 1));
							break;
						}
						else if (pieces[throneX + 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY - 1].toString().equals("Bishop") || pieces[throneX + 1][throneY - 1].getColor().equals(enemyColor) && pieces[throneX + 1][throneY - 1].toString().equals("Queen"))
						{
							System.out.println("SOUTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX + 1][throneY - 1].getColorEnglish() + " " + pieces[throneX + 1][throneY - 1].toString() + " at " + (throneY - 1) + ", " + (throneX + 1));
//							threatX = throneX + 1;
//							threatY = throneY - 1;
							threatCount++;
							threat = true;
							break;
						}
					}
					else 
					{
						//southWestEscape = true;
						break;
						//System.out.println("SOUTH-WEST CLEAR - path " + (throneX + i) + ", " + (throneY - i) + " is clear.");
					}	
				}
			}
			else {
				//top row on box
				if (throneX == 8 && throneY > 0)
				{
					//System.out.println("SOUTH-WEST MAP BLOCK");
					mapBlock = true;
					blockCount++;
					//southWestEscape = false;
				}
				else {
					for (int i = 1; i < distance + 1; i++)
					{
						if (throneX + i > 8 || throneY - i < 1)
						{
							//stuff
						//	southWestEscape = false;
							break;
						}
						else if (pieces[throneX + i][throneY - i] != null)
						{
							if (pieces[throneX + i][throneY - i].getColor().equals(meatShieldColor))
							{
								//System.out.println("King protected SOUTH-WEST via meatshield by " + pieces[throneX + i][throneY - i].getColorEnglish() + " " + pieces[throneX + i][throneY - i].toString() + " at " + (throneX + i) + ", " + (throneY - i));	
								if (i == 1)
								{
									meatShieldBlock = true;
									meatCount++;
							//		southWestEscape = false;
									break;
								}
								else {
								//	southWestEscape = true;
									break;
								}
							}
							else if (pieces[throneX + i][throneY - i].getColor().equals(enemyColor) && pieces[throneX + i][throneY - i].toString().equals("Bishop") || pieces[throneX + i][throneY - i].getColor().equals(enemyColor) && pieces[throneX + i][throneY - i].toString().equals("Queen"))
							{
								//System.out.println("SOUTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY - i].getColorEnglish() + " " + pieces[throneX + i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX + i));
								threatCount++;
								threat = true;
								threatX = throneX + i;
								threatY = throneY - i;
								
								defenceX = throneX + 1;
								defenceY = throneY - 1;
								
								//southWestEscape = false;
								break;
							}
						}
						else 
						{
							//southWestEscape = true;
							//System.out.println("SOUTH-WEST CLEAR - path " + (throneX + i) + ", " + (throneY - i) + " is clear.");
						}	
					}
				}
			}
		}

		private void allKnightSetKingChecks(int throneX, int throneY) {
			
			//checking up right
			int knightCheckUpRight1X = throneX - 2;
			int knightCheckUpRight1Y = throneY + 1;
			int knightCheckUpRight2X = throneX - 1;
			int knightCheckUpRight2Y = throneY + 2;
			
			//checking up left
			int knightCheckUpLeft3X = throneX - 2;
			int knightCheckUpLeft3Y = throneY - 1;
			int knightCheckUpLeft4X = throneX - 1;
			int knightCheckUpLeft4Y = throneY - 2;
			
			//checking up right
			int knightCheckDownRight1X = throneX + 2;
			int knightCheckDownRight1Y = throneY + 1;
			int knightCheckDownRight2X = throneX + 1;
			int knightCheckDownRight2Y = throneY + 2;
			
			//checking up left
			int knightCheckDownLeft3X = throneX + 2;
			int knightCheckDownLeft3Y = throneY - 1;
			int knightCheckDownLeft4X = throneX + 1;
			int knightCheckDownLeft4Y = throneY - 2;
						
			//checking up
			//-------------------------------------------------------------------------------------------------//
			//------------------------------------------------------------------------------------------------//
			//checking up right
			if (knightCheckUpLeft4X < 1 || knightCheckUpLeft4Y < 1)
			{
					
			}
			else if (pieces[knightCheckUpLeft4X][knightCheckUpLeft4Y] != null) 
			{
				knightCheckUpTop2(knightCheckUpLeft4X, knightCheckUpLeft4Y);
				//System.out.println("knightCheck4 = " + knightCheck4);
			}
			if (knightCheckDownRight1X > 8 || knightCheckDownRight1Y > 8)
			{
				//System.out.println("PHANTOM ZONE REACHED");
				//kingIsInCheck = false;
			}
			else if (pieces[knightCheckDownRight1X][knightCheckDownRight1Y] != null)
			{
				moarKnights2(knightCheckDownRight1X, knightCheckDownRight1Y);
				//System.out.println("knightCheck5 = " + knightCheck5);
			}
			if (knightCheckDownRight2X > 8 || knightCheckDownRight2Y > 8)
			{
				//System.out.println("PHANTOM ZONE");
				//kingIsInCheck = false;
			}
			else if (pieces[knightCheckDownRight2X][knightCheckDownRight2Y] != null) 
			{
				//knightCheck6 = knightCheckUpTop(kingIsInCheck, knightCheckDownRight2X, knightCheckDownRight2Y);
				//System.out.println("knightCheck6 = " + knightCheck6);
			}
			if (knightCheckDownLeft3X > 8 || knightCheckDownLeft3Y < 1)
			{
				//System.out.println("PHANTOM ZONE REACHED");
				//kingIsInCheck = false;
			}
			else if (pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y] != null) 
			{
				moKnights2(knightCheckDownLeft3X, knightCheckDownLeft3Y);
				//System.out.println("knightCheck7 = " + knightCheck7);
			}
			if(knightCheckDownLeft4X > 8 || knightCheckDownLeft4Y < 1)
			{
				//System.out.println("PHANTOM ZONE REACHED");
				//kingIsInCheck = false;
			}
			else if (pieces[knightCheckDownLeft4X][knightCheckDownLeft4Y] != null) 
			{
				knightCheckUpTop2(knightCheckDownLeft4X, knightCheckDownLeft4Y);
				//System.out.println("knightCheck8 = " + knightCheck8);
			}
			if (knightCheckUpRight1X < 1 || knightCheckUpRight1Y > 8)
			{
				//System.out.println("PHANTOM ZONE REACHED");
				//kingIsInCheck = false;
			}
			else if (pieces[knightCheckUpRight1X][knightCheckUpRight1Y] != null)
			{
				knightCheckUpRight2(knightCheckUpRight1X, knightCheckUpRight1Y);	
				//System.out.println("knightCheck1 = " + knightCheck1);
			}
			if (knightCheckUpRight2X < 1 || knightCheckUpRight2Y > 8)
			{
				//System.out.println("PHANTOM ZONE REACHED");
				//kingIsInCheck = false;
			}
			else if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y] != null) 
			{
				knightCheckUpTop2(knightCheckUpRight2X, knightCheckUpRight2Y);
				//System.out.println("knightCheck2 = " + knightCheck2);
			}
			if (knightCheckUpLeft3X < 1 || knightCheckUpLeft3Y < 1)
			{
				//System.out.println("PHANTOM ZONE REACHED");
				//kingIsInCheck = false;
			}
			else if (pieces[knightCheckUpLeft3X][knightCheckUpLeft3Y] != null) 
			{
				knightCheckUpTop2(knightCheckUpLeft3X, knightCheckUpLeft3Y);
			//	System.out.println("knightCheck3 = " + knightCheck3);
			}
			else 
			{
				//System.out.println("Clear!");
			}
			
			
//			if (knightCheck1 == true || knightCheck2 == true || knightCheck3 == true || knightCheck4 == true || knightCheck5 == true || knightCheck6 == true || knightCheck7 == true || knightCheck8 == true)
//			{
//				kingIsInCheck = true;
//			}
		}
		//SUPPORT KNIGHT CHECKS
		private void moKnights2(int knightCheckDownLeft3X, int knightCheckDownLeft3Y) 
		{
			if (pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y] != null )//&& pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString() != ("Knight"))
			{
				if(pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString().equals("Knight") && pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].getColor().equals(enemyColor)) //&& pieces[knightCheckDownLeft3Y][knightCheckDownLeft3Y].getColor().equals(enemyColor))
				{
					threat = true;
					threatCount++;
				//	kingIsInCheck = true;
					System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString() + " at : " + knightCheckDownLeft3Y + ", " + knightCheckDownLeft3X);	
					threatX = knightCheckDownLeft3X;
					threatY = knightCheckDownLeft3Y;
				}
				else {
					//System.out.println("BLAH BALAB ASFADF");
					//kingIsInCheck = false;
				}	
			}
		}
		private void moarKnights2(int knightCheckDownRight1X, int knightCheckDownRight1Y) {
			
			//checks knight attack up 2 right 1
			if (pieces[knightCheckDownRight1X][knightCheckDownRight1Y] != null ) //&& pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString() != ("Knight"))
			{
				if(pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString().equals("Knight") && pieces[knightCheckDownRight1X][knightCheckDownRight1Y].getColor().equals("d"))
				{
					threat = true;
					threatCount++;
					//kingIsInCheck = true;
					threatX = knightCheckDownRight1X;
					threatY = knightCheckDownRight1Y;
					
					System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString() + " at : " + knightCheckDownRight1Y + ", " + knightCheckDownRight1X);
				}
				else {
					//kingIsInCheck = false;
				}
			}
		}
		private void knightCheckUpTop2(int knightCheckUpRight2X, int knightCheckUpRight2Y) 
		{
			if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y] != null)// && pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString() != ("Knight"))
			{
				if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString().equals("Knight") && pieces[knightCheckUpRight2X][knightCheckUpRight2Y].getColor().equals(enemyColor))
				{
					//kingIsInCheck = true;
					threat = true;
					threatCount++;
					threatX = knightCheckUpRight2X;
					threatY = knightCheckUpRight2Y;
					System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString() + " at : " + knightCheckUpRight2Y + ", " + knightCheckUpRight2X);
				}
				else 
				{
					//System.out.println("BLAH BLAH BLAH BLAH");
					//kingIsInCheck = false;
				}
			}
			//return kingIsInCheck;
		}
		private void knightCheckUpRight2(int knightCheckUpRight1X, int knightCheckUpRight1Y) {	
			//checks knight attack up 2 right 1
			if (pieces[knightCheckUpRight1X][knightCheckUpRight1Y] != null)
			{
				if(pieces[knightCheckUpRight1X][knightCheckUpRight1Y].toString().equals("Knight") && pieces[knightCheckUpRight1X][knightCheckUpRight1Y].getColor().equals(enemyColor))
				{
					//kingIsInCheck = true;
					threat = true;
					threatCount++;
					threatX = knightCheckUpRight1X;
					threatY = knightCheckUpRight1Y;
					System.out.println("THREAT - path intercepted by " + pieces[knightCheckUpRight1X][knightCheckUpRight1Y].toString() + " at : " + knightCheckUpRight1Y + ", " + knightCheckUpRight1X);
				}
				else {
					//kingIsInCheck = false;
				}
			}
		}
	
	//---------------------------------------------------------------------------------------------------------------------------------------//	
			
	
		
		
	// RESCUE PIECES TO ATTACK THREAT OR DEFEND KING THREAT ---------------------------------------------------------------------------------//		
	
	//defines the threat location. checks if the friendly pieces can attack the threat or defend the king. 
	public boolean teamRescue() {
		
		boolean rescuePossible = false;
		
		System.out.println("\n\nThe enemy piece resides at " + threatY + ", " + threatX);
		System.out.println("The threat is the " + pieces[threatX][threatY].getColorEnglish() + " " + pieces[threatX][threatY].toString());
		
		if (pieceTeamFinder())
		{
			rescuePossible = true;
		}
		else {
			rescuePossible = false;
		}
		return rescuePossible;
	}
	// iterates through the board looking for friendly pieces that can act.
	public boolean pieceTeamFinder()
	{
		boolean pieceCanAct = false;
		boolean rookCanAct = false;
		boolean bishopCanAct = false;
		boolean knightCanAct = false;
		boolean queenCanAct = false;
		//find rook or rooks
		
		for (int x = 0; x < 9; x++) 
		{
		    for (int y = 0; y < 9; y++)
		    {
		    	 if (pieces[x][y] != null) //add color check in there later
		    	 {
			    	if (pieces[x][y].toString().equals("Rook") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
			    	 {
			           // System.out.println("The " + meatShieldColor + " rook resides at y: " + y + ", x: " + x);
			            rookX = x;
			            rookY = y;
			            if (rookAvenger(rookX, rookY))
			            {
			            	rookCanAct = true;
			            }   	
			    	}
			    	else if (pieces[x][y].toString().equals("Bishop") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
		    		 {
		    	       //System.out.println("The " + meatShieldColor + " bishop resides at y: " + y + ", x: " + x);
		    	       bishopX = x;
		    	       bishopY = y;
		    	       if (bishopAvenger(bishopX, bishopY))
		    	       {
		    	       		bishopCanAct = true;
		    	       }
		    	       //run rook from here, would go through rook 1 first and then the next rook, saving time
		    		}
			    	else if (pieces[x][y].toString().equals("Knight") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
		    		 {
		    	       //System.out.println("The " + meatShieldColor + " knight resides at y: " + y + ", x: " + x);
		    	       knightX = x;
		    	       knightY = y;
		    	       if (knightAvenger(knightX, knightY))
		    	       {
		    	       		knightCanAct = true;
		    	       }
		    	       //run rook from here, would go through rook 1 first and then the next rook, saving time
		    		}
			    	else if (pieces[x][y].toString().equals("Queen") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
		    		 {
		    	       //System.out.println("The " + meatShieldColor + " Queen resides at y: " + y + ", x: " + x);
		    	       queenX = x;
		    	       queenY = y;
		    	       if (queenAvenger(queenX, queenY))
		    	       {
		    	       		queenCanAct = true;
		    	       }
		    	       //run rook from here, would go through rook 1 first and then the next rook, saving time
		    		}
			    	
			    	else {
			    		//System.out.println("Continue searching");
			    	}
		    	 }
		    }
		}
		
		if (rookCanAct || bishopCanAct || knightCanAct || queenCanAct)
		{
			pieceCanAct = true;
		}
		
		return pieceCanAct;
	}	

	//attacking or defence actions for the remaining friendly peices
	public boolean rookAvenger(int rookX, int rookY)
	{
		boolean rookAct = false;
		boolean rookCanAttack = false;
		boolean rookCanDefend = false;
		
		System.out.println("This is rook defence position: " + defenceY + ", " + defenceX);
		
		if (pieces[rookX][rookY] != null)
		{
			//System.out.println("ROOK PRESENT.");
			//attack
			if (pieces[rookX][rookY].legalMove(pieces, rookY, rookX, threatY, threatX)) //checks legal moves
			{
				if (pieces[rookX][rookY].pathIsClear(pieces, rookY, rookX, threatY, threatX))//checks for barriers
				{
					rookCanAttack = true;
					//System.out.println("Rook can attack the threat.");
				}		
				else 
				{
					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
					rookCanAttack = false;
				}
			}
			//defend
			else if(pieces[rookX][rookY].legalMove(pieces, rookY, rookX, defenceY, defenceX))
			{
				if(pieces[rookX][rookY].pathIsClear(pieces, rookY, rookX, defenceY, defenceX))
				{
					rookCanDefend = true;
					System.out.println("Rook can defend the threat.");
				}
				else {
					rookCanDefend = false;
					System.out.println("MOVE BLOCKED - another piece is blocking your route.");
				}
				
			}
			else
			{
				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
				rookCanAttack = false;
				rookCanDefend = false;
			}	
		}
		
		if(rookCanAttack || rookCanDefend) 
		{
			rookAct = true;
		}
		
		return rookAct;
	}
	public boolean bishopAvenger(int bishopX, int bishopY)
	{
		boolean bishopAct = false;
		boolean bishopCanAttack = false;
		boolean bishopCanDefend = false;
	
		if (pieces[bishopX][bishopY] != null)
		{
			//System.out.println("BISHOP PRESENT.");
			if (pieces[bishopX][bishopY].legalMove(pieces, bishopY, bishopX, threatY, threatX)) //checks legal moves
			{
				if (pieces[bishopX][bishopY].pathIsClear(pieces, bishopY, bishopX, threatY, threatX))//checks for barriers
				{
					bishopCanAttack = true;
					//System.out.println("Bishop can attack the threat.");
				}
				else 
				{
					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
					bishopCanAttack = false;
				}
			}
			else if (pieces[bishopX][bishopY].legalMove(pieces, bishopY, bishopX, defenceY, defenceX)) //checks legal moves
			{
				if (pieces[bishopX][bishopY].pathIsClear(pieces, bishopY, bishopX, defenceY, defenceX))//checks for barriers
				{
					bishopCanDefend = true;
					System.out.println("Bishop can defend the threat.");
				}
				else 
				{
					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
					bishopCanDefend = false;
				}
			}
			else
			{
				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
				bishopCanDefend = false;
				bishopCanAttack = false;
			}	
		}
		
		if (bishopCanAttack || bishopCanDefend)
		{
			bishopAct = true;
		}
		
		return bishopAct;
	}
	public boolean knightAvenger(int knightX, int knightY)
	{
		boolean knightAct = false;
		boolean knightCanAttack = false;
		boolean knightCanDefend = false;
		
	
		if (pieces[knightX][knightY] != null)
		{
			//System.out.println("KNIGHT PRESENT.");
			if (pieces[knightX][knightY].legalMove(pieces, knightY, knightX, threatY, threatX)) //checks legal moves
			{
				if (pieces[knightX][knightY].pathIsClear(pieces, knightY, knightX, threatY, threatX))//checks for barriers
				{
					knightCanAttack = true;
					//System.out.println("Knight can attack the threat.");
				}
				else 
				{
					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
					knightCanAttack = false;
				}
			}
			//System.out.println("KNIGHT PRESENT.");
			else if (pieces[knightX][knightY].legalMove(pieces, knightY, knightX, defenceY, defenceX)) //checks legal moves
			{
				if (pieces[knightX][knightY].pathIsClear(pieces, knightY, knightX, defenceY, defenceX))//checks for barriers
				{
					knightCanDefend = true;
					System.out.println("Knight can defend the threat.");
				}
				else 
				{
					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
					knightCanDefend = false;
				}
			}
			else
			{
				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
				knightCanAttack = false;
				knightCanDefend = false;
			}	
		}
		
		if (knightCanAttack || knightCanDefend)
		{
			knightAct = true;
		}
		
		return knightAct;
	}
	public boolean queenAvenger(int queenX, int queenY)
	{
		boolean queenCanAttack = false;
		boolean queenCanDefend = false;
		boolean queenAct = false;
	
		if (pieces[queenX][queenY] != null)
		{
			//System.out.println("QUEEN PRESENT.");
			if (pieces[queenX][queenY].legalMove(pieces, queenY, queenX, threatY, threatX)) //checks legal moves
			{
				if (pieces[queenX][queenY].pathIsClear(pieces, queenY, queenX, threatY, threatX))//checks for barriers
				{
					queenCanAttack = true;
					System.out.println("Queen can attack the threat.");
				}
				else 
				{
					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
					queenCanAttack = false;
				}
			}
			else if (pieces[queenX][queenY].legalMove(pieces, queenY, queenX, defenceY, defenceX)) //checks legal moves
			{
				if (pieces[queenX][queenY].pathIsClear(pieces, queenY, queenX, defenceY, defenceX))//checks for barriers
				{
					queenCanDefend = true;
					System.out.println("Queen can defend the threat.");
				}
				else 
				{
					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
					queenCanDefend = false;
				}
			}
			else
			{
				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
				queenCanAttack = false;
				queenCanDefend = false;
			}	
		}
		if (queenCanAttack || queenCanDefend)
		{
			queenAct = true;
		}
		return queenAct;
	}
	//-----------------------------------------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	// SUPPORT // ----------------------------------------------------------------------------------------------------------------//
	private void getMoveItemElements(Matcher basicMoveMatcher) //gets elements of move from the string that is read in. 
	{
		getWhichColor(basicMoveMatcher); //finds color + piece and its case
		getInitialPosition(basicMoveMatcher); //initial position (placeY, placeX) //run transfer chars //run backwards column syndrome 
		getFinalPosition(basicMoveMatcher); //final position (placeY, placeX) //run transfer chars //run backwards column syndrome 
	}	
	private void getWhichColor(Matcher basicMoveMatcher) {
		color = basicMoveMatcher.group("color");
		if (color.equals("d"))
			piece = basicMoveMatcher.group("piece").toLowerCase();
		else if(color.equals("l"))
			piece = basicMoveMatcher.group("piece").toUpperCase();
	}	
	private void getInitialPosition(Matcher basicMoveMatcher) //finds initial position
	{
		starty = basicMoveMatcher.group("x");
		transferChars();
		
		startx = Integer.parseInt(basicMoveMatcher.group("y"));
		backwardsColumnNumberingSyndrome();
	}
	private void getFinalPosition(Matcher basicMoveMatcher) //finds final position
	{
		endy = basicMoveMatcher.group("x2");
		transferChars2();
		
		endx = Integer.parseInt(basicMoveMatcher.group("y2"));
		backwardsColumnNumberingSyndrome2();
	}
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







