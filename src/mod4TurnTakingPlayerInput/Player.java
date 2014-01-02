package mod4TurnTakingPlayerInput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Player {

	Pattern findMove = Pattern.compile("([A-Ha-h])([1-9])");
	String starty;
	int startx;
	int startY;
	int startX;
	
	String newy;
	int newx;
	int newY;
	int newX;
	ChessPiece[][] pieces = new ChessPiece[9][9];
	ChessPiece[][] boardUpdate = new ChessPiece[9][9];
	boolean chooseTurn;
	
	
	
	public void runMoves() throws IOException
	{
		//what piece to move
		System.out.println("Move piece: ");
		whatPieceAndWhere(); 
		
		System.out.println("To: ");
		moveTo();	
	}
	
	//checks turn-taking
	private void turnChecking(boolean turn) throws IOException {
		
		chooseTurn = turn;
		
		System.out.println(startX);
		System.out.println(startY);
		
//		if (pieces[startX][startY] != null)
//		System.out.println("present!");
//	else if (pieces[startX][startY] == null)
//		System.out.println("empty");
		
	
		if(chooseTurn && pieces[startX][startY].getColor().equals("l")) //if its whites turn, and the piece is white
		{
			chooseTurn = false;
			move();
		}
		else if (!chooseTurn && pieces[startX][startY].getColor().equals("d")) //if its blacks turn, and the piece is black
		{
			chooseTurn = true;
			move();
		}
		else if (chooseTurn && pieces[startX][startY].getColor().equals("d")) // if its whites turn and the piece is black
		{
			System.out.println("MOVING OUT OF ORDER - Black piece attempted to move out of turn. It is currently WHITE's turn.");
			chooseTurn = true;
		}
		else if(!chooseTurn && pieces[startX][startY].getColor().equals("l")) {
			System.out.println("MOVING OUT OF ORDER - White piece attempted to move out of turn. It is currently BLACKS's turn.");
			chooseTurn = false;
		}
	}
	
	public void getCurrentBoardAndMakeMove(ChessPiece[][] currentBoard, boolean whiteTurn) throws IOException
	{
		pieces = currentBoard;
		boolean isItWhitesTurn = whiteTurn; 
		turnChecking(isItWhitesTurn);
//		move();	
	}
	
	public void move() throws IOException
	{
		System.out.println("whiteTurn = " + chooseTurn);
		if (pieces[startX][startY] != null)
		{
			if (pieces[startX][startY].legalMove(pieces, startY, startX, newY, newX)) //checks legal moves
			{
				if (pieces[startX][startY].pathIsClear(pieces, startY, startX, newY, newX))//checks for barriers
				{
					pieces[newX][newY] =  pieces[startX][startY];
					pieces[startX][startY] = null; //set old spot to "| - |"
							
					//update the board	//sends new board design
					Board board = new Board();
					board.updateBoard(pieces, chooseTurn); 
				}
				else 
					System.out.println("MOVE BLOCKED - another piece is blocking your route.");
			}
			else
			{
				System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
				//answer to recursive. 
				Board board = new Board();
				board.inCaseOfIllegalMove(pieces); 
			}	
		}
		else
			System.out.println("ERROR - could not locate piece you designated to move.");
			Board board = new Board();
			board.inCaseOfIllegalMove(pieces); 
	}
	
	private void whatPieceAndWhere() 
	{		
		Matcher newMove = findMove.matcher(readLine());
	
		if (newMove.find()) {
			starty = coordinateY(newMove).toLowerCase();
			transferChars();
			startx = coordinateX(newMove);
			backwardsColumnNumberingSyndrome();
		}
		else {
			System.out.println("ERROR - invalid entry. Please try again.");
			whatPieceAndWhere();
		}
	}
	
	private void moveTo() 
	{		
		Matcher newMove = findMove.matcher(readLine());
	
		if (newMove.find()) {
			newy = coordinateY(newMove).toLowerCase();
			transferChars2(); //DUPLICATE CODE
			newx = coordinateX(newMove);
			backwardsColumnNumberingSyndrome2(); //CUPLICATE CODE
		}
		else {
			System.out.println("ERROR - invalid entry. Please try again.");
			moveTo();
		}
	}
	
	private String coordinateY(Matcher newMove) 
	{
		String tempY;
		tempY = newMove.group(1);
		
		return tempY;
	}

	private int coordinateX(Matcher newMove) 
	{
		int tempX;
		tempX = Integer.parseInt(newMove.group(2));
		return tempX;
	}
	
	public void transferChars()
	{
		switch (starty.charAt(0)) {
		case 'a':
			startY = 1;
			break;
		case 'b':
			startY = 2;
			break;
		case 'c':
			startY = 3;
			break;
		case 'd':
			startY = 4;
			break;
		case 'e':
			startY = 5;
			break;
		case 'f':
			startY = 6;
			break;
		case 'g':
			startY = 7;
			break;
		case 'h':
			startY = 8;
			break;
		default:
			break;
		}
	}
	
	public void transferChars2()
	{
		switch (newy.charAt(0)) {
		case 'a':
			newY = 1;
			break;
		case 'b':
			newY = 2;
			break;
		case 'c':
			newY = 3;
			break;
		case 'd':
			newY = 4;
			break;
		case 'e':
			newY = 5;
			break;
		case 'f':
			newY = 6;
			break;
		case 'g':
			newY = 7;
			break;
		case 'h':
			newY = 8;
			break;
		default:
			break;
		}
	}
	
	public void backwardsColumnNumberingSyndrome()
	{
		switch (startx) {
		case 1:
			startX = 8;
			break;
		case 2:
			startX = 7;
			break;
		case 3:
			startX = 6;
			break;
		case 4:
			startX = 5;
			break;
		case 5:
			startX = 4;
			break;
		case 6:
			startX = 3;
			break;
		case 7:
			startX = 2;
			break;
		case 8:
			startX = 1;
			break;
		default:
			break;
		}
	}
	
	public void backwardsColumnNumberingSyndrome2()
	{
		switch (newx) {
		case 1:
			newX = 8;
			break;
		case 2:
			newX = 7;
			break;
		case 3:
			newX = 6;
			break;
		case 4:
			newX = 5;
			break;
		case 5:
			newX = 4;
			break;
		case 6:
			newX = 3;
			break;
		case 7:
			newX = 2;
			break;
		case 8:
			newX = 1;
			break;
		default:
			break;
		}
	}
		
	private String readLine() {
		try {
			return new BufferedReader(new InputStreamReader(System.in))
					.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
