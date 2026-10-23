package game;

public class Grid {
	enum Owner {
		UNOWNED, PLAYER, COMPUTER; //Null is Uncontrolled/Empty

		public char toChar() {
			return name().charAt(0); //name() gets the name of the enum
		}
		
		public char stateToChar() {
			if (this == Owner.UNOWNED) { return ' '; }
			else if (this == Owner.PLAYER) { return 'X'; }
			else { return 'C'; }
		}
	}
	
	private Owner[][] worldGrid  = new Owner[8][8];//[8][8];

	public boolean abcCol = false;
	public boolean abcRow = false;
	public boolean canOverride = false;
	public GridSize lengths = new GridSize(worldGrid.length, worldGrid[0].length);
	
	public Grid(int colCount, int rowCount) {
		if (colCount > 26 || colCount < 1) colCount = 8;
		if (rowCount > 26 || rowCount < 1) rowCount = 8;
		
		worldGrid = new Owner[colCount][rowCount];
		lengths = new GridSize(worldGrid.length, worldGrid[0].length);
        for (int y = 0; y < worldGrid.length; y++) {
            for (int x = 0; x < worldGrid[0].length; x++) {
                this.worldGrid[y][x] = Owner.UNOWNED;
            }
        }
	}
	
	public Owner getState(int row, int col) {
		return worldGrid[col][row];
	}
	
	public void resetCell(int row, int col) {
		if (row < worldGrid.length && col < worldGrid[0].length)
			this.worldGrid[col][row] = Owner.UNOWNED;	
	}
	
	public boolean isCellValid(int row, int col, Owner state) 	{
		return inBounds(row, col) && worldGrid[col][row] != state
				&& (canOverride || worldGrid[col][row] == Owner.UNOWNED); //Either override allows must be on, or the cell must be unowned
	}
	
	public boolean setCell(int row, int col, Owner state) {
		if (!isCellValid(row, col, state))
			return false;	
		
		if (worldGrid[col][row] != Owner.UNOWNED) {
			this.worldGrid[col][row] = Owner.UNOWNED;
		}
		else {
			this.worldGrid[col][row] = state;
		}
		return true;	
	}
	
	private boolean inBounds(int x, int y) {
		return x < worldGrid[0].length && y < worldGrid.length && x >= 0 && y >= 0;
	}
	
	public void printGrid() {
		System.out.printf("   ");		
		int i = 0;
		for (; i < worldGrid[0].length; i++) {
			if (abcCol) {
				System.out.printf("%c   ", (char)('A' + i));
			}
			else {
				System.out.printf("%2d  ", i);
			}
		}
			
		String header = Styles.gridHead;
		String bar = Styles.gridBar;	
		String foot = Styles.gridFoot;

		System.out.printf("\n  %s\n", header);
		if (abcRow)
		{
			char rowChar = 'A';
			for (i = 0; i < worldGrid.length; i++) {
				System.out.printf("%-2c%s", (char)(i + rowChar), Styles.vBar);
				for (int j = 0; j < worldGrid[i].length; j++) {
					System.out.printf(" %c %s", worldGrid[i][j].stateToChar(), Styles.vBar);
				}
				if (i != worldGrid.length - 1) {
					System.out.printf("\n  %s\n", bar);
				}
			}
		}
		else
		{
			for (i = 0; i < worldGrid.length; i++) {
				System.out.printf("%-2d%s", i, Styles.vBar);
				for (int j = 0; j < worldGrid[i].length; j++) {
					System.out.printf(" %c %s", worldGrid[i][j].stateToChar(), Styles.vBar);
				}
				if (i != worldGrid.length - 1) {
					System.out.printf("\n  %s\n", bar);
				}
			}
		}
		System.out.printf("\n  %s\n", foot);
	}
}
