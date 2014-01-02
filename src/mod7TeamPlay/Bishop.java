package mod7TeamPlay;

public class Bishop extends ChessPiece {
	
	String name;

	public Bishop(String name) 
	{
		this.name = name;
	}	
	
	public String setInBoard() {
		return "| " + name + " |";
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getColor()
	{
		String pieceColor = "";
		if (name.equals("b"))
		{
			pieceColor = "d";
		}
		else if (name.equals("B"))
		{
			pieceColor = "l";
		}
		return pieceColor;	
	}
	
	public String getColorEnglish()
	{
		String pieceColor = null;
		if (name.equals("b"))
		{
			pieceColor = "Black";
		}
		else if (name.equals("B"))
		{
			pieceColor = "White";
		}
		return pieceColor;	
	}
	
	public String toString() {
		//something to find the color in here
		return "Bishop";
	}


	//CHECK IF ITS A LEGAL MOVE
	//-------------------------------------------------------------------------------------------------------//

	//check if move is legal
	public boolean legalMove(ChessPiece[][] chess, int startY, int startX, int endY, int endX) 
	{
		boolean thisIsAValidMove = false;
		int diagonalDistance = endY - startY;
		
		//calculates the diagonals
		if (startY != endY && startX != endX || startY == endY && startX != endX || startY != endY && startX == endX) 
		{
			thisIsAValidMove = calculatingDiagonalPatternPathObstructions(startY, startX, endY, endX, diagonalDistance);
		}
		//calculates the up-down-left-right
		else
		{
			thisIsAValidMove = false;
			System.out.println("ATTEMPT TO BREAK SYSTEM - You did not move, or attempted to move to the same location your piece is already at. Please choose a valid move.");
		}
		
		return thisIsAValidMove;
	}

	//calculates if diagonal moves are legal
	private boolean calculatingDiagonalPatternPathObstructions(int startY, int startX, int endY, int endX, int diagonalDistance) 
	{
		boolean thisIsAValidMove;
		if ((startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, right
			thisIsAValidMove = true;
		else if ((startX - diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, left
			thisIsAValidMove = true;
		else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //down, right
			thisIsAValidMove = true;
		else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, left
			thisIsAValidMove = true;
		else
		{
			System.out.println("INVALID MOVE - cannot move bishop like this.");
			System.out.println("------------------------------------------------------------");
			System.out.println("CHECKING - distance is endY - StartY. endY: " + endY + " startY:" + startY + " . endY - startY = " + diagonalDistance);
			thisIsAValidMove = false;
		}
		return thisIsAValidMove;
	}
		
	//-------------------------------------------------------------------------------------------------------//
	
	//CHECKS IF PATH IS CLEAR
	//-------------------------------------------------------------------------------------------------------//
	//checks to see if end location is clear. 
	public boolean pathIsClear(ChessPiece[][] pieces, int startY, int startX, int endY, int endX) 
	{
		boolean pathIsClear = true;
		//checks end location for pieces. if piece is present, no matter what color, it will not capture it.
		if (pieces[endX][endY] != null) {

			//pathIsClear = finalLocationCheckForPiece(pieces, endY, endX);
			if (pieces[endX][endY].getColor().equals(pieces[startX][startY].getColor()))
			{
				System.out.println("BLOCKED - blocked by " + pieces[endX][endY].getColorEnglish() + " " + pieces[endX][endY].toString());
				pathIsClear = false;
			}
			else {
				pathIsClear = checkSquaresBetweenInitialAndFinalLocations(pieces, startY, startX, endY, endX, pathIsClear);
				System.out.println("CAPTURED - captured the " + pieces[endX][endY].getColorEnglish() + " " + pieces[endX][endY].toString());
				pieces[endX][endY] = null;
				
				//pieces[endX][endY].
				return pathIsClear;
			}
		}
		//if end location is clear, check all other squares for piece interference(disregards color)
		else 
			pathIsClear = checkSquaresBetweenInitialAndFinalLocations(pieces, startY, startX, endY, endX, pathIsClear);
		
		return pathIsClear;
	}
	
	//checks all squares between initial and final locations. 
	private boolean checkSquaresBetweenInitialAndFinalLocations(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear)
	{
		int diagonalDistance = (endY - startY);	
		pathIsClear = bishopMovingRight(pieces, startY, startX, endX, pathIsClear, diagonalDistance);
		pathIsClear = bishopMovingLeft(pieces, startY, startX, endY, endX, pathIsClear, diagonalDistance);
		return pathIsClear;
	}

	//various moves
	private boolean bishopMovingLeft(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear, int diagonalDistance) 
	{
		if (diagonalDistance < 0) //triggers  distanceDown_Left/upLeft
		{
			int distanceDown_Left = startY - endY; //to get positive distance
			System.out.println("startY - endY = " + distanceDown_Left); //= 5
			
			if(startX < endX)
			{
				pathIsClear = rookMovesDownLeft(pieces, startY, startX, pathIsClear, distanceDown_Left);
			}
			else if ( startX > endX )
			{
				pathIsClear = rookMovesUpLeft(pieces, startY, startX, pathIsClear, distanceDown_Left);
			}
		}
		return pathIsClear;
	}

	private boolean rookMovesUpLeft(ChessPiece[][] pieces, int startY, int startX, boolean pathIsClear, int distanceDown_Left) 
	{
		for (int i = 1; i < distanceDown_Left ; i++)//UP-LEFT
		{
			if (pieces[startX - i][startY - i] != null)
			{
				System.out.println("BLOCKED - path blocked by " + pieces[startX - i][startY - i].toString());
				pathIsClear = false;
				break;
			}
			else 
				pathIsClear = true;
		}
		return pathIsClear;
	}

	private boolean rookMovesDownLeft(ChessPiece[][] pieces, int startY,
			int startX, boolean pathIsClear, int distanceDown_Left) {
		for (int i = 1; i < distanceDown_Left ; i++)//DOWN-LEFT
		{
			if (pieces[startX + i][startY - i] != null)
			{
				System.out.println("BLOCKED - path blocked by " + pieces[startX + i][startY - i].toString());
				pathIsClear = false;
				break;
			}
			else 
				pathIsClear = true;
		}
		return pathIsClear;
	}

	private boolean bishopMovingRight(ChessPiece[][] pieces, int startY, int startX, int endX, boolean pathIsClear, int diagonalDistance) 
	{
		//UP-RIGHT
		if (diagonalDistance > 0 )//&& (startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //moveing up, right
		{
			System.out.println("endY - startY = " + diagonalDistance); 
			
			if (startX > endX) //UP-RIGHT
				pathIsClear = bishopMovesUpRight(pieces, startY, startX, pathIsClear, diagonalDistance);
			else if (startX < endX) //DOWN-RIGHT
				pathIsClear = bishopMovesDownRight(pieces, startY, startX, pathIsClear, diagonalDistance);
		}
		return pathIsClear;
	}

	private boolean bishopMovesDownRight(ChessPiece[][] pieces, int startY,
			int startX, boolean pathIsClear, int diagonalDistance) {
		for (int i = 1; i < diagonalDistance; i++)
		{
			if (pieces[startX + i][startY + i] != null) //if (pieces[startX - i][startY + i] != null)
			{
				System.out.println("BLOCKED - path blocked by " + pieces[startX + i][startY + i].toString());
				pathIsClear = false;
				break;
			}
			else 
				pathIsClear = true;
		}
		return pathIsClear;
	}

	private boolean bishopMovesUpRight(ChessPiece[][] pieces, int startY,
			int startX, boolean pathIsClear, int diagonalDistance) {
		for (int i = 1; i < diagonalDistance; i++)
		{
			if (pieces[startX - i][startY + i] != null) //if (pieces[startX - i][startY + i] != null)
			{
				System.out.println("BLOCKED - path blocked by " + pieces[startX - i][startY + i].toString());
				pathIsClear = false;
				break;
			}
			else 
				pathIsClear = true;
		}
		return pathIsClear;
	}	
	//-------------------------------------------------------------------------------------------------------//
}
