package Mod2;

public class Queen extends ChessPiece {
	
	String name;

	public Queen(String name) 
	{
		this.name = name;
	}	
	
	public String setInBoard() {
		return "| " + name + " |";
	}
}
