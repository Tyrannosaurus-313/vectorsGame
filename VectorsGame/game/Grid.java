package game;

public class Grid {
	enum ControlState {
		//Null is Uncontrolled/Empty
		PLAYER,
		COMPUTER;

		public char toChar() {
			return name().charAt(0);
		}
	}
	
	private ControlState[][] worldGrid = new ControlState[8][8];
	public final GridSize lengths = new GridSize(worldGrid.length, worldGrid[0].length);
	
	public ControlState getState(int row, int col) {
		return worldGrid[col][row];
	}
	
	public boolean setCell(int row, int col, ControlState owner) {
		if (row < worldGrid.length && col < worldGrid[0].length && worldGrid[col][row] == null) {
			worldGrid[col][row] = owner;
			return true;
		}
<<<<<<< HEAD
		return false;		
=======
		return false;
>>>>>>> branch 'main' of git@github.com:Tyrannosaurus-313/vectorsGame.git
	}
	
	public void printGrid()
	{
		System.out.printf("  0   1   2   3   4   5   6   7  \n");
		for (int i = 0; i < worldGrid.length; i++) {
			System.out.printf("---------------------------------\n|");
			for (int j = 0; j < worldGrid[i].length; j++) {
				System.out.printf(" %c |", stateToChar(worldGrid[i][j]));
			}
			System.out.printf("% d\n", i);
		}
		System.out.printf("---------------------------------\n");
	}
	
	public char stateToChar(ControlState state) {
		if (state == null) { return ' '; }
		return state.toChar();
	}
}
