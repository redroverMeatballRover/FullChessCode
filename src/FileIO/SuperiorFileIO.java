package FileIO;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SuperiorFileIO {

	
	public static void main(String[] args) throws IOException {
		moveBetter();
	}
	
	public static void moveBetter() throws IOException
	{
		String piece = " "; //1
		String color = " "; //2
		String position = " "; //3
		String position2 = " "; //4
		String capture = " "; //5
		//or
		String kingPos1 = "";//1
		String kingPos2 = "";//2
		String rookPos1 = "";//3
		String rookPos2 = "";//4
		
		ReadFile readFile = new ReadFile();		
		String input = readFile.readTextFile("TestMoves");
		
		Pattern matchingMoves = Pattern.compile("(q|k|b|p|n|r+)(l|d)(\\w\\d) ?(\\w\\d)?([*])?|(\\w\\d) (\\w\\d) (\\w\\d) (\\w\\d)");
		Matcher m1 = matchingMoves.matcher(input);
		
		while (m1.find())
		{
			//find piece
				if(m1.group(6) != null && m1.group(6).matches("(\\w\\d)")) //castling part 1
					kingPos1 = m1.group(6);
				else if (m1.group(1).equals("q"))
					piece = "Queen";
				else if (m1.group(1).equals("k"))
					piece = "King";
				else if (m1.group(1).equals("b"))
					piece = "Bishop";
				else if (m1.group(1).equals("p"))
					piece = "Pawn";
				else if (m1.group(1).equals("n"))
					piece = "Knight";
				else if (m1.group(1).matches("(q|k|b|p|n|r+)") &&  m1.group(1).equals("r"))
					piece = "Rook";
				else {
					System.out.println("PIECE FAILED");
					piece = "piece Failed";
				}
			//find color
				if(m1.group(7) != null && m1.group(7).matches("(\\w\\d)")) //castling part 2
					kingPos2 = m1.group(7);
				else if(m1.group(2).equals("d"))
					color = "Black";
				else if(m1.group(2).matches("(l|d)") && m1.group(2).equals("l"))
					color = "White";
				else {
					System.out.println("COLOR FAILED");
					piece = "color Failed";
				}
			//set starting position
				if (m1.group(7) != null && m1.group(7).matches("(\\w\\d)") )
				rookPos1 = m1.group(7); //castling part 3
				else
					position = m1.group(3);
			//Castling
				if (m1.group(8) != null && m1.group(9) != null && m1.group(8).matches("(\\w\\d)") && m1.group(9).matches("(\\w\\d)"))
				{
					rookPos1 = m1.group(8);
					rookPos2 = m1.group(9);
					System.out.println("The king moves from " + kingPos1 + " to " + kingPos2 + " while the rook moves from " + rookPos1 + " to " + rookPos2);	
				}
			//if the player does not move // finds a piece
				else if (m1.group(4) == null)
					System.out.println("The " + color + " " + piece + " resides at " + position);			
			
			//set ending position
				else if (m1.group(4) != null && m1.group(5) == null)
				{
					position2 = m1.group(4);
					System.out.println("The " + color + " " + piece + " moves from " + position + " to " + position2);
				}
			//set ending position and capture
				else if (m1.group(4) != null && m1.group(5) != null)
				{
					//System.out.println("capture occured");
					position2 = m1.group(4);
					capture = m1.group(5); //unused
					System.out.println("The " + color + " " + piece + " moves from " + position + " to " + position2  + " and captures the piece on " + position2);
				}
				
				else {
					System.out.println("PARSE ERROR");
				}
		}
	}
}

