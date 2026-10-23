package game;

import game.ComputerPlayerUnit.CPUState;
import game.Grid.Owner;
import game.Styles.Style;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Vectors {
	private Highscore scores = new Highscore();
	private int rounds = 20;
	private Grid gameGrid;
	
	public void setNumTurns(int rounds) { //rounds > 0 and rounds < cellCount / 2
		if (rounds > 0 && rounds < gameGrid.lengths.rowCount * gameGrid.lengths.colCount * 0.5)
			this.rounds = rounds;
	}
	
	public void run() {
//		scores.forceReset(); //For force resetting the high score
		Scanner scnr = new Scanner(System.in);

		Coordinate maxCoord = new Coordinate(-1, -1);
		System.out.printf("Enter Grid Size Between y (1-26), x (1-26): ");
		maxCoord = parseMove(scnr.nextLine());
		if (maxCoord.x == -1) System.out.println("Invalid input. Default 8 was used");
		gameGrid = new Grid(maxCoord.y, maxCoord.x);

		System.out.printf("Please enter Num Turns (1-%d): ", (int)(gameGrid.lengths.rowCount * gameGrid.lengths.colCount * 0.5));
		try { setNumTurns(scnr.nextInt()); }
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to 20"); }

		int select = 0;
		System.out.printf("Please enter Difficulty, (%s): ", CPUState.getList());
		try { select = scnr.nextInt(); }
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Random"); }
		CPUState chosenDiff = CPUState.intToState(select);

		System.out.printf("Rows use Letters? (y or n): ");
		gameGrid.abcRow = isYes(scnr.next().trim());
		
		System.out.printf("Columns use Letters? (y or n): ");
		gameGrid.abcCol = isYes(scnr.next().trim());
		
		System.out.printf("Do you want to be able to overwrite owned cells? (y or n): ");
		gameGrid.canOverride = isYes(scnr.next().trim());
		
		scnr.nextLine(); //use nextLine for Style input

		System.out.printf("Style (%s): ", Style.getList());		
		try { select = scnr.nextInt(); }
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Default"); }
		Styles.setAllStyles(Style.intToState(select));
		
		scnr.nextLine(); //use nextLine for Round Input

		Styles.constructGridHeadAndFoot(gameGrid.lengths.rowCount); //possibly backwards

		ComputerPlayerUnit cpu = new ComputerPlayerUnit(chosenDiff, gameGrid);
		ScoreCalculator scoreCalc = new ScoreCalculator();
		
		for (int i = 0; i < rounds; i++) {
			round(i, cpu, scoreCalc, scnr);
		}
		printRound(rounds, scoreCalc);
		System.out.printf("\n\nGame Over %s\n", scoreCalc.getWhoIsWinner());
		
		int score = scoreCalc.getPlayerScore() - scoreCalc.getComputerScore();
		if (score > scores.score.getScore()) {
			System.out.printf("\n\nNew Record!\nPlease Enter Your Name: ");
			scores.saveScore(new Score(score, scnr.nextLine()));
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
		System.out.printf("CPU(X) Turn (Row Col): %d %d", cpuCoord.y, cpuCoord.x);
		gameGrid.setCell(cpuCoord.x, cpuCoord.y, Owner.COMPUTER);
		scoreCalc.updateScores(gameGrid);
	}
	
	public void printRound(int roundNum, ScoreCalculator scoreCalc) {
		System.out.printf("\n%s%s V E C T O R S                      %s\n%s", Styles.header, Styles.dash, Styles.vertbar, Styles.lnbreak);
		System.out.printf("%s High Score: %s – %-5d\t     %s\n", Styles.dash, scores.score.getName(), scores.score.getScore(), Styles.vertbar);
		System.out.printf("%s%s Player Score:\t %4d Last Round%4d %s\n", Styles.lnbreak, Styles.dash, scoreCalc.getPlayerScore(), prevPScore, Styles.vertbar);
		System.out.printf("%s Computer Score:%4d Last Round%4d %s\n%s", Styles.dash, scoreCalc.getComputerScore(), prevCScore, Styles.vertbar, Styles.lnbreak);
		System.out.printf("%s Turn %2d of %2d\t\t\t     %s\n%s", Styles.dash, roundNum, rounds, Styles.vertbar, Styles.foot);
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
