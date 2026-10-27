package game;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Highscore {
	private final String filename = "users.dat";	
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
			inFile.close(); //Potential failure to close if an exception occurs
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
			outFile.close(); //Potential failure to close if an exception occurs
		}
		catch (FileNotFoundException ex)	{ ex.printStackTrace(); }
		catch (IOException ex) 				{ ex.printStackTrace(); }
	}
	
	public void saveScore(Score newScore) {
		if (newScore.getScore() > score.getScore())
			score = newScore;			
	}
	
	public void forceReset() {
		this.score = new Score();
		this.saveFile();
	}
}
