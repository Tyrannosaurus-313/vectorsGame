package game;

import game.Grid.Owner;

public class ScoreCalculator {
	
	private int playerScore;
	private int computerScore;
	public int getPlayerScore() 	{ return playerScore; }
	public int getComputerScore()	{ return computerScore; }
	
	public void updateScores(Grid gameGrid) {
		int cols = gameGrid.lengths.colCount;
		int rows = gameGrid.lengths.rowCount;
		playerScore = 0;
		computerScore = 0;
		boolean[][] used  = new boolean[rows][cols]; //I think rows and cols got swapped, so I am swapping them back

		//Search from chain length 5 to 3
        for (int len = 5; len >= 3; len--) { //TODO: Diagonal needs to be handled as well    
        									 //TODO: OVERLAP Should be allowed, but only when it is not the same direction (i.e. horizontal and vertical could overlap, but not horizontal and horizontal.) 
            							     // Or more simply put, nesting is not allowed, but overlap is.
        	for (int r = 0; r < rows; r++) { // Horizontal
                for (int c = 0; c <= cols - len; c++) {
//                    System.out.println(r + " " + rows + " cols " + c + " " + cols);
                    if (isLineValid(gameGrid, r, c, len, true, used)) {
                        markUsed(used, r, c, len, true);
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }
            }
        	
            for (int r = 0; r <= rows - len; r++) { // Vertical
                for (int c = 0; c < cols; c++) {
//                    System.out.println(r + " " + rows + "cols\t" + c + " " + cols);
                    if (isLineValid(gameGrid, r, c, len, false, used)) {
                        markUsed(used, r, c, len, false);
                        computeScore(gameGrid.getState(r, c), len);
                    }
                }
            }
        }
	}
	
    private boolean isLineValid(Grid grid, int r, int c, int len, boolean horizontal, boolean[][] used) {
        Owner firstState = grid.getState(r, c);
        
        // A line cannot be formed if the starting cell is empty/null
        if (firstState == Owner.UNOWNED) return false;

        for (int i = 0; i < len; i++) {
        	int currR = r;
        	int currC = c + i;
        	if (!horizontal) {
        		currR = r + i;
        		currC = c;
        	}
        	
//			System.out.println(used.length + " " + used[0].length);
//			System.out.println(currR + " " + currC + " i" + i);
            if (used[currR][currC]) return false; //Fail if already used
            if (grid.getState(currR, currC) != firstState) return false; //Fail if not same owner
        }
        return true;
    }

    private void markUsed(boolean[][] used, int r, int c, int len, boolean horizontal) {
        for (int i = 0; i < len; i++) {
            int currR = r;
            int currC = c + i;
        	if (!horizontal) {
        		currR = r + i;
        		currC = c;
        	}
            used[currR][currC] = true;
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
