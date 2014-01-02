package mod8InteractivePlay;

import java.util.ArrayList;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

public class RunGUI implements Serializable
{
	private static final long serialVersionUID = -5502586734068739286L;
	private Location selectedPiece;
	private Location promotedPiece;
	private BoardState board;
	private GUI gui;
	private boolean isWhiteTurn;
	private boolean highlight;
	int game1 = -1;
	int game2 = 1;
	int promotionRow1 = 0;
	int promotionRow2 = 7;
	int antiNum = 1;
	int rowTwo = 2;
	int rowOne = antiNum;
	int rowThree = 3;
	int rowFour = 4;
	int colSix = 6;
	int verifiedKill = 1;
	int nullAttack = 0;
	
	int pawnPromotionNum = -4;
	int addQueenCommand = -1;
	int addRookCommand = -2;
	int addBishopCommand = -3;
	int addKnightCommand = -4;
	
	
	private ArrayList<Integer> wasPieceTaken;
	private ArrayList<Location> moves;
	
	public RunGUI()
	{
		board = new BoardState();
		gui = new GUI(board, this);
		highlight = true;
		moves = new ArrayList<Location>();
	}
	
	public void setVisible()
	{
		gui.setVisible(true);
		startGame();
	}
	
	public void startGame()
	{
		board.setOriginBoard();
		selectedPiece = null;
		isWhiteTurn = true;
		board.setWhoseTurn(isWhiteTurn);
		gui.updateBoard(board);
		
		gui.enableSide(isWhiteTurn);
		wasPieceTaken = new ArrayList<Integer>();
	}
	
	public boolean getTurn()
	{
		return isWhiteTurn;
	}
	
	public ArrayList<Location> getMoves(Location piece)
	{
		ArrayList<Location> allMoves = board.getState()[piece.getRow()][piece.getCol()].getMoves(board);
		for(int a=0; a<allMoves.size(); a++)
			if(!isLegalMove(piece, allMoves.get(a), 0))
			{
				allMoves.remove(a);
				a--;
			}
		return allMoves;
	}
	
	public void checkEndGame()
	{
		int checkMateDesignation = 0;
		ArrayList<Location> moves = new ArrayList<Location>();
		ArrayList<Location> temp = new ArrayList<Location>();
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
			{
				if(board.getState()[y][x]!=null && board.getState()[y][x].getColor()==isWhiteTurn)
				{
					temp = getMoves(new Location(y,x));
					for(Location z : temp)
						moves.add(z);
					if(moves.size()>0)
						break;
				}
				if(moves.size()>0)
					break;
			}
		if(moves.size()==checkMateDesignation)
		{
			boolean isCheckMate = true;
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						isCheckMate = ((King)board.getState()[y][x]).isChecked();
						System.out.println("CHECKMATE - King is in checkmate.");
						break;
					}
			if(isCheckMate)
			{
				gui.updateSystemChecking("Game Over!", true);
				if(isWhiteTurn)
				{
					gui.gameOver(game1);
				}
				else
				{
					gui.gameOver(game2);
				}
			}
			else
			{
				gui.updateSystemChecking("Tie Game!", true);
				gui.gameOver(0);
			}
		}

