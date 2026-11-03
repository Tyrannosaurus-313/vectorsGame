package game;

public class GameData { //data useful for the Computer Player
	public int currentTurn;
	public int totalTurns;
	
	public ScoreCalculator scoreCalc = new ScoreCalculator();	
	
	public Grid grid;
	
	public Coordinate prevCompCoord;
	
	public int prevPScore = 0;
	public int prevCScore = 0;
	
	public GameData(int totalTurns, Grid grid) {
		this.totalTurns = totalTurns;
		this.grid = grid;
	}
	
	public void updateScore() {
		scoreCalc.updateScores(grid);
		prevPScore = scoreCalc.getPlayerScore();
		prevCScore = scoreCalc.getComputerScore();
	}
}
