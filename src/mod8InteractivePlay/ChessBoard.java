package mod8InteractivePlay;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.*;
import java.io.Serializable;

public class ChessBoard extends JPanel implements Serializable
{
	private static final long serialVersionUID = -1246957520079405796L;
	private BoardState boardState;
	
	private boolean derivitive;
	transient private GUI listener;
	private JButton[][] squares;
	private Color whiteSquare;
	private Color redSquare;
	BufferedImage img;
	
	public ChessBoard(BoardState boardState, GUI listener, Dimension size)
	{
		super(new GridLayout(8,8));
		
		this.boardState = boardState;
		derivitive = false;
		this.listener = listener;
		//***
	
		whiteSquare = new Color(255,255,255);
		redSquare = new Color(178,34,34);

		
		setOpaque(true);
		setBackground(redSquare);
		setPreferredSize(size);
		
		
		//PAINT BUTTON CLASS
		//-----------------------------------------------------------------------//
		@SuppressWarnings("serial")
		class PaintedButton extends JButton
		{
			private BoardState boardState;
			
			//paint button
			public PaintedButton(BoardState boardState)
			{
				this.boardState = boardState;
			}
		
			public void paintComponent(Graphics g) 
			{	
				super.paintComponent(g); // Paint background, border
				int x = -1;
				int y = -1;
				for(int a=0; a<boardState.getState().length; a++)
					for(int b=0; b<boardState.getState()[a].length; b++)
						if(squares[a][b].equals(this))
						{
							y=a;
							x=b;
						}
				if(x!=-1 && y!=-1)
				{
					x = locationSystem(new Location(x,y)).getRow();
					y = locationSystem(new Location(x,y)).getCol();
					if(boardState.getState()[y][x]!=null)
					{
						boardState.getState()[y][x].draw(g);
					}
				}
			}
		}
		//--------------------------------------------------------------------//
		
		squares = new PaintedButton[8][8];
		
		for(int y=0; y<squares.length; y++)
			for(int x=0; x<squares.length; x++)
			{
				squares[y][x] = new PaintedButton(this.boardState);
				squares[y][x].setBorder(BorderFactory.createRaisedBevelBorder());
				squares[y][x].setActionCommand(""+y+x);
			}
		
		for(int y=0; y<squares.length; y++)
			for(int x=0; x<squares.length; x++)
				add(squares[y][x]);
		resetBackground();
	}
	
	public void setListener(GUI listener)
	{
		this.listener = listener;
	}
	
	public int getSide()
	{
		return squares.length;
	}
	
	public Location locationSystem(Location loc)
	{
		int reposs = 7; 
		if(!derivitive)
			return loc;
		else
			return new Location(reposs-loc.getRow(),reposs-loc.getCol());
	}
	
	public JButton getAtLocation(Location loc)
	{
		loc = locationSystem(loc);
		return squares[loc.getRow()][loc.getCol()]; 
	}
	
	public JButton getButton(int y, int x)
	{
		if(y >= 0 && y <= 7 && x >=0 && x <= 7)
			return squares[y][x];
		else
			return null;
	}
	
	public boolean hasActionListener(Location loc)
	{
		ActionListener[] list = squares[loc.getRow()][loc.getCol()].getActionListeners();
		if(list.length > 0)
				return true;
		return false;
	}
	
	public void disableAll()
	{
		for(int y=0; y<squares.length; y++)
			for(int x=0; x<squares.length; x++)
				if(hasActionListener(new Location(y,x)))
					squares[y][x].removeActionListener(listener);
	}
	
	public void enableSide(boolean turn)
	{
		disableAll();
		for(int y=0; y<squares.length; y++)
			for(int x=0; x<squares.length; x++)
				if(boardState.getState()[y][x]!=null && boardState.getState()[y][x].getColor()==turn && !hasActionListener(locationSystem(new Location(y,x))))
					getAtLocation(new Location(y,x)).addActionListener(listener);
	}
	
	public void enableLocation(Location loc)
	{
		if(!hasActionListener(new Location(loc.getRow(),loc.getCol())))
			squares[loc.getRow()][loc.getCol()].addActionListener(listener);
	}
	
	public void deritivePull()
	{
		derivitive = !derivitive;
		updateBoard(boardState);
	}
	
	public void resetBorders()
	{
		for(int y=0; y<squares.length; y++)
			for(int x=0; x<squares.length; x++)
				squares[y][x].setBorder(BorderFactory.createRaisedBevelBorder());
	}
	
	public void resetBackground()
	{
		int start = 0;
		
		for(int y=0; y<squares.length; y++)
			for(int x=0; x<squares.length; x++)
			{
				if(y%2==0)
					if(x%2==start)
						squares[y][x].setBackground(whiteSquare);
					else
						squares[y][x].setBackground(redSquare);
				else
					if(x%2==start)
						squares[y][x].setBackground(redSquare);
					else
						squares[y][x].setBackground(whiteSquare);
			}
		repaint();
	}
	
	public void enable(Location loc, boolean highlight)
	{
		loc = locationSystem(loc);
		enableLocation(loc);
		if(highlight)
			highlightPossibleMoves(loc);
	}
	
	// highlight selected and update
	//-----------------------------------------------------------------------//
	public void highlightPossibleMoves(Location loc)
	{
		squares[loc.getRow()][loc.getCol()].setBackground(new Color(0,191,255));
	}
	
	public void selectedPiece(Location loc)
	{
		getAtLocation(new Location(loc.getRow(),loc.getCol())).setBorder(BorderFactory.createLineBorder(Color.BLUE, 5));
	}
	
	public void updateBoard(BoardState boardState)
	{
		resetBackground();
		resetBorders();
		repaint();
	}
	//-----------------------------------------------------------------------//
}
