package game;

import game.ComputerPlayerUnit.CPUState;
import game.Grid.ControlState;
import java.util.Scanner;

public class Vectors {
	private Highscore scores = new Highscore();
	private int numRounds = 20;
	private Grid gameGrid = new Grid();
	private CPUState chosenDiff = CPUState.RANDOM;
	
	public void setNumTurns(int numRounds) {
		if (numRounds > 0 && numRounds < 32) //Max rounds for a 64 cell board must be 32 as 2 cells will be set per round 
			this.numRounds = numRounds;
	}
	
	public void run() {		
		Scanner scnr = new Scanner(System.in);
		System.out.printf("Please enter Num Turns (0 to skip): ");
		setNumTurns(scnr.nextInt());
		System.out.printf("Please enter Difficulty, (0 = Random, 1 = Easy, 2 = Medium, 3 = Hard, 4 = Aggressive): ");
		int select = scnr.nextInt();		
		System.out.printf("Do you want the Columns to use Letters? (y or n) ");
		String in = scnr.next().toLowerCase().trim();
		if (in.equalsIgnoreCase("yes") || in.equalsIgnoreCase("y") || in.equalsIgnoreCase("true"))
			gameGrid.abcCol = true;

		scnr.nextLine(); //Clear Scanner For Round Input

		if (select == 0)	  chosenDiff = CPUState.RANDOM;
		else if (select == 1) chosenDiff = CPUState.EASY;
		else if (select == 2) chosenDiff = CPUState.MEDIUM;
		else if (select == 3) chosenDiff = CPUState.HARD;
		else if (select == 4) chosenDiff = CPUState.AGGRESSIVE;

		ComputerPlayerUnit cpu = new ComputerPlayerUnit(chosenDiff, gameGrid);
		ScoreCalculator scoreCalc = new ScoreCalculator();
		
		for (int i = 0; i < numRounds; i++) {
			round(i, cpu, scoreCalc, scnr);
		}
		System.out.printf("\n\nGame Over %s\n", scoreCalc.getWhoIsWinner());
		printRound(numRounds, scoreCalc);

		if (scoreCalc.getPlayerScore() > scores.score.getScore())
		{
			System.out.printf("\n\nNew Record!\nPlease Enter Your Name: ");
			scores.saveScore(new Score(scoreCalc.getPlayerScore(), scnr.next()));
			scores.saveFile();
		}
		scnr.close();
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
			String input = scnr.nextLine().trim();

			try {
				Coordinate moveCoord = parseMove(input);
				turnOver = gameGrid.setCell(moveCoord.x, moveCoord.y, ControlState.PLAYER);
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter numbers only.");
			}
		}
		
		Coordinate cpuCoord = cpu.getTurn();
		gameGrid.setCell(cpuCoord.x, cpuCoord.y, ControlState.COMPUTER);
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

	private static Coordinate parseMove(String input) {
		input = input.trim().toUpperCase();

		if (input.matches("[A-Z0-9]{2}")) { //Sees if input is A-Z or single digit char in a 2 long string
			return new Coordinate(
				parseCoordinate(input.charAt(0) + ""),
				parseCoordinate(input.charAt(1) + "")
			);
		}

		String[] parts = input.split("[,.\\s]+"); //'\\s' and ',' handles potential whitespace 

		if (parts.length != 2) {
			System.out.println("Invalid coordinate");
			return new Coordinate(-1, -1); //Returns invalid coord
		}

		return new Coordinate(
			parseCoordinate(parts[0]),
			parseCoordinate(parts[1])
		);
	}

	private static int parseCoordinate(String input) {
		input = input.trim().toUpperCase();

		if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
			return input.charAt(0) - 'A';
		}

		return Integer.parseInt(input);
	}
}
