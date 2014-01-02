package Mod2;

public class King extends ChessPiece {
	
	String name;

	public King(String name) 
	{
		this.name = name;
	}	
	
	public String setInBoard() {
		return "| " + name + " |";
	}
}