		boolean isWhiteKnight = false;
		boolean isWhiteBishop = false;
		boolean isBlackKnight = false;
		boolean isBlackBishop = false;
		boolean isDraw = true;
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
			{
				if(board.getState()[y][x]!=null && (board.getState()[y][x] instanceof Queen || board.getState()[y][x] instanceof Rook || board.getState()[y][x] instanceof Pawn))
				{
					isDraw=false;
					break;
				}
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Bishop && board.getState()[y][x].getColor()==isWhiteTurn)
					isWhiteBishop=true;
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Knight && board.getState()[y][x].getColor()==isWhiteTurn)
					isWhiteKnight = true;
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Bishop && board.getState()[y][x].getColor()!=isWhiteTurn)
					isBlackBishop=true;
				else if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof Knight && board.getState()[y][x].getColor()!=isWhiteTurn)
					isBlackKnight = true;
			}
		if(isDraw)
			if(isWhiteBishop && isWhiteKnight || isBlackBishop && isBlackKnight || isBlackBishop && isWhiteBishop || isBlackKnight && isWhiteKnight)
				isDraw = false;
		if(isDraw)
		{
			gui.updateSystemChecking("Draw!", true);
			gui.gameOver(0);
		}
	}
	
	public boolean isLegalMove(Location start, Location end, int restraint)
	{
		boolean isLegal = true;
		boolean complete = true;
		int queue;
		
		if(restraint!=antiNum && board.getState()[start.getRow()][start.getCol()] instanceof King)
		{
			if(start.getRow()==end.getRow() && Math.abs(start.getCol()-end.getCol())==rowTwo)
			{
				((King)board.getState()[start.getRow()][start.getCol()]).updateIsChecked(board);
				if(((King)board.getState()[start.getRow()][start.getCol()]).getHasMoved() || ((King)board.getState()[start.getRow()][start.getCol()]).isChecked())
				{
					isLegal = false;
					complete = false;
				}
				else if(end.getCol()==rowTwo && board.isBlocked(new Location(start.getRow(),start.getCol()-rowOne)) && board.isBlocked(new Location(start.getRow(),start.getCol()-rowTwo)) && board.isBlocked(new Location(start.getRow(),start.getCol()-rowThree)) && !board.isBlocked(new Location(start.getRow(),start.getCol()-rowFour)) && board.getState()[start.getRow()][start.getCol()-rowFour] instanceof Rook && !((Rook)board.getState()[start.getRow()][start.getCol()-rowFour]).getHasMoved())
				{
					if(isLegalMove(start, new Location(start.getRow(), start.getCol()-rowOne), rowOne) && isLegalMove(start, end, rowOne))
					{
						isLegal = true;
						complete = false;
					}
					else
					{
						isLegal = false;
						complete = false;
					}
				}
				else if(end.getCol()==colSix && board.isBlocked(new Location(start.getRow(),start.getCol()+rowOne)) && board.isBlocked(new Location(start.getRow(),start.getCol()+rowTwo)) && !board.isBlocked(new Location(start.getRow(),start.getCol()+rowThree)) && board.getState()[start.getRow()][start.getCol()+rowThree] instanceof Rook && !((Rook)board.getState()[start.getRow()][start.getCol()+rowThree]).getHasMoved())
				{
					if(isLegalMove(start, new Location(start.getRow(), start.getCol()+rowOne), rowOne) && isLegalMove(start, end, rowOne))
					{
						isLegal = true;
						complete = false;
					}
					else
					{
						isLegal = false;
						complete = false;
					}
				}
				else
				{
					isLegal = false;
					complete = false;
				}
			}
		}
		
		if(complete)
		{
			queue = board.saved();
			board.moveFrom_To(start, end);
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						((King)board.getState()[y][x]).updateIsChecked(board);
						isLegal = !((King)board.getState()[y][x]).isChecked();
						break;
					}
			board.ghostingRedoMove(queue);
		}
		return isLegal;
	}
	
	public void movePiece(Location loc)
	{
		if(board.getState()[loc.getRow()][loc.getCol()]!=null)
		{
			wasPieceTaken.add(verifiedKill);
		}
		else
		{
			wasPieceTaken.add(nullAttack);
		}
		
		board.moveFrom_To(selectedPiece, loc);
		gui.updateMoveStringLists(selectedPiece, loc);
		isWhiteTurn = !isWhiteTurn;
		board.setWhoseTurn(isWhiteTurn);
		selectedPiece=null;
		gui.updateBoard(board);
		gui.enableSide(isWhiteTurn);
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
				if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
				{
					((King)board.getState()[y][x]).updateIsChecked(board);
					if(((King)board.getState()[y][x]).isChecked())
					{
						System.out.println("CHECK - King is in check");
					}
					break;
				}
	}
	
	public void processMoves()
	{
		boolean isCheck = true;
		moves = getMoves(selectedPiece);
		if(moves.size()==0)
		{
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						isCheck = ((King)board.getState()[y][x]).isChecked();
						break;
					}
			if(isCheck)
			{
				System.out.println("CHECK - King is in check.");
			}
			else
			{
				System.out.println("POTENTIAL CHECK - Moving this piece will result in a check. Meatshield enabled.");
			}
		}
		else
			for(Location z : moves)
				gui.enable(z, highlight);
	}
	
	public void enableSelectionAndMove(Location loc)
	{
		if(board.getState()[loc.getRow()][loc.getCol()]!=null)
		{
			if((board.getState()[loc.getRow()][loc.getCol()].getColor() && isWhiteTurn) || (!board.getState()[loc.getRow()][loc.getCol()].getColor() && !isWhiteTurn))
			{
				gui.enableSide(isWhiteTurn);
				selectedPiece = loc;
				gui.resetBackground();
				gui.resetBorders();
				gui.selectedItem(selectedPiece);
				processMoves();
			}
			else
			{
				movePiece(loc);
				board.resetOtherPawns(loc);
				checkPromotion(loc);
				checkEndGame();
			}
		}
		else
		{
			movePiece(loc);
			board.resetOtherPawns(loc);
			checkPromotion(loc);
			checkEndGame();
		}
	}
	
	public void checkPromotion(Location loc)
	{
		if(board.getState()[loc.getRow()][loc.getCol()] instanceof Pawn && (loc.getRow()==promotionRow1 || loc.getRow()==promotionRow2))
		{
			gui.promotion();
			promotedPiece = loc;
		}
	}
	
	public void pawnPromotionSystem(int command)
	{	
		if(command>= pawnPromotionNum)
		{
			if(command==addQueenCommand)
				board.addQueen(promotedPiece);
			
			else if(command==addRookCommand)
				board.addRook(promotedPiece);
			
			else if(command==addBishopCommand)
				board.addBishop(promotedPiece);
			
			else if(command==addKnightCommand)
				board.addKnight(promotedPiece);
			for(int y=0; y<board.getState().length; y++)
				for(int x=0; x<board.getState().length; x++)
					if(board.getState()[y][x]!=null && board.getState()[y][x] instanceof King && board.getState()[y][x].getColor()==isWhiteTurn)
					{
						((King)board.getState()[y][x]).updateIsChecked(board);
						if(((King)board.getState()[y][x]).isChecked())
							System.out.println("CHECK - King is in check");
						break;
					}
			gui.endPromotion();
			gui.updateBoard(board);
		}
	}

}