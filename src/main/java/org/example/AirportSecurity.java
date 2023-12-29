package org.example;

public class AirportSecurity implements ParkingLotObserver {
    @Override
    public void notifyFull() {
        System.out.println("Parking lot is full. Redirecting security staff.");

    }
    @Override
    public void notifyEmpty() {
        System.out.println("Parking lot has space again. Owner notified.");

    }
}