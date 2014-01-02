package mod4TurnTaking;

public class Rook extends ChessPiece {	
	String name;
	public Rook(String name) 
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
		String pieceColor = null;
		if (name.equals("r"))
		{
			pieceColor = "d";
		}
		else if (name.equals("R"))
		{
			pieceColor = "l";
		}
		return pieceColor;	
	}
	
	public String getColorEnglish()
	{
		String pieceColor = null;
		if (name.equals("r"))
		{
			pieceColor = "Black";
		}
		else if (name.equals("R"))
		{
			pieceColor = "White";
		}
		return pieceColor;	
	}
	
	
	public String toString() {
		//something to find the color in here
		return "Rook";
	}
	
	//legal moves
	//-------------------------------------------------------------------------------------------------------//
	
	public boolean legalMove(ChessPiece[][] chess, int startY, int startX, int endY, int endX) 
	{
		boolean thisIsAValidMove = false;
		
		//MOVING NORTH-SOUTH //if its in the same column and not in the same row//
		if (startY == endY && startX != endX ) 
			thisIsAValidMove = true;
		
		//MOVING EAST-WEST // if its in the same row but not the same column//
		else if(startX == endX && startY != endY)
			thisIsAValidMove = true;
		else
			thisIsAValidMove = false;
		return thisIsAValidMove;
	}
	//-------------------------------------------------------------------------------------------------------//
	
	
	//CHECK IF PATH IS CLEAR
	//-------------------------------------------------------------------------------------------------------//
	
	//CHECK IF PATH IS CLEAR
		//-------------------------------------------------------------------------------------------------------//
		
		//checks to see that piece does not bunny hop over other friendly peices. //built for  BACKWARDS ARRAY SYNDROME.
		public boolean pathIsClear(ChessPiece[][] pieces, int startY, int startX, int endY, int endX) 
		{		
			boolean pathIsClear = checkingIfPathIsClear(pieces, startY, startX, endY, endX); 
			return pathIsClear;
		}

		//checks to see if end location is clear. 
		private boolean checkingIfPathIsClear(ChessPiece[][] pieces, int startY, int startX, int endY, int endX) 
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
					pathIsClear = checkAllSquaresBetweenInitialAndFinal(pieces, startY, startX, endY, endX, pathIsClear);	
					System.out.println("CAPTURED - captured the " + pieces[endX][endY].getColorEnglish() + " " + pieces[endX][endY].toString());
					pieces[endX][endY] = null;
					
					//pieces[endX][endY].
					return pathIsClear;
				}
			}
			//if end location is clear, check all other squares for piece interference(disregards color)
			else 
				pathIsClear = checkAllSquaresBetweenInitialAndFinal(pieces, startY, startX, endY, endX, pathIsClear);	
			
			return pathIsClear;
		}

		//checks all squares between initial and final.
		private boolean checkAllSquaresBetweenInitialAndFinal( ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear) {
			int distanceToCrossX = (startX - endX);
			int distanceToCrossY = (startY - endY);
				
			//moving up / moving right
			if (distanceToCrossX > 0 || distanceToCrossY > 0) // if distance is positive //
			{
				pathIsClear = goingUpOrMovingRight(pieces, startY, startX, endY, endX, pathIsClear, distanceToCrossX, distanceToCrossY);
			}

			//going down / moving left
			if (distanceToCrossX < 0 || distanceToCrossY < 0) // if distance is negative //
			{
				pathIsClear = rookMovingDown(pieces, startX, endY, endX, pathIsClear, distanceToCrossX);
				pathIsClear = rookMovingLeft(pieces, startY, endY, endX, pathIsClear, distanceToCrossY);
			}
			return pathIsClear;
		}
		
		//rook moving left
		private boolean rookMovingLeft(ChessPiece[][] pieces, int startY, int endY,
				int endX, boolean pathIsClear, int distanceToCrossY) {
			//y going left
			if (distanceToCrossY < 0) 
			{
				int distanceSwitch = endY - startY;
				//System.out.println("distance To cross = " + distanceSwitch);
				for (int i = startY + 1; i < endY ; i++)
				{
					//System.out.println("Space " + i + " at y:" + endX + " , x: " + i +  ": " +  pieces[endX][i] + " distanceToCross: " + (distanceSwitch - i));
					if (pieces[endX][i] != null)
					{
						System.out.println("BLOCKED - path blocked by " + pieces[endX][i].toString() + " at location (" + endX + ", " + i +")");
						pathIsClear = false;
						break;
					}
					else 
					{
						//System.out.println("CLEAR - path is clear.");
						pathIsClear = true;
					}
				}
			}
			return pathIsClear;
		}

		//rook moving down
		private boolean rookMovingDown(ChessPiece[][] pieces, int startX, int endY,
				int endX, boolean pathIsClear, int distanceToCrossX) {
			//x
			if (distanceToCrossX < 0) 
			{
				int distanceSwitch = endX - startX;
				//System.out.println("distance To cross = " + distanceSwitch);
				for (int i = startX + 1; i < endX ; i++)
				{
					//System.out.println("Space " + i + " at y:" + endY + " , x: " + i +  ": " +  pieces[i][endY] + " distanceToCross: " + (distanceSwitch - i));
					if (pieces[i][endY] != null)
					{
						System.out.println("BLOCKED - path blocked by " + pieces[i][endY].toString() + " at location (" + endY + ", " + i +")");
						pathIsClear = false;
						break;
					}
					else 
					{
						//System.out.println("CLEAR - path is clear.");
						pathIsClear = true;
					}
				}
			}
			return pathIsClear;
		}

		//holder for methods below. 
		private boolean goingUpOrMovingRight(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear, int distanceToCrossX, int distanceToCrossY) {
			pathIsClear = rookMoveUp(pieces, startX, endY, endX, pathIsClear, distanceToCrossX);
			pathIsClear = rookMoveRight(pieces, startY, endY, endX, pathIsClear, distanceToCrossY);
			return pathIsClear;
		}

		//rook moves right
		private boolean rookMoveRight(ChessPiece[][] pieces, int startY, int endY, int endX, boolean pathIsClear, int distanceToCrossY) {
			//Y -- going right
			if (distanceToCrossY > 0 )
			{
				//System.out.println("startY - endY = " + distanceToCrossY); //= 5
				for (int i = startY - 1; i > endY ; i--)
				{
					//System.out.println("Space " + i + " at y:" + endY + " , x: " + i +  ": " +  pieces[endX][i] + " distanceToCross: " + (distanceToCrossY - i));
					if (pieces[endX][i] != null)
					{
						System.out.println("BLOCKED - path blocked by " + pieces[endX][i].toString());
						pathIsClear = false;
						break;
					}
					else 
					{
						//System.out.println("CLEAR - path is clear.");
						pathIsClear = true;
					}
				}
			}
			return pathIsClear;
		}

		//rook moves up
		private boolean rookMoveUp(ChessPiece[][] pieces, int startX, int endY,
				int endX, boolean pathIsClear, int distanceToCrossX) {
			//X -- going up
			if (distanceToCrossX > 0 )
			{
				//System.out.println("startX - endX = " + distanceToCrossX); //= 5
				for (int i = startX - 1; i > endX ; i--)
				{
					//System.out.println("Space " + i + " at y:" + endY + " , x: " + i +  ": " +  pieces[i][endY] + " distanceToCross: " + (distanceToCrossX - i));
					if (pieces[i][endY] != null)
					{
						System.out.println("BLOCKED - path blocked by " + pieces[i][endY].toString());
						pathIsClear = false;
						break;
					}
					else 
					{
						//System.out.println("CLEAR - path is clear.");
						pathIsClear = true;
					}
				}
			}
			return pathIsClear;
		}	
		
	//-------------------------------------------------------------------------------------------------------//
}
