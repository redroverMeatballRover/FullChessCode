package mod8InteractivePlay;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Rook extends ChessPiece
{
	private static final long serialVersionUID = 3819757220079709631L;
	private static final int TYPE = 0;
	private boolean hasMoved;
	
	BufferedImage img;// = (BufferedImage) new ImageIcon("rl.png");
	BufferedImage buffered;
	Image image;
	
	ImageObserver observer;
	
	public Rook(boolean isWhite, Location loc)
	{
		super(isWhite, loc);
		hasMoved=false;
	}
		
	public void draw(Graphics g)
	{
		final int positioningSide = 20;
		final int positioningTop = 15;
		
		if(getColor())
		{
			try {
				img = ImageIO.read(new File("rl.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide,  positioningTop, null);
		}
		else
		{
			try {
				img = ImageIO.read(new File("rd.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img,positioningSide,  positioningTop, null);
		}
	}
	
	public boolean getHasMoved()
	{
		return hasMoved;
	}
	
	public void setHasMoved(boolean input)
	{
		hasMoved=input;
	}
	
	public ArrayList<Location> getMoves(BoardState board)
	{
		int newLocationNum = 32;
		int blockedRowNum = 4;
		int byteSize1 = 8;
		int byteSize2 = 16;
		int byteSize3 = 24;
		int minimum = 1;
		int boardNum = 8;
		
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		int y = getLocation().getRow();
		int x = getLocation().getCol();
		
		boolean[] isRowBlocked = new boolean[blockedRowNum];
		
		Location[] locs = new Location[newLocationNum];
		
		for(int z=1; z<=newLocationNum; z++)
		{
			if(z<=boardNum)
				locs[z-minimum] = new Location(y-z%boardNum, x);
			else if(z<=byteSize2)
				locs[z-minimum] = new Location(y, x+z%boardNum);
			else if(z<= byteSize3)
				locs[z-minimum] = new Location(y+z%boardNum, x);
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
		hasMoved=true;
	}
	
	public String toString()
	{
		return super.toString()+" Rook";
	}

	
}