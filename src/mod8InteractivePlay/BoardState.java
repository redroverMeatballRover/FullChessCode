package mod8InteractivePlay;

import java.util.ArrayList;
import java.io.*;

@SuppressWarnings("serial")
public class BoardState implements Serializable
{
	private ChessPiece[][] boardState;
	private ChessPiece takenPiece;
	private boolean isWhiteTurn;
	private ArrayList<ChessPiece[][]> savedStates;
	private int savedStateQueue;
	int designateCoord = 0;
	int blackRow = 0;
	int whiteRow = 7;
	int rookOriginX1 = 0;
	int rookOriginX2 = 7;
	int knightOriginX1 = 1;
	int knightOriginX2 = 6;
	int bishopOriginX1 = 2;
	int bishopOriginX2 = 5;
	int queenOriginX = 3;
	int blackPawnOriginRow = 1; 
	int whitePawnOriginRow = 6; 
	
	public BoardState()
	{
		boardState = new ChessPiece[8][8];
		savedStates = new ArrayList<ChessPiece[][]>();
		savedStateQueue = 0;
		setOriginBoard();
	}
	
	public ChessPiece[][] getState()
	{
		return boardState;
	}
	
	//turns systems
	//-----------------------------------------------------------------------//
	public boolean isPieceWhite(Location loc)
	{
		return boardState[loc.getRow()][loc.getCol()].getColor();
	}
	
	public void setWhoseTurn(boolean isWhite)
	{
		isWhiteTurn = isWhite;
	}
	
	public boolean getWhoseTurn()
	{
		return isWhiteTurn;
	}
	//-----------------------------------------------------------------------//
	
	
	public void resetOtherPawns(Location currentPiece)
	{
		for(int y=0; y<boardState.length; y++)
		{
			for(int x=0; x<boardState.length; x++)
			{
				Location loc = new Location(y,x);
				if(isValidMove(loc) && !isBlocked(loc) && boardState[y][x] instanceof Pawn)
				{
					if(!currentPiece.equals(loc))
					{
						((Pawn)boardState[y][x]).setDoubleMove(false);
					}
				}
			}
		}
	}
	
	public int saved()
	{
		int boardNums = 8;
		savedStates.add(new ChessPiece[boardNums][boardNums]);
		for(int y=0; y<boardState.length; y++)
		{
			for(int x=0; x<boardState.length; x++)
			{
				savedStates.get(savedStateQueue)[y][x]=ChessPiece.build(boardState[y][x]);
			}
		}
		savedStateQueue++;
		return savedStateQueue-1;
	}
	
	public void ghostingRedoMove(int queueNumber)
	{
		for(int y=0; y<boardState.length; y++)
			for(int x=0; x<boardState.length; x++)
					boardState[y][x]=savedStates.get(queueNumber)[y][x];
	}
	
	
	
	//move systems
	//-----------------------------------------------------------------------//
	public void moveFrom_To(Location start, Location end)
	{
		boardState[start.getRow()][start.getCol()].moveTo(end);
		ChessPiece temp = boardState[start.getRow()][start.getCol()];
		takenPiece = boardState[end.getRow()][end.getCol()];
		boardState[start.getRow()][start.getCol()] = null;
		boardState[end.getRow()][end.getCol()] = temp;
		
		if(takenPiece == null && start.getCol()!=end.getCol() && temp instanceof Pawn)
			if(temp.getColor())
			{
				Location EP = new Location(end.getRow()+1, end.getCol());
				if(!isBlocked(EP) && !boardState[EP.getRow()][EP.getCol()].getColor() && boardState[EP.getRow()][EP.getCol()] instanceof Pawn)
					boardState[EP.getRow()][EP.getCol()] = null;
			}
			else
			{
				Location EP = new Location(end.getRow()-1, end.getCol());
				if(!isBlocked(EP) && boardState[EP.getRow()][EP.getCol()].getColor() && boardState[EP.getRow()][EP.getCol()] instanceof Pawn)
					boardState[EP.getRow()][EP.getCol()] = null;
			}
		if(takenPiece == null && temp instanceof King)
			if(start.getCol()-end.getCol() == 2)
			{
				boardState[end.getRow()][end.getCol()-2].moveTo(new Location(end.getRow(), end.getCol()+1));
				moveFrom_To(new Location(end.getRow(), end.getCol()-2), new Location(end.getRow(), end.getCol()+1));
			}
			else if(start.getCol()-end.getCol() == -2)
			{
				boardState[end.getRow()][end.getCol()+1].moveTo(new Location(end.getRow(), end.getCol()-1));
				moveFrom_To(new Location(end.getRow(), end.getCol()+1), new Location(end.getRow(), end.getCol()-1));	
			}
	}
		
