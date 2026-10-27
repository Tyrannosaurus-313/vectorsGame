package game;

import game.Grid.Owner;

public class ScoreCalculator {	
	private int playerScore;
	private int computerScore;
	public int getPlayerScore() 	{ return playerScore; }
	public int getComputerScore()	{ return computerScore; }
	
	enum Direction { HORIZONTAL, VERTICAL, DIAGONAL_MAIN, DIAGONAL_ANTI }; //Null is unclaimed

	public static Direction intToDirection(int ordinal) {
		Direction[] list = Direction.values();
		for (int i = 0; i < list.length; i++) {
			if (ordinal == list[i].ordinal())
				return list[i];
		}
		return Direction.HORIZONTAL;
	}

    //In the deltas, it is (row, col) where 1 means incrementing by 1, 0 means no incrementing, and -1 is decrementing in th corresponding array position.
    private static final int[][] DELTAS = {{0, 1}, {1, 0}, {1, 1}, {1, -1}};
    private int[] delta(int ordinal) { return DELTAS[ordinal]; }

	public void updateScores(Grid gameGrid) {
		int rows = gameGrid.getRowCount();
		int cols = gameGrid.getColCount();
		playerScore = 0;
		computerScore = 0;
		Direction[][] claimed = new Direction[rows][cols];

		//Search from chain length 5 down to 3
        for (int len = 5; len >= 3; len--) {
            // Horizontal: r in [0, rows), c in [0, cols-len]
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c <= cols - len; c++) {
                    if (isLineValid(gameGrid, r, c, len, Direction.HORIZONTAL, delta(Direction.HORIZONTAL.ordinal()), claimed)) {
                        markUsed(claimed, r, c, len, Direction.HORIZONTAL, delta(Direction.HORIZONTAL.ordinal()));
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }
            }
            
            for (int r = 0; r <= rows - len; r++) {
                // Vertical: r in [0, rows-len], c in [0, cols)
                for (int c = 0; c < cols; c++) {
                    if (isLineValid(gameGrid, r, c, len, Direction.VERTICAL, delta(Direction.VERTICAL.ordinal()), claimed)) {
                        markUsed(claimed, r, c, len, Direction.VERTICAL, delta(Direction.VERTICAL.ordinal()));
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }
                
                // Anti-diagonal (/): r in [0, rows-len], c in [len-1, cols)
                for (int c = len - 1; c < cols; c++) {
                    if (isLineValid(gameGrid, r, c, len, Direction.DIAGONAL_ANTI, delta(Direction.DIAGONAL_ANTI.ordinal()), claimed)) {
                        markUsed(claimed, r, c, len, Direction.DIAGONAL_ANTI, delta(Direction.DIAGONAL_ANTI.ordinal()));
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }

                // Diagonal main (\): r in [0, rows-len], c in [0, cols-len]
                for (int c = 0; c <= cols - len; c++) {
                    if (isLineValid(gameGrid, r, c, len, Direction.DIAGONAL_MAIN, delta(Direction.DIAGONAL_MAIN.ordinal()), claimed)) {
                        markUsed(claimed, r, c, len, Direction.DIAGONAL_MAIN, delta(Direction.DIAGONAL_MAIN.ordinal()));
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }
            }
        }
	}
	
    private boolean isLineValid(Grid grid, int r, int c, int len, Direction dir, int[] delta, Direction[][] claimed) {
        Owner firstState = grid.getState(r, c);
        
        // A line cannot be formed if the starting cell is empty/null
        if (firstState == Owner.UNOWNED)
             return false;

        for (int i = 0; i < len; i++) {
            int currR = r + i * delta[0];
            int currC = c + i * delta[1];
            
            // Check for diagonals out of bounds
            if (currR < 0 || currR >= grid.getRowCount() || currC < 0 || currC >= grid.getColCount()) 
                return false;
            
            if (claimed[currR][currC] == dir) 
                return false; //Fail if same direction already claimed (nesting)
            if (grid.getState(currR, currC) != firstState)
                 return false; //Fail if not same owner
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
