package game;

import game.Grid.Owner;
import java.util.Random;

public class ComputerPlayer {
	public enum Difficulty { RANDOM, AGGRESSIVE, EASY, MEDIUM, HARD; } 
	
	// public static String getList() {
	// 	String str = "";
	// 	Difficulty[] list = Difficulty.values();
	// 	for (int i = 0; i < list.length; i++) {
	// 		str += list[i].ordinal() + " = " + list[i].name();
	// 		if (i != list.length - 1)
	// 			str += ", ";
	//     }
	// 	return str;
	// }
		
	// public static Difficulty intToState(int ordinal) {
	// 	Difficulty[] list = Difficulty.values();
	// 	for (int i = 0; i < list.length; i++) {
	// 		if (ordinal == list[i].ordinal())
	// 			return list[i];
	// 	}
	// 	return Difficulty.RANDOM;
	// }	

	public Coordinate getTurn(GameData data, Difficulty difficulty) {
        if (difficulty == Difficulty.AGGRESSIVE)	{
            return calculateSmartMove(data.grid, 10.0, 0.45); 
        }
		else if (difficulty == Difficulty.HARD)	{ 
            return calculateSmartMove(data.grid, 0.4, 0.49); 
        }
		else if (difficulty == Difficulty.MEDIUM) { 
            return calculateSmartMove(data.grid, 3.0, 0.45); 
        }
		else if (difficulty == Difficulty.EASY)	{ 
            return getEasyMove(data.grid, data.prevCompCoord); 
        }
		return getRandomMove(data.grid, new Random());
	}

	private Coordinate calculateSmartMove(Grid grid, double aggroWeight, double expansionWeight) {
        int[][] scores = new int[grid.getRowCount()][grid.getColCount()];
        ScoreCalculator cloneCalc = new ScoreCalculator();
        Grid cloneGrid = createGridClone(grid);
        
        cloneCalc.updateScores(cloneGrid);
        int origComputerScore = cloneCalc.getComputerScore();

        for (int y = 0; y < scores.length; y++) {
            for (int x = 0; x < scores[0].length; x++) {
                if (!grid.isCellValid(y, x, Owner.COMPUTER)) {
                    scores[y][x] = -999;
                    continue;
                }

                cloneGrid.setCell(y, x, Owner.COMPUTER);
                cloneCalc.updateScores(cloneGrid);
                int computerGain = cloneCalc.getComputerScore() - origComputerScore;
                cloneGrid.resetCell(y, x);

                int playerPrevScore = cloneCalc.getPlayerScore();
                cloneGrid.setCell(y, x, Owner.PLAYER);
                cloneCalc.updateScores(cloneGrid);
                int playerPotentialGain = cloneCalc.getPlayerScore() - playerPrevScore;
                double blockingValue = playerPotentialGain * aggroWeight;

                int expansionBonus = getExpansionBonus(grid, y, x);
				
				double wallPenalty = 0;
				if (isAtWall(grid, y, x)) {
					wallPenalty = 1.9; 
				}

                double finalScore = computerGain + blockingValue + (expansionBonus * expansionWeight) - wallPenalty;
                scores[y][x] = (int)(finalScore + 0.5); // Rounding

                cloneGrid.resetCell(y, x);
            }
        }
        return findBestCoordinate(grid, scores);
    }

    private Coordinate getEasyMove(Grid grid, Coordinate prevCoord) {
        if (prevCoord.x == -1) return getRandomMove(grid, new Random());

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            Coordinate hCoord = new Coordinate(prevCoord.y, prevCoord.x + dx[i]);
            if (isWithinBounds(grid, hCoord) && grid.getState(hCoord.y, hCoord.x) == Owner.UNOWNED) {
                return hCoord;
            }
            Coordinate vCoord = new Coordinate(prevCoord.y + dy[i], prevCoord.x);
            if (isWithinBounds(grid, vCoord) && grid.getState(vCoord.y, vCoord.x) == Owner.UNOWNED) {
                return vCoord;
            }
        }
        return getRandomMove(grid, new Random());
    }

    private Coordinate getRandomMove(Grid grid, Random rand) {
        Coordinate coord;
        do {
            coord = new Coordinate(rand.nextInt(grid.getRowCount()), rand.nextInt(grid.getColCount()));
        } while (grid.getState(coord.y, coord.x) != Owner.UNOWNED);
        
        return coord;
    }

	
    private Grid createGridClone(Grid grid) {
        Grid clone = new Grid(grid.getRowCount(), grid.getColCount(), grid.getCanOverride());
        for (int y = 0; y < grid.getRowCount(); y++) {
            for (int x = 0; x < grid.getColCount(); x++) {
                clone.setCell(y, x, grid.getState(y, x));
            }
        }
        return clone;
    }

    private int getExpansionBonus(Grid grid, int y, int x) {
        int bonus = 0;
        int[] dy = {1, -1, 0, 0};
        int[] dx = {0, 0, 1, -1};
        for (int i = 0; i < 4; i++) {
            int ny = y + dy[i];
            int nx = x + dx[i];
            if (isWithinBounds(grid, ny, nx) && grid.getState(ny, nx) == Owner.UNOWNED) {
                bonus++;
            }
        }
        return bonus;
    }

    private Coordinate findBestCoordinate(Grid grid, int[][] scores) {
        int currMax = -1000;
        Coordinate bestCoord = null;
        for (int y = 0; y < grid.getRowCount(); y++) {
            for (int x = 0; x < grid.getColCount(); x++) {
                if (scores[y][x] > currMax) {
                    currMax = scores[y][x];
                    bestCoord = new Coordinate(x, y);
                }
            }
        }
        return bestCoord;
    }
	
    private boolean isWithinBounds(Grid grid, Coordinate c) { return isWithinBounds(grid, c.y, c.x); }
    
    private boolean isWithinBounds(Grid grid, int y, int x) {
        return y >= 0 && y < grid.getRowCount()
        		&& x >= 0 && x < grid.getColCount();
    }

    private boolean isAtWall(Grid grid, int y, int x) {
        return y == 0 || y == (grid.getRowCount() - 1) 
        		|| x == 0 || x == (grid.getColCount() - 1);
    }
}
