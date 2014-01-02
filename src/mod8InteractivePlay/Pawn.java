package mod8InteractivePlay;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class Pawn extends ChessPiece
{
	BufferedImage img;// = (BufferedImage) new ImageIcon("rl.png");
	BufferedImage buffered;
	Image image;
	
	private boolean doubleMove;
	
	public Pawn(boolean isWhite, Location loc)
	{
		super(isWhite, loc);
	}
		
	
	public void draw(Graphics g)
	{
		final int positioningSide = 20;
		final int positioningTop = 15;
		
		if(getColor())
		{
			try {
				img = ImageIO.read(new File("pl.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
		else
		{
			try {
				img = ImageIO.read(new File("pd.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
	}
	
	public boolean getDoubleMove()
	{
		return doubleMove;
	}
	
	public void setDoubleMove(boolean input)
	{
		doubleMove=input;
	}
	
	public ArrayList<Location> getMoves(BoardState board)
	{
		int pawnSingle = 1;
		int pawnDouble = 2;
		int diagonalAttack = 1;
		int pawnRow1 = 1;
		int pawnRow2 = 6;
		int powerUpRow1 = 0;
		int powerUpRow2 = 7;
		
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		int y = getLocation().getRow();
		int x = getLocation().getCol();
		
		Location whiteOne = new Location(y-pawnSingle,x);
		Location whiteTwo = new Location(y-pawnDouble,x);
		Location whiteLeft = new Location(y-diagonalAttack,x-diagonalAttack);
		Location whiteRight = new Location(y-diagonalAttack,x+diagonalAttack);
				
		Location blackOne = new Location(y+pawnSingle,x);
		Location blackTwo = new Location(y+pawnDouble,x);
		Location blackLeft = new Location(y+diagonalAttack,x-diagonalAttack);
		Location blackRight = new Location(y+diagonalAttack,x+diagonalAttack);
		
		if(getColor())
		{
			if(y!= powerUpRow1)
			{
				if(board.isValidMove(whiteOne) && board.isBlocked(whiteOne))
					possibleMoves.add(whiteOne);
				if(board.isValidMove(whiteTwo) && getLocation().getRow()==pawnRow2 && board.isBlocked(whiteTwo) && board.isBlocked(whiteOne))
					possibleMoves.add(whiteTwo);
				if(board.isValidMove(whiteLeft) && !board.isBlocked(whiteLeft) && !board.isPieceWhite(whiteLeft))
					possibleMoves.add(whiteLeft);
				if(board.isValidMove(whiteRight) && !board.isBlocked(whiteRight) && !board.isPieceWhite(whiteRight))
					possibleMoves.add(whiteRight);
			}
		}
		else
		{
			if(y!= powerUpRow2)
			{
				if(board.isValidMove(blackOne) && board.isBlocked(blackOne))
					possibleMoves.add(blackOne);
				if(board.isValidMove(blackTwo) && getLocation().getRow()==pawnRow1 && board.isBlocked(blackTwo) && board.isBlocked(blackOne))
					possibleMoves.add(blackTwo);
				if(board.isValidMove(blackLeft) && !board.isBlocked(blackLeft) && board.isPieceWhite(blackLeft))
					possibleMoves.add(blackLeft);
				if(board.isValidMove(blackRight) && !board.isBlocked(blackRight) && board.isPieceWhite(blackRight))
					possibleMoves.add(blackRight);
			}
		}
		return possibleMoves;
	}
	
	public void moveTo(Location moveLoc)
	{
		if((int)Math.abs(getLocation().getRow()-moveLoc.getRow()) > 1)
			doubleMove = true;
		else
			doubleMove = false;
		setLocation(moveLoc);
	}
	
	public String toString()
	{
		return super.toString()+" Pawn";
	}
}