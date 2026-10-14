package game;

public class Grid {
	enum ControlState {
		//Null is Empty
		Player,
		Computer
	}
	
	private ControlState[][] worldGrid = new ControlState[8][8];
	public int[] getGridLengths()
	{
		int[] lengths = new int[2];
		lengths[0] = worldGrid.length;
		lengths[1] = worldGrid[0].length;
		return lengths;
	}
	
	public ControlState getState(int row, int col) {
		return worldGrid[col][row];
	}
	
	public boolean setCell(int row, int col, ControlState state) {
		if (row < worldGrid.length && col < worldGrid[0].length && worldGrid[col][row] == null) {
			worldGrid[col][row] = state;
			return true;
		}
		else {
			return false;
		}
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
	
	public char stateToChar(ControlState state)
	{
		if (state == ControlState.Player) {
			return 'P';
		}
		else if (state == ControlState.Computer) {
			return 'C';
		}
		else {
			return ' ';
		}
	}
}
