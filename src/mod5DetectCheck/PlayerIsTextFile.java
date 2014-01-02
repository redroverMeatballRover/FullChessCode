package mod5DetectCheck;

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
	
	boolean escape1 = false;
	boolean escape2 = false;
	boolean escape3 = false;
	boolean escape4 = false;
	boolean escape5 = false;
	boolean escape6 = false;
	boolean escape7 = false;
	boolean escape8 = false;
	boolean subEscape = false;
	
	
	int movementOptions = 8;
	int spotsOpenAfterChecks = 0;
	
	int blockCount = 0;
	int meatCount = 0;
	int threatCount = 0;
	
	
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
	
	
	
	
	// moves the piece and checks if the piece has moved into check//------------------------------------------------------------------------------------------------//
	private void move() throws IOException 
	{
		int keepX = startX;
		int keepY = startY;

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
			
			pieceColorUponMisMovedCheckScenario();
		}	
	}	
	//-----------------------------------------------------------------------------------------------------------------//

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
	
	
	
	
	//check king security --------------------------------------------------------------------------------------------------------------//
	public boolean kingIsInCheck(ChessPiece[][] pieces) //, int startY, int startX, int endY, int endX 
	{
		boolean kingIsInCheck = false;
		designateKingColorAndMeatShieldColorOnNewTurn(); //might need to add knights to this
		locateKingOnBoard();
		
		//TEST CHECKS SYSTEM --------------------------------------------------------------------------------//
		checkForEnemiesStraightUp(throneX, throneY); //CHECKING FOR PIECE UP
		checkForEnemiesStraightDown(throneX, throneY);	//CHECKING FOR PIECE DOWN
		checkForEnemiesStraightRight(throneX, throneY); //moving right
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
		
		if (escape1 || escape2 || escape3 || escape4 || escape5 || escape6 || escape7 || escape8 )
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
		System.out.println("KING CHECK STATUS: " + kingIsInCheck);
		return kingIsInCheck;
	}
	
	private boolean meatBlockOrEscapePath(int i, boolean houdini) {
		if (i == 1)
		{
			meatShieldBlock = true;
			meatCount++;
			houdini = false;
		}
		else {
			houdini = true;
		}
		return houdini;
	}
	
	//north
	private void checkForEnemiesStraightUp(int throneX, int throneY) 
	{	
		if (throneX - 1 <= 0)
		{
			blockCount++;
			//System.out.println("North is Map-Blocked.");
			mapBlock = true;
		}
		for (int i = 1; i < throneX; i++)
		{
			if (pieces[throneX - i][throneY] != null)
			{	
				if (pieces[throneX - i][throneY].getColor().equals(meatShieldColor))
				{
					//System.out.println("North: King protected via meatshield by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
					meatBlockOrEscapePath(i, escape1);
					break;
				}
				else if (pieces[throneX - i][throneY].getColor().equals(enemyColor) && pieces[throneX - i][throneY].toString().equals("Rook") || pieces[throneX - i][throneY].getColor().equals(enemyColor) && pieces[throneX - i][throneY].toString().equals("Queen")) {
					//System.out.println("NORTH THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
					threat = true;
					threatCount++;
					break;
				}
			}
			else 
			{
				//System.out.println("NORTH CLEAR - path " + (throneX - i) + ", " + throneY + " is clear.");
				escape1 = true;
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
					}
					else {
						escape2 = true;
					}
					break;
				}
				else if (pieces[throneX + i][throneY].getColor().equals(enemyColor) && pieces[throneX + i][throneY].toString().equals("Rook") || pieces[throneX + i][throneY].getColor().equals(enemyColor) && pieces[throneX + i][throneY].toString().equals("Queen")) {
					//System.out.println("SOUTH THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY].getColorEnglish() + " " + pieces[throneX + i][throneY].toString() + " at " + (throneX + i) + ", " + throneY);
					threat = true;
					threatCount++;
					break;
				}
			}
			else {
				//System.out.println("SOUTH CLEAR - path " + (throneX + i) + ", " + throneY + " is clear.");
				escape2 = false;
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
					}
					else {
						escape3 = true;
					}
					
					break;
				}
				else if (pieces[throneX][throneY - i].getColor().equals(enemyColor) && pieces[throneX][throneY - i].toString().equals("Rook") || pieces[throneX][throneY - i].getColor().equals(enemyColor) && pieces[throneX][throneY - i].toString().equals("Queen")) 
				{
					//System.out.println("WEST THREAT FOUND - path intercepted by " + pieces[throneX][throneY - i].getColorEnglish() + " " + pieces[throneX][throneY - i].toString() + " at " + (throneY - i) + ", " + throneX);
					threat = true;
					threatCount++;
					break;
				}
			}
			else 
			{
				//.out.println("WEST CLEAR - path " + throneX + ", " + (throneY - i) + " is clear.");
				escape3 = true;
			}
		}
	}
	//right
	private void checkForEnemiesStraightRight(int throneX, int throneY) 
	{
		int totalBoard = 8;
		int distance = (totalBoard - throneY);
		
		if (distance == 0)
		{
			mapBlock = true;
			blockCount++;
			//System.out.println("East is map-blocked.");
		}
		for(int i = 1; i < distance + 1; i++)
		{
			if (pieces[throneX][throneY + i] != null)
			{
				if (pieces[throneX][throneY + i].getColor().equals(meatShieldColor))
				{
					//System.out.println("King protected East via meatshield by " + pieces[throneX][throneY + i].getColorEnglish() + " " + pieces[throneX][throneY + i].toString() + " at " + throneX + ", " + (throneY + i));
					if (i == 1)
					{
						meatShieldBlock = true;
						meatCount++;
					}
					else {
						escape4 = true;
					}
					break;
				}
				else if (pieces[throneX][throneY + i].getColor().equals(enemyColor) && pieces[throneX][throneY + i].toString().equals("Rook") || pieces[throneX][throneY + i].getColor().equals(enemyColor) && pieces[throneX][throneY + i].toString().equals("Queen")) {
					//System.out.println("EAST THREAT FOUND - path intercepted by " + pieces[throneX][throneY + i].getColorEnglish() + " " + pieces[throneX][throneY + i].toString() + " at " + (throneY + i) + ", " + throneX);
					threat = true;
					threatCount++;
					break;
				}
			}
			else 
			{
				//System.out.println("EAST CLEAR - path " + throneX + ", " + (throneY + i) + " is clear.");
				escape4 = true;
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
					escape5 = true;
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
			}
			else {
				for (int i = 1; i < distance + 1; i++)
				{
					if (throneX + i >= 9 || throneY + i >= 9)
					{
						//SPACE DID THE JOB
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
							}
							else {
								escape5 = true;
							}
							break;
						}
						else if (pieces[throneX + i][throneY + i].getColor().equals(enemyColor) && pieces[throneX + i][throneY + i].toString().equals("Bishop") || pieces[throneX + i][throneY + i].getColor().equals(enemyColor) && pieces[throneX + i][throneY + i].toString().equals("Queen")) //else if enemy piece
						{
							//System.out.println("SOUTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY + i].getColorEnglish() + " " + pieces[throneX + i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX + i));
							threat = true;
							threatCount++;
							break;
						}
					}
					else 
					{
						//System.out.println("SOUTH-EAST CLEAR - path " + (throneX + i) + ", " + (throneY + i) + " is clear.");	
						escape5 = true;
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
					escape6 = true;
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
							}
							else {
								escape6 = true;
							}
							break;
						}
						else if (pieces[throneX - i][throneY + i].getColor().equals(enemyColor) && pieces[throneX - i][throneY + i].toString().equals("Bishop") || pieces[throneX - i][throneY + i].getColor().equals(enemyColor) && pieces[throneX - i][throneY + i].toString().equals("Queen")) {
							//System.out.println("NORTH-EAST THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY + i].getColorEnglish() + " " + pieces[throneX - i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX - i));
							threat = true;
							threatCount++;
							break;
						}
					}
					else 
					{
						escape6 = true;
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
						escape7 = true;
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
			}
			else {
				for (int i = 1; i < distance + 1; i++)
				{
					if ((throneX - i) <= 0 || (throneY - i) <= 0) //checks if piece is on the 1 y,x
					{
						//space did the job.
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
							}
							else {
								escape7 = true;
							}
							break;
						}
						else if (pieces[throneX - i][throneY - i].getColor().equals(enemyColor) && pieces[throneX - i][throneY - i].toString().equals("Bishop") || pieces[throneX - i][throneY - i].getColor().equals(enemyColor) && pieces[throneX - i][throneY - i].toString().equals("Queen")) {
							//System.out.println("NORTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY - i].getColorEnglish() + " " + pieces[throneX - i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX - i));
							threat = true;
							threatCount++;
							break;
						}
					}
					else 
					{
						escape7 = true;
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
					escape8 = true;
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
			}
			else {
				for (int i = 1; i < distance + 1; i++)
				{
					if (throneX + i > 8 || throneY - i < 1)
					{
						//stuff
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
							}
							else {
								escape8 = true;
							}
							break;
						}
						else if (pieces[throneX + i][throneY - i].getColor().equals(enemyColor) && pieces[throneX + i][throneY - i].toString().equals("Bishop") || pieces[throneX + i][throneY - i].getColor().equals(enemyColor) && pieces[throneX + i][throneY - i].toString().equals("Queen"))
						{
							//System.out.println("SOUTH-WEST THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY - i].getColorEnglish() + " " + pieces[throneX + i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX + i));
							threatCount++;
							threat = true;
							break;
						}
					}
					else 
					{
						escape8 = true;
						//System.out.println("SOUTH-WEST CLEAR - path " + (throneX + i) + ", " + (throneY - i) + " is clear.");
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
	
	// KING CHECKS
	//---------------------------------------------------------------------------------------------------------------------------------------//
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
			            //System.out.println("The " + kingColorDesignation + " king resides at y: " + y + ", x: " + x);
			            throneX = x;
			            throneY = y;
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
				//	kingIsInCheck = true;
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
					//kingIsInCheck = true;
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
					System.out.println("THREAT - path intercepted by " + pieces[knightCheckUpRight1X][knightCheckUpRight1Y].toString() + " at : " + knightCheckUpRight1Y + ", " + knightCheckUpRight1X);
				}
				else {
					//kingIsInCheck = false;
				}
			}
		}	
	
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





