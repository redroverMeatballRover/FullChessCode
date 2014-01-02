package mod7TeamPlay;

import java.io.IOException;
import java.util.ArrayList;

public class IndividualPieceMoveMap {

	PlayerIsTextFile player = new PlayerIsTextFile();
	boolean theTurn;
	int chessBoardInteger = 9;
	ChessPiece[][] potentialMove = new ChessPiece[chessBoardInteger][chessBoardInteger];
	ChessPiece[][] initialPos;	
	String kingColorDesignation;
	String meatShieldColor;
	String enemyColor;
	String englishTeamColor;
	int potentialX = 0;
	int potentialX2 = 0;
	int potentialY = 0;
	
	int boardLength = 8;
	
	int upDownMoves = 0;
	int rightLeft = 0;
	int diagonals = 0;
	
	int southMoves = 0;
	int westMoves = 0;
	int eastMoves = 0;
	
	int distance1 = 0;
	int distance2 = 0;
	
	int pieceX;
	int pieceY;
	
	GPSLocation locationXY;
	GPSLocation matcherPositions = new GPSLocation(pieceX, pieceY);
	
	String piecex = "";
	String piecey = "";
	
	String allPotentialMoves = "";
	String allRookMoves = "";
	String allBishopMoves = "";
	String allQueenMoves = "";
	String allKnightMoves = "";
	String allWhitePawnMoves = "";
	String allBlackPawnMoves = "";
	String allKingMoves = "";
		
	ArrayList<GPSLocation> potentialMoveList = new ArrayList<GPSLocation>();
//	ArrayList<GPSLocation> rookMoveList = new ArrayList<GPSLocation>();
//	
//	ArrayList<GPSLocation> bishopMoveList = new ArrayList<GPSLocation>();
//	ArrayList<GPSLocation> queenMoveList = new ArrayList<GPSLocation>();
//	ArrayList<GPSLocation> knightMoveList = new ArrayList<GPSLocation>();
//	ArrayList<GPSLocation> whitePawnMoveList = new ArrayList<GPSLocation>();
//	ArrayList<GPSLocation> blackPawnMoveList = new ArrayList<GPSLocation>();
//	ArrayList<GPSLocation> kingMoveList = new ArrayList<GPSLocation>();
	

	StringBuffer sb = new StringBuffer(2000); 
	String pieceOutput = "";
	
	//print out the moving team's potential moves
	// -- get each piece on that team
	// -- print out each coordinate each piece could move to, until they run out of room, or are 
	//	  blocked by another friendly piece
	// -- if piece in the way is an enemy color, take that enemy place
	// -- create an arrayList for each living piece, rooks under one array list, pawns, and so on
	// -- wipe arrayList clean after each move. 
	// -- checkmate references the lists. 
	// -- note if piece can capture. 
	// print out of potential moves formatted as:
	// rook potential moves: a2, a3, a4, a5*
	
	String piece = "";
	String pieceColor = "";
	
	
	public void getIndividualPotentialMoves(ChessPiece[][] theCurrentBoard, boolean isWhiteTurn, int startX, int startY, String pathPiece, String pathColor ) throws IOException
	{

		potentialMove = theCurrentBoard;
		theTurn = isWhiteTurn;
		pieceX = startX;
		pieceY = startY;
		piece = pathPiece;
		pieceColor = pathColor;
		
		//System.out.println("Piece: " + potentialMove[pieceX][pieceY].getColorEnglish() + " " + potentialMove[pieceX][pieceY].toString() + " at " + pieceY + ", " + pieceX);
		//System.out.println("Piece: " + pieceColor + " " + piece + " at " + pieceY + ", " + pieceX);
		
		
		designateKingColorAndMeatShieldColorOnNewTurn();
		mapOutMoves();
		
		System.out.println("Potential " + englishTeamColor +  " " + piece + " moves: " +  allPotentialMoves);
		sb.delete(0, sb.length()); 
		
	}
	
