package Mod2;

public class Rook extends ChessPiece {
	
	String name;

	public Rook(String name) 
	{
		this.name = name;
	}	
	
	
	public String setInBoard() {
		return "| " + name + " |";
	}
}
