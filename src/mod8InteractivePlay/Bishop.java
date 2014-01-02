package mod8InteractivePlay;


import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Bishop extends ChessPiece
{
	private static final long serialVersionUID = -7644829780638720717L;
	BufferedImage img;
	BufferedImage buffered;
	Image image;
	int newLocationNum = 32;
	int blockedRowNum = 4;
	int byteSize1 = 8;
	int byteSize2 = 16;
	int byteSize3 = 24;
	int minimum = 1;
	int boardNum = 8;
	
	
	public Bishop(boolean isWhite, Location loc)
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
				img = ImageIO.read(new File("bl.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
		else
		{
			try {
				img = ImageIO.read(new File("bd.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
	}
		
	public ArrayList<Location> getMoves(BoardState board)
	{
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		int y = getLocation().getRow();
		int x = getLocation().getCol();
		
		boolean[] isRowBlocked = new boolean[blockedRowNum];
		
		Location[] locs = new Location[newLocationNum];
		
		for(int z=1; z<=newLocationNum; z++)
		{
			if(z<=byteSize1)
				locs[z-minimum] = new Location(y-z%boardNum, x-z%boardNum);
			else if(z<= byteSize2)
				locs[z-minimum] = new Location(y-z%boardNum, x+z%boardNum);
			else if(z<= byteSize3)
				locs[z-minimum] = new Location(y+z%boardNum, x+z%boardNum);
			else
				locs[z-minimum] = new Location(y+z%boardNum, x-z%boardNum);
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
				if(board.isValidMove(locs[i]) && !isRowBlocked[i/8] && (board.isBlocked(locs[i]) || board.isPieceWhite(locs[i])))
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
		return super.toString()+" Bishop";
	}
}