package mod5DetectCheck;

import java.io.IOException;
import java.util.ArrayList;

/*
 * Generates Pieces
 */
public class Piece {
	
	public ChessPiece[][] pieces = new ChessPiece[9][9]; 
	ArrayList<PrototypePiece> listOfPrototypes = new ArrayList<PrototypePiece>();
	String spawnPiece;
	String spawnColor;
	String pieceRepresentation;
	String originy;
	int originx;
	int originY;
	int originX;
	
	// MAIN
	//-------------------------------------------------------------------------------------------------------------------//
	
	// Creates Piece out of prototypePiece object. 
	public void getPrototypePeiceArrayList(ArrayList<PrototypePiece> protoList) {
		listOfPrototypes = protoList;
		
		for(int i = 0; i < listOfPrototypes.size(); i++ )
		{
			spawnPiece = listOfPrototypes.get(i).type;
			spawnColor = listOfPrototypes.get(i).team;	
			createPieceRepresentation(); //gets the white/dark designation of the piece.
			
			originy =  listOfPrototypes.get(i).y; 
			transferChars(); //change placeXRP to number
			
			originx = Integer.parseInt(listOfPrototypes.get(i).x);
			backwardsColumnNumberingSyndrome(); //deals with the backwards column numbering
			generatePieceClass();
		}
	}
	
	
	
	//ALTERNATE // combines parsed elements to create a piece // ORIGINAL
	public void getPieceFromReadParse(String pieceRP, String colorRP, String placeXRP, String placeYRP )
	{
//		spawnPiece = pieceRP; 
//		spawnColor = colorRP;
//		createPieceRepresentation(); //gets the white/dark designation of the piece.
//		originy = placeXRP; 
//		transferChars(); //change placeXRP to number
//		originx = Integer.parseInt(placeYRP);
//		backwardsColumnNumberingSyndrome(); //deals with the backwards column numbering
//		generatePieceClass(); //turns elements into piece. 
	}
	
	//-------------------------------------------------------------------------------------------------------------------//
	
	
	//SUPPORT
	//-------------------------------------------------------------------------------------------------------------------//
	
	// Y Coordinates: Translates letters into numbers 
	public void transferChars()
	{
		switch (originy.charAt(0)) {
		case 'a':
			originY = 1;
			break;
		case 'b':
			originY = 2;
			break;
		case 'c':
			originY = 3;
			break;
		case 'd':
			originY = 4;
			break;
		case 'e':
			originY = 5;
			break;
		case 'f':
			originY = 6;
			break;
		case 'g':
			originY = 7;
			break;
		case 'h':
			originY = 8;
			break;
		default:
			break;
		}
	}
	
	
	//X Coordinates: deals with the backwards column numbering 
	public void backwardsColumnNumberingSyndrome()
	{
		switch (originx) {
		case 1:
			originX = 8;
			break;
		case 2:
			originX = 7;
			break;
		case 3:
			originX = 6;
			break;
		case 4:
			originX = 5;
			break;
		case 5:
			originX = 4;
			break;
		case 6:
			originX = 3;
			break;
		case 7:
			originX = 2;
			break;
		case 8:
			originX = 1;
			break;
		default:
			break;
		}
	}
	
	
	//Creates representation of Piece and its color on the board.
	private void createPieceRepresentation()
	{		
		char pieceChar = spawnPiece.charAt(0);
		String pieceDesignation = Character.toString(pieceChar);
		
		if (spawnColor == "d")
			pieceRepresentation = pieceDesignation.toLowerCase();
		else
			pieceRepresentation = pieceDesignation;	
	}
	
	
	//Alternate/Suggested representation of Piece and its color on the board 
	public String createPieceRepresentation6666(String pieceType, String colorLetter)
	{		
		char pieceChar = pieceType.charAt(0);
		String pieceLetter = Character.toString(pieceChar);
		
		String pieceRepresentation = (colorLetter == "d")? pieceLetter.toLowerCase(): pieceLetter.toUpperCase();
		return pieceRepresentation;
	}
	
	
	//generates a piece. 
	private void generatePieceClass()
	{
		ChessPieceFactory cpf = new ChessPieceFactory();
		ChessPiece piece = cpf.create(spawnPiece, pieceRepresentation);
		pieces[originX][originY] = piece; 				//LOGIC: pieces[a][b] = new (WHATEVER PIECE WAS MATCHED)("position + color");
	}

	//-------------------------------------------------------------------------------------------------------------------//
	
	
	//ACTIVATE BOARD
	//-------------------------------------------------------------------------------------------------------------------//
	//sends created 2d array to BOARD CLASS
	public void sendToBoard() throws IOException
	{
		Board board = new Board();
		board.getPieces2DArray(pieces);		
//		SetOriginBoard sob = new SetOriginBoard();
//		sob.setupOriginBoard(pieces);	
	}

	//-------------------------------------------------------------------------------------------------------------------//
}
