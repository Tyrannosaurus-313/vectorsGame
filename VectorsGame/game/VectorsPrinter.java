package game;

public class VectorsPrinter {
	
	public static void printRound(int turns, int roundNum, ScoreCalculator scoreCalc, Highscore scores, int prevPScore, int prevCScore, Grid gameGrid) {
		Styles.Theme t = Styles.currentTheme;
		System.out.printf("\n%s%s V E C T O R S                      %s\n%s", t.header, t.dash, t.vertbar, t.lnbreak);
		System.out.printf("%s High Score: %s – %-5d\t     %s\n", t.dash, scores.score.getName(), scores.score.getScore(), t.vertbar);
		System.out.printf("%s%s Player Score:\t %4d Last Round%4d %s\n", t.lnbreak, t.dash, scoreCalc.getPlayerScore(), prevPScore, t.vertbar);
		System.out.printf("%s Computer Score:%4d Last Round%4d %s\n%s", t.dash, scoreCalc.getComputerScore(), prevCScore, t.vertbar, t.lnbreak);
		System.out.printf("%s Turn %2d of %2d\t\t\t     %s\n%s", t.dash, roundNum, turns, t.vertbar, t.foot);
	}	

	
	// -- Grid Printing Methods --
	
	//TODO: Try to get rid of these static global variables possibly?
    private static String header;
    private static String bar;
    private static String footer;
	private static char vBar;
    private static boolean frameBuilt = false; 
	public static void printGrid(Grid grid, boolean abcCol, boolean abcRow) {
		if (!frameBuilt)
			buildGridFrame(grid.getColCount());

		int i = 0;
		String gridStr = "   ";
		for (; i < grid.getColCount(); i++) {
			 gridStr += formatLabel(i, abcCol, false);
		}
		gridStr += "\n  " + header + "\n";

		for (i = 0; i < grid.getRowCount() - 1; i++) {
			gridStr += formatLabel(i, abcRow, true) + vBar + getRow(grid, i) + "\n  " + bar + "\n";
		}
		gridStr += formatLabel(i, abcRow, true) + vBar + getRow(grid, grid.getRowCount() - 1);
		System.out.printf("%s\n  %s\n", gridStr, footer);
	}

	private static String getRow(Grid grid, int i) {
		String str = "";
		for (int j = 0; j < grid.getColCount(); j++) {
			str += " " + grid.getState(i, j).rep + " " + vBar;
		}
		return str;
	}

	private static String formatLabel(int i, boolean letter, boolean leftAlign) {
		if (leftAlign && letter) return String.format("%-2c", (char)('A' + i));
		else if (leftAlign) return String.format("%-2d", i);
		else if (letter) return String.format("%2c  ", (char)('A' + i));
		return String.format("%2d  ", i);
	}

	private static void buildGridFrame(int cols) {
		Styles.Theme t = Styles.currentTheme;
		vBar = t.vBar;
		header = t.topLeft + "";
     	bar = t.midLeft + "";
     	footer = t.bottomLeft + "";
     	for (int i = 0; i < cols - 1; i++) {
     		header += t.spacer + t.topMid;
	 		bar += t.spacer + t.midMid;
	 		footer += t.spacer + t.bottomMid;
		}
		header += t.spacer + t.topRight;
		bar += t.spacer + t.midRight;
		footer += t.spacer + t.bottomRight;
		frameBuilt = true;
	}

}
