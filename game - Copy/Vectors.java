package game;

import game.ComputerPlayerUnit.CPUState;
import game.Grid.Owner;
import game.Styles.Style;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Vectors {
	private Highscore scores = new Highscore();
	private int numRounds = 20;
	private Grid gameGrid;
	
	public void setNumTurns(int numRounds) { //rounds > 0 and rounds < cellCount / 2
		if (numRounds > 0 && numRounds < gameGrid.lengths.rowCount * gameGrid.lengths.colCount * 0.5)
			this.numRounds = numRounds;
	}
	
	public void run() {
//		scores.forceReset();
		Scanner scnr = new Scanner(System.in);

		System.out.printf("Enter Grid Size Between y (1-32), x (1-32): ");
		try {
			Coordinate maxCoord = parseMove(scnr.nextLine());
			if (maxCoord.x < 1 || maxCoord.y < 1)
				throw new NumberFormatException();
			gameGrid = new Grid(maxCoord.y, maxCoord.x);
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid input. Default 8 was used");
			gameGrid = new Grid(8, 8);
		}

		System.out.printf("Please enter Num Turns (1-%d): ", (int)(gameGrid.lengths.rowCount * gameGrid.lengths.colCount * 0.5));
		try { 
			setNumTurns(scnr.nextInt());
		}
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to 20"); }

		System.out.printf("Please enter Difficulty, (0 = Random, 1 = Aggressive, 2 = Easy, 3 = Medium, 4 = Hard): ");
		int select = 0;
		try { select = scnr.nextInt(); }
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Random"); }
		CPUState chosenDiff = CPUState.RANDOM;
		if (select == 1) 	  chosenDiff = CPUState.AGGRESSIVE;
		else if (select == 2) chosenDiff = CPUState.EASY;
		else if (select == 3) chosenDiff = CPUState.MEDIUM;
		else if (select == 4) chosenDiff = CPUState.HARD;

		System.out.printf("Rows use Letters? (y or n): ");
		String in = scnr.next().trim();
		gameGrid.abcRow = in.equalsIgnoreCase("yes") || in.equalsIgnoreCase("y") || in.equalsIgnoreCase("true");
		System.out.printf("Columns use Letters? (y or n): ");
		in = scnr.next().trim();
		gameGrid.abcCol = in.equalsIgnoreCase("yes") || in.equalsIgnoreCase("y") || in.equalsIgnoreCase("true");
		System.out.printf("Do you want to be able to overwrite owned cells? (y or n): ");
		in = scnr.next().trim();
		gameGrid.canOverride = in.equalsIgnoreCase("yes") || in.equalsIgnoreCase("y") || in.equalsIgnoreCase("true");

		System.out.printf("Style (0 = Default, 1 = Clean, 2 = Bold): ");		
		scnr.nextLine(); //use nextLine for Round Input
		try { select = scnr.nextInt(); }
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Default"); }
		if (select == 1) 	  { Styles.setAllStyles(Style.Clean); }
		else if (select == 2) { Styles.setAllStyles(Style.Bold); }
		else 				  { Styles.setAllStyles(Style.Default); }
		scnr.nextLine(); //use nextLine for Round Input


		ComputerPlayerUnit cpu = new ComputerPlayerUnit(chosenDiff, gameGrid);
		ScoreCalculator scoreCalc = new ScoreCalculator();
		
		for (int i = 0; i < numRounds; i++) {
			round(i, cpu, scoreCalc, scnr);
		}
		printRound(numRounds, scoreCalc);
		System.out.printf("\n\nGame Over %s\n", scoreCalc.getWhoIsWinner());

		if (scoreCalc.getPlayerScore() > scores.score.getScore()) {
			System.out.printf("\n\nNew Record!\nPlease Enter Your Name: ");
			scores.saveScore(new Score(scoreCalc.getPlayerScore(), scnr.nextLine()));
			scores.saveFile();
		}
		scnr.close();
	}
	
	public int prevPScore = 0;
	public int prevCScore = 0;
	public void round(int roundNum, ComputerPlayerUnit cpu, ScoreCalculator scoreCalc, Scanner scnr) {
		printRound(roundNum, scoreCalc);
		prevPScore = scoreCalc.getPlayerScore();
		prevCScore = scoreCalc.getComputerScore();

		boolean turnOver = false;
		while (!turnOver) {
			System.out.printf("Player(X) Turn (Row Col): ");
			String input = scnr.nextLine().trim();

			try {
				Coordinate moveCoord = parseMove(input);
				turnOver = gameGrid.setCell(moveCoord.x, moveCoord.y, Owner.PLAYER);
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter numbers only.");
			}
		}
		
		Coordinate cpuCoord = cpu.getTurn();
		gameGrid.setCell(cpuCoord.x, cpuCoord.y, Owner.COMPUTER);
		scoreCalc.updateScores(gameGrid);
	}
	
	public void printRound(int roundNum, ScoreCalculator scoreCalc) {
		System.out.printf("\n%s%s V E C T O R S                      %s\n%s", Styles.header, Styles.dash, Styles.vertbar, Styles.lnbreak);
		System.out.printf("%s High Score: %s – %-5d\t     %s\n", Styles.dash, scores.score.getName(), scores.score.getScore(), Styles.vertbar);
		System.out.printf("%s%s Player Score:\t %4d Last Round%4d %s\n", Styles.lnbreak, Styles.dash, scoreCalc.getPlayerScore(), prevPScore, Styles.vertbar);
		System.out.printf("%s Computer Score:%4d Last Round%4d %s\n%s", Styles.dash, scoreCalc.getComputerScore(), prevCScore, Styles.vertbar, Styles.lnbreak);
		System.out.printf("%s Turn %2d of %2d\t\t\t     %s\n%s", Styles.dash, roundNum, numRounds, Styles.vertbar, Styles.foot);
		gameGrid.printGrid();
	}
	
	
	//	Input Parsing

	private static Coordinate parseMove(String input) {
		input = input.trim().toUpperCase();

		if (input.matches("[A-`0-9]{2}")) { //Sees if input is A-~ or single digit char in a 2 long string
			return new Coordinate(
				parseCoord(input.charAt(1) + ""),
				parseCoord(input.charAt(0) + "")
			);
		}

		String[] parts = input.split("[,.\\s]+"); //'\\s' and ',.' handles potential whitespace 

		if (parts.length != 2) {
			System.out.println("Invalid coordinate");
			return new Coordinate(-1, -1); //Returns invalid coord
		}

		return new Coordinate(parseCoord(parts[1]), parseCoord(parts[0])
		);
	}

	private static int parseCoord(String input) {
		input = input.trim().toUpperCase();

		if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
			return input.charAt(0) - 'A';
		}
		return Integer.parseInt(input);
	}
}
