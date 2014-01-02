	
//	public boolean pieceTeamFinder()
//	{
//		boolean pieceCanAct = false;
//		boolean rookCanAct = false;
//		boolean bishopCanAct = false;
//		boolean knightCanAct = false;
//		boolean queenCanAct = false;
//		//find rook or rooks
//		
//		for (int x = 0; x < 9; x++) 
//		{
//		    for (int y = 0; y < 9; y++)
//		    {
//		    	 if (pieces[x][y] != null) //add color check in there later
//		    	 {
//			    	if (pieces[x][y].toString().equals("Rook") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
//			    	 {
//			           // System.out.println("The " + meatShieldColor + " rook resides at y: " + y + ", x: " + x);
//			            rookX = x;
//			            rookY = y;
//			            if (rookAvenger(rookX, rookY))
//			            {
//			            	rookCanAct = true;
//			            }   	
//			    	}
//			    	else if (pieces[x][y].toString().equals("Bishop") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
//		    		 {
//		    	       //System.out.println("The " + meatShieldColor + " bishop resides at y: " + y + ", x: " + x);
//		    	       bishopX = x;
//		    	       bishopY = y;
//		    	       if (bishopAvenger(bishopX, bishopY))
//		    	       {
//		    	       		bishopCanAct = true;
//		    	       }
//		    	       //run rook from here, would go through rook 1 first and then the next rook, saving time
//		    		}
//			    	else if (pieces[x][y].toString().equals("Knight") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
//		    		 {
//		    	       //System.out.println("The " + meatShieldColor + " knight resides at y: " + y + ", x: " + x);
//		    	       knightX = x;
//		    	       knightY = y;
//		    	       if (knightAvenger(knightX, knightY))
//		    	       {
//		    	       		knightCanAct = true;
//		    	       }
//		    	       //run rook from here, would go through rook 1 first and then the next rook, saving time
//		    		}
//			    	else if (pieces[x][y].toString().equals("Queen") && pieces[x][y].getColor().equals(meatShieldColor)) //add color check in there later
//		    		 {
//		    	       //System.out.println("The " + meatShieldColor + " Queen resides at y: " + y + ", x: " + x);
//		    	       queenX = x;
//		    	       queenY = y;
//		    	       if (queenAvenger(queenX, queenY))
//		    	       {
//		    	       		queenCanAct = true;
//		    	       }
//		    	       //run rook from here, would go through rook 1 first and then the next rook, saving time
//		    		}
//			    	
//			    	else {
//			    		//System.out.println("Continue searching");
//			    	}
//		    	 }
//		    }
//		}
//		
//		if (rookCanAct || bishopCanAct || knightCanAct || queenCanAct)
//		{
//			pieceCanAct = true;
//		}
//		
//		return pieceCanAct;
//	}	
//
//	//attacking or defence actions for the remaining friendly peices
//	public boolean rookAvenger(int rookX, int rookY)
//	{
//		boolean rookAct = false;
//		boolean rookCanAttack = false;
//		boolean rookCanDefend = false;
//		
//		System.out.println("This is rook defence position: " + defenceY + ", " + defenceX);
//		
//		if (pieces[rookX][rookY] != null)
//		{
//			//System.out.println("ROOK PRESENT.");
//			//attack
//			if (pieces[rookX][rookY].legalMove(pieces, rookY, rookX, threatY, threatX)) //checks legal moves
//			{
//				if (pieces[rookX][rookY].pathIsClear(pieces, rookY, rookX, threatY, threatX))//checks for barriers
//				{
//					rookCanAttack = true;
//					//System.out.println("Rook can attack the threat.");
//				}		
//				else 
//				{
//					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//					rookCanAttack = false;
//				}
//			}
//			//defend
//			else if(pieces[rookX][rookY].legalMove(pieces, rookY, rookX, defenceY, defenceX))
//			{
//				if(pieces[rookX][rookY].pathIsClear(pieces, rookY, rookX, defenceY, defenceX))
//				{
//					rookCanDefend = true;
//					System.out.println("Rook can defend the threat.");
//				}
//				else {
//					rookCanDefend = false;
//					System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//				}
//				
//			}
//			else
//			{
//				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
//				rookCanAttack = false;
//				rookCanDefend = false;
//			}	
//		}
//		
//		if(rookCanAttack || rookCanDefend) 
//		{
//			rookAct = true;
//		}
//		
//		return rookAct;
//	}
//	public boolean bishopAvenger(int bishopX, int bishopY)
//	{
//		boolean bishopAct = false;
//		boolean bishopCanAttack = false;
//		boolean bishopCanDefend = false;
//	
//		if (pieces[bishopX][bishopY] != null)
//		{
//			//System.out.println("BISHOP PRESENT.");
//			if (pieces[bishopX][bishopY].legalMove(pieces, bishopY, bishopX, threatY, threatX)) //checks legal moves
//			{
//				if (pieces[bishopX][bishopY].pathIsClear(pieces, bishopY, bishopX, threatY, threatX))//checks for barriers
//				{
//					bishopCanAttack = true;
//					//System.out.println("Bishop can attack the threat.");
//				}
//				else 
//				{
//					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//					bishopCanAttack = false;
//				}
//			}
//			else if (pieces[bishopX][bishopY].legalMove(pieces, bishopY, bishopX, defenceY, defenceX)) //checks legal moves
//			{
//				if (pieces[bishopX][bishopY].pathIsClear(pieces, bishopY, bishopX, defenceY, defenceX))//checks for barriers
//				{
//					bishopCanDefend = true;
//					System.out.println("Bishop can defend the threat.");
//				}
//				else 
//				{
//					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//					bishopCanDefend = false;
//				}
//			}
//			else
//			{
//				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
//				bishopCanDefend = false;
//				bishopCanAttack = false;
//			}	
//		}
//		
//		if (bishopCanAttack || bishopCanDefend)
//		{
//			bishopAct = true;
//		}
//		
//		return bishopAct;
//	}
//	public boolean knightAvenger(int knightX, int knightY)
//	{
//		boolean knightAct = false;
//		boolean knightCanAttack = false;
//		boolean knightCanDefend = false;
//		
//	
//		if (pieces[knightX][knightY] != null)
//		{
//			//System.out.println("KNIGHT PRESENT.");
//			if (pieces[knightX][knightY].legalMove(pieces, knightY, knightX, threatY, threatX)) //checks legal moves
//			{
//				if (pieces[knightX][knightY].pathIsClear(pieces, knightY, knightX, threatY, threatX))//checks for barriers
//				{
//					knightCanAttack = true;
//					//System.out.println("Knight can attack the threat.");
//				}
//				else 
//				{
//					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//					knightCanAttack = false;
//				}
//			}
//			//System.out.println("KNIGHT PRESENT.");
//			else if (pieces[knightX][knightY].legalMove(pieces, knightY, knightX, defenceY, defenceX)) //checks legal moves
//			{
//				if (pieces[knightX][knightY].pathIsClear(pieces, knightY, knightX, defenceY, defenceX))//checks for barriers
//				{
//					knightCanDefend = true;
//					System.out.println("Knight can defend the threat.");
//				}
//				else 
//				{
//					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//					knightCanDefend = false;
//				}
//			}
//			else
//			{
//				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
//				knightCanAttack = false;
//				knightCanDefend = false;
//			}	
//		}
//		
//		if (knightCanAttack || knightCanDefend)
//		{
//			knightAct = true;
//		}
//		
//		return knightAct;
//	}
//	public boolean queenAvenger(int queenX, int queenY)
//	{
//		boolean queenCanAttack = false;
//		boolean queenCanDefend = false;
//		boolean queenAct = false;
//	
//		if (pieces[queenX][queenY] != null)
//		{
//			//System.out.println("QUEEN PRESENT.");
//			if (pieces[queenX][queenY].legalMove(pieces, queenY, queenX, threatY, threatX)) //checks legal moves
//			{
//				if (pieces[queenX][queenY].pathIsClear(pieces, queenY, queenX, threatY, threatX))//checks for barriers
//				{
//					queenCanAttack = true;
//					System.out.println("Queen can attack the threat.");
//				}
//				else 
//				{
//					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//					queenCanAttack = false;
//				}
//			}
//			else if (pieces[queenX][queenY].legalMove(pieces, queenY, queenX, defenceY, defenceX)) //checks legal moves
//			{
//				if (pieces[queenX][queenY].pathIsClear(pieces, queenY, queenX, defenceY, defenceX))//checks for barriers
//				{
//					queenCanDefend = true;
//					System.out.println("Queen can defend the threat.");
//				}
//				else 
//				{
//					//System.out.println("MOVE BLOCKED - another piece is blocking your route.");
//					queenCanDefend = false;
//				}
//			}
//			else
//			{
//				//System.out.println("ILLEGAL MOVE - move is against the rules. Please try again.");
//				queenCanAttack = false;
//				queenCanDefend = false;
//			}	
//		}
//		if (queenCanAttack || queenCanDefend)
//		{
//			queenAct = true;
//		}
//		return queenAct;
//	}