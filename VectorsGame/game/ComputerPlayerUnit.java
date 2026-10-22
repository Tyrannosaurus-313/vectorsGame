package game;

import game.Grid.Owner;
import java.util.Random;

public class ComputerPlayerUnit {
	public enum CPUState { RANDOM, EASY, MEDIUM, AGGRESSIVE, HARD }
	
	public CPUState difficulty;
	public Random randy = new Random();
	public Grid gridRef;
	
	public ComputerPlayerUnit(Grid gridRef) {
		this.gridRef = gridRef;
	}
	public ComputerPlayerUnit(CPUState difficulty, Grid gridRef) {
		this.difficulty = difficulty;
		this.gridRef = gridRef;
	}	
		
	Coordinate pastCoord;
	public Coordinate getTurn() {
		if (difficulty == CPUState.AGGRESSIVE)	{ return calculateSmartMove(10.0, 0.45); }
		else if (difficulty == CPUState.HARD)	{ return calculateSmartMove(3.0, 0.45); }
		else if (difficulty == CPUState.MEDIUM) { return calculateSmartMove(0.4, 0.49); }
		else if (difficulty == CPUState.EASY)	{ return getEasyMove(); }
		return getRandomMove();
	}

	private Coordinate calculateSmartMove(double blockingWeight, double expansionWeight) {
        int[][] scores = new int[gridRef.lengths.rowCount][gridRef.lengths.colCount];
        ScoreCalculator cloneCalc = new ScoreCalculator();
        Grid cloneGrid = createGridClone();
        
        cloneCalc.updateScores(cloneGrid);
        int origComputerScore = cloneCalc.getComputerScore();

        for (int y = 0; y < scores.length; y++) {
            for (int x = 0; x < scores[0].length; x++) {
                if (!gridRef.isCellValid(x,  y,  Owner.COMPUTER)) {//gridRef.getState(x, y) != null && !(gridRef.canOverride)) {
                    scores[y][x] = -999;
                    continue;
                }

                cloneGrid.setCell(x, y, Owner.COMPUTER);
                cloneCalc.updateScores(cloneGrid);
                int computerGain = cloneCalc.getComputerScore() - origComputerScore;
                cloneGrid.resetCell(x, y);

                int playerPrevScore = cloneCalc.getPlayerScore();
                cloneGrid.setCell(x, y, Owner.PLAYER);
                cloneCalc.updateScores(cloneGrid);
                int playerPotentialGain = cloneCalc.getPlayerScore() - playerPrevScore;
                double blockingValue = playerPotentialGain * blockingWeight;

                int expansionBonus = getExpansionBonus(x, y);
				
				double wallPenalty = 0;
				if (isAtWall(x, y)) {
					wallPenalty = 2.0; 
				}

                double finalScore = computerGain + blockingValue + (expansionBonus * expansionWeight) - wallPenalty;
                scores[y][x] = (int) (finalScore + 0.5); // Rounding

                cloneGrid.resetCell(x, y);
            }
        }
        return findBestCoordinate(scores);
    }

    private Coordinate getEasyMove() {
        if (pastCoord == null) return getRandomMove();

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        for (int i = 0; i < 4; i++) {
            Coordinate hCoord = new Coordinate(pastCoord.x + dx[i], pastCoord.y);
            if (isWithinBounds(hCoord) && gridRef.getState(hCoord.x, hCoord.y) == Owner.UNOWNED) {
                return updatePastCoord(hCoord);
            }
            Coordinate vCoord = new Coordinate(pastCoord.x, pastCoord.y + dy[i]);
            if (isWithinBounds(vCoord) && gridRef.getState(vCoord.x, vCoord.y) == Owner.UNOWNED) {
                return updatePastCoord(vCoord);
            }
        }
        return getRandomMove();
    }

    private Coordinate getRandomMove() {
        Coordinate coord;
        do {
            coord = new Coordinate(randy.nextInt(gridRef.lengths.rowCount), randy.nextInt(gridRef.lengths.colCount));
        } while (gridRef.getState(coord.x, coord.y) != Owner.UNOWNED);
        
        pastCoord = coord;
        return coord;
    }

	
    private Grid createGridClone() {
        Grid clone = new Grid(gridRef.lengths.colCount, gridRef.lengths.rowCount);
        for (int y = 0; y < gridRef.lengths.rowCount; y++) {
            for (int x = 0; x < gridRef.lengths.colCount; x++) {
                clone.setCell(x, y, gridRef.getState(x, y));
            }
        }
        return clone;
    }

    private int getExpansionBonus(int x, int y) {
        int bonus = 0;
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            if (isWithinBounds(nx, ny) && gridRef.getState(nx, ny) == Owner.UNOWNED) {
                bonus++;
            }
        }
        return bonus;
    }

    private Coordinate findBestCoordinate(int[][] scores) {
        int currMax = -999;
        Coordinate bestCoord = null;
        for (int y = 0; y < gridRef.lengths.rowCount; y++) {
            for (int x = 0; x < gridRef.lengths.colCount; x++) {
                if (scores[y][x] > currMax) {
                    currMax = scores[y][x];
                    bestCoord = new Coordinate(x, y);
                }
            }
        }
        return bestCoord;
    }
	
    private boolean isWithinBounds(Coordinate c) {
        return isWithinBounds(c.x, c.y);
    }
    private boolean isWithinBounds(int x, int y) {
        return x >= 0 && x < 8 && y >= 0 && y < 8;
    }

    private boolean isAtWall(int x, int y) {
        return x == 0 || x == (8 - 1) || y == 0 || y == (8 - 1);
    }

    private Coordinate updatePastCoord(Coordinate c) {
        this.pastCoord = c;
        return c;
    }
}
