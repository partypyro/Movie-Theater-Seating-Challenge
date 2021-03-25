package main.java.utils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

public class InputParser {
    public static LinkedHashMap<String, Integer> parseInput(List<String> reservations) {
        // Use a linked hash map to preserve the ordering of reservations while also maintaining responsive lookups using
        // the reservation identifier.
        // Being a linked list, this data structure also has the advantage of maintaining order for insertions and
        // deletions, should those need to become features for customers in the future.
        LinkedHashMap<String, Integer> parsedReservations = new LinkedHashMap<>();
        reservations.forEach(
            // Split each input row into the reservation identifier and the number of seats, and add it to
            // the linked hash map
            reservation -> {
                String[] split = reservation.split(" ");
                try {
                    parsedReservations.put(split[0], Integer.valueOf(split[1]));
                } catch (IndexOutOfBoundsException e) {

                } catch (NumberFormatException e) {
                    System.out.println("Non-numeric number of seats requested for reservation ID " + split[0] + ": ");
                    e.printStackTrace();
                }
            }
        );
        return parsedReservations;
    }

    public static LinkedHashMap<String, Integer> parseFile(String path) throws IOException {
        return parseInput(FileManager.loadFile(path));
    }
}
