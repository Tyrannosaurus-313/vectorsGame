package game;

public class Styles {
    public enum Style { Default, Clean, Bold; }

    public static void setAllStyles(Style style) {
        setGridStyle(style);
        setGameStyle(style);
    }

	public static char topLeft, topMid, topRight;
	public static char midLeft, midMid, midRight;
	public static char bottomLeft, bottomMid, bottomRight;
    public static String spacer;
	// public static String gridHeader;
	public static String gridBar;
	// public static String gridRow;
	// public static String gridFoot;
    public static void setGridStyle(Style style)
	{
		if (style == Style.Default) {
            topLeft = '-';
            topRight = topLeft;
            topMid = topLeft;
            midLeft = topLeft;
            midMid = topLeft;
            midRight = topLeft;
            bottomLeft = topLeft;
            bottomMid = topLeft;
            bottomRight = topLeft;
            spacer = "---";
			// gridHeader = "---------------------------------";
			gridBar = "|";
			// gridRow = "---------------------------------";
			// gridFoot = "---------------------------------";	
		}
		else if (style == Style.Clean) {
            topLeft = '┌';
            topRight = '┐';
            topMid = '┬';
            midLeft = '├';
            midMid = '┼';
            midRight = '┤';
            bottomLeft = '└';
            bottomMid = '┴';
            bottomRight = '┘';
            spacer = "───";
			// gridHeader = "┌───┬───┬───┬───┬───┬───┬───┬───┐";
			gridBar = "│";
			// gridRow = "├───┼───┼───┼───┼───┼───┼───┼───┤";
			// gridFoot = "└───┴───┴───┴───┴───┴───┴───┴───┘";			
		}
		else if (style == Style.Bold) {
            topLeft = '┏';
            topRight = '┓';
            topMid = '┳';
            midLeft = '┣';
            midMid = '╋';
            midRight = '┫';
            bottomLeft = '┗';
            bottomMid = '┻';
            bottomRight = '┛';
            spacer = "━━━";
			// gridHeader = "┏━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┳━━━┓";
			gridBar = "┃";
			// gridRow = "┣━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━╋━━━┫";
			// gridFoot = "┗━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┻━━━┛";				
		}
	}

	public static String lnbreak;
	public static String header;
	public static String foot;
	public static String dash;
	public static String vertbar;
    public static void setGameStyle(Style style) {
		if (style == Style.Default) {
            lnbreak = "**************************************\n";
			header = lnbreak;
			foot = lnbreak;
			vertbar = "*";
			dash = "-";
		}
		else if (style == Styles.Style.Clean) {
			lnbreak = "├────────────────────────────────────┤\n";
			header = "┌────────────────────────────────────┐\n";
			foot = "└────────────────────────────────────┘\n";
			vertbar = "│";
			dash = vertbar;
		}
		else if (style == Style.Bold) {
			lnbreak = "┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n";
			header = "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n";
			foot = "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n";	
			vertbar = "┃";
			dash = vertbar;			
		}
	}
}
