package game;

import java.io.Serializable;

public class Score implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name = "No Record";
	public String getName()	{ return name; }
	public void setName(String name) { this.name = name; }	
	
	private int score = -1;
	public int getScore() { return score; }
	public void setScore(int score) { this.score = score; }
	
	public Score() {}
	public Score(int score, String name) {
		setName(name);
		setScore(score);
	}	
}