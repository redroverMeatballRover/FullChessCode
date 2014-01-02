package mod8InteractivePlay;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public abstract class ChessPiece implements Serializable
{
	private boolean isWhite;
	
	private Location loc;
	
	public ChessPiece(boolean isWhite, Location loc)
	{
		this.isWhite = isWhite;
		this.loc = loc;
	}
	
	public void setLocation(Location loc)
	{
		this.loc = loc;
	}
	
	public Location getLocation()
	{
		return loc;
	}
	
	public boolean getColor()
	{
		return isWhite;
	}
	
	public abstract void draw(Graphics g);
		
	public abstract ArrayList<Location> getMoves(BoardState board);
	
	public abstract void moveTo(Location moveLoc);
	
	public static ChessPiece build(ChessPiece thisPiece)
	{
		if(thisPiece instanceof King)
		{
			King piece = new King(thisPiece.getColor(), thisPiece.getLocation());
			piece.setHasMoved(((King)thisPiece).getHasMoved());
			piece.setIsChecked(((King)thisPiece).isChecked());
			return piece;
		}
		else if(thisPiece instanceof Queen)
			return new Queen(thisPiece.getColor(), thisPiece.getLocation());
		else if(thisPiece instanceof Pawn)
		{
			Pawn piece = new Pawn(thisPiece.getColor(), thisPiece.getLocation());
			piece.setDoubleMove(((Pawn)thisPiece).getDoubleMove());
			return piece;
		}
		else if(thisPiece instanceof Rook)
		{
			Rook piece = new Rook(thisPiece.getColor(), thisPiece.getLocation());
			piece.setHasMoved(((Rook)thisPiece).getHasMoved());
			return piece;
		}
		else if(thisPiece instanceof Bishop)
			return new Bishop(thisPiece.getColor(), thisPiece.getLocation());
		else if(thisPiece instanceof Knight)
			return new Knight(thisPiece.getColor(), thisPiece.getLocation());
		else
			return null;
	}

	public String toString()
	{
		if(isWhite)
			return "White";
		else
			return "Black";
	}
}