	private void designateKingColorAndMeatShieldColorOnNewTurn() {
		if (theTurn) //means that whiteTurn has passed isWhiteTurn and must complete the move for black to move. 
 		{
			kingColorDesignation = "l";
			meatShieldColor = "l";
			enemyColor = "d";
			englishTeamColor = "White";
		}
		else {
			kingColorDesignation = "d";
			meatShieldColor = "d";
			enemyColor = "l";
			englishTeamColor = "Black";
		}
	}

	
	public void mapOutMoves()
	{
		if (piece.equals("Rook"))
		{
			upDownMoves = pieceX;
			distance1 = upDownMoves;
			rightLeft = (boardLength - pieceY);
			distance2 = rightLeft;
		
			northMovementBank(pieceX, pieceY);
			southMovementBank(pieceX, pieceY);
			eastMovementBank(pieceX, pieceY);
			westMovementBank(pieceX, pieceY);
			
			allPotentialMoves = pieceOutput; //mixing if multiple rooks/queens?
		}
		else if (piece.equals("Bishop"))
		{
			diagonals = (boardLength - pieceY);
			distance2 = diagonals;

			southEastMovementBank(pieceX, pieceY);
			northEastMovementBank(pieceX, pieceY);
			northWestMovementBank(pieceX, pieceY);
			southWestMovementBank(pieceX, pieceY);
			
			allPotentialMoves = pieceOutput;
		}
		else if (piece.equals("Queen"))
		{
			upDownMoves = pieceX;
			distance1 = upDownMoves;
			diagonals = (boardLength - pieceY);
			distance2 = diagonals;

			northMovementBank(pieceX, pieceY);
			southMovementBank(pieceX, pieceY);
			eastMovementBank(pieceX, pieceY);
			westMovementBank(pieceX, pieceY);
			southEastMovementBank(pieceX, pieceY);
			northEastMovementBank(pieceX, pieceY);
			northWestMovementBank(pieceX, pieceY);
			southWestMovementBank(pieceX, pieceY);		
			
			allPotentialMoves = pieceOutput;
		}
		
		else if (piece.equals("Knight"))
		{
			upDownMoves = pieceX;
			distance1 = upDownMoves;
			diagonals = (boardLength - pieceY);
			distance2 = diagonals;

			knightMovement1Bank(pieceX, pieceY);
			knightMovement2Bank(pieceX, pieceY);
			knightMovement3Bank(pieceX, pieceY);
			knightMovement4Bank(pieceX, pieceY);
			knightMovement5Bank(pieceX, pieceY);
			knightMovement6Bank(pieceX, pieceY);
			knightMovement7Bank(pieceX, pieceY);
			knightMovement8Bank(pieceX, pieceY);
			
			allPotentialMoves = pieceOutput;
		}
		else if (piece.equals("Pawn") && pieceColor.equals("l"))
		{
			whitePawnMoveBank(pieceX, pieceY);
			whitePawnAttackDiagonalLeftBank(pieceX, pieceY);
			whitePawnAttackDiagonalRightBank(pieceX, pieceY);
			allPotentialMoves = pieceOutput;
		}
		else if (piece.equals("Pawn") && pieceColor.equals("d"))
		{
			darkPawnMoveBank(pieceX, pieceY);
			darkPawnAttackDiagonalLeftBank(pieceX, pieceY);
			darkPawnAttackDiagonalRightBank(pieceX, pieceY);
			allPotentialMoves = pieceOutput;
		}
		else if (piece.equals("King"))
		{
			kingMovementNorthBank(pieceX, pieceY);
			kingMovementSouthBank(pieceX, pieceY);
			kingMovementEastBank(pieceX, pieceY);
			kingMovementWestBank(pieceX, pieceY);
			kingMovementSouthEastBank(pieceX, pieceY);
			kingMovementSouthWestBank(pieceX, pieceY);
			kingMovementNorthEastBank(pieceX, pieceY);
			kingMovementNorthWestBank(pieceX, pieceY);
			allPotentialMoves = pieceOutput;
		}
	}	
	
	
	private void northMovementBank(int pieceX, int pieceY) {
			for(int i = 1; i < distance1 + 1; i++ ) //getting north moves
			{		
				potentialX = (pieceX - i);
				potentialY = pieceY;
				//System.out.println( potentialY + ", " + potentialX);
				
				if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
				{
					break;
				}
				else {
					if (potentialMove[potentialX][potentialY] != null)
					{	
						if (potentialX < 1)
						{
							break;
						}	
						else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
						{
							break; //stop the for loop, can't move anymore
						}
						else {
							
							//potentialMoveList.add(matcherPositions(potentialX, potentialY)); //get it all going north
							//matcherPositions
							//potentialMoveList.add(potentialMove[potentialX][potentialY]);
							locationXY = new GPSLocation(potentialX, potentialY);
							potentialMoveList.add(locationXY);
							
							transferIntX();
							transferIntY();
							String addingToMoves  =  piecey + piecex + "*";
							pieceOutput = sb.append(addingToMoves).toString();
							break; //stop the loop, captured and stop.
						}
					}
					else
					{
						//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + ", ";
						pieceOutput = sb.append(addingToMoves).toString();
					}	
				}
			}
		}
		
