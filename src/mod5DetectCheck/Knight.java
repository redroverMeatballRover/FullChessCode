package mod5DetectCheck;

public class Knight extends ChessPiece {
	
	String name;

	public Knight(String name) 
	{
		this.name = name;
	}	
	
	public String getName()
	{
		String accountForKN = ""; 
		if (name.equals("k"))
		{
			accountForKN = "n";
		}
		else if (name.equals("K"))
		{
			accountForKN = "N";
		}
		return accountForKN;
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
	
	public String setInBoard() {
		String accountForKN = ""; 
		if (name.equals("k"))
		{
			accountForKN = "n";
		}
		else if (name.equals("K"))
		{
			accountForKN = "N";
		}
		
		return "| " + accountForKN + " |";
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
		return "Knight";
	}
	
	//legal moves
		public boolean legalMove(ChessPiece[][] chess, int startY, int startX, int endY, int endX) 
		{
			boolean thisIsAValidMove = false;
		
			if (startY != endY && startX != endX ) 
			{
				thisIsAValidMove = checkLegalKnightMove(startY, startX, endY, endX);
			}
			else
			{
				thisIsAValidMove = false;
				System.out.println("ATTEMPT TO BREAK SYSTEM - You did not move, or attempted to move to the same location your piece is already at. Please choose a valid move.");
			}
			return thisIsAValidMove;
		}

		//legal move for knight
		private boolean checkLegalKnightMove(int startY, int startX, int endY, int endX) {
			boolean thisIsAValidMove;
			if(endY == (startY + 1) || endY == (startY - 1))
			{
				if(endX == (startX + 2) || endX == (startX - 2))
				{
					thisIsAValidMove = true;
				}
				else
				{
					thisIsAValidMove = false;
					System.out.println("INVALID MOVE - KNIGHT - piece cannot be moved this way. Please make a valid move.");
				}
			}
			else if (endX == (startX + 1) || endX == (startX - 1))
			{
				if(endY == (startY + 2) || endY == (startY - 2))
				{
					thisIsAValidMove = true;
				}
				else
				{
					thisIsAValidMove = false;
					System.out.println("INVALID MOVE - KNIGHT - piece cannot be moved this way. Please make a valid move.");
				}
			}
			
			else
			{
				thisIsAValidMove = false;
				System.out.println("INVALID MOVE - KNIGHT - piece cannot be moved this way. Please make a valid move.");
			}
			return thisIsAValidMove;
		}
	
	
	
	
	
	
	//checks if path is clear, including final location.
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
				System.out.println("CAPTURED - captured the " + pieces[endX][endY].getColorEnglish() + " " + pieces[endX][endY].toString());
				pieces[endX][endY] = null;
				return pathIsClear;
			}
		}
		//if end location is clear, check all other squares for piece interference(disregards color)
		return pathIsClear;
	}
}
