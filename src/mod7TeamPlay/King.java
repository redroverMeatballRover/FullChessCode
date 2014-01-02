package mod7TeamPlay;



public class King extends ChessPiece {
	
	String name;

	public King(String name) 
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
	
	public String getColorEnglish()
	{
		String pieceColor = null;
		if (name.equals("k"))
		{
			pieceColor = "Black";
		}
		else if (name.equals("K"))
		{
			pieceColor = "White";
		}
		return pieceColor;	
	}
	
	public String getColor()
	{
		String pieceColor = "";
		if (name.equals("k"))
		{
			pieceColor = "d";
		}
		else if (name.equals("K"))
		{
			pieceColor = "l";
		}
		return pieceColor;	
	}
	
	public String toString() {
		//something to find the color in here
		return "King";
	}
	
	
	//CHECKING IF MOVE IS LEGAL
	//-------------------------------------------------------------------------------------------------------//
	//legal moves
	public boolean legalMove(ChessPiece[][] chess, int startY, int startX, int endY, int endX) 
	{
		boolean thisIsAValidMove = false;
		int diagonalDistance = 1;
	
			//check if diagonals are valid //--------------------------------------------------------------------//
			if ((startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, right
				thisIsAValidMove = true;
			
			else if ((startX - diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, left
				thisIsAValidMove = true;
			
			else if ((startX + diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, right
				thisIsAValidMove = true;
			
			else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, left
				thisIsAValidMove = true;
			//--------------------------------------------------------------------------------------------------//
			else
			{
				thisIsAValidMove = validateKingUpDownLeftRight(startY, startX, endY, endX, diagonalDistance);
			}							
			return thisIsAValidMove;
		}
	
	//validate king up down right left moves.
	private boolean validateKingUpDownLeftRight(int startY, int startX, int endY, int endX, int diagonalDistance) 
	{
		boolean thisIsAValidMove;
		//MOVING NORTH-SOUTH //if its in the same column and not in the same row//
		if ((startX - 1) == endX && startY == endY ) 
			thisIsAValidMove = true;
		else if ((startX + 1) == endX && startY == endY ) 
			thisIsAValidMove = true;
		
		//MOVING EAST-WEST // if its in the same row but not the same column//
		else if((startY - 1) == endY && startX == endX)
			thisIsAValidMove = true;
		else if((startY + 1) == endY && startX == endX)
			thisIsAValidMove = true;
		
		else{
			System.out.println("INVALID MOVE - cannot move King like this.");
			System.out.println("------------------------------------------------------------");
			System.out.println("CHECKING - distance is endY - StartY. endY: " + endY + " startY:" + startY + " . endY - startY = " + diagonalDistance);
			thisIsAValidMove = false;
			}
		return thisIsAValidMove;
	}
	
	//-------------------------------------------------------------------------------------------------------//
	
	

	// CHECKING IF PATH IS CLEAR
	//-------------------------------------------------------------------------------------------------------//
	//check if the path is clear for king
	
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
//				int pieceRestoreX = endX;
//				int pieceRestoreY = endY; 
//				
				
				pathIsClear = pathDiagonalsAndCrosses(pieces, startY, startX, endY, endX, pathIsClear);	
				System.out.println("CAPTURED - captured the " + pieces[endX][endY].getColorEnglish() + " " + pieces[endX][endY].toString());
				pieces[endX][endY] = null;
				
				//pieces[endX][endY].
				return pathIsClear;
			}
		}
		//if end location is clear, check all other squares for piece interference(disregards color)
		else 
			pathIsClear = pathDiagonalsAndCrosses(pieces, startY, startX, endY, endX, pathIsClear);	
		
		return pathIsClear;
	}
	
	
	
	private boolean pathDiagonalsAndCrosses(ChessPiece[][] piece, int startY, int startX, int endY, int endX, boolean pathIsClear) 
	{
		//boolean pathIsClear = true;
		
			int diagonalDistance = 1;
		
			if ((startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, right
			{
				pathIsClear = true;
			}
			else if ((startX - diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, left
			{
				pathIsClear = true;
			}
			else if ((startX + diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, right
			{
				pathIsClear = true;
			}
			else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, left
			{
				pathIsClear = true;
			}
			else
			{
				pathIsClear = queenMovingUpDownRightLeft(piece, startY, startX, endY, endX, pathIsClear);
			}		
		return pathIsClear;
	}

	//non-diagonal movements 
	private boolean queenMovingUpDownRightLeft(ChessPiece[][] piece, int startY, int startX, int endY, int endX, boolean pathIsClear) {
		//MOVING NORTH-SOUTH //if its in the same column and not in the same row//
		if ((startX - 1) == endX && startY == endY ) 
		{
			pathIsClear = true;
		}
		else if ((startX + 1) == endX && startY == endY ) 
		{
			pathIsClear = true;
		}
		//MOVING EAST-WEST // if its in the same row but not the same column//
		else if((startY - 1) == endY && startX == endX)
		{
			pathIsClear = true;
		}
		else if((startY + 1) == endY && startX == endX)
		{
			pathIsClear = true;
		}
		return pathIsClear;
	}
	
	//-------------------------------------------------------------------------------------------------------//
	
}

