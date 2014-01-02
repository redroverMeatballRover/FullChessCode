package Mod2;
import java.io.IOException;

/*
 * Creates and populates board
 */
public class Board {
	
	//SUPPORT
	//-----------------------------------------------------------------------------------------------------//
	Piece p = new Piece();
	ChessPiece[][] pieces = new ChessPiece[9][9];
	
	//gets 2d array of pieces created in Piece Class and calls doChess()
	public void getPieces2DArray(ChessPiece[][] getTheArray) 
	{
		pieces = getTheArray;
		doChess();
	}
	//-----------------------------------------------------------------------------------------------------//
	
	// MAIN
	//-----------------------------------------------------------------------------------------------//
	
	//populates the chessboard with pieces
	public void doChess() 
	{
		int row1st = 0;
		int row2nd = 0;
		int colStart = 0;
		int colEnd = 0;
		int x = 0;
		int y = 0;
		print(pieces, row1st, colStart, colEnd, row2nd);
	}
	
	//setup for printing
	public static void print(ChessPiece[][] chess, int row1st, int col1st, int col2nd, int row2nd) 
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
					array[a][b] = chess[a][b].setInBoard(); //this sets the thing in board
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
	//-----------------------------------------------------------------------------------------------//
}
