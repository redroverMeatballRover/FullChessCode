//package mod7TeamPlay;
//
//import java.io.BufferedReader;
//import java.io.FileReader;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class holdingCheck6 {
//	
//	public ArrayList<String> textFileCommander = new ArrayList<String>();
//	public final Pattern basicMove = Pattern.compile("(?<piece>q|k|b|p|n|r+)(?<color>l|d)(?<x>\\w)(?<y>\\d) ?(?<x2>\\w)(?<y2>\\d)(?<capture>[*])?");
//	String fileName = "setMovesFile";
//	
//	int chessBoardInteger = 9;
//	protected String piece = " ";
//	protected String color = " ";
//	
//	public String starty = "";
//	public int startx;
//	
//	public int startY;
//	public int startX;
//	
//	public String endy = " ";
//	public int endx;
//	
//	public int endY;
//	public int endX;
//	
//	ChessPiece[][] pieces = new ChessPiece[chessBoardInteger][chessBoardInteger];
//	boolean isWhiteTurn = true;
//	
//	private int throneX = 0;
//	private int throneY = 0;
//	
//	boolean kc1 = false;
//	boolean kc2 = false;
//	boolean kc3 = false;
//	boolean kc4 = false;
//	boolean kc5 = false;
//	boolean kc6 = false;
//	boolean kc7 = false;
//	boolean kc8 = false;
//	boolean kc9 = false;
//	
//	String kingColorDesignation;
//	String kingColorEnglish;
//	String meatShieldColor;
//	String enemyColor;
//	
//	boolean friendlyBlock = false;
//	boolean mapBlock = false;
//	
//	int keepX;// = startX;
//	int keepY; // = startY;
//	
//	int threat1X;
//	int threat1YX;
//	int threat2X;
//	int threat2YX;
//	
//	int blockCount = 0;
//	int meatCount = 0;
//	int threatCount = 0;
//	
//	
//	
//	// PIECE SETUP AND PARSING
//	//-----------------------------------------------------------------------------------------------------------------//
//	//returns current board and initiates the board movement
//	public void getCurrentBoardAndMakeMove(ChessPiece[][] currentBoard) throws IOException {
//		pieces = currentBoard;
//		timeToMove(fileName);
//	}
//	
//	//looks through list of moves, finds moves that are basic, and runs them.
//	public void timeToMove(String fileName) throws IOException
//	{
//		String line;
//		BufferedReader br = new BufferedReader(new FileReader(fileName));
//		while((line = br.readLine()) != null) 
//	    {
//	       textFileCommander.add(line.toLowerCase()); 
//	    }
//        for(String item : textFileCommander)
//        {
//        	parsingMoves(item);
//        }
//	}
//	
//	//parses moves and begins verifying the pieces.
//	private void parsingMoves(String item) throws IOException {
//				
//		Matcher basicMoveMatcher = basicMove.matcher(item);
//
//		if(item != null)
//		{
//			if(basicMoveMatcher.find())
//			{
//				System.out.println("\n *----------------------------next move------------------------------*\n");
//				//System.out.println("WhiteTurn boolean is: " + isWhiteTurn);
//				getMoveItemElements(basicMoveMatcher);	
//				verifyPieceIsLegit();
//			}
//		}
//	}
//	//-----------------------------------------------------------------------------------------------------------------//
//	
//	
//	
//	
//	
//	//PIECE LEGAL SYSTEM
//	//-----------------------------------------------------------------------------------------------------------------//
//	//checks that the string move item matches and applies to pieces on the board. 
//	private void verifyPieceIsLegit() throws IOException {
//		if (pieces[startX][startY]!= null ) //verify the square has a piece
//		{
//			if (pieces[startX][startY].getName().equals(piece) && (pieces[startX][startY].getColor().equals(color))) // verify that the piece is who the board wants to move
//			{
//				//System.out.println("MATCH - text file string matches " + pieces[startX][startY].getColorEnglish() + " " + pieces[startX][startY].toString() + " at " + startY +  ", " + startX );
//				verificationSystem();
//			}
//			else {
//				System.out.println("NO MATCH ON PIECE NAME, COLOR AT THE INITIAL COORDINATES- text file string does not match a piece on the board. This is an attempt to illegally move a a piece.");
//			} 	
//		}
//		else {
//			System.out.println("NO PIECE PRESENT - text file has tried to access a piece that is not present at the file's designated coordinates. Cannot move.");
//		}
//	}
//
//	//checks that the piece will perform a legal move.
//	private void verificationSystem() throws IOException {
//			if (pieces[startX][startY].legalMove(pieces, startY, startX, endY, endX)) //checks legal moves
//			{
//				verifyClearPath();
//			}
//			else
//			{
//				System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
//			}
//		}
//	
//	//checks to make sure that the path the piece will travel is clear of obstructions
//	private void verifyClearPath() throws IOException {
//			if (pieces[startX][startY].pathIsClear(pieces, startY, startX, endY, endX))//checks for barriers
//			{
//				verifyTurnIsCorrect();		
//			}
//			else 
//			{
//				System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//			}
//		}
//	
//	//verifies that it is the pieces turn
//	private void verifyTurnIsCorrect() throws IOException {
//			if (turnChecking()) //check for turn
//			{
//				//CHECK FOR CHECK
//				if (kingIsInCheck())
//				{
//					System.out.println("CHECK - " + kingColorDesignation + " King is in check!");
//					move();
//				}
//				else 
//				{
//					//System.out.println("The " + kingColorDesignation + " king is safe");
//					move(); //actually moves them.
//				}	
//			}
//			else
//			{
//				System.out.println("ERROR - piece is moving out of turn." );
//			}
//		}
//	//-----------------------------------------------------------------------------------------------------------------//
//	
//	// moves the piece and checks if the piece has moved into check//------------------------------------------------------------------------------------------------//
//	private void move() throws IOException 
//	{
//		keepX = startX;
//		keepY = startY;
//	
//		pieces[endX][endY] = pieces[startX][startY];
//		pieces[startX][startY] = null; 
//		
//		System.out.println("\n ---------------------- Move Made ---------------------- \n");
//	
//		//CHECK FOR CHECK //------------------------------------------------------------------------//
//		if (!kingIsInCheck()) //if king is not in check, place piece
//		{
//			Board board = new Board();
//			board.updateBoard(pieces);
//		}
//		else 
//		{
//			movedIntoCheck(keepX, keepY);
//		}	
//	}	
//	//-----------------------------------------------------------------------------------------------------------------//
//	
//	// takes over if the king has moved into check. 
//	private void movedIntoCheck(int keepX, int keepY) throws IOException {
//		
//		//MOVING + CAPTURING = if the king captures a piece, but also moves into check, the piece will not respawn
//		System.out.println("CHOOSEN CHECK - You moved the King into check!");
//		System.out.println("REPOSITIONING PIECE TO INITIAL POSITION - you cannot move the king into check. ");
//		
//		//put piece back into the initial position
//		pieces[keepX][keepY] = pieces[endX][endY]; 
//		pieces[endX][endY] = null; // or 
//		
//		Board board = new Board();
//		board.updateBoard(pieces);
//		
//		locateKingOnBoard();
//		
//		System.out.println("\nCHECKING FOR CHECKMATE SCENARIO THREAT - Checking escape routes for potential checkmate scenario. Waiting...");
//		
//		
//		
//		if (!checkMate())
//		{
//			System.out.println("Play on.");
//			
//			//keeps the isWhiteBoolean at current state
//			if (isWhiteTurn)
//			{
//				System.out.println("SWITCHING COLORS - Black previously moved the king into check. It is Black's turn. ");
//				isWhiteTurn = false;
//			}
//			else 
//			{
//				System.out.println("SWITCHING COLORS - White's previously moved the king went check. It is White's turn.");
//				isWhiteTurn = true;
//			}
//		}
//		else {
//			System.out.println("CHECKMATE. GAME OVER.");
//			System.exit(0);
//		}
//	}
//	
//	//check king security //--------------------------------------------------------------------------------------------------------------//
//	public boolean kingIsInCheck() //, int startY, int startX, int endY, int endX 
//	{
//		boolean kingIsInCheck = false;
//		designateKingColorAndMeatShieldColorOnNewTurn(); 
//		locateKingOnBoard();
//		
//		//crosses---------------------------------------------------------------------------//
//		kc1 = checkForEnemiesStraightUp(kingIsInCheck, throneX, throneY); //CHECKING FOR PIECE UP
//		kc2 = checkForEnemiesStraightDown(kingIsInCheck, throneX, throneY);	//CHECKING FOR PIECE DOWN
//		kc3 = checkForEnemiesStraightRight(kingIsInCheck, throneX, throneY); //moving right
//		kc4 = checkForEnemiesStraightLeft(kingIsInCheck, throneX, throneY); //moving left
//		
//		//diagonals //--------------------------------------------------------------------------------//
//		kc5 = checkForEnemiesDiagonalDownLeft(kingIsInCheck, throneX,throneY); //moving down-left
//		kc6 = checkForEnemiesDiagonalUpLeft(kingIsInCheck, throneX, throneY); //diagonal up left
//		kc7 = checkForEnemiesDiagonalUpRight(kingIsInCheck, throneX, throneY); //diagonal up right
//		kc8 = checkForenemiesDiagonalDownRight(kingIsInCheck, throneX, throneY); 	// diagonal down right
//		
//		//knight moves //--------------------------------------------------------------------------------//
//		kc9 = allKnightChecks(kingIsInCheck, throneX, throneY);
//		//-------------------------------------------------------------------------------//
//		
//		//finalizer //-------------------------------------------------------------------------------------------------------------------------------------------//
//		//negates one single "true" boolean at the end jepordizing the security of the king
//		if (kc1 == true || kc2 == true || kc3 == true || kc4 == true || kc5 == true || kc6 == true || kc7 == true || kc8 == true || kc9 == true)
//		{
//			kingIsInCheck = true;
//		}
//		System.out.println("KING CHECK STATUS: " + kingIsInCheck);
//		return kingIsInCheck;
//	}
//	
//	//designates the turn-taking
//	private boolean turnChecking() {
//	
//	boolean isValidTurn = true;		
//	if (color.equals("l"))
//	{
//		if (isWhiteTurn)
//		{
//			isWhiteTurn = false;
//			isValidTurn = true;
//		}
//		else { 
//			isWhiteTurn = false;
//			isValidTurn = false;
//		}	
//	}	
//	else if (color.equals("d")) //if color is dark
//	{
//		if (!isWhiteTurn) 
//		{
//			isWhiteTurn = true;
//			isValidTurn = true;
//		}
//		else { 
//			isWhiteTurn = true;
//			isValidTurn = false;
//		}
//	}
//	return isValidTurn;
//}
//	
//	// KING CHECKS
//	//---------------------------------------------------------------------------------------------------------------------------------------//
//	// Designate color of meatshields, enemy pieces, knights, and and kings that are being tracked
//	private void designateKingColorAndMeatShieldColorOnNewTurn() {
//		if (isWhiteTurn == false) //means that whiteTurn has passed isWhiteTurn and must complete the move for black to move. 
// 		{
//			kingColorDesignation = "l";
//			kingColorEnglish = "White";
//			meatShieldColor = "l";
//			enemyColor = "d";
//		}
//		else {
//			kingColorDesignation = "d";
//			kingColorEnglish = "Black";
//			meatShieldColor = "d";
//			enemyColor = "l";
//		}
//	}
//
//	//locates the king on the board
//	private void locateKingOnBoard() {
//		//get king location
//		for (int x = 0; x < 9; x++) 
//		{
//		    for (int y = 0; y < 9; y++)
//		    {
//		    	 if (pieces[x][y] != null) //add color check in there later
//		    	 {
//			    	if (pieces[x][y].getName().equals("K") && pieces[x][y].getColor().equals(kingColorDesignation) || pieces[x][y].getName().equals("k") && pieces[x][y].getColor().equals(kingColorDesignation)) //add color check in there later
//			    	//if (pieces[x][y].getColor().equals(kingColorDesignation) && is a king) //add color check in there later
//			    	 {
//			            System.out.println("The " + kingColorDesignation + " king resides at y: " + y + ", x: " + x);
//			            throneX = x;
//			            throneY = y;
//			    	 }
//		    	 }
//		    }
//		}
//	}
//
//	// MAIN KNIGHT CHECK 
//	private boolean allKnightChecks(boolean kingIsInCheck, int throneX, int throneY) {
//		
//		//checking up right
//		int knightCheckUpRight1X = throneX - 2;
//		int knightCheckUpRight1Y = throneY + 1;
//		int knightCheckUpRight2X = throneX - 1;
//		int knightCheckUpRight2Y = throneY + 2;
//		
//		//checking up left
//		int knightCheckUpLeft3X = throneX - 2;
//		int knightCheckUpLeft3Y = throneY - 1;
//		int knightCheckUpLeft4X = throneX - 1;
//		int knightCheckUpLeft4Y = throneY - 2;
//		
//		//checking up right
//		int knightCheckDownRight1X = throneX + 2;
//		int knightCheckDownRight1Y = throneY + 1;
//		int knightCheckDownRight2X = throneX + 1;
//		int knightCheckDownRight2Y = throneY + 2;
//		
//		//checking up left
//		int knightCheckDownLeft3X = throneX + 2;
//		int knightCheckDownLeft3Y = throneY - 1;
//		int knightCheckDownLeft4X = throneX + 1;
//		int knightCheckDownLeft4Y = throneY - 2;
//		
//		boolean knightCheck1 = false;
//		boolean knightCheck2 = false;
//		boolean knightCheck3 = false;
//		boolean knightCheck4 = false;
//		boolean knightCheck5 = false;
//		boolean knightCheck6 = false;
//		boolean knightCheck7 = false;
//		boolean knightCheck8 = false;
//		
//		
//		//checking up
//		//-------------------------------------------------------------------------------------------------//
//		//------------------------------------------------------------------------------------------------//
//		//checking up right
//		if (knightCheckUpLeft4X < 1 || knightCheckUpLeft4Y < 1)
//		{
//			//System.out.println("PHANTOM ZONE REACHED");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckUpLeft4X][knightCheckUpLeft4Y] != null) 
//		{
//			knightCheck4 = knightCheckUpTop(kingIsInCheck, knightCheckUpLeft4X, knightCheckUpLeft4Y);
//			//System.out.println("knightCheck4 = " + knightCheck4);
//		}
//		if (knightCheckDownRight1X > 8 || knightCheckDownRight1Y > 8)
//		{
//			//System.out.println("PHANTOM ZONE REACHED");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckDownRight1X][knightCheckDownRight1Y] != null)
//		{
//			knightCheck5 = moarKnights(kingIsInCheck, knightCheckDownRight1X, knightCheckDownRight1Y);
//			//System.out.println("knightCheck5 = " + knightCheck5);
//		}
//		if (knightCheckDownRight2X > 8 || knightCheckDownRight2Y > 8)
//		{
//			System.out.println("PHANTOM ZONE");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckDownRight2X][knightCheckDownRight2Y] != null) 
//		{
//			knightCheck6 = knightCheckUpTop(kingIsInCheck, knightCheckDownRight2X, knightCheckDownRight2Y);
//			//System.out.println("knightCheck6 = " + knightCheck6);
//		}
//		if (knightCheckDownLeft3X > 8 || knightCheckDownLeft3Y < 1)
//		{
//			System.out.println("PHANTOM ZONE REACHED");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y] != null) 
//		{
//			knightCheck7 = moKnights(kingIsInCheck, knightCheckDownLeft3X, knightCheckDownLeft3Y);
//			//System.out.println("knightCheck7 = " + knightCheck7);
//		}
//		if(knightCheckDownLeft4X > 8 || knightCheckDownLeft4Y < 1)
//		{
//			System.out.println("PHANTOM ZONE REACHED");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckDownLeft4X][knightCheckDownLeft4Y] != null) 
//		{
//			knightCheck8 = knightCheckUpTop(kingIsInCheck, knightCheckDownLeft4X, knightCheckDownLeft4Y);
//			//System.out.println("knightCheck8 = " + knightCheck8);
//		}
//		if (knightCheckUpRight1X < 1 || knightCheckUpRight1Y > 8)
//		{
//			//System.out.println("PHANTOM ZONE REACHED");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckUpRight1X][knightCheckUpRight1Y] != null)
//		{
//			knightCheck1 = knightCheckUpRight(kingIsInCheck, knightCheckUpRight1X, knightCheckUpRight1Y);	
//			//System.out.println("knightCheck1 = " + knightCheck1);
//		}
//		if (knightCheckUpRight2X < 1 || knightCheckUpRight2Y > 8)
//		{
//			System.out.println("PHANTOM ZONE REACHED");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y] != null) 
//		{
//			knightCheck2 = knightCheckUpTop(kingIsInCheck, knightCheckUpRight2X, knightCheckUpRight2Y);
//			//System.out.println("knightCheck2 = " + knightCheck2);
//		}
//		if (knightCheckUpLeft3X < 1 || knightCheckUpLeft3Y < 1)
//		{
//			System.out.println("PHANTOM ZONE REACHED");
//			kingIsInCheck = false;
//		}
//		else if (pieces[knightCheckUpLeft3X][knightCheckUpLeft3Y] != null) 
//		{
//			knightCheck3 = knightCheckUpTop(kingIsInCheck, knightCheckUpLeft3X, knightCheckUpLeft3Y);
//		//	System.out.println("knightCheck3 = " + knightCheck3);
//		}
//		else 
//		{
//			//System.out.println("Clear!");
//		}
//		
//		
//		if (knightCheck1 == true || knightCheck2 == true || knightCheck3 == true || knightCheck4 == true || knightCheck5 == true || knightCheck6 == true || knightCheck7 == true || knightCheck8 == true)
//		{
//			kingIsInCheck = true;
//		}
//		
//		return kingIsInCheck;
//	}
//
//	//SUPPORT KNIGHT CHECKS
//	private boolean moKnights(boolean kingIsInCheck, int knightCheckDownLeft3X, int knightCheckDownLeft3Y) 
//	{
//		if (pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y] != null )//&& pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString() != ("Knight"))
//		{
//			if(pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString().equals("Knight") && pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].getColor().equals(enemyColor)) //&& pieces[knightCheckDownLeft3Y][knightCheckDownLeft3Y].getColor().equals(enemyColor))
//			{
//				kingIsInCheck = true;
//				//System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckDownLeft3X][knightCheckDownLeft3Y].toString() + " at : " + knightCheckDownLeft3Y + ", " + knightCheckDownLeft3X);	
//			}
//			else {
//				//System.out.println("BLAH BALAB ASFADF");
//				kingIsInCheck = false;
//			}	
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean moarKnights(boolean kingIsInCheck, int knightCheckDownRight1X, int knightCheckDownRight1Y) {
//		
//		//checks knight attack up 2 right 1
//		if (pieces[knightCheckDownRight1X][knightCheckDownRight1Y] != null ) //&& pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString() != ("Knight"))
//		{
//			if(pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString().equals("Knight") && pieces[knightCheckDownRight1X][knightCheckDownRight1Y].getColor().equals("d"))
//			{
//				kingIsInCheck = true;
//				//System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckDownRight1X][knightCheckDownRight1Y].toString() + " at : " + knightCheckDownRight1Y + ", " + knightCheckDownRight1X);
//			}
//			else {
//				//System.out.println("BLAH BLAH BLAH BLAH");
//				kingIsInCheck = false;
//			}
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean knightCheckUpTop(boolean kingIsInCheck, int knightCheckUpRight2X, int knightCheckUpRight2Y) 
//	{
//		if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y] != null)// && pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString() != ("Knight"))
//		{
//			if (pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString().equals("Knight") && pieces[knightCheckUpRight2X][knightCheckUpRight2Y].getColor().equals(enemyColor))
//			{
//				kingIsInCheck = true;
//				//System.out.println("THREAT FOUND - path intercepted by " + pieces[knightCheckUpRight2X][knightCheckUpRight2Y].toString() + " at : " + knightCheckUpRight2Y + ", " + knightCheckUpRight2X);
//			}
//			else 
//			{
//				//System.out.println("BLAH BLAH BLAH BLAH");
//				kingIsInCheck = false;
//			}
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean knightCheckUpRight(boolean kingIsInCheck, int knightCheckUpRight1X, int knightCheckUpRight1Y) {	
//		//checks knight attack up 2 right 1
//		if (pieces[knightCheckUpRight1X][knightCheckUpRight1Y] != null)
//		{
//			if(pieces[knightCheckUpRight1X][knightCheckUpRight1Y].toString().equals("Knight") && pieces[knightCheckUpRight1X][knightCheckUpRight1Y].getColor().equals(enemyColor))
//			{
//				kingIsInCheck = true;
//				//System.out.println("THREAT that is a queen as a knight - path intercepted by " + pieces[knightCheckUpRight1X][knightCheckUpRight1Y].toString() + " at : " + knightCheckUpRight1Y + ", " + knightCheckUpRight1X);
//			}
//			else {
//				kingIsInCheck = false;
//			}
//		}
//		return kingIsInCheck;
//	}
//
//	//CROSSES AND DIAGONALS CHECKS
//	private boolean checkForenemiesDiagonalDownRight(boolean kingIsInCheck, int throneX, int throneY) {
//		
//		int endColumn = 8;
//		int distance = endColumn - throneX;
//		
//		for (int i = 1; i < distance; i++)//Down Right
//		{
//			if (throneX + i >= 9 || throneY + i >= 9)
//			{
//				//System.out.println("REACHED PHANTOM ZOME");
//				break;
//			}
//			else if (pieces[throneX + i][throneY + i] != null)
//			{
//				if (pieces[throneX + i][throneY + i].getColor().equals(meatShieldColor)) // if its a friendly piece
//				{
//					//check for meat shield
//					kingIsInCheck = false;
//					//System.out.println("King protected via meatshield by " + pieces[throneX + i][throneY + i].getColorEnglish() + " " + pieces[throneX + i][throneY + i].toString() + " at " + (throneX + i) + ", " + (throneY + i));
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//				else if (pieces[throneX + i][throneY + i].getColor().equals(enemyColor) && pieces[throneX + i][throneY + i].toString().equals("Knight"))
//				{}
//				else //else if enemy piece
//				{
//					//System.out.println("THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY + i].getColorEnglish() + " " + pieces[throneX + i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX + i));
//					kingIsInCheck = true;
//					break;
//				}
//			}
//			else 
//			{
//				kingIsInCheck = false;
//				//System.out.println("CLEAR - path " + (throneX + i) + ", " + (throneY + i) + " is clear.");	
//			}
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean checkForEnemiesDiagonalUpRight(boolean kingIsInCheck, int throneX, int throneY) {
////			int endColumn = 1;
////			int distance = throneY - endColumn;
////			int endColumn = 1;
////			int distance = throneX - endColumn;
//		
//		//System.out.println("CHECKING FOR ENEMIES DIAGONAL UP RIGHT");
//		int endColumn = 1;
//		int distance = throneX - endColumn;
//		
//		for (int i = 1; i < distance; i++)
//		{
//			if (throneX - i == 0 || throneY + i == 9)
//			{
//				//System.out.println("REACHED PHANTOM ZOME");
//				break;
//			}
//			
//			else if (pieces[throneX - i][throneY + i] != null)
//			{
//				if (pieces[throneX - i][throneY + i].getColor().equals(meatShieldColor))
//				{
//					//check for meat shield
//					kingIsInCheck = false;
//					//System.out.println("King protected via meatshield by " + pieces[throneX - i][throneY + i].getColorEnglish() + " " + pieces[throneX - i][throneY + i].toString() + " at " + (throneX + i) + ", " + (throneY - i));
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//				else if (pieces[throneX - i][throneY + i].getColor().equals(enemyColor) && pieces[throneX - i][throneY + i].toString().equals("Knight"))
//				{
//					//System.out.println("Its only a knight.");
//				}
//				else {
//					//System.out.println("THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY + i].getColorEnglish() + " " + pieces[throneX - i][throneY + i].toString() + " at " + (throneY + i) + ", " + (throneX - i));
//					kingIsInCheck = true;
//					break;
//				}
//			}
//			else 
//				kingIsInCheck = false;
//				//System.out.println("CLEAR - path " + (throneX - i) + ", " + (throneY + i) + " is clear.");	
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean checkForEnemiesDiagonalUpLeft(boolean kingIsInCheck, int throneX, int throneY) {
//		int endColumn = 1;
//		int distance = throneY - endColumn;
//		//int distance = throneX - endColumn;
//		
//		//System.out.println("CHECKING FOR ENEMIES DIAGONAL UP LEFT");
//		
//		for (int i = 1; i < distance ; i++)//DOWN-LEFT
//		{
//			if (throneX - i <= 0 || throneY + i == 9)
//			{
//				//System.out.println("REACHED PHANTOM ZOME");
//				break;
//			}
//			else if (pieces[throneX - i][throneY - i] != null)
//			{
//				if (pieces[throneX - i][throneY - i].getColor().equals(meatShieldColor))
//				{
//					//check for meat shield
//					kingIsInCheck = false;
//					//System.out.println("King protected via meatshield by " + pieces[throneX - i][throneY - i].getColorEnglish() + " " + pieces[throneX - i][throneY - i].toString() + " at " + (throneX - i) + ", " + (throneY - i));
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//				else if (pieces[throneX - i][throneY - i].getColor().equals(enemyColor) && pieces[throneX - i][throneY - i].toString().equals("Knight"))
//				{
//					//System.out.println("Its only a knight.");
//				}
//				else {
//					//System.out.println("THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY - i].getColorEnglish() + " " + pieces[throneX - i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX - i));
//					kingIsInCheck = true;
//					break;
//				}
//			}
//			else 
//			{
//				kingIsInCheck = false;
//				//System.out.println("CLEAR - path " + (throneX - i) + ", " + (throneY - i) + " is clear.");
//			}	
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean checkForEnemiesDiagonalDownLeft(boolean kingIsInCheck, int throneX, int throneY) 
//	{
//		//int endColumn = 1;
//		//int distance = throneY - endColumn;
//		int endColumn = 1;
//		int distance = endColumn - throneX;
//		
//		for (int i = 1; i < distance ; i++)//DOWN-LEFT
//		{
//			if (pieces[throneX + i][throneY - i] != null)
//			{
//				if (pieces[throneX + i][throneY - i].getColor().equals(meatShieldColor))
//				{
//					//check for meat shield
//					kingIsInCheck = false;
//					//System.out.println("King protected via meatshield by " + pieces[throneX + i][throneY - i].getColorEnglish() + " " + pieces[throneX + i][throneY - i].toString() + " at " + (throneX + i) + ", " + (throneY - i));
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//				else if (pieces[throneX + i][throneY - i].getColor().equals(enemyColor) && pieces[throneX + i][throneY - i].toString().equals("Knight"))
//				{
//					//System.out.println("Its only a knight.");
//				}
//				else {
//					//System.out.println("THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY - i].getColorEnglish() + " " + pieces[throneX + i][throneY - i].toString() + " at " + (throneY - i) + ", " + (throneX + i));
//					kingIsInCheck = true;
//					break;
//				}
//			}
//			else 
//			{
//				kingIsInCheck = false;
//				//System.out.println("CLEAR - path " + (throneX + i) + ", " + (throneY - i) + " is clear.");
//			}	
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean checkForEnemiesStraightLeft(boolean kingIsInCheck, int throneX, int throneY) {
//		//int distanceToCheckLeft = throneY;
//		for (int i = 1; i < throneY; i++)
//		{
//			//System.out.println("Space " + i + " at y:" + endY + " , x: " + i +  ": " +  pieces[i][throneY] + " distanceToCross: " + (distanceToCheckUpwards - i));
//			//System.out.println(i);
//			if (pieces[throneX][throneY - i] != null)
//			{
//				if (pieces[throneX][throneY - i].getColor().equals(meatShieldColor))
//				{
//					//check for meat shield
//					kingIsInCheck = false;
//					//System.out.println("King protected via meatshield by " + pieces[throneX][throneY - i].getColorEnglish() + " " + pieces[throneX][throneY - i].toString() + " at " + throneX + ", " + (throneY - i));
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//				else if (pieces[throneX][throneY - i].getColor().equals(enemyColor) && pieces[throneX][throneY - i].toString().equals("Knight"))
//				{
//					//System.out.println("Its only a knight.");
//				}
//				else {
//					//System.out.println("THREAT FOUND - path intercepted by " + pieces[throneX][throneY - i].getColorEnglish() + " " + pieces[throneX][throneY - i].toString() + " at " + (throneY - i) + ", " + throneX);
//					kingIsInCheck = true;
//					break;
//				}
//			}
//			else 
//			{
//				//System.out.println("CLEAR - path " + (throneY - i) + ", " + throneX + " is clear.");
//				kingIsInCheck = false;
//			}
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean checkForEnemiesStraightRight(boolean kingIsInCheck, int throneX, int throneY) {
//		int totalBoard = 8;
//		int distance = (totalBoard - throneY);
//		
//		System.out.println("distance : " + distance);
//		
//		for(int i = 1; i < distance + 1; i++)
//		{
//			if (pieces[throneX][throneY + i] != null)
//			{
//				if (pieces[throneX][throneY + i].getColor().equals(enemyColor) && pieces[throneX][throneY + i].toString().equals("Knight"))
//				{
//					//System.out.println("Its only a knight.");
//					kingIsInCheck = false;
//				}
//				else if (pieces[throneX][throneY + i].getColor().equals(enemyColor)) {
//					kingIsInCheck = true;
//					System.out.println("GOTCHA BITCH");
//					break;
//				}	
//				
//				else if (pieces[throneX][throneY + i].getColor().equals(meatShieldColor))
//				{
//					kingIsInCheck = false;
//					//System.out.println("King protected via meatshield by " + pieces[throneX][throneY + i].getColorEnglish() + " " + pieces[throneX][throneY + i].toString() + " at " + throneX + ", " + (throneY + i));
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//			}
//			else {
//				System.out.println("CLEAR - path " + throneX + ", " + (throneY + i) + " is clear.");
//				kingIsInCheck = false;
//			}
//		}
//		return kingIsInCheck;
//	}
//
//	private boolean checkForEnemiesStraightUp(boolean kingIsInCheck, int throneX, int throneY) {
//		//int distanceToCheckUpwards = throneX;
//		for (int i = 1; i < throneX; i++)
//		{
//			if (pieces[throneX - i][throneY] != null)
//			{
//				//If piece.getColor = boolean color, king is not in check
//				//else king is in check
//				
//				//check for meat shield
//				if (pieces[throneX - i][throneY].getColor().equals(meatShieldColor))
//				{
//					kingIsInCheck = false;
//					
//					System.out.println("King protected via meatshield by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//				else if (pieces[throneX - i][throneY].getColor().equals(enemyColor) && pieces[throneX - i][throneY].toString().equals("Knight"))
//				{
//					//System.out.println("Its only a knight.");
//				}
//				else {
//					//System.out.println("THREAT FOUND - path intercepted by " + pieces[throneX - i][throneY].getColorEnglish() + " " + pieces[throneX - i][throneY].toString() + " at " + (throneX - i) + ", " + throneY);
//					kingIsInCheck = true;
//					break;
//				}
//			}
//			else 
//			{
//				//System.out.println("CLEAR - path " + (throneX - i) + ", " + throneY + " is clear.");
//				kingIsInCheck = false;
//			}
//		}
//		return kingIsInCheck;
//	}
//	
//	private boolean checkForEnemiesStraightDown(boolean kingIsInCheck, int throneX, int throneY) {
//		int totalBoard = 8;
//		int distance = (totalBoard - throneX); 
//	
//		for(int i = 1; i < distance + 1; i++)
//		{
//			if (pieces[throneX + i][throneY] != null)
//			{
//				
//				if (pieces[throneX + i][throneY].getColor().equals(meatShieldColor))
//				{
//					kingIsInCheck = false;
//					//System.out.println("King protected via meatshield by " + pieces[throneX + i][throneY].getColorEnglish() + " " + pieces[throneX + i][throneY].toString() + " at " + (throneX + i) + ", " + throneY);
//					//System.out.println("Continue Checking for threats.");
//					break;
//				}
//				else if (pieces[throneX + i][throneY].getColor().equals(enemyColor) && pieces[throneX + i][throneY].toString().equals("Knight"))
//				{
//					//System.out.println("Its only a knight.");
//				}
//				else {
//					//System.out.println("THREAT FOUND - path intercepted by " + pieces[throneX + i][throneY].getColorEnglish() + " " + pieces[throneX + i][throneY].toString() + " at " + (throneX + i) + ", " + throneY);
//					kingIsInCheck = true;
//					break;
//				}
//			}
//			else {
//				//System.out.println("CLEAR - path " + (throneX + i) + ", " + throneY + " is clear.");
//				//kingIsInCheck = false;
//			}
//		}
//		return kingIsInCheck;
//	}
//
//	//------------------------------------------------------------------------------------------------------------------------//
//	//------------------------------------------------------------------------------------------------------------------------//
//	
//
//	//checkmate
//	//------------------------------------------------------------------------------------------------------------------------//
//	//------------------------------------------------------------------------------------------------------------------------//
//	private boolean checkMate()
//	{
//		boolean checkMate = false;
//		boolean north = false;
//		boolean south = false;
//		boolean east = false;
//		boolean west = false;
//
//		boolean northEast = false;
//		boolean northWest = false;
//		boolean southEast = false;
//		boolean southWest = false;
//		
//		int throneGhostX = 0;
//		int throneGhostY = 0;
//		
//		boolean block1 = false;
//		boolean block2 = false;
//		boolean block3 = false;
//		boolean block4 = false;
//		boolean block5 = false;
//		boolean block6 = false;
//		boolean block7 = false;
//		boolean block8 = false;
//		
//		boolean meatShield1 = false;
//		boolean meatShield2 = false;
//		boolean meatShield3 = false;
//		boolean meatShield4 = false;
//		boolean meatShield5 = false;
//		boolean meatShield6 = false;
//		boolean meatShield7 = false;
//		boolean meatShield8 = false;
//	
//		boolean escape1 = false;
//		boolean escape2 = false;
//		boolean escape3 = false;
//		boolean escape4 = false;
//		boolean escape5 = false;
//		boolean escape6 = false;
//		boolean escape7 = false;
//		boolean escape8 = false;
//		
//		boolean threat1 = false;
//		boolean threat2 = false;
//		boolean threat3 = false;
//		boolean threat4 = false;
//		boolean threat5 = false;
//		boolean threat6 = false;
//		boolean threat7 = false;
//		boolean threat8 = false;
//	
//		int travel = 1;
//		
//		//return blocked zones
//		
//		
//		southCheckMate(travel, block1, meatShield1, escape1, threat1);
//		northCheckMate(travel, block2, meatShield2, escape2, threat2);
//		eastCheckMate(travel, block3, meatShield3, escape3, threat3);
//		westCheckMate(travel, block4, meatShield4, escape4, threat4);
//		northEastCheckMate(travel, block5, meatShield5, escape5, threat5);
//		northWestCheckMate(travel, block6, meatShield6, escape6, threat6);
//		southEastCheckMate(travel, block7, meatShield7, escape7, threat7);
//		southWestCheckMate(travel, block8, meatShield8, escape8, threat8);
//		
//		if (escape1 == true || escape2 == true || escape3 == true || escape4 == true || escape5 == true || escape6 == true || escape7 == true || escape8 == true)
//		{
//			checkMate = false;
//			System.out.println("Escape exists. Play On.");
//		}
//		else {
//			
//			if (block1 == true)
//				blockCount++;
//			if (block2 == true)
//				blockCount++;
//			if (block3 == true)
//				blockCount++;
//			if (block4 == true)
//				blockCount++;
//			if (block5 == true)
//				blockCount++;
//			if (block6 == true)
//				blockCount++;
//			if (block7 == true)
//				blockCount++;
//			if (block8 == true)
//				blockCount++;
//			
//			if (meatShield1 == true)
//				meatCount++;
//			if (meatShield2 == true)
//				meatCount++;
//			if (meatShield3 == true)
//				meatCount++;
//			if (meatShield4 == true)
//				meatCount++;
//			if (meatShield5 == true)
//				meatCount++;
//			if (meatShield6 == true)
//				meatCount++;
//			if (meatShield7 == true)
//				meatCount++;
//			if (meatShield8 == true)
//				meatCount++;
//			
//			if (threat1 == true)
//				threatCount--;
//			if (threat2 == true)
//				threatCount--;
//			if (threat3 == true)
//				threatCount--;
//			if (threat4 == true)
//				threatCount--;
//			if (threat5 == true)
//				threatCount--;
//			if (threat6 == true)
//				threatCount--;
//			if (threat7 == true)
//				threatCount--;
//			if (threat8 == true)
//				threatCount--;
//			
//			System.out.println("Number of blocked spaces: " + blockCount);
//			System.out.println("Number of meatshields: " + meatCount);
//			System.out.println("Number of threats: " + threatCount);
//			
//			int totalCount = blockCount + meatCount + threatCount;
//			System.out.println("Total: " + totalCount);
//			
//		}
//		
//		
//		//can king move?
//		//how many paths are blocked?
//		
//		//check for escapes -- if one exists, no check
//		// if no escapes exists, then go to------
//		
//		//check for blocks
//		//check for meatshields
//		//checks for threats
//		
//		//for each block that is true, true = +1, false = -1
//		//for each meatshield that is true, true = +1 , false = -1
//		//for each threat, true = - 1
//		
//		// if totalCount = 8, 
//		
//		
//		
////		if (southCheckMate(travel) == true && northCheckMate(travel) == true && eastCheckMate(travel) == true && westCheckMate(travel) == true && northEastCheckMate(travel) == true && northWestCheckMate(travel) == true && southEastCheckMate(travel) == true && southWestCheckMate(travel) == true)
////		{
////			checkMate = true;
////			System.out.println("\n----------------------------------------------------------\nCHECKMATE - " + kingColorEnglish + " King is in checkmate!\n----------------------------------------------------------");
////		}
////		else {
////			checkMate = false;
////			System.out.println("Checkmate avoided. Play On.");
////		}
//		return checkMate;
//	}
//
//	private void southCheckMate(int travel, boolean block1, boolean meatShield1, boolean escape1, boolean threat1) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX + travel;
//		throneGhostY = startY;
//		
//		if(throneGhostX > 8)
//		{
//			block1 = true;
//			System.out.println("SOUTH BLOCKED.");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield1 = true; 
//		}
//		else 
//		{
//			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
//			pieces[startX][startY] = null; 
//			
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape1 = true;
//			}
//			else 
//			{
//				threat1 = true;
//				System.out.println("South Escape Route Compromised.");
//			}
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; 
//		}
//	}
//
//	private void northCheckMate(int travel, boolean block2, boolean meatShield2, boolean escape2, boolean threat2) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX - travel;
//		throneGhostY = startY;
//				
//		if(throneGhostX < 1)
//		{
//			block2 = true;
//			System.out.println("NORTH BLOCKED");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield2 = true;
//		}
//		else {
//			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
//			pieces[startX][startY] = null; 
//	
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape2 = true;
//			}
//			else {
//				threat2 = true;
//				System.out.println("North Escape Route Compromised.");
//			}
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; // or 
//		}
//	}
//	
//	private void eastCheckMate(int travel, boolean block3, boolean meatShield3, boolean escape3, boolean threat3) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX;
//		throneGhostY = startY + travel;
//			
//		if(throneGhostY > 8)
//		{
//			block3 = true;
//			System.out.println("EAST BLOCKED");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield3 = true;
//		}
//		else {
//			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
//			pieces[startX][startY] = null; 
//			
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape3 = true;
//			}
//			else {
//				threat3 = true;
//				System.out.println("East Escape Route Compromise.");
//			}
//				
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; // or 
//		}	
//	}
//	
//	private void westCheckMate(int travel, boolean block4, boolean meatShield4, boolean escape4, boolean threat4) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX;
//		throneGhostY = startY - travel;
//			
//		if(throneGhostY < 1)
//		{
//			block4 = true;
//			System.out.println("WEST BLOCKED");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield4 = true;
//		}
//		else {
//			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
//			pieces[startX][startY] = null; 
//			
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape4 = true;
//			}
//			else {
//				threat4 = true;
//				System.out.println("West Escape Route Compromise.");
//			}
//				
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; // or 
//		}	
//	}
//	
//	private void northEastCheckMate(int travel, boolean block5, boolean meatShield5, boolean escape5, boolean threat5) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX - travel;
//		throneGhostY = startY + travel;
//		
//		if(throneGhostX < 1 || throneGhostY > 8)
//		{
//			block5 = false;
//			System.out.println("NORTH EAST BLOCKED");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield5 = false;
//		}
//		else {
//			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
//			pieces[startX][startY] = null; 
//			
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape5 = false;
//			}
//			else {
//				threat5 = true;
//				System.out.println("north east Escape Route Compromised.");
//			}
//				
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; // or 
//		}
//	}
//	
//	private void northWestCheckMate(int travel, boolean block6, boolean meatShield6, boolean escape6, boolean threat6) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX - travel;
//		throneGhostY = startY - travel;
//		
//		if(throneGhostX < 1 || throneGhostY < 1)
//		{
//			block6 = true;
//			System.out.println("NORTH WEST BLOCKED");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield6 = true;
//		}
//		else {
//			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
//			pieces[startX][startY] = null; 
//			
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape6 = true;
//			}
//			else {
//				threat6 = false;
//				System.out.println("NorthWest Escape Route Blocked.");
//			}
//				
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; // or 
//		}
//	}
//
//	private void southEastCheckMate(int travel, boolean block7, boolean meatShield7, boolean escape7, boolean threat7) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX + travel;
//		throneGhostY = startY + travel;
//		
//		if(throneGhostX > 8 || throneGhostY > 8)
//		{
//			block7 = true;
//			System.out.println("SOUTH EAST BLOCKED");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield7 = true;
//		}
//		else {
//			pieces[throneGhostX][throneGhostY] = pieces[startX][startY];
//			pieces[startX][startY] = null; 
//			
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape7 = false;
//			}
//			else {
//				threat7 = true;
//				System.out.println("southEast Escape Route Blocked.");
//			}
//				
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; // or 
//		}
//	}
//
//	private void southWestCheckMate(int travel, boolean block8, boolean meatShield8, boolean escape8, boolean threat8) {
//		
//		int throneGhostX;
//		int throneGhostY;
//			
//		throneGhostX = startX + travel;
//		throneGhostY = startY - travel;
//			
//		if(throneGhostX > 8 || throneGhostY < 1)
//		{
//			block8 = true;
//			System.out.println("SOUTH WEST BLOCKED");
//		}
//		else if (pieces[throneGhostX][throneGhostY] != null && pieces[throneGhostX][throneGhostY].getColor().equals(meatShieldColor))
//		{
//			System.out.println("FRIENDLY PIECE -- CANNOT MOVE HERE.");
//			meatShield8 = true;
//		}
//		else {
//			if(!runCheckMateChecks(throneGhostX, throneGhostY))
//			{
//				System.out.println("Escape route at " + throneGhostY +  ", " + throneGhostX);
//				escape8 = false;
//			}
//			else {
//				threat8 = true;
//				//System.out.println("southWest Escape Route Blocked.");
//			}
//				
//			pieces[keepX][keepY] = pieces[throneGhostX][throneGhostY]; 
//			pieces[throneGhostX][throneGhostY] = null; // or 
//		}
//	}
//	
//	public boolean runCheckMateChecks(int throneX, int throneY) //, int startY, int startX, int endY, int endX 
//	{
//		boolean kingIsInCheck = false;
//		designateKingColorAndMeatShieldColorOnNewTurn(); //might need to add knights to this
//		//locateGhostKingOnBoard();
//		
//		//TEST CHECKS SYSTEM -------------------------------------------------------------------------------------------------------------------------------//
//		//crosses---------------------------------------------------------------------------//
//		kc1 = checkForEnemiesStraightUp(kingIsInCheck, throneX, throneY); //CHECKING FOR PIECE UP
//		kc2 = checkForEnemiesStraightDown(kingIsInCheck, throneX, throneY);	//CHECKING FOR PIECE DOWN
//		kc3 = checkForEnemiesStraightRight(kingIsInCheck, throneX, throneY); //moving right
//		kc4 = checkForEnemiesStraightLeft(kingIsInCheck, throneX, throneY); //moving left
//		
//		//diagonals //--------------------------------------------------------------------------------//
//		kc5 = checkForEnemiesDiagonalDownLeft(kingIsInCheck, throneX,throneY); //moving down-left
//		kc6 = checkForEnemiesDiagonalUpLeft(kingIsInCheck, throneX, throneY); //diagonal up left
//		kc7 = checkForEnemiesDiagonalUpRight(kingIsInCheck, throneX, throneY); //diagonal up right
//		kc8 = checkForenemiesDiagonalDownRight(kingIsInCheck, throneX, throneY); 	// diagonal down right
//		
//		//knight moves //--------------------------------------------------------------------------------//
//		kc9 = allKnightChecks(kingIsInCheck, throneX, throneY);
//		//-------------------------------------------------------------------------------//
//		
//		//finalizer //-------------------------------------------------------------------------------------------------------------------------------------------//
//		//negates one single "true" boolean at the end jepordizing the security of the king
//		if (kc1 == true || kc2 == true || kc3 == true || kc4 == true || kc5 == true || kc6 == true || kc7 == true || kc8 == true || kc9 == true)
//		{
//			kingIsInCheck = true;
//		}
//		//System.out.println("CHECKMATE STATUS: " + kingIsInCheck);
//		return kingIsInCheck;
//	}
//	//------------------------------------------------------------------------------------------------------------------------//
//	//------------------------------------------------------------------------------------------------------------------------//
//	
//	
//	
//	// SUPPORT // ----------------------------------------------------------------------------------------------------------------/
//	private void getMoveItemElements(Matcher basicMoveMatcher) //gets elements of move from the string that is read in. 
//	{
//		getWhichColor(basicMoveMatcher); //finds color + piece and its case
//		getInitialPosition(basicMoveMatcher); //initial position (placeY, placeX) //run transfer chars //run backwards column syndrome 
//		getFinalPosition(basicMoveMatcher); //final position (placeY, placeX) //run transfer chars //run backwards column syndrome 
//	}	
//	private void getWhichColor(Matcher basicMoveMatcher) {
//		color = basicMoveMatcher.group("color");
//		if (color.equals("d"))
//			piece = basicMoveMatcher.group("piece").toLowerCase();
//		else if(color.equals("l"))
//			piece = basicMoveMatcher.group("piece").toUpperCase();
//	}	
//	private void getInitialPosition(Matcher basicMoveMatcher) //finds initial position
//	{
//		starty = basicMoveMatcher.group("x");
//		transferChars();
//		
//		startx = Integer.parseInt(basicMoveMatcher.group("y"));
//		backwardsColumnNumberingSyndrome();
//	}
//	private void getFinalPosition(Matcher basicMoveMatcher) //finds final position
//	{
//		endy = basicMoveMatcher.group("x2");
//		transferChars2();
//		
//		endx = Integer.parseInt(basicMoveMatcher.group("y2"));
//		backwardsColumnNumberingSyndrome2();
//	}
//	public void transferChars()
//	{
//		switch (starty.charAt(0)) {
//		case 'a':
//			startY = 1;
//			break;
//		case 'b':
//			startY = 2;
//			break;
//		case 'c':
//			startY = 3;
//			break;
//		case 'd':
//			startY = 4;
//			break;
//		case 'e':
//			startY = 5;
//			break;
//		case 'f':
//			startY = 6;
//			break;
//		case 'g':
//			startY = 7;
//			break;
//		case 'h':
//			startY = 8;
//			break;
//		default:
//			break;
//		}
//	}
//	public void transferChars2()
//	{
//		switch (endy.charAt(0)) {
//		case 'a':
//			endY = 1;
//			break;
//		case 'b':
//			endY = 2;
//			break;
//		case 'c':
//			endY = 3;
//			break;
//		case 'd':
//			endY = 4;
//			break;
//		case 'e':
//			endY = 5;
//			break;
//		case 'f':
//			endY = 6;
//			break;
//		case 'g':
//			endY = 7;
//			break;
//		case 'h':
//			endY = 8;
//			break;
//		default:
//			break;
//		}
//	}
//	private void backwardsColumnNumberingSyndrome()
//	{
//		switch (startx) {
//		case 1:
//			startX = 8;
//			break;
//		case 2:
//			startX = 7;
//			break;
//		case 3:
//			startX = 6;
//			break;
//		case 4:
//			startX = 5;
//			break;
//		case 5:
//			startX = 4;
//			break;
//		case 6:
//			startX = 3;
//			break;
//		case 7:
//			startX = 2;
//			break;
//		case 8:
//			startX = 1;
//			break;
//		default:
//			break;
//		}
//	}
//	private void backwardsColumnNumberingSyndrome2()
//{
//	switch (endx) {
//	case 1:
//		endX = 8;
//		break;
//	case 2:
//		endX = 7;
//		break;
//	case 3:
//		endX = 6;
//		break;
//	case 4:
//		endX = 5;
//		break;
//	case 5:
//		endX = 4;
//		break;
//	case 6:
//		endX = 3;
//		break;
//	case 7:
//		endX = 2;
//		break;
//	case 8:
//		endX = 1;
//		break;
//	default:
//		break;
//	}
//}
//
////-----------------------------------------------------------------------------------------------------------------//
//}
//
//
//
