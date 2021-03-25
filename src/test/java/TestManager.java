package test.java;

import main.java.model.Theater;
import main.java.utils.FileManager;
import main.java.utils.InputParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

public class TestManager {
    private Theater theater;
    private String testFile;

    public TestManager(String testFile) {
        this.testFile = testFile;
    }

    private boolean fileLoadTest() {
        try {
            FileManager.loadFile(testFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean fileWriteTest() {
        try {
            FileManager.writeFile(System.getProperty("user.dir") + "/testOutput.txt",
                    Collections.singletonList("Test"));
            return true;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean inputParseTest() {
        LinkedHashMap<String, Integer> testReservation = InputParser.parseInput(Arrays.asList(
                "R001 10",
                "R002 0"
        ));
        return testReservation.get("R001") == 10 && testReservation.get("R002") == 0;
    }

    private boolean fileParseTest() {
        try {
            LinkedHashMap<String, Integer> testReservation = InputParser.parseFile(testFile);
            return testReservation.get("R001") == 2 &&
                    testReservation.get("R002") == 4 &&
                    testReservation.get("R003") == 4;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }

    }

    private boolean theaterInstantiationTest() {
        this.theater = new Theater(testFile);
        return this.theater != null;
    }

    private boolean theaterReservationTest() {
        this.theater.fillReservation("R001", 3);
        return true;
    }

    public void runTests() {
        System.out.println("Beginning testing...");
        System.out.println("File loading test: " +
                (fileLoadTest() ? "Success - Test file loaded successfully" : "Fail - Test file unable to load"));
        System.out.println("File write test: " +
                (fileWriteTest() ? "Success - Test file successfully written" : "Fail - Test file failed to write"));
        System.out.println("Input parse test: " +
                (inputParseTest() ? "Success - Sample reservations successfully parsed" : "Fail - Sampled reservations failed to parse"));
        System.out.println("File parse test: " +
                (fileParseTest() ? "Success - Reservation file successfully parsed" : "Fail - Reservation file failed to parse"));
        System.out.println("Theater instantiation test: " +
                (theaterInstantiationTest() ? "Success - Theater object successfully instantiated" : "Fail - Theater object failed to instantiate"));
        System.out.println("Theater reservation test: " +
                (theaterReservationTest() ? "Success - Reservation successfully filled" : "Fail - Reservation failed to fill"));
    }
}
