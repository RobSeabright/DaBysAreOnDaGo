package game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author Rob
 */

public class ScoreHandler {
    
    private static ScoreHandler instance = new ScoreHandler();
    
    public static ScoreHandler getInstance() {
            return instance;
    }

    public void writeScore(int score) throws IOException {
        if (score > this.readScore()){
            File file = new File("Score.txt");

            BufferedWriter outputStream = new BufferedWriter(new FileWriter(file));
            String output = String.valueOf(score);
        
            try {
                outputStream.write(output);
                outputStream.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public int readScore() {
        File file = new File("Score.txt");

        try {
            BufferedReader inputStream = new BufferedReader(new FileReader(file));

            String l;
            while ((l = inputStream.readLine()) != null) {
                return Integer.parseInt(l);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return 0;
    }
}
