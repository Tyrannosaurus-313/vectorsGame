package game;

import game.ComputerPlayerUnit.CPUState;

public class Styles {
    public enum Style { Default, Clean, Bold; 
    	
		public static String getList() {
			String str = "";
			Style[] list = Style.values();
			for (int i = 0; i < list.length; i++) {
				str += list[i].ordinal() + " = " + list[i].name();
				if (i != list.length - 1)
					str += ", ";
			}
			return str;
		}
		
		public static Style intToState(int ordinal) {
			Style[] list = Style.values();
			for (int i = 0; i < list.length; i++) {
				if (ordinal == list[i].ordinal())
					return list[i];
			}
			return Style.Default;
		}
    }

    public static void setAllStyles(Style style) {
        setGridStyle(style);
        setGameStyle(style);
    }

	private static char topLeft, topMid, topRight;
	private static char midLeft, midMid, midRight;
	private static char bottomLeft, bottomMid, bottomRight;
    public static String spacer;
	public static String vBar;
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
            vBar = "|";
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
            vBar = "│";		
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
            vBar = "┃";			
		}
	}
    
    public static String gridHead = "";
    public static String gridBar = "";
    public static String gridFoot = "";
    public static void constructGridHeadAndFoot(int maxNum)
    {
    	gridHead += Styles.topLeft;
    	gridBar += Styles.midLeft;
    	gridFoot += Styles.bottomLeft;
    	for (int i = 0; i < maxNum; i++) {
    		gridHead += Styles.spacer;
			gridBar += Styles.spacer;
			gridFoot += Styles.spacer;
			if (i == maxNum - 1) {				
				gridHead += Styles.topRight;
				gridBar += Styles.midRight;
				gridFoot += Styles.bottomRight;
			}
			else {
				gridHead += Styles.topMid;
				gridBar += Styles.midMid;	
				gridFoot += Styles.bottomMid;			
			}
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
