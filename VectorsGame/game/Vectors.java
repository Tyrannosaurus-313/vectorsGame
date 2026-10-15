package game;

import java.util.Scanner;

import game.Grid.ControlState;

public class Vectors {
	private Highscore scores = new Highscore();
	private int numRounds = 20;
	private Grid gameGrid = new Grid();
	
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
	
	public void round(int roundNum, ComputerPlayerUnit cpu, ScoreCalculator scoreCalc, Scanner scnr)
	{
		printRound(roundNum, scoreCalc);
		boolean gridIsSet = false;
		while (!gridIsSet)
		{
			System.out.printf("Player(X) Turn (Row Col): ");
			int x = scnr.nextInt();
			int y = scnr.nextInt();
			gridIsSet = gameGrid.setCell(x, y, ControlState.Player);
		}
		
		int[] cpuTurnCoords = cpu.getTurn();
		gridIsSet = gameGrid.setCell(cpuTurnCoords[0], cpuTurnCoords[1], ControlState.Computer);
		scoreCalc.updateScores(gameGrid);
	}
	
	public int prevPScore = 0;
	public int prevCScore = 0;
	public void printRound(int roundNum, ScoreCalculator scoreCalc)
	{		
		System.out.println();
		System.out.printf("%s- V E C T O R S                      *\n%s", bar(), bar());
		scores.show();
		System.out.printf("%s- Player Score:\t %4d Last Round%4d *\n", bar(), scoreCalc.getPlayerScore(), prevPScore);
		System.out.printf("- Computer Score:%4d Last Round%4d *\n%s", scoreCalc.getComputerScore(), prevCScore, bar());
		System.out.printf("- Turn %2d  of %2d\t\t     *\n%s", roundNum, numRounds, bar());

		prevPScore = scoreCalc.getPlayerScore();
		prevCScore = scoreCalc.getComputerScore();
		
		gameGrid.printGrid();
	}
	
	public String bar()
	{
		return "**************************************\n";
	}
}