	private void southMovementBank(int pieceX, int pieceY) {
			for(int i = 1; i < distance1 + 1; i++ ) //getting north moves
			{		
				potentialX = (pieceX + i);
				potentialY = pieceY;
				//System.out.println( potentialY + ", " + potentialX);
				
				if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
				{
					break;
				}
				else {
					if (potentialMove[potentialX][potentialY] != null)
					{	
						if (potentialX < 1)
						{
							break;
						}	
						else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
						{
							break; //stop the for loop, can't move anymore
						}
						else {
							
							//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
							locationXY = new GPSLocation(potentialX, potentialY);
							potentialMoveList.add(locationXY);
							
							transferIntX();
							transferIntY();
							String addingToMoves  =  piecey + piecex + "*";
							pieceOutput = sb.append(addingToMoves).toString();
							break; //stop the loop, captured and stop.
						}
					}
					else
					{
						//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + ", ";
						pieceOutput = sb.append(addingToMoves).toString();
					}	
				}
			}
		}
	
	private void eastMovementBank(int pieceX, int pieceY) {
			for(int i = 1; i < distance2 + 1; i++ ) //getting north moves
			{		
				potentialX = pieceX;
				potentialY = pieceY + i;
				//System.out.println( potentialY + ", " + potentialX);
				
				if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
				{
					break;
				}
				else {
					if (potentialMove[potentialX][potentialY] != null)
					{	
						if (potentialX < 1)
						{
							break;
						}	
						else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
						{
							break; //stop the for loop, can't move anymore
						}
						else {
							
							//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
							
							locationXY = new GPSLocation(potentialX, potentialY);
							potentialMoveList.add(locationXY);
							
							transferIntX();
							transferIntY();
							String addingToMoves  =  piecey + piecex + "*";
							pieceOutput = sb.append(addingToMoves).toString();
							break; //stop the loop, captured and stop.
						}
					}
					else
					{
						//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
						
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + ", ";
						pieceOutput = sb.append(addingToMoves).toString();
					}	
				}
			}
		}		
		
	private void westMovementBank(int pieceX, int pieceY) {
			for(int i = 1; i < distance2 + 1; i++ ) //getting north moves
			{		
				potentialX = pieceX;
				potentialY = pieceY - i;
			//	System.out.println( potentialY + ", " + potentialX);
				
				if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
				{
					break;
				}
				else {
					if (potentialMove[potentialX][potentialY] != null)
					{	
						if (potentialX < 1)
						{
							break;
						}	
						else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
						{
							break; //stop the for loop, can't move anymore
						}
						else {
							
							//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
							
							locationXY = new GPSLocation(potentialX, potentialY);
							potentialMoveList.add(locationXY);
							
							transferIntX();
							transferIntY();
							String addingToMoves  =  piecey + piecex + "*";
							pieceOutput = sb.append(addingToMoves).toString();
							break; //stop the loop, captured and stop.
						}
					}
					else
					{
						//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
						
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + ", ";
						pieceOutput = sb.append(addingToMoves).toString();
					}	
				}
			}
		}		

