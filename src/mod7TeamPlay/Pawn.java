package mod7TeamPlay;

public class Pawn extends ChessPiece {
	
	String name;

	public Pawn(String name) 
	{
		this.name = name;
	}	
	
	public String getName()
	{
		return name;
	}
	
	public String getColorEnglish()
	{
		String pieceColor = null;
		if (name.equals("p"))
		{
			pieceColor = "Black";
		}
		else if (name.equals("P"))
		{
			pieceColor = "White";
		}
		return pieceColor;	
	}
	
	public String setInBoard() {
		return "| " + name + " |";
	}
	
	public String getColor()
	{
		String pieceColor = "";
		if (name.equals("p"))
		{
			pieceColor = "d";
		}
		else if (name.equals("P"))
		{
			pieceColor = "l";
		}
		return pieceColor;	
	}
	
	public String toString() {
		//something to find the color in here
		return "Pawn";
	}
	
	
	//CHECKING IF MOVE IS LEGAL
	//-------------------------------------------------------------------------------------------------------//
	//legal moves
	public boolean legalMove(ChessPiece[][] chess, int startY, int startX, int endY, int endX) 
	{
		boolean thisIsAValidMove = false;
		int diagonalDistance = 1;
		int homeWhiteRow = 2;
		int homeBlackRow = 7;
		
		
	
		//color piece is white = he can move straight up, diagonal for attacking players
		//color piece is black = he can move straight down, diagonal for attacking players
		
		if (chess[startX][startY].getColor().equals("l"))
		{
			thisIsAValidMove = whitePawnMoveAndAttackRules(chess, startY, startX, endY, endX, thisIsAValidMove, diagonalDistance);
		}
		else if (chess[startX][startY].getColor().equals("d"))
		{
			thisIsAValidMove = blackPawnMoveAndAttackRules(chess, startY, startX, endY, endX, thisIsAValidMove, diagonalDistance);
		}	
	return thisIsAValidMove;
}

