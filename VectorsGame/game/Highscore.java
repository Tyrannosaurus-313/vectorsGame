package game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Highscore {
	private String filename = "users.dat";	
	public Score score = new Score(-1, "No Record");
	
	public Highscore() {
		loadFile();
	}
	
	public void loadFile() {
		FileInputStream inFile;
		ObjectInputStream in;
		try {
			inFile = new FileInputStream(filename);
			in = new ObjectInputStream(inFile);
			score = (Score) in.readObject();
			in.close();
			inFile.close();
		}
		catch (FileNotFoundException ex)	{ ex.printStackTrace(); }
		catch (IOException ex) 				{ ex.printStackTrace(); } 
		catch (ClassNotFoundException ex)	{ ex.printStackTrace(); }		
	}
	
	public void saveFile() {
		FileOutputStream outFile;
		ObjectOutputStream out;
		try {
			outFile = new FileOutputStream(filename);
			out = new ObjectOutputStream(outFile);
			out.writeObject(score);
			out.close();
			outFile.close();
		}
		catch (FileNotFoundException ex)	{ ex.printStackTrace(); }
		catch (IOException ex) 				{ ex.printStackTrace(); }
	}
	
	public void saveScore(Score newScore) {
		if (newScore.getScore() > score.getScore())
			score = newScore;			
	}
	
	public void show() {
		System.out.printf("- High Score: %s – %-5d\t     *\n", score.getName(), score.getScore());
	}
	
	public void forceReset() {
		this.score = new Score();
		this.saveFile();
	}
}
