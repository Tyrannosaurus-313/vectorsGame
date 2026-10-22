package game;

public class Grid {
	enum Owner {
		UNOWNED, PLAYER, COMPUTER; //Null is Uncontrolled/Empty

		public char toChar() {
			return name().charAt(0); //name() gets the name of the enum
		}
	}
	
	private Owner[][] worldGrid  = new Owner[8][8];//[8][8];

	public boolean abcCol = false;
	public boolean abcRow = false;
	public boolean canOverride = false;
	public GridSize lengths = new GridSize(worldGrid.length, worldGrid[0].length);
	
	public Grid(int colCount, int rowCount) {
		if (colCount > 32)
			colCount = 32;
		if (rowCount > 32)
			rowCount = 32;
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
		if (isCellValid(row, col, state)) { //IF its valid
			if (worldGrid[col][row] != Owner.UNOWNED) {
				System.out.println("Trying to set " + col + " " + row + " to " + null);
				this.worldGrid[col][row] = null;
				this.worldGrid[col][row] = Owner.UNOWNED;
			}
			else {
				this.worldGrid[col][row] = state;
			}
			return true;
		}
		return false;		
	}
	
	private boolean inBounds(int x, int y) {
		return x < worldGrid[0].length && y < worldGrid.length && x >= 0 && y >= 0;
	}
	
	public void printGrid() {
		System.out.printf("    ");		
		int i = 0;
		if (abcCol) {
			for (; i < worldGrid[0].length; i++) {
				System.out.printf("%c   ", (char)('A' + i));
			}
		}
		else
		{
			for (; i < worldGrid[0].length; i++) {
				System.out.printf("%2d  ", i);
			}			
		}
		
		String header = Styles.topLeft + "";
		for (i = 0; i < worldGrid[0].length; i++)
		{
			header += Styles.spacer;
			if (i == worldGrid[0].length - 1)
				header += Styles.topRight;
			else
				header += Styles.topMid;
		}

		String bar = Styles.midLeft + "";		
		for (i = 0; i < worldGrid[0].length; i++)
		{
			bar += Styles.spacer;
			if (i == worldGrid[0].length - 1)
				bar += Styles.midRight;
			else
				bar += Styles.midMid;
		}

		String foot = Styles.bottomLeft + "";
		for (i = 0; i < worldGrid[0].length; i++)
		{
			foot += Styles.spacer;
			if (i == worldGrid[0].length - 1)
				foot += Styles.bottomRight;
			else
				foot += Styles.bottomMid;
		}

		System.out.printf("\n  %s\n", header);
		if (abcRow)
		{
				char rowChar = 'A';
				for (i = 0; i < worldGrid.length; i++) {
				System.out.printf("%-2c%s", (char)(i + rowChar), Styles.gridBar);
				for (int j = 0; j < worldGrid[i].length; j++) {
					System.out.printf(" %c %s", stateToChar(worldGrid[i][j]), Styles.gridBar);
				}
				if (i != worldGrid.length - 1)
					System.out.printf("\n  %s\n", bar);
			}
		}
		else
		{
			for (i = 0; i < worldGrid.length; i++) {
				System.out.printf("%-2d%s", i, Styles.gridBar);
				for (int j = 0; j < worldGrid[i].length; j++) {
					System.out.printf(" %c %s", stateToChar(worldGrid[i][j]), Styles.gridBar);
				}
				if (i != worldGrid.length - 1)
					System.out.printf("\n  %s\n", bar);
			}
		}
		System.out.printf("\n  %s\n", foot);
	}
	
	public char stateToChar(Owner state) {
		if (state == Owner.UNOWNED) { return ' '; }
		else if (state == Owner.PLAYER) { return 'X'; }
		else { return 'C'; }
	}
}
