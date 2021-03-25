import main.java.model.Theater;
import test.java.TestManager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        TestManager testManager = new TestManager(args[0]);
        testManager.runTests();

        Theater theater = new Theater(args[0]);
        theater.fillTheatre();
        theater.print();

        try {
            System.out.println("Output file generated at: " + theater.generateOutputFile());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
