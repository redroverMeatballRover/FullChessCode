package Mod2;

public class Bishop extends ChessPiece {
	
	String name;

	public Bishop(String name) 
	{
		this.name = name;
	}	
	
	public String setInBoard() {
		return "| " + name + " |";
	}
}
