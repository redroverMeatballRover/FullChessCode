package Mod3TextFilePlayer;

public class Pawn extends ChessPiece {
	
	String name;

	public Pawn(String name) 
	{
		this.name = name;
	}	
	
	public String getName()
	{
		return name;
	}
	
	public String getColorEnglish()
	{
		String pieceColor = null;
		if (name.equals("p"))
		{
			pieceColor = "Black";
		}
		else if (name.equals("P"))
		{
			pieceColor = "White";
		}
		return pieceColor;	
	}
	
	public String setInBoard() {
		return "| " + name + " |";
	}
	
	public String getColor()
	{
		String pieceColor = "";
		if (name.equals("p"))
		{
			pieceColor = "d";
		}
		else if (name.equals("P"))
		{
			pieceColor = "l";
		}
		return pieceColor;	
	}
	
	public String toString() {
		//something to find the color in here
		return "Pawn";
	}
}
