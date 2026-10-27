package game;

import game.Grid.Owner;
import java.util.Random;

// TODO: More Java-style comment prefixes for methods throughout, especially in this class

public class ComputerPlayerUnit {
	public enum CPUState { RANDOM, AGGRESSIVE, EASY, MEDIUM, HARD; } 
	
	public static String getList() {
		String str = "";
		CPUState[] list = CPUState.values();
		for (int i = 0; i < list.length; i++) {
			str += list[i].ordinal() + " = " + list[i].name();
			if (i != list.length - 1)
				str += ", ";
	    }
		return str;
	}
		
	public static CPUState intToState(int ordinal) {
		CPUState[] list = CPUState.values();
		for (int i = 0; i < list.length; i++) {
			if (ordinal == list[i].ordinal())
				return list[i];
		}
		return CPUState.RANDOM;
	}

//	public Coordinate getTurn(Coordinate prevCoord, CPUState difficulty, Grid grid) {
//        // TODO: Adjust weights as after the ScoreCalculator patch adding in overlapping, blocking is much less effective
//		if (difficulty == CPUState.AGGRESSIVE)	{
//            return calculateSmartMove(grid, 10.0, 0.45); 
//        }
//		else if (difficulty == CPUState.HARD)	{ 
//            return calculateSmartMove(grid, 3.0, 0.45); 
//        }
//		else if (difficulty == CPUState.MEDIUM) { 
//            return calculateSmartMove(grid, 0.4, 0.49); 
//        }
//		else if (difficulty == CPUState.EASY)	{ 
//            return getEasyMove(grid, prevCoord); 
//        }
//		return getRandomMove(grid);
//	}
	

	public Coordinate getTurn(GameData data, CPUState difficulty) {
        // TODO: Adjust weights as after the ScoreCalculator patch adding in overlapping, blocking is much less effective
		if (difficulty == CPUState.AGGRESSIVE)	{
            return calculateSmartMove(data.grid, 10.0, 0.45); 
        }
		else if (difficulty == CPUState.HARD)	{ 
            return calculateSmartMove(data.grid, 3.0, 0.45); 
        }
		else if (difficulty == CPUState.MEDIUM) { 
            return calculateSmartMove(data.grid, 0.4, 0.49); 
        }
		else if (difficulty == CPUState.EASY)	{ 
            return getEasyMove(data.grid, data.prevCompCoord); 
        }
		return getRandomMove(data.grid, new Random());
	}

	private Coordinate calculateSmartMove(Grid grid, double blockingWeight, double expansionWeight) {
        int[][] scores = new int[grid.getRowCount()][grid.getColCount()];
        ScoreCalculator cloneCalc = new ScoreCalculator();
        Grid cloneGrid = createGridClone(grid);
        
        cloneCalc.updateScores(cloneGrid);
        int origComputerScore = cloneCalc.getComputerScore();

        for (int y = 0; y < scores.length; y++) {
            for (int x = 0; x < scores[0].length; x++) {
                if (!grid.isCellValid(y,  x,  Owner.COMPUTER)) {
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
                double blockingValue = playerPotentialGain * blockingWeight;

                int expansionBonus = getExpansionBonus(grid, y, x);
				
				double wallPenalty = 0;
				if (isAtWall(grid, y, x)) {
					wallPenalty = 2.0; 
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
        
        //prevCoord = coord;
        return coord;
    }

	
    private Grid createGridClone(Grid grid) {
        Grid clone = new Grid(grid.getRowCount(), grid.getColCount(), false);
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
        int currMax = -999;
        Coordinate bestCoord = null;
        for (int y = 0; y < grid.getRowCount(); y++) {
            for (int x = 0; x < grid.getColCount(); x++) {
                if (scores[y][x] > currMax) {
                    currMax = scores[y][x];
                    bestCoord = new Coordinate(x, y); // Coordinate uses x, y (perhaps should swapped)?
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
