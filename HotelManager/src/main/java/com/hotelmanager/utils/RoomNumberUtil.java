package com.hotelmanager.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoomNumberUtil {

    public static List<Integer> findNextAvailableRoomNumbers(Set<Integer> existingNumbers, int startNumber, int quantity) {
        Set<Integer> availableNumbers = new HashSet<>();
        int currentNumber = startNumber;

        while (availableNumbers.size() < quantity) {
            if (!existingNumbers.contains(currentNumber)) {
                availableNumbers.add(currentNumber);
            }
            currentNumber++;
        }

        return availableNumbers.stream().sorted().collect(Collectors.toList());
    }
}
