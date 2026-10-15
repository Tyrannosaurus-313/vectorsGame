package game;

import java.util.Random;

public class ComputerPlayerUnit {
	enum controlState {
		RANDOM,
//		Easy,
		DEFENSIVE,
		HARD;
	}
	
	public controlState currentState;
	public Random randy = new Random();
	public Grid gridRef;
	
	public ComputerPlayerUnit(Grid gridRef)
	{
		this.gridRef = gridRef;
	}
	public ComputerPlayerUnit(controlState state, Grid gridRef)
	{
		currentState = state;
		this.gridRef = gridRef;
	}
	
	public int[] getTurn()
	{
		int[] coordArray = new int[2];
		if (currentState == controlState.Defensive)
		{
			
		}
		else if (currentState == controlState.Hard)
		{
			
		}
		else
		{
			
		}
		coordArray[0] = randy.nextInt(gridRef.getGridLengths()[0]);
		coordArray[1] = randy.nextInt(gridRef.getGridLengths()[1]);
		return coordArray;
	}
}
