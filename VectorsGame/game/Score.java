package game;

import java.io.Serializable;

public class Score implements Serializable {
	private static final long serialVersionUID = 1L;
	private int score = -1;
	private String name = "No Record";
	
	public Score() {}
	
	public Score(int score, String name) {
		setName(name);
		setScore(score);
	}
	
	public int getScore()	{ return score; }
	public String getName()	{ return name; }

	public void setScore(int score) 	{ this.score = score; }
	public void setName(String name)	{ this.name = name; }	
<<<<<<< HEAD
}
=======
}
>>>>>>> branch 'main' of git@github.com:Tyrannosaurus-313/vectorsGame.git
