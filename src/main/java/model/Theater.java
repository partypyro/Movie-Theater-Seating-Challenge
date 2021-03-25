package main.java.model;

import main.java.utils.FileManager;
import main.java.utils.InputParser;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Theater {
    // Theater size constants
    private static final int MAX_ROWS = 10;
    private static final int MAX_SEATS = 20;
    private static final int START_ROW = (MAX_ROWS / 2);
    // Seating label constants
    private static final String EMPTY = "E";
    private static final String BUFFER = "B";
    // Safety constants
    private static final int BUFFER_ROWS = 1;
    private static final int BUFFER_SEATS = 3;

    // Seating and reservation variables
    private String[][] seatingChart;
    private int remainingSeats;
    private LinkedHashMap<String, Integer> reservations;
    private LinkedHashMap<String, String> assignedSeats;

    public Theater(String path) {
        seatingChart = new String[MAX_ROWS][MAX_SEATS];
        // Mark all seats in the theater as empty
        for (String[] row : seatingChart)
            Arrays.fill(row, EMPTY);
        remainingSeats = MAX_ROWS * MAX_SEATS;

        loadReservations(path);
        assignedSeats = new LinkedHashMap<>();
        for (String ID : reservations.keySet())
            assignedSeats.put(ID, "");
    }

    private int remainingSeatsInRow(int row) {
        // Look for contiguous empty seats in the specified row
        int count = 0;
        int i = 0;
        // Wait until empty seat is found to start counting
        while (i < MAX_SEATS && !seatingChart[row][i].equals(EMPTY) ) {
            i++;
        }
        // If we reach the end of the row without finding any empty seats, the row has no contiguous empty seats
        if (i >= MAX_SEATS) return 0;
        // Count and return the number of contiguous empty seats
        while (i < MAX_SEATS && seatingChart[row][i].equals(EMPTY)) {
            count++;
            i++;
        }
        return count;
    }

    public void fillReservation(String ID, int numSeats) {
        // Start at the middle row, the row for the highest priority reservations
        int currRow = START_ROW;
        // From the middle row, work toward the back and front of the theatre looking for rows empty enough to accommodate
        // the entire reservation group
        if (numSeats <= MAX_SEATS) {
            int i = START_ROW, j = START_ROW;
            while (i >= 0 && j < MAX_ROWS) {
                if (remainingSeatsInRow(i) >= numSeats) {
                    currRow = i;
                    break;
                }
                if (remainingSeatsInRow(j) >= numSeats) {
                    currRow = j;
                    break;
                }
                i--;
                j++;
            }
        }
        // If no rows are empty enough, seats will be assigned as available
        int direction = 1;
        while (numSeats > 0) {
            // If we have run out of seats filling them towards the back of theater flip directions and fill seats towards
            // the front of the theater.
            if (currRow >= MAX_ROWS || currRow <= 0) {
                currRow = START_ROW;
                direction *= -1;
            }

            // Fill seats starting at the first available seat in the row and going til the last available seat, or until all
            // seats in the reservation are assigned
            for (int currSeat = 0; currSeat < MAX_SEATS; currSeat++) {
                if (seatingChart[currRow][currSeat].equals(EMPTY)) {
                    seatingChart[currRow][currSeat] = ID;
                    assignedSeats.put(ID, assignedSeats.get(ID) + (getCharForNumber(currRow + 1) + currSeat + ","));
                    numSeats--;
                    remainingSeats--;
                }
                if (numSeats <= 0) break;
            }

            // Move onto the next row, going either toward the front or back of the theater
            currRow += direction;
        }

        // Surround all newly assigned seats with buffer seats
        fillBufferSeats();
    }

    private void fillBufferSeats() {
        // Iterate through each seat - if the seat is assigned to an ID, and is surrounded with any neighboring unassigned
        // seats that are supposed to be buffer seats, assign them as buffer seats.
        for (int row = 0; row < MAX_ROWS; row++) {
            for (int seat = 0; seat < MAX_SEATS; seat++) {
                if (seatingChart[row][seat].startsWith("R")) {
                    for (int i = -BUFFER_ROWS; i <= BUFFER_ROWS; i++) {
                        for (int j = -BUFFER_SEATS; j <= BUFFER_SEATS; j++) {
                            if (row + i >= 0 && row + i < MAX_ROWS && seat + j >= 0 && seat + j < MAX_SEATS && seatingChart[row + i][seat + j].equals(EMPTY)) {
                                seatingChart[row + i][seat + j] = BUFFER;
                                remainingSeats--;
                            }
                        }
                    }
                }
            }
        }
    }

    private void loadReservations(String path) {
        try {
            reservations = InputParser.parseFile(path);
        } catch (IOException e) {
            System.out.println("Reservation file not found.");
            e.printStackTrace();
        }
    }

    public void fillTheatre() {
        // Iterate through each reservation and attempt to assign seats to the reservation if sufficient seats exist
        for (Map.Entry<String, Integer> reservation : reservations.entrySet()) {
            if (remainingSeats >= reservation.getValue()) {
                fillReservation(reservation.getKey(), reservation.getValue());
            }
        }
    }

    private String getCharForNumber(int i) {
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }

    public String generateOutputFile() throws IOException {
        String outputPath = System.getProperty("user.dir") + "/output.txt";
        // Combine each reservation ID with its string of assigned seats to put in the output file
        FileManager.writeFile(outputPath, assignedSeats.entrySet().stream()
                .map(entry -> entry.getKey() + " " + entry.getValue().replaceAll(",$", ""))
                .collect(Collectors.toList())
        );
        return outputPath;
    }

    public LinkedHashMap<String, String> getAssignedSeats() {
        return assignedSeats;
    }

    public void print() {
        int i = 1;
        System.out.println(" SCREEN \n");
        for (String[] row : seatingChart) {
            System.out.print("  " + getCharForNumber(i) + "   ");
            for (String seat : row) {
                System.out.printf("%4s,", seat);
            }
            System.out.println();
            i++;
        }
    }
}
