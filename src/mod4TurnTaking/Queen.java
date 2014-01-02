package mod4TurnTaking;



public class Queen extends ChessPiece {
	
	String name;

	public Queen(String name) 
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
		if (name.equals("q"))
		{
			pieceColor = "d";
		}
		else if (name.equals("Q"))
		{
			pieceColor = "l";
		}
		else {
			System.out.println("Error confirming color for Queen.");
		}
		return pieceColor;	
	}
	
	public String getColorEnglish()
	{
		String pieceColor = null;
		if (name.equals("q"))
		{
			pieceColor = "Black";
		}
		else if (name.equals("Q"))
		{
			pieceColor = "White";
		}
		return pieceColor;	
	}
	
	public String toString() {
		//something to find the color in here
		return "Queen";
	}
	
	
	//LEGAL MOVES
	//-------------------------------------------------------------------------------------------------------//
	//legal moves
	public boolean legalMove(ChessPiece[][] chess, int startY, int startX, int endY, int endX) 
		{
			boolean thisIsAValidMove = false;
			int diagonalDistance = endY - startY;
			
			//checking if Diagonal and Cross moves are valid. 
			if (startY != endY && startX != endX || startY == endY && startX != endX || startY != endY && startX == endX) 
			{	
				thisIsAValidMove = validateDiagonalsAndCross(startY, startX, endY, endX, diagonalDistance);					
			}
			else
			{
				thisIsAValidMove = false;
				System.out.println("Confused");
			}
			
			return thisIsAValidMove;
		}

	//validates diagonal and cross moves.
	private boolean validateDiagonalsAndCross(int startY, int startX, int endY, int endX, int diagonalDistance) 
	{
		boolean thisIsAValidMove;
		//checks all diagonals first. 
		if ((startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, right
		{
			thisIsAValidMove = true;
		}
		else if ((startX - diagonalDistance) == endX && (startY - diagonalDistance) == endY) //down, left
		{
			thisIsAValidMove = true;
		}
		else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //down, right
		{
			thisIsAValidMove = true;
		}
		else if ((startX - diagonalDistance) == endX && (startY + diagonalDistance) == endY) //up, left
		{
			thisIsAValidMove = true;
		}
		
		//checks crosses.
		else
		{
			thisIsAValidMove = validateUpDownRightLeft(startY, startX, endY, endX, diagonalDistance);
		}
		return thisIsAValidMove;
	}

	//validates cross moves.
	private boolean validateUpDownRightLeft(int startY, int startX, int endY, int endX, int diagonalDistance) 
	{
		boolean thisIsAValidMove;
		//MOVING NORTH-SOUTH //if its in the same column and not in the same row//
		if (startY == endY && startX != endX ) 
			thisIsAValidMove = true;
		
		//MOVING EAST-WEST // if its in the same row but not the same column//
		else if(startX == endX && startY != endY)
			thisIsAValidMove = true;
		else{
			System.out.println("INVALID MOVE - cannot move Queen like this.");
			System.out.println("------------------------------------------------------------");
			System.out.println("CHECKING - distance is endY - StartY. endY: " + endY + " startY:" + startY + " . endY - startY = " + diagonalDistance);
			thisIsAValidMove = false;
		}
		return thisIsAValidMove;
	}
		
	//-------------------------------------------------------------------------------------------------------//
	
	
	//-------------------------------------------------------------------------------------------------------//
	//main
	//-------------------------------------------------------------------------------------------------------//
	
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
		
		//checks between final and initial
		private boolean checkSquaresBetweenInitialAndFinalLocations(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear) 
		{
			int diagonalDistance = (endY - startY);

			//UP-RIGHT
			if (diagonalDistance > 0 )//&& (startX + diagonalDistance) == endX && (startY + diagonalDistance) == endY) //moveing up, right
			{
				pathIsClear = queenMovingRight(pieces, startY, startX, endX, pathIsClear, diagonalDistance);
			}
			
			else if (diagonalDistance < 0) //triggers  distanceDown_Left/upLeft
			{
				int distanceDown_Left = startY - endY; //to get positive distance
				System.out.println("startY - endY = " + distanceDown_Left); 
				
				pathIsClear = queenMovingLeft(pieces, startY, startX, endY, endX, pathIsClear, distanceDown_Left);	
			}
			else 
			{
				pathIsClear = queenMoveUpDownRightLeft(pieces, startY, startX, endY, endX, pathIsClear);	
			}
			return pathIsClear;
		}

		//checks crosses
		private boolean queenMoveUpDownRightLeft(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear) 
		{
			int distanceToCrossX = (startX - endX);
			int distanceToCrossY = (startY - endY);
					
			if (distanceToCrossX > 0 || distanceToCrossY > 0) // if distance is positive //
			{
				
				pathIsClear = queenMovingUpOrRight(pieces, startY, startX, endY, endX, pathIsClear, distanceToCrossX, distanceToCrossY);
			}
			
			//going down
			else if (distanceToCrossX < 0 || distanceToCrossY < 0) // if distance is negative //
			{
				//x
				if (distanceToCrossX < 0) 
				{
					pathIsClear = queenMovingDown(pieces, startX, endY, endX, pathIsClear);
				}
				
				//y going left
				if (distanceToCrossY < 0) 
				{
					pathIsClear = queenMovingLeft(pieces, startY, endY, endX,
							pathIsClear);
				}
			}
			return pathIsClear;
		}

		//support moves
		//-------------------------------------------------------------------------------------------------------//
		private boolean queenMovingLeft(ChessPiece[][] pieces, int startY, int endY, int endX, boolean pathIsClear) 
		{
			int distanceSwitch = endY - startY;
			System.out.println("distance To cross = " + distanceSwitch);
			for (int i = startY + 1; i < endY ; i++)
			{
				System.out.println("Space " + i + " at y:" + endX + " , x: " + i +  ": " +  pieces[endX][i] + " distanceToCross: " + (distanceSwitch - i));
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
			return pathIsClear;
		}

		private boolean queenMovingDown(ChessPiece[][] pieces, int startX, int endY, int endX, boolean pathIsClear) 
		{
			int distanceSwitch = endX - startX;
			System.out.println("distance To cross = " + distanceSwitch);
			for (int i = startX + 1; i < endX ; i++)
			{
				System.out.println("Space " + i + " at y:" + endY + " , x: " + i +  ": " +  pieces[i][endY] + " distanceToCross: " + (distanceSwitch - i));
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
			return pathIsClear;
		}

		private boolean queenMovingUpOrRight(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear, int distanceToCrossX, int distanceToCrossY) 
		{
			//X -- going up
			if (distanceToCrossX > 0 )
			{
				pathIsClear = queenMovesUp(pieces, startX, endY, endX,
						pathIsClear, distanceToCrossX);
			}
			
			//Y -- going right
			if (distanceToCrossY > 0 )
			{
				pathIsClear = queenMovesRight(pieces, startY, endY, endX,
						pathIsClear, distanceToCrossY);
			}
			return pathIsClear;
		}

		private boolean queenMovingLeft(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear, int distanceDown_Left) 
		{
			if(startX < endX)
			{
				pathIsClear = queenMovingDownLeft(pieces, startY, startX, pathIsClear, distanceDown_Left);
			}
			else if ( startX > endX )
			{
				pathIsClear = queenMovingUpLeft(pieces, startY, startX, pathIsClear, distanceDown_Left);
			}
			else {
				pathIsClear = bishopMovingUpDownRightLeft(pieces, startY, startX, endY, endX, pathIsClear);		
			}
			return pathIsClear;
		}

		private boolean queenMovingUpLeft(ChessPiece[][] pieces, int startY, int startX, boolean pathIsClear, int distanceDown_Left) 
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
				{
					pathIsClear = true;
				}
			}
			return pathIsClear;
		}

		private boolean queenMovingDownLeft(ChessPiece[][] pieces, int startY,
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
				{
					pathIsClear = true;
				}
			}
			return pathIsClear;
		}

		private boolean queenMovingRight(ChessPiece[][] pieces, int startY,
				int startX, int endX, boolean pathIsClear, int diagonalDistance) {
			System.out.println("endY - startY = " + diagonalDistance); 
			
			if (startX > endX) //UP-RIGHT
			{
				pathIsClear = queenMovesUpRight(pieces, startY, startX, pathIsClear, diagonalDistance);
			}
			else if (startX < endX) //DOWN-RIGHT
			{
				pathIsClear = queenMovesDownRight(pieces, startY, startX, pathIsClear, diagonalDistance);	
			}
			return pathIsClear;
		}

		private boolean bishopMovingUpDownRightLeft(ChessPiece[][] pieces, int startY, int startX, int endY, int endX, boolean pathIsClear) 
		{
			int distanceToCrossX = (startX - endX);
			int distanceToCrossY = (startY - endY);
					
			if (distanceToCrossX > 0 || distanceToCrossY > 0) // if distance is positive //
			{
				//X -- going up
				if (distanceToCrossX > 0 )
				{
					pathIsClear = queenMovesUp(pieces, startX, endY, endX,pathIsClear, distanceToCrossX);
				}
				
				//Y -- going right
				else if (distanceToCrossY > 0 )
				{
					pathIsClear = queenMovesRight(pieces, startY, endY, endX, pathIsClear, distanceToCrossY);
				}
			}
			
			//--------------------------------------------------------------------------------------------------------------------///
			
			//going down
			if (distanceToCrossX < 0 || distanceToCrossY < 0) // if distance is negative //
			{
				//x
				if (distanceToCrossX < 0) 
				{
					pathIsClear = queenMovingDown(pieces, startX, endY, endX,
							pathIsClear);
				}
				
				//y going left
				else if (distanceToCrossY < 0) 
				{
					pathIsClear = queenMovingLeft(pieces, startY, endY, endX,
							pathIsClear);
				}
			}
			return pathIsClear;
		}

		private boolean queenMovesRight(ChessPiece[][] pieces, int startY, int endY, int endX, boolean pathIsClear, int distanceToCrossY) 
		{
			System.out.println("startY - endY = " + distanceToCrossY); //= 5
			for (int i = startY - 1; i > endY ; i--)
			{
				System.out.println("Space " + i + " at y:" + endY + " , x: " + i +  ": " +  pieces[endX][i] + " distanceToCross: " + (distanceToCrossY - i));
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
			return pathIsClear;
		}

		private boolean queenMovesUp(ChessPiece[][] pieces, int startX, int endY, int endX, boolean pathIsClear, int distanceToCrossX) 
		{
			System.out.println("startX - endX = " + distanceToCrossX); //= 5
			for (int i = startX - 1; i > endX ; i--)
			{
				System.out.println("Space " + i + " at y:" + endY + " , x: " + i +  ": " +  pieces[i][endY] + " distanceToCross: " + (distanceToCrossX - i));
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
			return pathIsClear;
		}

		private boolean queenMovesDownRight(ChessPiece[][] pieces, int startY,
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

		private boolean queenMovesUpRight(ChessPiece[][] pieces, int startY,
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














