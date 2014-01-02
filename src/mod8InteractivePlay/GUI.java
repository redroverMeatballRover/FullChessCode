package mod8InteractivePlay;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

import javax.swing.*;

public class GUI extends JFrame implements ActionListener
{
	int numDivide = 2;
	public boolean fader = false;
	private static final long serialVersionUID = -5825115473793035743L;
	private RunGUI runner;
	private ChessBoard board;
	private PawnPromotion pawnPromo;
	private MoveOutput movesRecording;
	
	
	public GUI(BoardState boardState, RunGUI runner)
	{
		//--------------------------------------------------------------------------------------//
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension boardSize = new Dimension(600,600);
		this.runner = runner;
		
		// MAIN-------------------------------------------------------------------------------//
		setLayout(new BorderLayout());
		setResizable(false);
		//-------------------------------------------------------------------------------//
	
		//MOVE OUTPUT-------------------------------------------------------------------//		
		movesRecording = new MoveOutput();
		//-------------------------------------------------------------------------------//

		//BOARD//-------------------------------------------------------------------//
		board = new ChessBoard(boardState, this, boardSize);
		updateBoard(boardState);		
		add(board,BorderLayout.CENTER);
		//-------------------------------------------------------------------------------//

		pack();
		setLocation(screen.width/numDivide-getSize().width/numDivide, screen.height/numDivide-getSize().height/numDivide);
		repaint();
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	}
	
	public ChessBoard getChessBoard()
	{
		return board;
	}
		
	public boolean getDesignation()
	{
		return fader;
	}
	
	
	public MoveOutput getStringOutputMoves()
	{
		return movesRecording;
	}
	
	public Location processLocation(Location loc)
	{
		int desig = 7;
		if(!fader)
			return loc;
		else
			return new Location(desig-loc.getRow(),desig-loc.getCol());
	}
	
	public boolean getDesignated()
	{
		return fader;
	}
		
	public void gameOver(int outcome)
	{
		int whiteResult = 1;
		int blackResult = -1;
		
		if(outcome==whiteResult)
		{
			System.out.println("CHECKMATE - WHITE WINS");
			
			System.out.println("Enter any Letter Key and ENTER to close.");
			Scanner scan = new Scanner(System.in);
			String acceptDefeat = "";
			acceptDefeat = scan.nextLine();
			System.exit(0);
		}
		else if(outcome==blackResult)
		{
			System.out.println("CHECKMATE - BLACK WINS");
			
			System.out.println("Enter any Letter Key and ENTER to close.");
			Scanner scan = new Scanner(System.in);
			String acceptDefeat = "";
			acceptDefeat = scan.nextLine();
			System.exit(0);
		}
		else
		{
			System.out.println("NO WINNER - GAME IS A TIE");
			
			System.out.println("Enter any Letter Key and ENTER to close.");
			Scanner scan = new Scanner(System.in);
			String acceptDefeat = "";
			acceptDefeat = scan.nextLine();
			System.exit(0);
		}
		setEnabled(false);
	}
	
	//Update string output
	//-----------------------------------------------------------------------//
	public void updateMoveStringLists(Location start, Location end)
	{
		movesRecording.updateMoveOutput(start, end, runner.getTurn());
	}
	
	//PAWN PROMOTION GUI
	//-----------------------------------------------------//
	public void promotion()
	{
		setEnabled(false);
		pawnPromo = new PawnPromotion(this);
		pawnPromo.setVisible(true);
	}
	public void endPromotion()
	{
		setEnabled(true);
		pawnPromo.dispose();
	}
	//--------------------------------------------------//
	
	public void enableSide(boolean turn)
	{
		board.enableSide(turn);
	}
	public void resetBorders()
	{
		board.resetBorders();
	}
	public void resetBackground()
	{
		board.resetBackground();
	}
	public void enable(Location loc, boolean highlight)
	{
		board.enable(loc, highlight);
	}
	public void selectedItem(Location loc)
	{
		board.selectedPiece(loc);
	}
	public void updateSystemChecking(String output, boolean isAlert)
	{}
	public void updateMoveToCheckOrCheckMate(JButton button)
	{
		for(int y=0; y<board.getSide(); y++)
		{
			for(int x=0; x<board.getSide(); x++)
			{
				if(button==board.getButton(y,x))
				{
					updateSystemChecking(""+processLocation(new Location(y,x)), false);
				}
			}
		}
	}
	public void updateBoard(BoardState boardState)
	{
		board.updateBoard(boardState);
	}
	public void actionPerformed(ActionEvent event)
	{
		int netherNum = 0;
		int netherNumPlusOne = 10;
		
		int command = Integer.parseInt(event.getActionCommand());
		
		if(command >= netherNum)
		{
			int x = command%netherNumPlusOne;
			int y = command/netherNumPlusOne;
			
			Location loc = processLocation(new Location(y,x)); 
			runner.enableSelectionAndMove(loc);
		}
		else
			runner.pawnPromotionSystem(command);
	}

//------------------------------------------------------------------------------//

}