package game;

public class Grid {
	enum ControlState {
		NULL, PLAYER, COMPUTER; //Null is Uncontrolled/Empty

		public char toChar() {
			return name().charAt(0); //name() gets the name of the enum
		}
	}
	
	private ControlState[][] worldGrid = new ControlState[8][8];
	public boolean abcCol = false;
	public boolean canOverride = false;
	public final GridSize lengths = new GridSize(worldGrid.length, worldGrid[0].length);
	
	public Grid() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                worldGrid[x][y] = ControlState.NULL;
            }
        }
	}
	
	public ControlState getState(int row, int col) {
		return worldGrid[col][row];
	}
	
	public void resetCell(int row, int col) {
		if (row < worldGrid.length && col < worldGrid[0].length)
			worldGrid[col][row] = ControlState.NULL;	
	}
	
	public boolean isCellValid(int row, int col, ControlState state) 	{
		return (canOverride || worldGrid[col][row] == ControlState.NULL) && inBounds(row, col) && worldGrid[col][row] != state;
	}
	
	public boolean setCell(int row, int col, ControlState state) {
		if (isCellValid(row, col, state)) {
			if (worldGrid[col][row] != ControlState.NULL) {
				System.out.println("Trying to set " + col + " " + row + " to " + null);
				worldGrid[row][col] = null;
				worldGrid[row][col] = ControlState.NULL;
			}
			else {
				worldGrid[col][row] = state;
			}
			return true;
		}
		return false;		
	}
	
	private boolean inBounds(int row, int col) {
		return row < worldGrid.length && col < worldGrid[0].length && row >= 0 && col >= 0;
	}
	
	public void printGrid()
	{
		if (abcCol) { System.out.printf("    A   B   C   D   E   F   G   H  \n"); }
		else 		{ System.out.printf("    0   1   2   3   4   5   6   7  \n"); }

		for (int i = 0; i < worldGrid.length; i++) {
			System.out.printf("  ---------------------------------\n");
			System.out.printf("%-2d|", i);
			for (int j = 0; j < worldGrid[i].length; j++) {
				System.out.printf(" %c |", stateToChar(worldGrid[i][j]));
			}
			System.out.printf("\n", i);
		}
		System.out.printf("  ---------------------------------\n");
	}
	
	public char stateToChar(ControlState state) {
		if (state == ControlState.NULL) { return ' '; }
		else if (state == ControlState.PLAYER) { return 'X'; }
		else { return 'C'; }
//		return state.toChar();
	}
}
