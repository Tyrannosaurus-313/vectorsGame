package game;

import game.Grid.Owner;

public class ScoreCalculator {	
	private int playerScore;
	private int computerScore;
	public int getPlayerScore() 	{ return playerScore; }
	public int getComputerScore()	{ return computerScore; }
    public int getNetScore() { return playerScore - computerScore; }
	
	public enum Direction { HORIZONTAL, VERTICAL, DIAG_MAIN, DIAG_ANTI };

    private static final int[][] DELTAS = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
    private int[] delta(int ordinal) { return DELTAS[ordinal]; }

    public void updateScores(Grid grid) {
		int rows = grid.getRowCount();
		int cols = grid.getColCount();
		playerScore = 0;
		computerScore = 0;
		Direction[][] claimed = new Direction[rows][cols];

		//Search from chain length 5 down to 3
        int r;
        int c;
        for (int len = 5; len >= 3; len--) {
            // Horizontal
            for (r = 0; r < rows; r++) {
                for (c = 0; c <= cols - len; c++) {
                    processDirection(grid, r, c, len, claimed, Direction.HORIZONTAL);
                }
            }
            
            for (r = 0; r <= rows - len; r++) {
                // Vertical
                for (c = 0; c < cols; c++) {
                    processDirection(grid, r, c, len, claimed, Direction.VERTICAL);
                }
                
                // Diagonal main (\)
                for (c = 0; c <= cols - len; c++) {
                    processDirection(grid, r, c, len, claimed, Direction.DIAG_MAIN);
                }
                
                // Anti-diagonal (/)
                for (c = len - 1; c < cols; c++) {
                    processDirection(grid, r, c, len, claimed, Direction.DIAG_ANTI);
                }
            }
        }
	}
    
	private void processDirection(Grid grid, int r, int c, int len, Direction[][] claimed, Direction dir) {
		if (isLineValid(grid, r, c, len, dir, delta(dir.ordinal()), claimed)) {
			markUsed(claimed, r, c, len, dir, delta(dir.ordinal()));
			computeScore(grid.getState(r, c), len);
	    }
	}
	
    private boolean isLineValid(Grid grid, int r, int c, int len, Direction dir, int[] delta, Direction[][] claimed) {
        Owner owner = grid.getState(r, c);
        
        if (owner == Owner.UNOWNED) // Early exit for Unowned Cells
             return false;

        for (int i = 0; i < len; i++) {
            int currR = r + i * delta[0];
            int currC = c + i * delta[1];
          
            if (!grid.inBounds(currR, currC) || grid.getState(currR, currC) != owner || claimed[currR][currC] == dir) 
                 return false; //Fail if out of bounds or not same owner or direction already claimed (nesting)
        }
        return true;
    }

    private void markUsed(Direction[][] claimed, int r, int c, int len, Direction dir, int[] delta) {
        for (int i = 0; i < len; i++) {
            int currR = r + i * delta[0]; // We multiply by 0 to cancel out the directions we do not need.
            int currC = c + i * delta[1];            
            claimed[currR][currC] = dir;
        }
    }

	private void computeScore(Owner player, int count) {
        int points = 0;
        if (count == 3) points = 4;
        else if (count == 4) points = 6;
        else if (count == 5) points = 10;

        if (player == Owner.PLAYER) {
            playerScore += points;
        } else if (player == Owner.COMPUTER) {
            computerScore += points;
        }
	}
	
	public String getWhoIsWinner() {
		if (playerScore > computerScore) 	  return "The Player Wins!";
		else if (computerScore > playerScore) return "The Computer Wins!";
		return "Its a tie!";
	}
}
