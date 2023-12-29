package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ParkingLot {

    private int capacity;
    private int availableSpots;
    private Map<String, String> parkedCars;
    private Map<String, Car> parkedCarsWithColor;
    public Map<String, Long> parkingTimestamps;
    private List<ParkingLotObserver> fullObservers;
    private List<ParkingLotObserver> emptyObservers;

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.availableSpots = capacity;
        this.parkedCars = new HashMap<>();
        this.parkedCarsWithColor=new HashMap<>();
        this.parkingTimestamps = new HashMap<>();
        this.fullObservers = new ArrayList<>();
        this.emptyObservers = new ArrayList<>();
    }

    public void registerFullObserver(ParkingLotObserver observer) {
        fullObservers.add(observer);
    }

    public void registerEmptyObserver(ParkingLotObserver observer) {
        emptyObservers.add(observer);
    }

    public boolean parkCar(String carNumber, String driverName) {
        if (availableSpots > 0) {
            parkedCars.put(carNumber, driverName);
            parkingTimestamps.put(carNumber, System.currentTimeMillis());
            availableSpots--;
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public boolean parkCar(String carNumber, String driverName, String color, String make, String location, String plateNumber, String attendantName) {
        if (availableSpots > 0) {
            parkedCarsWithColor.put(carNumber, new Car(carNumber,driverName,color,make,location,plateNumber,attendantName));
            parkingTimestamps.put(carNumber, System.currentTimeMillis());
            availableSpots--;
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public boolean unparkCar(String carNumber) {
        if (parkedCars.containsKey(carNumber)) {
            parkedCars.remove(carNumber);
            parkingTimestamps.remove(carNumber);
            availableSpots++;
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    public boolean isFull() {
        return availableSpots == 0;
    }

    public Map<String, String> getParkedCars() {
        return new HashMap<>(parkedCars);
    }

    public Map<String, Long> getParkingTimestamps() {
        return new HashMap<>(parkingTimestamps);
    }

    public int getAvailableSpots() {
        return availableSpots;
    }

    private void notifyObservers() {
        if (isFull()) {
            for (ParkingLotObserver observer : fullObservers) {
                observer.notifyFull();
            }
        } else if (availableSpots == capacity - 1) {
            for (ParkingLotObserver observer : emptyObservers) {
                observer.notifyEmpty();
            }
        }
    }
    public String findCar(String carNumber) {
        return parkedCars.get(carNumber);
    }

    public Map<String, String> getLocationOfParkedWhiteCars() {
        return parkedCarsWithColor.entrySet().stream()
                .filter(entry -> entry.getValue().getColor().equalsIgnoreCase("white"))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getLocation()));
    }

    private static class Car {
        private String color;
        private String make;
        private String location;
        private String plateNumber;
        private String attendantName;
        private String carNumber;
        private String driverName;

        public Car(String carNumber, String driverName, String color, String make, String location, String plateNumber, String attendantName) {
            this.color = color;
            this.make = make;
            this.location = location;
            this.plateNumber = plateNumber;
            this.attendantName = attendantName;
            this.carNumber=carNumber;
            this.driverName=driverName;
        }

        public String getColor() {
            return color;
        }

        public String getMake() {
            return make;
        }

        public String getLocation() {
            return location;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public String getAttendantName() {
            return attendantName;
        }
    }

    public Map<String, CarInfo> getInfoOfParkedBlueToyotaCars() {
        return parkedCarsWithColor.entrySet().stream()
                .filter(entry -> entry.getValue().getColor().equalsIgnoreCase("blue") &&
                        entry.getValue().getMake().equalsIgnoreCase("Toyota"))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new CarInfo(
                        entry.getValue().getLocation(),
                        entry.getValue().getPlateNumber(),
                        entry.getValue().getAttendantName()
                )));
    }

    public static class CarInfo {
        private String location;
        private String plateNumber;
        private String attendantName;

        public CarInfo(String location, String plateNumber, String attendantName) {
            this.location = location;
            this.plateNumber = plateNumber;
            this.attendantName = attendantName;
        }

        public String getLocation() {
            return location;
        }

        public String getPlateNumber() {
            return plateNumber;
        }

        public String getAttendantName() {
            return attendantName;
        }
    }

    public Map<String, CarInfo> getInfoOfParkedBMW() {
        return parkedCarsWithColor.entrySet().stream()
                .filter(entry -> entry.getValue().getMake().equalsIgnoreCase("BMW"))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new CarInfo(
                        entry.getValue().getLocation(),
                        entry.getValue().getPlateNumber(),
                        entry.getValue().getAttendantName()
                )));
    }
    public Map<String, CarInfo> getInfoOfCarsParkedLast30Minutes() {
        long currentTimestamp = System.currentTimeMillis();

        return parkedCarsWithColor.entrySet().stream()
                .filter(entry -> currentTimestamp - parkingTimestamps.get(entry.getKey()) <= 30 * 60 * 1000)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> new CarInfo(
                        entry.getValue().getLocation(),
                        entry.getValue().getPlateNumber(),
                        entry.getValue().getAttendantName()
                )));
    }
}


