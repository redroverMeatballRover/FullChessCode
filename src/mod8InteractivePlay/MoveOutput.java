package mod8InteractivePlay;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.io.Serializable;

public class MoveOutput extends JPanel implements Serializable
{
	private static final long serialVersionUID = -454150266407155639L;
	
	private ArrayList<String> movePieceOutput;
	int moveNum1 = 99;
	int moveNum2 = 9;
	
	public MoveOutput()
	{
		movePieceOutput = new ArrayList<String>();
	}
	
	public void updateMoveOutput(Location start, Location end, boolean turn)
	{
		String colorOfPiece = "";
		if (turn)
		{
			colorOfPiece = "White";
		}
		else {
			colorOfPiece = "Black";
		}
		
		if(turn) //white piece
		{
			if(movePieceOutput.size()>moveNum1) 
			{
				System.out.println(colorOfPiece  + ": " + start + " " + end);
			}
			else if(movePieceOutput.size()>moveNum2)
			{
				System.out.println(colorOfPiece  + ": " + start + " " + end);
			}
			else
			{
				System.out.println(colorOfPiece  + ": " + start + " " + end);
			}
		}
		else //black piece goes
		{
			if(movePieceOutput.size()>moveNum1)
			{
				System.out.println(colorOfPiece  + ": " + start + " " + end);
			}
			else if(movePieceOutput.size()>moveNum2)
			{
				System.out.println(colorOfPiece  + ": " + start + " " + end);
			}
			else
			{	
				System.out.println(colorOfPiece  + ": " + start + " " + end);
			}
		}
	}
}
//------------------------------------------------------------------------------------------//