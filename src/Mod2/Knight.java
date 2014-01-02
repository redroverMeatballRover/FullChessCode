package Mod2;

public class Knight extends ChessPiece {
	
	String name;

	public Knight(String name) 
	{
		this.name = name;
	}	
	
	public String setInBoard() {
		return "| " + name + " |";
	}
}