	private void southEastMovementBank(int pieceX, int pieceY) {
		for(int i = 1; i < distance2 + 1; i++ ) //getting north moves
		{		
			potentialX = (pieceX + i);
			potentialY = (pieceY + i);
			//System.out.println( potentialY + ", " + potentialX);
			
			if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
			{
				break;
			}
			else {
				if (potentialMove[potentialX][potentialY] != null)
				{	
					if (potentialX < 1)
					{
						break;
					}	
					else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
					{
						break; //stop the for loop, can't move anymore
					}
					else {
						
						//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
						
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + "*";
						pieceOutput = sb.append(addingToMoves).toString();
						break; //stop the loop, captured and stop.
					}
				}
				else
				{
					//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + ", ";
					pieceOutput = sb.append(addingToMoves).toString();
				}	
			}
		}
	}	
	
	private void northEastMovementBank(int pieceX, int pieceY) {
		for(int i = 1; i < distance2 + 1; i++ ) //getting north moves
		{		
			potentialX = (pieceX - i);
			potentialY = (pieceY + i);
			//System.out.println( potentialY + ", " + potentialX);
			
			if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
			{
				break;
			}
			else {
				if (potentialMove[potentialX][potentialY] != null)
				{	
					if (potentialX < 1)
					{
						break;
					}	
					else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
					{
						break; //stop the for loop, can't move anymore
					}
					else {
						
						//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
						
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + "*";
						pieceOutput = sb.append(addingToMoves).toString();
						break; //stop the loop, captured and stop.
					}
				}
				else
				{
					//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + ", ";
					pieceOutput = sb.append(addingToMoves).toString();
				}	
			}
		}
	}	

	private void northWestMovementBank(int pieceX, int pieceY) {
		for(int i = 1; i < distance2 + 1; i++ ) //getting north moves
		{		
			potentialX = (pieceX - i);
			potentialY = (pieceY - i);
			//System.out.println( potentialY + ", " + potentialX);
			
			if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
			{
				break;
			}
			else {
				if (potentialMove[potentialX][potentialY] != null)
				{	
					if (potentialX < 1)
					{
						break;
					}	
					else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
					{
						break; //stop the for loop, can't move anymore
					}
					else {
						
						//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
						
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + "*";
						pieceOutput = sb.append(addingToMoves).toString();
						break; //stop the loop, captured and stop.
					}
				}
				else
				{
					//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + ", ";
					pieceOutput = sb.append(addingToMoves).toString();
				}	
			}
		}
	}	

	private void southWestMovementBank(int pieceX, int pieceY) {
		for(int i = 1; i < distance2 + 1; i++ ) //getting north moves
		{		
			potentialX = (pieceX + i);
			potentialY = (pieceY - i);
			//System.out.println( potentialY + ", " + potentialX);
			
			if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
			{
				break;
			}
			else {
				if (potentialMove[potentialX][potentialY] != null)
				{	
					if (potentialX < 1)
					{
						break;
					}	
					else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
					{
						break; //stop the for loop, can't move anymore
					}
					else {
						
						//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
						
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						String addingToMoves  =  piecey + piecex + "*";
						pieceOutput = sb.append(addingToMoves).toString();
						break; //stop the loop, captured and stop.
					}
				}
				else
				{
					//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + ", ";
					pieceOutput = sb.append(addingToMoves).toString();
				}	
			}
		}
	}	
	
