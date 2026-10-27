package game;

import game.ComputerPlayerUnit.CPUState;
import game.Grid.Owner;
import game.Styles.Theme;
import java.util.InputMismatchException;
import java.util.Scanner;

// TODO: Think about separation of concerns and how you might separate the display / printing
// logic from the coordination and calculation logic

public class Vectors {	
	public void run() {
//		scores.forceReset(); //For force resetting the high score
		Scanner scnr = new Scanner(System.in);

		Coordinate maxCoord = new Coordinate(-1, -1);
		System.out.printf("Enter Grid Size Between y (1-26), x (1-26): ");
		maxCoord = parseMove(scnr.nextLine());
		if (maxCoord.x == -1) System.out.println("Invalid input. Default 8 was used");

		int select = 0;
		System.out.printf("Please enter Difficulty, (%s): ", ComputerPlayerUnit.getList());
		try { select = scnr.nextInt(); }
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Random"); }
		CPUState chosenDiff = ComputerPlayerUnit.intToState(select);

		System.out.printf("Rows use Letters? (y or n): ");
		boolean abcRow = isYes(scnr.next().trim());
		
		System.out.printf("Columns use Letters? (y or n): ");
		boolean abcCol = isYes(scnr.next().trim());
		
		System.out.printf("Do you want to be able to overwrite owned cells? (y or n): ");
		boolean canOverride = isYes(scnr.next().trim());
		
		Grid gameGrid = new Grid(maxCoord.y, maxCoord.x, abcCol, abcRow, canOverride);

		int turns = 20;
		System.out.printf("Please enter Num Turns (1-%d): ", (int)(maxCoord.x * maxCoord.y * 0.5));
		try { 
			int chosenTurns = scnr.nextInt(); 
			if (chosenTurns > 0 && chosenTurns < gameGrid.getRowCount() * gameGrid.getColCount() * 0.5)
				turns = chosenTurns;
		}
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to 20"); }

		scnr.nextLine(); //use nextLine for Style input

		System.out.printf("Style (%s): ", Theme.getList());		
		try { select = scnr.nextInt(); }
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Default"); }
		Styles.currentTheme = Theme.themeFromInt(select);
		
		scnr.nextLine(); //use nextLine for Round Input

		ComputerPlayerUnit cpu = new ComputerPlayerUnit();
		ScoreCalculator scoreCalc = new ScoreCalculator();
		Highscore scores = new Highscore();
		
		int prevPScore = 0;
		int prevCScore = 0;
		Coordinate prevCoord = new Coordinate(-1, -1);
		for (int i = 0; i < turns; i++) {
			prevPScore = scoreCalc.getPlayerScore();
			prevCScore = scoreCalc.getComputerScore();
			prevCoord = round(turns, i, cpu, scoreCalc, scores, scnr, prevPScore, prevCScore, prevCoord, chosenDiff, gameGrid);
		}
		printRound(turns, turns, scoreCalc, scores, prevPScore, prevCScore, gameGrid);
		System.out.printf("\n\nGame Over %s\n", scoreCalc.getWhoIsWinner());
		
		int score = scoreCalc.getPlayerScore() - scoreCalc.getComputerScore();
		if (score > scores.score.getScore()) {
			System.out.printf("\n\nNew Record!\nPlease Enter Your Name: ");
			scores.saveScore(new Score(score, scnr.nextLine()));
			scores.saveFile();
		}
		scnr.close();
	}
	
	public Coordinate round(int turns, int roundNum, ComputerPlayerUnit cpu, ScoreCalculator scoreCalc, Highscore scores, Scanner scnr, int prevPScore, int prevCScore, Coordinate prevCoord, CPUState difficulty, Grid grid) {
		printRound(turns, roundNum, scoreCalc, scores, prevPScore, prevCScore, grid);

		boolean turnOver = false;
		while (!turnOver) {
			System.out.printf("Player(X) Turn (Row Col): ");
			String input = scnr.nextLine().trim();

			try {
				Coordinate moveCoord = parseMove(input);
				turnOver = grid.setCell(moveCoord.y, moveCoord.x, Owner.PLAYER);
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter numbers only.");
			}
		}

		Coordinate cpuCoord = cpu.getTurn(prevCoord, difficulty, grid);
		System.out.printf("CPU(X) Turn (Row Col): %d %d", cpuCoord.y, cpuCoord.x);
		grid.setCell(cpuCoord.y, cpuCoord.x, Owner.COMPUTER);
		scoreCalc.updateScores(grid);
		return cpuCoord;
	}
	
	public void printRound(int turns, int roundNum, ScoreCalculator scoreCalc, Highscore scores, int prevPScore, int prevCScore, Grid gameGrid) {
		Theme t = Styles.currentTheme;
		System.out.printf("\n%s%s V E C T O R S                      %s\n%s", t.header, t.dash, t.vertbar, t.lnbreak);
		System.out.printf("%s High Score: %s – %-5d\t     %s\n", t.dash, scores.score.getName(), scores.score.getScore(), t.vertbar);
		System.out.printf("%s%s Player Score:\t %4d Last Round%4d %s\n", t.lnbreak, t.dash, scoreCalc.getPlayerScore(), prevPScore, t.vertbar);
		System.out.printf("%s Computer Score:%4d Last Round%4d %s\n%s", t.dash, scoreCalc.getComputerScore(), prevCScore, t.vertbar, t.lnbreak);
		System.out.printf("%s Turn %2d of %2d\t\t\t     %s\n%s", t.dash, roundNum, turns, t.vertbar, t.foot);
		gameGrid.printGrid();
	}
	
	
	//	Input Parsing

	private static Coordinate parseMove(String in) {
		in = in.trim().toUpperCase();
		if (in.matches("[A-Z0-9]{2}")) { //Sees if input is A-Z or single digit char in a 2 long string
			return new Coordinate(parseCoord(in.charAt(1) + ""), parseCoord(in.charAt(0) + ""));
		}

		String[] parts = in.split("[,.\\s]+"); //'\\s' and ',.' handles potential whitespace 
		if (parts.length != 2) {
			System.out.println("Invalid coordinate");
			return new Coordinate(-1, -1); //Returns invalid coord
		}

		return new Coordinate(parseCoord(parts[1]), parseCoord(parts[0]));
	}

	private static int parseCoord(String in) {
		in = in.trim().toUpperCase();

		if (in.length() == 1 && Character.isLetter(in.charAt(0))) {
			return in.charAt(0) - 'A';
		}
		return Integer.parseInt(in);
	}
	
	private static boolean isYes(String in) {
		return in.equalsIgnoreCase("yes") || in.equalsIgnoreCase("y") || in.equalsIgnoreCase("true");
	}
}
