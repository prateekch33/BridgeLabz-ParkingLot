package org.example;

public class ParkingLotOwner implements ParkingLotObserver {
    @Override
    public void notifyFull() {
        System.out.println("Parking lot is full. Owner notified.");
        // Additional actions for the owner when the parking lot is full
    }

    @Override
    public void notifyEmpty() {
        System.out.println("Parking lot has space again. Owner notified.");
        // Additional actions for the owner when the parking lot has space
    }
}