	private void knightMovement1Bank(int pieceX, int pieceY) {
			
		potentialX = pieceX - 2;
		potentialY = pieceY + 1;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}			
	private void knightMovement2Bank(int pieceX, int pieceY) {
		
		potentialX = pieceX - 1;
		potentialY = pieceY + 2;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}		
	private void knightMovement3Bank(int pieceX, int pieceY) {
		
		potentialX = pieceX - 2;
		potentialY = pieceY - 1;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}		
	private void knightMovement4Bank(int pieceX, int pieceY) {
		
		potentialX = pieceX - 1;
		potentialY = pieceY - 2;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}			
	private void knightMovement5Bank(int pieceX, int pieceY) {
		
		potentialX = pieceX + 2;
		potentialY = pieceY + 1;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}			
	private void knightMovement6Bank(int pieceX, int pieceY) {
		
		potentialX = pieceX + 2;
		potentialY = pieceY + 1;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}			
	private void knightMovement7Bank(int pieceX, int pieceY) {
		
		potentialX = pieceX + 2;
		potentialY = pieceY - 1;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}			
	private void knightMovement8Bank(int pieceX, int pieceY) {
		
		potentialX = pieceX + 1;
		potentialY = pieceY - 2;
			
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	
	
	private void whitePawnMoveBank(int pieceX, int pieceY) {
			
		int doubleMove = pieceX;
		potentialX = (pieceX - 1);
		potentialY = pieceY;
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				
			}
			if (doubleMove == 7)
			{
				
				potentialX = (pieceX - 2);
				
				if(potentialMove[potentialX][potentialY] == null)
				{
					//potentialX = (pieceX - i);
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + ", ";
					pieceOutput = sb.append(addingToMoves).toString();
					
					potentialX = (pieceX - 1);
					if(potentialMove[potentialX][potentialY] == null)
					{
						//potentialX = (pieceX - i);
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						addingToMoves  =  piecey + piecex + ", ";
						pieceOutput = sb.append(addingToMoves).toString();
						
						potentialX = (pieceX - 1);	
					}
					
				}
				
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	private void whitePawnAttackDiagonalLeftBank(int pieceX, int pieceY) 
	{	
		potentialX = (pieceX - 1);
		potentialY = (pieceY - 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null && potentialMove[potentialX][potentialY].getColor().equals("d"))
			{	
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}
			else
			{
				
			}	
		}
	}	
	private void whitePawnAttackDiagonalRightBank(int pieceX, int pieceY) 
	{	
		potentialX = (pieceX - 1);
		potentialY = (pieceY + 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null && potentialMove[potentialX][potentialY].getColor().equals("d"))
			{	
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}
			else
			{
				
			}	
		}
	}	
	
