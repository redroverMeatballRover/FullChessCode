package mod8InteractivePlay;


import java.awt.*;
import javax.swing.*;

public class AlertPawn extends JDialog
{
	private static final long serialVersionUID = -6382879648233899539L;
	private Point parentLoc;
	private GUI parent;
	int resetSizeNum = 2;
	
	public AlertPawn(GUI parent, String title)
	{
		super(parent,title);
		this.parent = parent;
		setSize(300,100);
		
		if(parent.isVisible())
		{
			parentLoc = parent.getLocationOnScreen();
			setLocation((int)parentLoc.getX()+(parent.getWidth()/resetSizeNum-getSize().width/resetSizeNum), (int)parentLoc.getY()+(parent.getHeight()/resetSizeNum-getSize().height/resetSizeNum));
		}
		setResizable(false);
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.PAGE_AXIS));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	public void reset()
	{
		if(parent.isVisible())
			setLocation((int)parentLoc.getX()+(parent.getWidth()/resetSizeNum-getSize().width/resetSizeNum), (int)parentLoc.getY()+(parent.getHeight()/resetSizeNum-getSize().height/resetSizeNum));
	}
}
