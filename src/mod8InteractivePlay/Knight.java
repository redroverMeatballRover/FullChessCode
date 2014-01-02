package mod8InteractivePlay;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class Knight extends ChessPiece
{
	BufferedImage img;
	BufferedImage buffered;
	Image image;
	
	private static final long serialVersionUID = 851797694201220077L;
	
	public Knight(boolean isWhite, Location loc)
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
				img = ImageIO.read(new File("nl.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
		else
		{
			try {
				img = ImageIO.read(new File("nd.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
	}
	
	public ArrayList<Location> getMoves(BoardState board)
	{
		int mandatory1 = 1;
		int mandatory2 = 2;
		int boardNum = 8;
		
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		int y = getLocation().getRow();
		int x = getLocation().getCol();
		
		Location[] locs = new Location[boardNum];
		locs[0] = new Location(y-mandatory1,x-mandatory2);
		locs[1] = new Location(y-mandatory2,x-mandatory1);
		locs[2] = new Location(y-mandatory2,x+mandatory1);
		locs[3] = new Location(y-mandatory1,x+mandatory2);
		locs[4] = new Location(y+mandatory1,x+mandatory2);
		locs[5] = new Location(y+mandatory2,x+mandatory1);
		locs[6] = new Location(y+mandatory2,x-mandatory1);
		locs[7] = new Location(y+mandatory1,x-mandatory2);
		
		if(getColor())
		{
			for(Location z: locs)
				if(board.isValidMove(z) && (board.isBlocked(z) || !board.isPieceWhite(z)))
					possibleMoves.add(z);
		}
		else
		{
			for(Location z: locs)
				if(board.isValidMove(z) && (board.isBlocked(z) || board.isPieceWhite(z)))
					possibleMoves.add(z);
		}
		return possibleMoves;
	}
	
	public void moveTo(Location moveLoc)
	{
		setLocation(moveLoc);
	}
	
	public String toString()
	{
		return super.toString()+" Knight";
	}
}