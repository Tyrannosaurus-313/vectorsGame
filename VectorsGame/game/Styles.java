package game;

public class Styles {	
    public enum Theme {
        DEFAULT('-', '-', '-', '-', '-', '-', '-', '-', '-', '|', "---", '-', '*',
				 "**************************************\n", "**************************************\n", "**************************************\n"), //<- Game Frame
        CLEAN('┌', '┬', '┐', '├', '┼', '┤', '└', '┴', '┘', '│', "───", '│', '│',
			"├────────────────────────────────────┤\n", "┌────────────────────────────────────┐\n", "└────────────────────────────────────┘\n"),
        BOLD('┏', '┳', '┓', '┣', '╋', '┫', '┗', '┻', '┛', '┃', "━━━", '┃', '┃',
              "┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫\n", "┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓\n", "┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛\n");

        public final char topLeft, topMid, topRight, 
							midLeft, midMid, midRight, 
							bottomLeft, bottomMid, bottomRight; //All Corners
        public final char vBar;
        public final String spacer;		
		public final char dash;
		public final char vertbar;
		public final String lnbreak;
		public final String header;
		public final String foot;

        Theme(char tl, char tm, char tr, char ml, char mm, char mr, char bl, char bm, char br, 
				char vBar, String spacer, char dash, char vertBar,
				String lnbreak, String header, String foot) {
            this.topLeft = tl;
            this.topMid = tm;
            this.topRight = tr;
            this.midLeft = ml;
            this.midMid = mm;
            this.midRight = mr;
            this.bottomLeft = bl;
            this.bottomMid = bm;
            this.bottomRight = br;
            this.vBar = vBar;
            this.spacer = spacer;
			this.lnbreak = lnbreak;
			this.header = header;
			this.foot = foot;
			this.dash = dash;
			this.vertbar = vertBar;
        }
    }

    public static Theme currentTheme = Theme.DEFAULT;
}