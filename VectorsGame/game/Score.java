package game;

import java.io.Serializable;

public class Score implements Serializable {
	private static final long serialVersionUID = 1L;
	private int score;
	private String name;
	
	public Score() {}
	
	public Score(int score, String name) {
		this.setName(name);
		this.setScore(score);
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}