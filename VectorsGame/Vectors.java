package game;

import game.ComputerPlayer.Difficulty;
import game.Grid.Owner;
import game.Styles.Theme;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Vectors {	
	public void run() {
		Scanner scnr = new Scanner(System.in);
		Highscore scores = new Highscore();
		// scores.forceReset(); //For force resetting the high score

		Coordinate maxCoord;
		int gridSize = 64;
		System.out.printf("Enter Grid Size Between y (1-26), x (1-26): ");
		maxCoord = parseMove(scnr.nextLine());
		if (maxCoord.x == -1)
			System.out.println("Invalid input. Default 8 was used");
		else
			gridSize = maxCoord.x * maxCoord.y;
		
		int turns = 20;
		System.out.printf("Please enter Num Turns (1-%.0f): ", gridSize * 0.5);
		try { 			
			int chosenTurns = scnr.nextInt();
			if (chosenTurns > 0 && chosenTurns <= gridSize * 0.5) {
				turns = chosenTurns;	
			}
		}
		catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to 20"); }

		int select = getInputFromEnum(scnr, Difficulty.class);//0;
		// System.out.printf("Please enter Difficulty, (%s): ", ComputerPlayerUnit.getList());
		// try { select = scnr.nextInt(); }
		// catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Random"); }
		// Difficulty chosenDiff = ComputerPlayerUnit.intToState(select);
		Difficulty chosenDiff = fromOrdinal(Difficulty.class, select);

		System.out.printf("Rows use Letters? (y or n): ");
		boolean abcRow = isYes(scnr.next());
		
		System.out.printf("Columns use Letters? (y or n): ");
		boolean abcCol = isYes(scnr.next());

		System.out.printf("Do you want to be able to overwrite owned cells? (y or n): ");
		boolean canOverride = isYes(scnr.next());
		
		Grid gameGrid = new Grid(maxCoord.y, maxCoord.x, canOverride);
		
		scnr.nextLine(); //consume the next line

		select = getInputFromEnum(scnr, Theme.class);//0;
		// System.out.printf("Style (%s): ", Theme.getList());		
		// try { select = scnr.nextInt(); }
		// catch (InputMismatchException e) { System.out.println("Invalid input. Defaulting to Default"); }
		Styles.currentTheme = fromOrdinal(Theme.class, select);//Theme.themeFromInt(select);
		
		scnr.nextLine(); //Consume the next line

		ComputerPlayer cpu = new ComputerPlayer();		
		GameData data = new GameData(turns, gameGrid);
		VectorsPrinter printer = new VectorsPrinter();

		for (int i = 0; i < turns; i++) {
			printer.printTurn(turns, i, data, gameGrid, scores, abcCol, abcRow);
			data.updateScore();
			data.currentTurn = i;
			data.prevCompCoord = round(data, cpu, scnr, chosenDiff);
		}
		printer.printTurn(turns, turns, data, gameGrid, scores, abcCol, abcRow);
		System.out.printf("\n\nGame Over %s\n", data.scoreCalc.getWhoIsWinner());
		
		int score = data.scoreCalc.getNetScore();
		if (score > scores.score.getScore()) {
			System.out.printf("\nNew Record!\nPlease Enter Your Name: ");
			scores.saveScore(new Score(score, scnr.nextLine()));
			scores.saveFile();
		}
		scnr.close();
	}

	public Coordinate round(GameData data, ComputerPlayer cpu, Scanner scnr, Difficulty difficulty) {
		boolean turnOver = false;
		while (!turnOver) {
			System.out.printf("Player(X) Turn (Row Col): ");
			String input = scnr.nextLine().trim();

			try {
				Coordinate moveCoord = parseMove(input);
				turnOver = data.grid.setCell(moveCoord.y, moveCoord.x, Owner.PLAYER);
			}
			catch (NumberFormatException e) {
				System.out.println("Invalid input. Please enter numbers only.");
			}
		}

		Coordinate cpuCoord = cpu.getTurn(data, difficulty);
		System.out.printf("CPU(X) Turn (Row Col): %d %d", cpuCoord.y, cpuCoord.x);
		data.grid.setCell(cpuCoord.y, cpuCoord.x, Owner.COMPUTER);
		data.scoreCalc.updateScores(data.grid);
		return cpuCoord;
	}
		
	//	Input Parsing

	private Coordinate parseMove(String in) {
		in = in.trim().toUpperCase();
		
		if (in.matches("[A-Z0-9]{2}")) { //Sees if input is A-Z or single digit char in a 2 long string
			return new Coordinate(parseCoord(in.charAt(1) + ""), parseCoord(in.charAt(0) + ""));
		}

		String[] parts = in.split("[,.\\s]+"); //splits at '\\s' (any whitespace) and ',.'
		if (parts.length != 2) {
			System.out.println("Invalid coordinate");
			return new Coordinate(-1, -1); //Returns invalid coord
		}

		return new Coordinate(parseCoord(parts[1]), parseCoord(parts[0]));
	}

	private int parseCoord(String in) {
		in = in.trim().toUpperCase();

		if (in.length() == 1 && Character.isLetter(in.charAt(0))) {
			return in.charAt(0) - 'A';
		}
		return Integer.parseInt(in);
	}
	
	private boolean isYes(String in) {
		in = in.trim().toLowerCase();
		return in.equals("yes") || in.equals("y") || in.equals("true");
	}



	// Enum Input Parsing using generics (Apparently you can't make a new extended enum class so this was the next best thing)

	private <E extends Enum<E>> int getInputFromEnum(Scanner scnr, Class<E> enumClass) {
		int select = 0;
		System.out.printf("Please enter %s, (%s): ", enumClass.getSimpleName(), getList(enumClass));
		try { select = scnr.nextInt(); }		
		catch (InputMismatchException e) { System.out.printf("Invalid input. Using %s.\n", fromOrdinal(enumClass, 0).name()); }
		return select;
	}

	private <E extends Enum<E>> E fromOrdinal(Class<E> enumClass, int ordinal) {
		E[] values = enumClass.getEnumConstants(); //This would be CPUState, or Styles for example
		if (ordinal < 0 || ordinal > values.length) 
			return values[0];
		return values[ordinal];
	}

	private <E extends Enum<E>> String getList(Class<E> enumClass) {
		String str = "";		
		E[] list = enumClass.getEnumConstants();
		int i;
		for (i = 0; i < list.length - 1; i++) {
			str += i + " = " + list[i].name() + ", ";
	    }
		return str + (i) + " = " + list[i];
	}
}
