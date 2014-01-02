package mod8InteractivePlay;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

public class King extends ChessPiece
{
	private static final long serialVersionUID = -3292941590288943851L;
	private boolean isChecked;
	private boolean hasMoved;
	BufferedImage img;// = (BufferedImage) new ImageIcon("rl.png");
	BufferedImage buffered;
	Image image;
	
	public King(boolean isWhite, Location loc)
	{
		super(isWhite, loc);
		
		hasMoved=false;
		
		isChecked=false;		
	}
	
	
	public void draw(Graphics g)
	{
		final int positioningSide = 20;
		final int positioningTop = 15;
		
		if(getColor())
		{
			try {
				img = ImageIO.read(new File("kl.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
		}
		else
		{
			try {
				img = ImageIO.read(new File("kd.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.drawImage(img, positioningSide, positioningTop, null);
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
	
	public boolean isChecked()
	{
		return isChecked;
	}
	
	public void setIsChecked(boolean check)
	{
		isChecked=check;
	}
	
	public void updateIsChecked(BoardState board)
	{
		ArrayList<Location> possibleChecks = getPossibleChecks(board);
		
		for(int a=0; a<possibleChecks.size(); a++)
			if(possibleChecks.get(a).equals(getLocation()))
			{
				isChecked = true;
				break;
			}
			else
				isChecked=false;
	}
	
	public ArrayList<Location> getPossibleChecks(BoardState board)
	{
		int colunLuck = 2;
		ArrayList<Location> checks = new ArrayList<Location>();
		for(int y=0; y<board.getState().length; y++)
			for(int x=0; x<board.getState().length; x++)
				if(board.getState()[y][x]!=null && board.getState()[y][x].getColor()!=getColor())
				{
					ArrayList<Location> tempChecks = board.getState()[y][x].getMoves(board);
					if(board.getState()[y][x] instanceof King)
					{
						for(int a=0; a<tempChecks.size(); a++)
							if(Math.abs((board.getState()[y][x].getLocation().getCol())-(tempChecks.get(a).getCol()))==colunLuck)
							{
								tempChecks.remove(a);
								a--;
							}
					}
					for(int a=0; a<tempChecks.size(); a++)
						checks.add(tempChecks.get(a));
				}
		return checks;
	}
	
	public ArrayList<Location> getMoves(BoardState board)
	{
		int kingMove = 1;
		int mig = 2;
		int originLoc = 10;
		
		
		ArrayList<Location> possibleMoves = new ArrayList<Location>();
		int y = getLocation().getRow();
		int x = getLocation().getCol();
		
		Location[] locs = new Location[originLoc];
		
		locs[0] = new Location(y-kingMove,x-kingMove);
		locs[1] = new Location(y-kingMove,x);
		locs[2] = new Location(y-kingMove,x+kingMove);
		locs[3] = new Location(y,x-kingMove);
		locs[4] = new Location(y,x+kingMove);
		locs[5] = new Location(y+kingMove,x+kingMove);
		locs[6] = new Location(y+kingMove,x);
		locs[7] = new Location(y+kingMove,x-kingMove);
		
		locs[8] = new Location(y,x-mig);
		locs[9] = new Location(y,x+mig);
		
		for(Location z: locs)
			if(board.isValidMove(z) && (board.isBlocked(z) || board.isPieceWhite(z)!=getColor()))
				possibleMoves.add(z);
		
		return possibleMoves;
	}

	public void moveTo(Location moveLoc)
	{
		setLocation(moveLoc);
		hasMoved=true;
	}
	
	public String toString()
	{
		return super.toString()+" King";
	}
}