	private void darkPawnMoveBank(int pieceX, int pieceY) {
		
		int doubleMove = pieceX;
		potentialX = (pieceX + 1);
		potentialY = pieceY;
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				
			}
			if (doubleMove == 2)
			{
				
				potentialX = (pieceX + 2);
				
				if(potentialMove[potentialX][potentialY] == null)
				{
					//potentialX = (pieceX - i);
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + ", ";
					pieceOutput = sb.append(addingToMoves).toString();
					
					potentialX = (pieceX + 1);
					if(potentialMove[potentialX][potentialY] == null)
					{
						//potentialX = (pieceX - i);
						locationXY = new GPSLocation(potentialX, potentialY);
						potentialMoveList.add(locationXY);
						
						transferIntX();
						transferIntY();
						addingToMoves  =  piecey + piecex + ", ";
						pieceOutput = sb.append(addingToMoves).toString();
					}
					
				}
				
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	private void darkPawnAttackDiagonalLeftBank(int pieceX, int pieceY) 
	{	
		potentialX = (pieceX + 1);
		potentialY = (pieceY - 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null && potentialMove[potentialX][potentialY].getColor().equals("d"))
			{	
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}
			else
			{
				
			}	
		}
	}	
	private void darkPawnAttackDiagonalRightBank(int pieceX, int pieceY) 
	{	
		potentialX = (pieceX + 1);
		potentialY = (pieceY + 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else 
		{
			if (potentialMove[potentialX][potentialY] != null && potentialMove[potentialX][potentialY].getColor().equals("d"))
			{	
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}
			else
			{
				
			}	
		}
	}	
	
	private void kingMovementNorthBank(int pieceX, int pieceY) {
			
		potentialX = (pieceX - 1);
		potentialY = pieceY;
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}
	private void kingMovementSouthBank(int pieceX, int pieceY) {
		
		potentialX = (pieceX + 1);
		potentialY = pieceY;
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}
	private void kingMovementWestBank(int pieceX, int pieceY) {
		
		potentialX = pieceX;
		potentialY = (pieceY - 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	private void kingMovementEastBank(int pieceX, int pieceY) {
		
		potentialX = pieceX;
		potentialY = (pieceY + 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	private void kingMovementSouthEastBank(int pieceX, int pieceY) {
		
		potentialX = (pieceX + 1);
		potentialY = (pieceY + 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	private void kingMovementSouthWestBank(int pieceX, int pieceY) {
		
		potentialX = (pieceX + 1);
		potentialY = (pieceY - 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	private void kingMovementNorthEastBank(int pieceX, int pieceY) {
		
		potentialX = (pieceX - 1);
		potentialY = (pieceY + 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}	
	private void kingMovementNorthWestBank(int pieceX, int pieceY) {
		
		potentialX = (pieceX - 1);
		potentialY = (pieceY - 1);
		
		if(potentialX < 1 || potentialX > 8 || potentialY < 1 || potentialY > 8)
		{
			
		}
		else {
			if (potentialMove[potentialX][potentialY] != null)
			{	
				if (potentialX < 1)
				{
				
				}	
				else if (potentialMove[potentialX][potentialY].getColor().equals(meatShieldColor))
				{
					//stop the for loop, can't move anymore
				}
				else {
					
					//potentialMoveList.add(potentialMove[potentialX][potentialY]); //get it all going north
					
					locationXY = new GPSLocation(potentialX, potentialY);
					potentialMoveList.add(locationXY);
					
					transferIntX();
					transferIntY();
					String addingToMoves  =  piecey + piecex + "*";
					pieceOutput = sb.append(addingToMoves).toString();
				}
			}
			else
			{
				//potentialMoveList.add(potentialMove[potentialX][potentialX]); //get it all going north
				
				locationXY = new GPSLocation(potentialX, potentialY);
				potentialMoveList.add(locationXY);
				
				transferIntX();
				transferIntY();
				String addingToMoves  =  piecey + piecex + ", ";
				pieceOutput = sb.append(addingToMoves).toString();
			}	
		}
	}		
	
	public void transferIntX()
	{
		switch (potentialX) {
		case 1:
			piecex = "8";
			break;
		case 2:
			piecex = "7";
			break;
		case 3:
			piecex = "6";
			break;
		case 4:
			piecex = "5";
			break;
		case 5:
			piecex = "4";
			break;
		case 6:
			piecex = "3";
			break;
		case 7:
			piecex = "2";
			break;
		case 8:
			piecex = "1";
			break;
		default:
			break;
		}
	}
	
	public void pawnIntX()
	{
		switch (potentialX2) {
		case 1:
			piecex = "8";
			break;
		case 2:
			piecex = "7";
			break;
		case 3:
			piecex = "6";
			break;
		case 4:
			piecex = "5";
			break;
		case 5:
			piecex = "4";
			break;
		case 6:
			piecex = "3";
			break;
		case 7:
			piecex = "2";
			break;
		case 8:
			piecex = "1";
			break;
		default:
			break;
		}
	}
	
	public void transferIntY()
		{
			switch (potentialY) {
			case 1:
				piecey = "a";
				break;
			case 2:
				piecey = "b";
				break;
			case 3:
				piecey = "c";
				break;
			case 4:
				piecey = "d";
				break;
			case 5:
				piecey = "e";
				break;
			case 6:
				piecey = "f";
				break;
			case 7:
				piecey = "g";
				break;
			case 8:
				piecey = "h";
				break;
			default:
				break;
			}
		}	
}







