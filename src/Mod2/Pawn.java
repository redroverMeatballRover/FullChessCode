package Mod2;

public class Pawn extends ChessPiece {
	
	String name;

	public Pawn(String name) 
	{
		this.name = name;
	}	
	
	public String setInBoard() {
		return "| " + name + " |";
	}
}