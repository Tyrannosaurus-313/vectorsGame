package game;

import java.util.Random;

import game.Grid.ControlState;

public class ComputerPlayerUnit {
	enum CPUState {
		RANDOM,
		EASY,
		MEDIUM,
		AGGRESSIVE,
		HARD
	}
	
	public CPUState difficulty;
	public Random randy = new Random();
	public Grid gridRef;
	
	public ComputerPlayerUnit(Grid gridRef)
	{
		this.gridRef = gridRef;
	}
	public ComputerPlayerUnit(CPUState difficulty, Grid gridRef)
	{
		this.difficulty = difficulty;
		this.gridRef = gridRef;
	}
	
		
	Coordinate pastCoord;
	public Coordinate getTurn()
	{
		int[] coordArray = new int[2];
		if (difficulty == difficulty.AGGRESSIVE) //Tries to place immediately after chains of 2
		{
			
		}
		else if (difficulty == difficulty.EASY && pastCoord != null) //Simply an empty neighbor to the last place it picked.
		{
			for (int offset = -1; offset <= 1; offset++)
			{
				if (offset == 0 || offset + pastCoord.x > 7 || offset + pastCoord.x < 0 
						|| offset + pastCoord.y > 7 || offset + pastCoord.y < 0 )
					continue;
				Coordinate coord = new Coordinate(pastCoord.x + offset, pastCoord.y);	
				Coordinate coord2 = new Coordinate(pastCoord.x, pastCoord.y + offset);		
				if (gridRef.getState(coord.x, coord.y) == null)
				{
					pastCoord = coord;
					return coord;
				}
				else if (gridRef.getState(coord2.x, coord2.y) == null)
				{
					pastCoord = coord2;
					return coord2;
				}
			}
		}
		else if (difficulty == difficulty.MEDIUM && pastCoord != null) //Find most immediate bump to score and pick it
		{

			int[][] deltaScores = new int[8][8];//We measure the delta points for each square

			ScoreCalculator cloneCalc = new ScoreCalculator();
			Grid cloneGrid = new Grid(); //We clone the grid & calc
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					cloneGrid.setCell(i, j, gridRef.getState(i, j));
				}
			}
			cloneCalc.updateScores(cloneGrid);
			
			int origScore = cloneCalc.getComputerScore();

			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					if (gridRef.getState(i, j) != null)
					{
						deltaScores[j][i] = -999;
						continue;
					}
					cloneGrid.setCell(i, j, ControlState.COMPUTER);
					cloneCalc.updateScores(cloneGrid);
					deltaScores[j][i] = cloneCalc.getComputerScore() - origScore;
					System.out.printf("%3d\t", deltaScores[j][i]);
					cloneGrid.setCell(i, j, null);
					cloneCalc.updateScores(cloneGrid);
				}
				System.out.println();
			}
			
			int currMax = 0;
			Coordinate maxValCoord = null;
			for (int i = 0; i < 8; i++)
			{
				for (int j = 0; j < 8; j++)
				{
					if (deltaScores[j][i] > currMax)
					{
						currMax = deltaScores[j][i];
						maxValCoord = new Coordinate(j, i);						
					}
				}
			}
			
			if (maxValCoord != null)
				return maxValCoord;
		}
		else if (difficulty == difficulty.HARD) //Weighs the points they could gain in the next turn against blocking the player, picks the better option
		{
//			int[] deltaScore = new int[64];//We measure the delta points for each square
//
//			ScoreCalculator cloneCalc = new ScoreCalculator();
//			Grid cloneGrid = new Grid(); //We clone the grid & calc
//			for (int i = 0; i < 8; i++)
//			{
//				for (int j = 0; j < 8; j++)
//				{
//					cloneGrid.setCell(i, j, gridRef.getState(i, j));
//				}
//			}
//			cloneCalc.updateScores(cloneGrid);
//			
//			int origScore = cloneCalc.getComputerScore();
//
//			for (int i = 0; i < 8; i++)
//			{
//				for (int j = 0; j < 8; j++)
//				{
//					cloneGrid.setCell(i, j, co);
//				}
//			}

		}
		
		Coordinate coord = new Coordinate(randy.nextInt(gridRef.lengths.colCount), randy.nextInt(gridRef.lengths.rowCount));
		while (gridRef.getState(coord.x, coord.y) != null)
		{
			System.out.println("Location Occupied, trying another");
			coord = new Coordinate(randy.nextInt(gridRef.lengths.colCount), randy.nextInt(gridRef.lengths.rowCount));			
		}
		System.out.println("Random Loc");
		pastCoord = coord;
		return coord;
	}
}
