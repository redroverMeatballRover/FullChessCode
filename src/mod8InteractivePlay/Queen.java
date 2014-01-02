package mod8InteractivePlay;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Queen extends ChessPiece
{
	BufferedImage img;// = (BufferedImage) new ImageIcon("rl.png");
	BufferedImage buffered;
	Image image;
	
	private static final long serialVersionUID = 716804568379608862L;
	
	public Queen(boolean isWhite, Location loc)
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
				img = ImageIO.read(new File("ql.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
		else
		{
			try {
				img = ImageIO.read(new File("qd.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
	}
	
	public ArrayList<Location> getMoves(BoardState board)
	{
		int newLocationNum = 32;
		int blockedRowNum = 4;
		int byteSize1 = 8;
		int byteSize2 = 16;
		int byteSize3 = 24;
		int queenMove4 = 32;
		int queenMove5 = 40;
		int queenMove6 = 48;
		int queenMove7 = 56;
		int minimum = 1;
		int boardNum = 8;
		int totalBoard = 64;
		
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		int y = getLocation().getRow();
		int x = getLocation().getCol();
		
		boolean[] isRowBlocked = new boolean[boardNum];
		
		Location[] locs = new Location[totalBoard];
		
		for(int z=1; z<=totalBoard; z++)
		{
			if(z<=boardNum)
				locs[z-minimum] = new Location(y-z%boardNum, x-z%boardNum);
			else if(z<=byteSize2)
				locs[z-minimum] = new Location(y-z%boardNum, x);
			else if(z<=byteSize3)
				locs[z-minimum] = new Location(y-z%boardNum, x+z%boardNum);
			else if(z<=queenMove4)
				locs[z-minimum] = new Location(y, x+z%boardNum);
			else if(z<=queenMove5)
				locs[z-minimum] = new Location(y+z%boardNum, x+z%boardNum);
			else if(z<=queenMove6)
				locs[z-minimum] = new Location(y+z%boardNum, x);
			else if(z<=queenMove7)
				locs[z-minimum] = new Location(y+z%boardNum, x-z%boardNum);
			else
				locs[z-minimum] = new Location(y, x-z%boardNum);
		}

		if(getColor())
		{
			for(int i=0; i<locs.length; i++)
				if(board.isValidMove(locs[i]) && !isRowBlocked[i/boardNum] && (board.isBlocked(locs[i]) || !board.isPieceWhite(locs[i])))
				{
					possibleMoves.add(locs[i]);
					if(!board.isBlocked(locs[i]) && !board.isPieceWhite(locs[i]))
						isRowBlocked[i/boardNum]=true;
				}
				else
					isRowBlocked[i/boardNum]=true;
		}
		else
		{
			for(int i=0; i<locs.length; i++)
				if(board.isValidMove(locs[i]) && !isRowBlocked[i/boardNum] && (board.isBlocked(locs[i]) || board.isPieceWhite(locs[i])))
				{
					possibleMoves.add(locs[i]);
					if(!board.isBlocked(locs[i]) && board.isPieceWhite(locs[i]))
						isRowBlocked[i/boardNum]=true;
				}
				else
					isRowBlocked[i/boardNum]=true;
		}
		return possibleMoves;
	}

	public void moveTo(Location moveLoc)
	{
		setLocation(moveLoc);
	}
	
	public String toString()
	{
		return super.toString()+" Queen";
	}
}