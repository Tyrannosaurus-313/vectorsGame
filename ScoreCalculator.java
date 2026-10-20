package game;

import game.Grid.ControlState;

public class ScoreCalculator {
	
	private int playerScore;
	private int computerScore;
	//Strategy
	// Search for the largest chain of control states to smallest, compiling the total point counts
	
//	public WinState
//	{
//		
//	}
	public int getPlayerScore() { return playerScore; }
	public int getComputerScore() { return computerScore; }
	
	public void updateScores(Grid gameGrid)
	{
		int colCount = gameGrid.lengths.colCount;
		int rowCount = gameGrid.lengths.rowCount;
		playerScore = 0;
		computerScore = 0;
		boolean[][] checkedCells  = new boolean[colCount][rowCount];
		for (int i = 0; i < colCount; i++)
		{
			for (int j = 0; j < rowCount; j++)
			{
				if (checkedCells[i][j])
				{
					continue;
				}
				
				ControlState currState = gameGrid.getState(i, j);
				if (currState != null)
				{
					int xCount = 1;
					for (int sRow = 1; sRow <= 5; sRow++)
					{			
//						System.out.println(j + " " + sRow + " " + rowCount);
						if (j + sRow >= colCount)
						{
							break;
						}
						if (currState != gameGrid.getState(i, j + sRow) || checkedCells[i][j + sRow])
						{
							break;
						}			
						else
						{
							xCount++;
						}
					}
					int yCount = 1;
					for (int sCol = 1; sCol <= 5; sCol++)
					{
//						System.out.println(i + sCol + " " + colCount);
						if (i + sCol >= rowCount)
						{
							break;
						}
						if (currState != gameGrid.getState(i + sCol, j) || checkedCells[i + sCol][j])
						{
							break;
						}
						else
						{
							yCount++;
						}
					}
					if (xCount >= yCount)
					{
						for (int c = 0; c < xCount; c++)
						{
							checkedCells[i][j + c] = true;
						}
						computeScore(currState, xCount);
					}
					else
					{
						for (int c = 0; c < yCount; c++)
						{
							checkedCells[i + c][j] = true;
						}
						computeScore(currState, yCount);
					}
				}
			}
		}
	}
	
	private void computeScore(ControlState player, int count)
	{
		int points = 0;
		if (count == 3)
		{
			points = 4;				
		}
		else if (count == 4)
		{
			points = 6;				
		}
		else if (count == 5)
		{
			points = 10;				
		}

		if (player == ControlState.PLAYER)
		{
			playerScore += points;
		}
		else if (player == ControlState.COMPUTER)
		{
			computerScore += points;
		}		
	}
		
	public String getWhoIsWinner()
	{
		if (playerScore > computerScore)
		{
			return "The Player Wins!";
		}
		else if (computerScore > playerScore)
		{
			return "The Computer Wins!";
		}
		else 
		{
			return "Its a tie!";
		}
	}

}