	private boolean blackPawnMoveAndAttackRules(ChessPiece[][] chess, int startY, int startX, int endY, int endX, boolean thisIsAValidMove, int diagonalDistance) {
		int blackHomeRow = 2;
		if (startX == blackHomeRow && (startX + 2) == endX && startY == endY  )
		{
			thisIsAValidMove = true;
		}
		else if ((startX + 1) == endX && startY == endY ) //straight
		{
			if (chess[endX][endY] != null) //if end location is full
			{
				thisIsAValidMove = false;
			}
			else { //if not
				thisIsAValidMove = true;
			}
		}
		else if ((startX - diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, left
		{
			if (chess[endX][endY] == null)
			{
				thisIsAValidMove = false;
			}
			else if (chess[endX][endY].getColor().equals("l"))
			{
				thisIsAValidMove = true;
			}
			else if (chess[endX][endY].getColor().equals("d"))
			{
				thisIsAValidMove = false;
			}
		}
		else if ((startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //down, right
		{
			if (chess[endX][endY] == null)
			{
				thisIsAValidMove = false;
			}
			else if (chess[endX][endY].getColor().equals("l"))
			{
				thisIsAValidMove = true;
			}
			else if (chess[endX][endY].getColor().equals("d"))
			{
				thisIsAValidMove = false;
			}
		}
		else {
			System.out.println("Invalid move for pawn.");
		}
		return thisIsAValidMove;
	}

	private boolean whitePawnMoveAndAttackRules(ChessPiece[][] chess, int startY, int startX, int endY, int endX, boolean thisIsAValidMove, int diagonalDistance) {
		int homeWhiteRow = 7;
		
		if (startX == homeWhiteRow && (startX - 2) == endX && startY == endY  )
		{
			thisIsAValidMove = true;
		}
		else if ((startX - 1) == endX && startY == endY ) 
		{
			if (chess[endX][endY] != null) //if end location is full
			{
				thisIsAValidMove = false;
			}
			else { //if not
				thisIsAValidMove = true;
			}
		}	
		else if ((startX - diagonalDistance) == endX && (startY - diagonalDistance) == endY) //up, left
		{
			if (chess[endX][endY] == null)
			{
				thisIsAValidMove = false;
			}
			else if (chess[endX][endY].getColor().equals("d"))
			{
				thisIsAValidMove = true;
			}
			else if (chess[endX][endY].getColor().equals("l"))
			{
				thisIsAValidMove = false;
			}
		}
		else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, right
		{
			if (chess[endX][endY] == null)
			{
				thisIsAValidMove = false;
			}
			else if (chess[endX][endY].getColor().equals("d"))
			{
				thisIsAValidMove = true;
			}
			else if (chess[endX][endY].getColor().equals("l"))
			{
				thisIsAValidMove = false;
			}
		}
		else {
			System.out.println("Invalid move for pawn.");
		}
		return thisIsAValidMove;
	}
	
	public boolean pathIsClear(ChessPiece[][] pieces, int startY, int startX, int endY, int endX) 
	{
		boolean o = true;
		return o;
	}
	
	//-------------------------------------------------------------------------------------------------------//
	
	
	
	
	
	
	
	
	
	

//	// CHECKING IF PATH IS CLEAR
//	//-------------------------------------------------------------------------------------------------------//
//	//check if the path is clear for king
//	
//	public boolean pathIsClear(ChessPiece[][] pieces, int startY, int startX, int endY, int endX) 
//	{
//		boolean pathIsClear = true;
//		//checks end location for pieces. if piece is present, no matter what color, it will not capture it.
//		if (pieces[endX][endY] != null) {
//
//			//pathIsClear = finalLocationCheckForPiece(pieces, endY, endX);
//			if (pieces[endX][endY].getColor().equals(pieces[startX][startY].getColor()))
//			{
//				System.out.println("BLOCKED - blocked by " + pieces[endX][endY].getColorEnglish() + " " + pieces[endX][endY].toString());
//				pathIsClear = false;
//			}
//			else {
////					int pieceRestoreX = endX;
////					int pieceRestoreY = endY; 
////					
//				
//				pathIsClear = pathDiagonalsAndCrosses(pieces, startY, startX, endY, endX, pathIsClear);	
//				System.out.println("CAPTURED - captured the " + pieces[endX][endY].getColorEnglish() + " " + pieces[endX][endY].toString());
//				pieces[endX][endY] = null;
//				
//				//pieces[endX][endY].
//				return pathIsClear;
//			}
//		}
//		//if end location is clear, check all other squares for piece interference(disregards color)
//		else 
//			pathIsClear = pathDiagonalsAndCrosses(pieces, startY, startX, endY, endX, pathIsClear);	
//		
//		return pathIsClear;
//	}
//	
//	
//	
//	private boolean pathDiagonalsAndCrosses(ChessPiece[][] piece, int startY, int startX, int endY, int endX, boolean pathIsClear) 
//	{
//		//boolean pathIsClear = true;
//		
//			int diagonalDistance = 1;
//		
//			if ((startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, right
//			{
//				pathIsClear = true;
//			}
//			else if ((startX - diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, left
//			{
//				pathIsClear = true;
//			}
//			else if ((startX + diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, right
//			{
//				pathIsClear = true;
//			}
//			else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, left
//			{
//				pathIsClear = true;
//			}
//			else
//			{
//				pathIsClear = queenMovingUpDownRightLeft(piece, startY, startX, endY, endX, pathIsClear);
//			}		
//		return pathIsClear;
//	}
//
//	//non-diagonal movements 
//	private boolean queenMovingUpDownRightLeft(ChessPiece[][] piece, int startY, int startX, int endY, int endX, boolean pathIsClear) {
//		//MOVING NORTH-SOUTH //if its in the same column and not in the same row//
//		if ((startX - 1) == endX && startY == endY ) 
//		{
//			pathIsClear = true;
//		}
//		else if ((startX + 1) == endX && startY == endY ) 
//		{
//			pathIsClear = true;
//		}
//		//MOVING EAST-WEST // if its in the same row but not the same column//
//		else if((startY - 1) == endY && startX == endX)
//		{
//			pathIsClear = true;
//		}
//		else if((startY + 1) == endY && startX == endX)
//		{
//			pathIsClear = true;
//		}
//		return pathIsClear;
//	}
		
		//-------------------------------------------------------------------------------------------------------//
	
}