	public boolean isValidMove(Location loc)
	{
		//designateCoord = 0
		if( (loc.getRow() < boardState.length && loc.getRow() >= designateCoord) && (loc.getCol() < boardState.length && loc.getCol() >=designateCoord) )
			return true;
		return false;
	}
	
	public boolean isBlocked(Location loc)
	{
		if(!isValidMove(loc))
			return false;
		if(boardState[loc.getRow()][loc.getCol()]==null)
			return true;
		return false;
	}
	//-----------------------------------------------------------------------//
	
	
	// add pieces
	//-----------------------------------------------------------------------//
	public void addQueen(Location loc)
	{
		boardState[loc.getRow()][loc.getCol()] = new Queen(boardState[loc.getRow()][loc.getCol()].getColor(), new Location(loc.getRow(),loc.getCol()));
	}
	
	public void addRook(Location loc)
	{
		boardState[loc.getRow()][loc.getCol()] = new Rook(boardState[loc.getRow()][loc.getCol()].getColor(), new Location(loc.getRow(),loc.getCol()));
	}
	
	public void addBishop(Location loc)
	{
		boardState[loc.getRow()][loc.getCol()] = new Bishop(boardState[loc.getRow()][loc.getCol()].getColor(), new Location(loc.getRow(),loc.getCol()));
	}
	
	public void addKnight(Location loc)
	{
		boardState[loc.getRow()][loc.getCol()] = new Knight(boardState[loc.getRow()][loc.getCol()].getColor(), new Location(loc.getRow(),loc.getCol()));
	}
	//-----------------------------------------------------------------------------------------------------------//
	
	// set origin board
	//----------------------------------------------//
	public void setOriginBoard()
	{
		takenPiece=null;
		savedStates = new ArrayList<ChessPiece[][]>();
		savedStateQueue = 0;
		for(int y=0; y<boardState.length; y++)
			for(int x=0; x<boardState.length; x++)
			{
				if(y==blackRow)
					if(x==rookOriginX1 || x==rookOriginX2)
						boardState[y][x] = new Rook(false, new Location(y,x));
				
					else if(x==knightOriginX1 || x==knightOriginX2)
						boardState[y][x] = new Knight(false, new Location(y,x));
				
					else if(x==bishopOriginX1 || x==bishopOriginX2)
						boardState[y][x] = new Bishop(false, new Location(y,x));
				
					else if(x==queenOriginX)
						boardState[y][x] = new Queen(false, new Location(y,x));
				
					else
						boardState[y][x] = new King(false, new Location(y,x));
				
				else if(y==blackPawnOriginRow)
					boardState[y][x] = new Pawn(false, new Location(y,x));
				
				else if(y==whitePawnOriginRow)
					boardState[y][x] = new Pawn(true, new Location(y,x));
				//------------------------------------------------------------------------------------//
				else if(y==whiteRow)
					if(x==rookOriginX1 || x==rookOriginX2)
						boardState[y][x] = new Rook(true, new Location(y,x));
				
					else if(x==knightOriginX1 || x==knightOriginX2)
						boardState[y][x] = new Knight(true, new Location(y,x));
				
					else if(x==bishopOriginX1 || x==bishopOriginX2)
						boardState[y][x] = new Bishop(true, new Location(y,x));
				
					else if(x==queenOriginX)
						boardState[y][x] = new Queen(true, new Location(y,x));
				
					else
						boardState[y][x] = new King(true, new Location(y,x));
				else
					boardState[y][x]=null;
			}
	}

}
