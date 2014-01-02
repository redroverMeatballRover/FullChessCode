package mod4TurnTakingPlayerInput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class VariablesOfReadParseSuperClass {

	//classes
	protected Piece pieceClass = new Piece();
	protected PrototypePiece prototypePiece;
	
	//matchers
	//public final Pattern settingPiece = Pattern.compile("(?<piece>q|k|b|p|n|r+)(?<color>l|d)(?<x>\\w)(?<y>\\d)"); //original
	public final Pattern settingPiece = Pattern.compile("^(?<piece>q|k|b|p|n|r+)(?<color>l|d)(?<x>\\w)(?<y>\\d)$"); // steves
	public final Pattern basicMove = Pattern.compile("(?<piece>q|k|b|p|n|r+)(?<color>l|d)(?<x>\\w)(?<y>\\d) ?(?<x2>\\w)(?<y2>\\d)(?<capture>[*])?");
	public final Pattern castling = Pattern.compile("(?<castle1>\\w\\d) (?<castle2>\\w\\d) (?<castle3>\\w\\d) (?<castle4>\\w\\d)");
	
	//identifier hashmaps
	protected HashMap<String, String> pieceMatches = new HashMap<String, String>();
	protected HashMap<String, String> colorMatches = new HashMap<String, String>();
	
	//arraylist carriers
	public ArrayList<String> fileLines = new ArrayList<String>();
	public ArrayList<PrototypePiece> prototypePieceList = new ArrayList<PrototypePiece>();
	public ArrayList<String> textFileCommander = new ArrayList<String>();
	
	//variables
	protected String piece = " ";
	protected String color = " ";
	String position1 = " ";
	String position2 = " ";
	protected String placeY = "";
	protected String placeX = "";
	protected String pieceColor = "";

	public VariablesOfReadParseSuperClass() {
		super();
	}
	
	//identifier hashmaps
	public void hashIdentities() {
		pieceMatches.put("q", "Queen");
		pieceMatches.put("k", "King");
		pieceMatches.put("b", "Bishop");
		pieceMatches.put("n", "Knight");
		pieceMatches.put("r", "Rook");
		pieceMatches.put("p", "Pawn");
		colorMatches.put("d", "d");
		colorMatches.put("l", "l");		
	}

	//english version hashmaps
	public void hashIdentitiesEnglishVersion() {
		pieceMatches.put("q", "Queen");
		pieceMatches.put("k", "King");
		pieceMatches.put("b", "Bishop");
		pieceMatches.put("n", "Knight");
		pieceMatches.put("r", "Rook");
		pieceMatches.put("p", "Pawn");
		colorMatches.put("d", "Black");
		colorMatches.put("l", "White");		
	}

}