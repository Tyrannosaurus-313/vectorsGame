package game;

public class Grid {
	enum ControlState {
		NULL, PLAYER, COMPUTER; //Null is Uncontrolled/Empty

		public char toChar() {
			return name().charAt(0); //name() gets the name of the enum
		}
	}
	
	private ControlState[][] worldGrid  = new ControlState[8][8];
	public boolean abcCol = false;
	public boolean abcRow = false;
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

	enum Style {
		Default, Clean, Bold;
	}
	public void setStyle(Style chosenStyle)
	{
		if (chosenStyle == Style.Default)
		{
			header = "---------------------------------";
			bar = "|";
			barRow = "---------------------------------";
			foot = "---------------------------------";	
		}
		else if (chosenStyle == Style.Clean)
		{
			header = "┌───┬───┬───┬───┬───┬───┬───┬───┐";
			bar = "│";
			barRow = "├───┼───┼───┼───┼───┼───┼───┼───┤";
			foot = "└───┴───┴───┴───┴───┴───┴───┴───┘";			
		}
		else if (chosenStyle == Style.Bold)
		{
			header = "┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓";
			bar = "┃";
			barRow = "┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫";
			foot = "┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛";				
		}
	}
	public String header;
	public String bar;
	public String barRow;
	public String foot;
	
	public void printGrid()
	{
		if (abcCol) { System.out.printf("    A   B   C   D   E   F   G   H  \n"); }
		else 		{ System.out.printf("    0   1   2   3   4   5   6   7  \n"); }
		
		char rowChar = '0';
		if (abcRow)
			rowChar = 'A';

		System.out.printf("  %s\n", header);
		for (int i = 0; i < worldGrid.length; i++) {
			System.out.printf("%-2c%s", i + rowChar, bar);
			for (int j = 0; j < worldGrid[i].length; j++) {
				System.out.printf(" %c %s", stateToChar(worldGrid[i][j]), bar);
			}
			if (i != worldGrid.length - 1)
				System.out.printf("\n  %s\n", barRow);
		}
		System.out.printf("\n  %s\n", foot);
	}
	
	public char stateToChar(ControlState state) {
		if (state == ControlState.NULL) { return ' '; }
		else if (state == ControlState.PLAYER) { return 'X'; }
		else { return 'C'; }
//		return state.toChar();
	}
}
