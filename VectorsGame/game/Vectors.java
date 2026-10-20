package game;

import java.util.Scanner;

import game.ComputerPlayerUnit.CPUState;
import game.Grid.ControlState;

public class Vectors {
	private Highscore scores = new Highscore();
	private Grid gameGrid = new Grid();
<<<<<<< HEAD
	private CPUState chosenDiff = CPUState.RANDOM;
=======
	private int numRounds = 20;
>>>>>>> branch 'main' of git@github.com:Tyrannosaurus-313/vectorsGame.git
	
	public void setNumTurns(int numRounds) {
		if (numRounds > 0 && numRounds < 64) //Max rounds for a 64 cell board must be 64
			this.numRounds = numRounds;
		else
			this.numRounds = 20;
	}
	
	public void run() {		
		Scanner scnr = new Scanner(System.in);
		System.out.printf("Please enter Num Turns (0 to skip): ");
		setNumTurns(scnr.nextInt());
		System.out.printf("Please enter Difficulty, (0 = Random, 1 = Easy, 2 = MEDIUM): ");
		int select = scnr.nextInt();
		if (select == 0)
		{
			chosenDiff = CPUState.RANDOM;
		}
		else if (select == 1)
		{
			chosenDiff = CPUState.EASY;
		}
		else if (select == 2)
		{
			chosenDiff = CPUState.MEDIUM;
		}
		ComputerPlayerUnit cpu = new ComputerPlayerUnit(chosenDiff, gameGrid);
		ScoreCalculator scoreCalc = new ScoreCalculator();
		
		for (int i = 0; i < numRounds; i++) {
			round(i, cpu, scoreCalc, scnr);
		}
		System.out.printf("\n\nGame Over %s\n", scoreCalc.getWhoIsWinner());
<<<<<<< HEAD
		printRound(numRounds, scoreCalc);

=======
		gameGrid.printGrid();
		scnr.close();
>>>>>>> branch 'main' of git@github.com:Tyrannosaurus-313/vectorsGame.git
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
		
<<<<<<< HEAD
		Coordinate cpuCoord = cpu.getTurn();
		gameGrid.setCell(cpuCoord.x, cpuCoord.y, ControlState.COMPUTER);
=======
		int[] cpuTurnCoords = cpu.getTurn();
		gameGrid.setCell(cpuTurnCoords[0], cpuTurnCoords[1], ControlState.COMPUTER);
>>>>>>> branch 'main' of git@github.com:Tyrannosaurus-313/vectorsGame.git
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
