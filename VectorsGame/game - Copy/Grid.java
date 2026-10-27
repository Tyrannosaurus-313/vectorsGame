package game;

import game.Styles.Theme;

public class Grid {
	// TODO: May want to eliminate this.
	enum Owner {
		UNOWNED(' '), PLAYER('X'), COMPUTER('C'); //Null is Uncontrolled/Empty
		final char rep;
		Owner(char c) { this.rep = c; }
	}
	
	private final Owner[][] worldGrid;
	private final int columnCount;
	private final int rowCount;
	public int getColCount() {
		return columnCount;
	}
	public int getRowCount() {
		return rowCount;
	}

	// TODO: Consider getting rid of these global variables.
	private final boolean abcCol;
	private final boolean abcRow;
	private final boolean canOverride;
	
	public Grid(int maxY, int maxX, boolean abcCol, boolean abcRow, boolean canOverride) {
		this.abcCol = abcCol;
		this.abcRow = abcRow;
		this.canOverride = canOverride;

		if (maxY > 26 || maxY < 1) maxY = 8;
		if (maxX > 26 || maxX < 1) maxX = 8;
		
		worldGrid = new Owner[maxY][maxX];
		rowCount = maxY;
		columnCount = maxX;
        for (int row = 0; row < worldGrid.length; row++) {
            for (int col = 0; col < worldGrid[0].length; col++) {
                worldGrid[row][col] = Owner.UNOWNED;
            }
        }
	}

	public Owner getState(int r, int c) {
		return worldGrid[r][c];
	}
	
	public void resetCell(int r, int c) {
		if (r < worldGrid.length && c < worldGrid[0].length)
			worldGrid[r][c] = Owner.UNOWNED;	
	}
	
	public boolean isCellValid(int r, int c, Owner state) {
		return inBounds(r, c) && worldGrid[r][c] != state && (canOverride || worldGrid[r][c] == Owner.UNOWNED); 
	}
	
	public boolean setCell(int r, int c, Owner state) {
		if (!isCellValid(r, c, state)) 
			return false;	
		
		if (worldGrid[r][c] != Owner.UNOWNED) {
			worldGrid[r][c] = Owner.UNOWNED;
		}
		else {
			worldGrid[r][c] = state;
		}
		return true;	
	}
	
	private boolean inBounds(int r, int c) {
		return r < rowCount && c < columnCount && r >= 0 && c >= 0;
	}
	
	// TODO: May want to consider avoiding these global variables too
    private String header;
    private String bar;
    private String footer;
	private char vBar;
    private boolean frameBuilt = false; 
	public void printGrid() {
		buildGridFrame(columnCount);

		int i = 0;
		String gridStr = "   ";
		for (; i < columnCount; i++) {
			 gridStr += formatLabel(i, abcCol, false);
		}
		gridStr += "\n  " + header + "\n";

		for (i = 0; i < rowCount - 1; i++) {
			gridStr += formatLabel(i, abcRow, true) + vBar + getRow(i) + "\n  " + bar + "\n";
		}
		gridStr += formatLabel(i, abcRow, true) + vBar + getRow(rowCount - 1);
		System.out.printf("%s\n  %s\n", gridStr, footer);
	}

	public String getRow(int i) {
		String str = "";
		for (int j = 0; j < columnCount; j++) {
			str += " " + worldGrid[i][j].rep + " " + vBar;
		}
		return str;
	}

	public String formatLabel(int i, boolean letter, boolean leftAlign) {
		if (leftAlign && letter) return String.format("%-2c", (char)('A' + i));
		else if (leftAlign) return String.format("%-2d", i);
		else if (letter) return String.format("%2c  ", (char)('A' + i));
		return String.format("%2d  ", i);
	}

	public void buildGridFrame(int cols) {
		if (frameBuilt) return;

        Theme t = Styles.currentTheme;
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
