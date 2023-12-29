package org.example;

public class AirportSecurity implements ParkingLotObserver {
    @Override
    public void notifyFull() {
        System.out.println("Parking lot is full. Redirecting security staff.");
        // Additional actions for security staff redirection can be added here
    }
    @Override
    public void notifyEmpty() {
        System.out.println("Parking lot has space again. Owner notified.");
        // Additional actions for the owner when the parking lot has space
    }
}