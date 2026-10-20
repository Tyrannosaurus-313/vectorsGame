package game;

import game.Grid.ControlState;

public class ScoreCalculator {
	
	private int playerScore;
	private int computerScore;
	public int getPlayerScore() 	{ return playerScore; }
	public int getComputerScore()	{ return computerScore; }
	
	public void updateScores(Grid gameGrid)
	{
		int cols = gameGrid.lengths.colCount;
		int rows = gameGrid.lengths.rowCount;
		playerScore = 0;
		computerScore = 0;
		boolean[][] used  = new boolean[cols][rows];

		
        for (int len = 5; len >= 3; len--) {
            
            // Horizontal
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c <= cols - len; c++) {
                    if (isLineValid(gameGrid, r, c, len, true, used)) {
                        markUsed(used, r, c, len, true);
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }
            }

            // Vertical
            for (int r = 0; r <= rows - len; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isLineValid(gameGrid, r, c, len, false, used)) {
                        markUsed(used, r, c, len, false);
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }
            }
        }
	}
	
    private boolean isLineValid(Grid grid, int r, int c, int len, boolean horizontal, boolean[][] used) {
        ControlState firstState = grid.getState(r, c);
        
        // A line cannot be formed if the starting cell is empty/null
        if (firstState == null) return false;

        for (int i = 0; i < len; i++) {
            int currR = horizontal ? r : r + i;
            int currC = horizontal ? c + i : c;

            // Check if cell is already part of another chain
            if (used[currR][currC]) return false;

            // Check if the state matches the first cell in the line
            if (grid.getState(currR, currC) != firstState) return false;
        }
        return true;
    }

    private void markUsed(boolean[][] used, int r, int c, int len, boolean horizontal) {
        for (int i = 0; i < len; i++) {
            int currR = horizontal ? r : r + i;
            int currC = horizontal ? c + i : c;
            used[currR][currC] = true;
        }
    }

	private void computeScore(ControlState player, int count) {
        int points = 0;
        if (count == 3) points = 4;
        else if (count == 4) points = 6;
        else if (count == 5) points = 10;

        if (player == ControlState.PLAYER) {
            playerScore += points;
        } else if (player == ControlState.COMPUTER) {
            computerScore += points;
        }
	}
		
	public String getWhoIsWinner() {
		if (playerScore > computerScore) 	  return "The Player Wins!";
		else if (computerScore > playerScore) return "The Computer Wins!";
		return "Its a tie!";
	}
}
