package Mod3TextFilePlayer;

import java.io.IOException;


/*
 * Creates and populates board
 */
public class Board {
	
	//SUPPORT
	//-----------------------------------------------------------------------------------------------------//
	//Player player = new Player();
	Piece p = new Piece();
	ChessPiece[][] pieces = new ChessPiece[9][9];
	PlayerIsTextFile playerText = new PlayerIsTextFile();
	String fileName = "SetMovesFile";
	
	//gets 2d array of pieces created in Piece Class and calls doChess() // starts the game
	public void getPieces2DArray(ChessPiece[][] getTheArray) throws IOException 
	{
		pieces = getTheArray;
		originBoardSetUp(); //sets up the origin board for the start of the game
	}
	
	//starts the game with a new board
	private void originBoardSetUp() throws IOException {
		int startX = 0;
		int endX = 0;
		int startY = 0;
		int endY = 0;
		
		print(pieces, startX, startY, endY, endX);
		doChess();
	}
	
	//MOVE CYCLE: doChess > UpdateBoard > continueChess > ... 
	//---------------------------------------------------------------------------------------------------//
	
	//populates the chessboard with pieces
	public void doChess() throws IOException 
	{
		playerText.getCurrentBoardAndMakeMove(pieces); //send current board to player and player moves
	}
	
	//recieve new coordinates back, prints and updates board, calls player.
	public void updateBoard(ChessPiece[][] updatedBoard) throws IOException
	{
		pieces = updatedBoard;
		
		int startX = 0;
		int endX = 0;
		int startY = 0;
		int endY = 0;
		print(pieces, startX, startY, endY, endX);
		//continueChess();
	}
	
	public void inCaseOfIllegalMove(ChessPiece[][] sameBoard) throws IOException
	{
		pieces = sameBoard;
		//doChess();
	}
	
	private void checkingPiecesLocationTester() {
		if(pieces[1][1] == null)
		{
			System.out.println("MISSING - no piece found at 1,1");
		}
		else if(pieces[1][1] != null)
		{
			System.out.println("FOUND - piece found at 1,1");
			System.out.println("The Piece Rep is: " + pieces[1][1].setInBoard());
			System.out.println("The Piece is: " + pieces[1][1].getClass());
			System.out.println("The Piece is: " + pieces[1][1].toString());
		}
	}
	//setup for printing
	public void print(ChessPiece[][] chess, int row1st, int col1st, int col2nd, int row2nd) 
	{
		int a;
		int b;
		String rows = " abcdefgh"; 
		String nullSquare = "| - |";
		String[][] array = new String[9][9];
		
		//SETS FULL AND EMPTY SPACES
		//-------------------------------------------------------------------//
		for (a = 1; a < 9; a++) 
		{
			for (b = 1; b < 9; b++) 
			{
				if (chess[a][b] != null) 
					array[a][b] = chess[a][b].setInBoard(); //this sets the thing in board //DOESNT RECOUGNIZE PIECE
				else 
					array[a][b] = nullSquare;
			}
		}
		//PRINTS BOARD
		//-------------------------------------------------------------------//
		rows = " abcdefgh";
		int i = 0;
		int j = 0;
		
		for (i = 0; i < 9; i++) 
		{
			for (j = 1; j < 9; j++) 
			{
				if (i == 0)
					System.out.print("  " + rows.charAt(j) + "  ");
				else 
				{
					if (i > 0)
						System.out.print(array[i][j]);
				}
			}
			if (i == 0)
				System.out.println();
			if (i > 0) 
				System.out.println(9 - i);
		}
	}	
}

