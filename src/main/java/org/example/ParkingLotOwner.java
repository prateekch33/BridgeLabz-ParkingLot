package org.example;

public class ParkingLotOwner implements ParkingLotObserver {
    @Override
    public void notifyFull() {
        System.out.println("Parking lot is full. Owner notified.");

    }

    @Override
    public void notifyEmpty() {
        System.out.println("Parking lot has space again. Owner notified.");

    }
}

