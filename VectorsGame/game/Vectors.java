package game;

import java.util.Scanner;

import game.Grid.ControlState;

public class Vectors {
	private Highscore scores = new Highscore();
	private Grid gameGrid = new Grid();
	private int numRounds = 20;
	
	public void setNumTurns(int numRounds) {
		if (numRounds > 0 && numRounds < 100)
			this.numRounds = numRounds;
		else
			this.numRounds = 20;
	}
	
	public void run() {		
		Scanner scnr = new Scanner(System.in);
		System.out.printf("Please enter Num Turns (0 to skip): ");
		ComputerPlayerUnit cpu = new ComputerPlayerUnit(gameGrid);
		ScoreCalculator scoreCalc = new ScoreCalculator();
		
		setNumTurns(scnr.nextInt());
		for (int i = 0; i < numRounds; i++) {
			round(i, cpu, scoreCalc, scnr);
		}
		System.out.printf("\n\nGame Over %s\n", scoreCalc.getWhoIsWinner());
		gameGrid.printGrid();

	}
	
	public int prevPScore = 0;
	public int prevCScore = 0;
	public void round(int roundNum, ComputerPlayerUnit cpu, ScoreCalculator scoreCalc, Scanner scnr)
	{
		printRound(roundNum, scoreCalc);
		prevPScore = scoreCalc.getPlayerScore();
		prevCScore = scoreCalc.getComputerScore();

		boolean turnOver = false;
		while (!turnOver)
		{
			System.out.printf("Player(X) Turn (Row Col): ");
			int x = scnr.nextInt();
			int y = scnr.nextInt();
			turnOver = gameGrid.setCell(x, y, ControlState.PLAYER);
		}
		
		int[] cpuTurnCoords = cpu.getTurn();
		gameGrid.setCell(cpuTurnCoords[0], cpuTurnCoords[1], ControlState.COMPUTER);
		scoreCalc.updateScores(gameGrid);
	}
	
	public void printRound(int roundNum, ScoreCalculator scoreCalc) {		
		final String bar = "**************************************\n";
		System.out.printf("\n%s- V E C T O R S                      *\n%s", bar, bar);
		scores.show();
		System.out.printf("%s- Player Score:\t %4d Last Round%4d *\n", bar, scoreCalc.getPlayerScore(), prevPScore);
		System.out.printf("- Computer Score:%4d Last Round%4d *\n%s", scoreCalc.getComputerScore(), prevCScore, bar);
		System.out.printf("- Turn %2d  of %2d\t\t     *\n%s", roundNum, numRounds, bar);
		gameGrid.printGrid();
	}
}
