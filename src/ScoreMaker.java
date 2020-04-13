import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

//Gives us our write functions,read abilities, etc. to use I/O
public class ScoreMaker {
	
	//list of scores and names read from the file
	private List<Integer> times;
	private List<String> names;
	
	public ScoreMaker(Reader r) throws IOException {
		if (r == null) {
			throw new IllegalArgumentException();
		}
		
		times = new LinkedList<Integer>();
		names = new LinkedList<String>();
		
		//We use a buffer reader to read line-by-line
		BufferedReader b = new BufferedReader(r);
		while(b.ready()) {
			String score = b.readLine().trim();
			int arrow = 0;
			String num = "";
			String name = "";
			for (int i = 0; i < score.length(); i++) {
				if (score.charAt(i) == '>') {
					arrow = i;
				}
			}
			
			//Separates each line into a name and score
			num = score.substring(arrow + 1);
			name = score.substring(0, arrow - 1);
			times.add(Integer.parseInt(num));
			names.add(name);
		}
		r.close();
	}
	
	//Writes a name and score to a single line on our file
	public void write(int time, String filename, String name) throws IOException {
		times.add(time);
		names.add(name);
		FileWriter f = new FileWriter(filename, true);
		BufferedWriter b = new BufferedWriter(f);
		String s = name + "->" + time;
		b.write(s);
		b.newLine();
		b.close();
		
	}
	
	//Makes a ScoreMaker from a given file
	public static ScoreMaker makeFromFile(String filename) throws IOException {
	    
	    if (filename == null) {
            throw new IllegalArgumentException();
        } else if (filename.isEmpty()) {
            throw new IllegalArgumentException();
        }
	    
	    File scores = new File(filename);
	    if (!scores.exists()) {
            try {
                scores.createNewFile();
            } catch (IOException e) {
                System.out.println("Error: Could not create scores file");
                System.exit(0);
            }
        }
		
		
		Reader origin = new FileReader(scores);
		ScoreMaker highscore = new ScoreMaker(origin);
		origin.close();
		
		return highscore;
		
	}
	
	//Getters
	public List<Integer> getTimes() {
		return this.times;
	}
	
	public List<String> getNames() {
		return this.names;
	}
}
