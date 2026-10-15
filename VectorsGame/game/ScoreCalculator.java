package game;

import game.Grid.ControlState;

public class ScoreCalculator {
	
	private int playerScore;
	private int computerScore;
	//Strategy
	// Search for the largest chain of control states to smallest, compiling the total point counts
	
	public int getPlayerScore() { return playerScore; }
	public int getComputerScore() { return computerScore; }
	
	public void updateScores(Grid gameGrid)
	{
		playerScore = 0;
		computerScore = 0;
		int[] lengths = gameGrid.getGridLengths();
		boolean[][] checkedCells  = new boolean[lengths[0]][lengths[1]];
		for (int i = 0; i < lengths[0]; i++)
		{
			for (int j = 0; j < lengths[1]; j++)
			{
				if (checkedCells[j][i])
				{
					continue;
				}
				
				ControlState owner = gameGrid.getState(j, i);
				if (owner != null)
				{
					int xCount = 1;
					for (int sRow = 1; sRow <= 5; sRow++)
					{			
						System.out.println(i + sRow + " " + lengths[0]);
						if (i + sRow >= lengths[0])
						{
							break;
						}
						if (owner != gameGrid.getState(j, i + sRow) || checkedCells[j][i + sRow])
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
						System.out.println(i + sCol + " " + lengths[1]);
						if (j + sCol >= lengths[1])
						{
							break;
						}
						if (owner != gameGrid.getState(j + sCol, i) || checkedCells[j + sCol][i])
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
							checkedCells[j][i + c] = true;
						}
						computeScore(owner, xCount);
					}
					else
					{
						for (int c = 0; c < yCount; c++)
						{
							checkedCells[j + c][i] = true;
						}
						computeScore(owner, yCount);
					}
				}
			}
		}
	}
	
	public void computeScore(ControlState player, int count)
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
