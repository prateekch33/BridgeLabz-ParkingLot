package org.example;

import java.util.Comparator;
import java.util.List;

public class ParkingAttendant {

    public boolean parkCar(ParkingLot parkingLot, String carNumber, String driverName) {

        return parkingLot.parkCar(carNumber, driverName);
    }

    private static int lastLotIndex = -1;

    public static boolean parkCar(List<ParkingLot> parkingLots, String carNumber, String driverName) {
        if (parkingLots.isEmpty()) {
            return false;
        }


        lastLotIndex = (lastLotIndex + 1) % parkingLots.size();
        ParkingLot chosenLot = parkingLots.get(lastLotIndex);

        return chosenLot.parkCar(carNumber, driverName);
    }
//    private static int lastLotIndex = -1;

    public static boolean parkCar(List<ParkingLot> parkingLots, String carNumber, String driverName, boolean isLarge) {
        if (parkingLots.isEmpty()) {
            return false;
        }


        ParkingLot chosenLot = roundRobinLot(parkingLots);

        return chosenLot.parkCar(carNumber, driverName);
    }

    private static ParkingLot roundRobinLot(List<ParkingLot> parkingLots) {
        lastLotIndex = (lastLotIndex + 1) % parkingLots.size();
        return parkingLots.get(lastLotIndex);
    }
}