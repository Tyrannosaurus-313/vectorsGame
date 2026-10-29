package game;

public class Grid {
	enum Owner {
		UNOWNED(' '), PLAYER('X'), COMPUTER('C');
		final char rep;
		Owner(char c) { this.rep = c; }
	}

	private final boolean canOverride;
	public boolean getCanOverride() { return canOverride; };

	private final Owner[][] worldGrid;
	
	public int getColCount() {
		return worldGrid[0].length;
	}
	public int getRowCount() {
		return worldGrid.length;
	}
	
	public Grid(int maxY, int maxX, boolean canOverride) {
		this.canOverride = canOverride;

		if (maxY > 26 || maxY < 1) maxY = 8;
		if (maxX > 26 || maxX < 1) maxX = 8;
		
		worldGrid = new Owner[maxY][maxX];
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
		if (inBounds(r, c))
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
	
	public boolean inBounds(int r, int c) {
		return r < getRowCount() && c < getColCount() && r >= 0 && c >= 0;
	}
}
