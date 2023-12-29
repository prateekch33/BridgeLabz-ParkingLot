

import org.example.AirportSecurity;
import org.example.ParkingAttendant;
import org.example.ParkingLot;
import org.example.ParkingLotOwner;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    @Test
    public void testParkCarSuccess() {
        ParkingLot parkingLot = new ParkingLot(100);
        assertTrue(parkingLot.parkCar("ABC123", "John Doe"));
    }

    @Test
    public void testParkCarFailureWhenFull() {
        ParkingLot parkingLot = new ParkingLot(1);
        parkingLot.parkCar("ABC123", "John Doe");
        assertFalse(parkingLot.parkCar("XYZ789", "Jane Doe"));
    }

    @Test
    public void testGetParkedCars() {
        ParkingLot parkingLot = new ParkingLot(2);
        parkingLot.parkCar("ABC123", "John Doe");
        parkingLot.parkCar("XYZ789", "Jane Doe");
        Map<String, String> expected = new HashMap<>();
        expected.put("ABC123", "John Doe");
        expected.put("XYZ789", "Jane Doe");
        assertEquals(expected, parkingLot.getParkedCars());
    }

    @Test
    public void testGetAvailableSpots() {
        ParkingLot parkingLot = new ParkingLot(3);
        parkingLot.parkCar("ABC123", "John Doe");
        assertEquals(2, parkingLot.getAvailableSpots());
    }

    @Test
    public void testUnparkCarSuccess() {
        ParkingLot parkingLot = new ParkingLot(2);
        parkingLot.parkCar("ABC123", "John Doe");
        assertTrue(parkingLot.unparkCar("ABC123"));
        assertEquals(2, parkingLot.getAvailableSpots());
    }

    @Test
    public void testUnparkCarFailureWhenNotFound() {
        ParkingLot parkingLot = new ParkingLot(2);
        parkingLot.parkCar("ABC123", "John Doe");
        assertFalse(parkingLot.unparkCar("XYZ789"));
        assertEquals(1, parkingLot.getAvailableSpots());
    }

    @Test
    public void testUnparkCarFailureWhenAlreadyUnparked() {
        ParkingLot parkingLot = new ParkingLot(2);
        assertFalse(parkingLot.unparkCar("ABC123"));
        assertEquals(2, parkingLot.getAvailableSpots());
    }

    @Test
    public void testIsFull() {
        ParkingLot parkingLot = new ParkingLot(2);
        assertFalse(parkingLot.isFull());

        parkingLot.parkCar("ABC123", "John Doe");
        assertFalse(parkingLot.isFull());

        parkingLot.parkCar("XYZ789", "Jane Doe");
        assertTrue(parkingLot.isFull());
    }

    @Test
    public void testNotifySecurityWhenFull() {
        ParkingLot parkingLot = new ParkingLot(2);
        AirportSecurity security = new AirportSecurity();
        parkingLot.registerFullObserver(security);

        parkingLot.parkCar("ABC123", "John Doe");
        assertFalse(parkingLot.isFull());

        parkingLot.parkCar("XYZ789", "Jane Doe");
        assertTrue(parkingLot.isFull());
    }

    @Test
    public void testNotifyOwnerWhenEmpty() {
        ParkingLot parkingLot = new ParkingLot(2);
        ParkingLotOwner owner = new ParkingLotOwner();
        parkingLot.registerEmptyObserver(owner);

        parkingLot.parkCar("ABC123", "John Doe");
        assertFalse(parkingLot.isFull()); // Not full yet

        parkingLot.unparkCar("ABC123");
        assertTrue(parkingLot.getAvailableSpots() == 2); // Now it has space, should notify owner
    }

    @Test
    public void testParkingAttendant() {
        ParkingLot parkingLot = new ParkingLot(2);
        ParkingAttendant attendant = new ParkingAttendant();

        assertTrue(attendant.parkCar(parkingLot, "ABC123", "John Doe"));
        assertFalse(parkingLot.isFull()); // Not full yet

        assertTrue(attendant.parkCar(parkingLot, "XYZ789", "Jane Doe"));
        assertTrue(parkingLot.isFull()); // Now it's full
    }

    @Test
    public void testFindCar() {
        ParkingLot parkingLot = new ParkingLot(2);
        ParkingAttendant attendant = new ParkingAttendant();

        attendant.parkCar(parkingLot, "ABC123", "John Doe");
        attendant.parkCar(parkingLot, "XYZ789", "Jane Doe");

        assertEquals("John Doe", parkingLot.findCar("ABC123"));
        assertEquals("Jane Doe", parkingLot.findCar("XYZ789"));
        assertNull(parkingLot.findCar("123XYZ")); // Car not found
    }

    @Test
    public void testParkingTimestamps() {
        ParkingLot parkingLot = new ParkingLot(2);
        ParkingAttendant attendant = new ParkingAttendant();

        attendant.parkCar(parkingLot, "ABC123", "John Doe");
        attendant.parkCar(parkingLot, "XYZ789", "Jane Doe");

        assertNotNull(parkingLot.getParkingTimestamps().get("ABC123"));
        assertNotNull(parkingLot.getParkingTimestamps().get("XYZ789"));
        assertNull(parkingLot.getParkingTimestamps().get("123XYZ")); // Car not found
    }

    @Test
    public void testEvenDistribution() {
        List<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(new ParkingLot(2));
        parkingLots.add(new ParkingLot(2));

        ParkingAttendant attendant = new ParkingAttendant();

        assertTrue(attendant.parkCar(parkingLots, "ABC123", "John Doe"));
        assertTrue(attendant.parkCar(parkingLots, "XYZ789", "Jane Doe"));

        assertEquals(1, parkingLots.get(0).getAvailableSpots());
        assertEquals(1, parkingLots.get(1).getAvailableSpots());
    }

    @Test
    public void testParkLargeCarToLotWithMostFreeSpaces() {
        List<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(new ParkingLot(2));
        parkingLots.add(new ParkingLot(3));

        ParkingAttendant attendant = new ParkingAttendant();

        // Park cars to fill the first lot
        attendant.parkCar(parkingLots, "ABC123", "John Doe", false);
        attendant.parkCar(parkingLots, "XYZ789", "Jane Doe", false);

        // Park large car, should go to the second lot with more free spaces
        assertTrue(attendant.parkCar(parkingLots, "DEF456", "Large Driver", true));

        // Check the number of available spots in each lot
        assertEquals(0, parkingLots.get(0).getAvailableSpots()); // First lot is full
        assertEquals(2, parkingLots.get(1).getAvailableSpots()); // Second lot has 2 free spaces
    }

    @Test
    public void testParkSmallCarWithRoundRobin() {
        List<ParkingLot> parkingLots = new ArrayList<>();
        parkingLots.add(new ParkingLot(2));
        parkingLots.add(new ParkingLot(3));

        ParkingAttendant attendant = new ParkingAttendant();

        // Park cars using the round-robin strategy
        attendant.parkCar(parkingLots, "ABC123", "John Doe", false);
        attendant.parkCar(parkingLots, "XYZ789", "Jane Doe", false);

        // Park small car, should go to the first lot (round-robin)
        assertTrue(attendant.parkCar(parkingLots, "DEF456", "Small Driver", false));

        // Check the number of available spots in each lot
        assertEquals(0, parkingLots.get(0).getAvailableSpots()); // First lot is full
        assertEquals(2, parkingLots.get(1).getAvailableSpots()); // Second lot has 2 free spaces
    }
    @Test
    public void testGetLocationOfParkedWhiteCars() {
        ParkingLot parkingLot = new ParkingLot(3);

        // Park cars with different colors
        parkingLot.parkCar("ABC123", "John Doe", "blue", "Toyota", "A1", "Plate123", "Attendant1");
        parkingLot.parkCar("XYZ789", "Jane Doe", "black", "Toyota", "A2", "Plate456", "Attendant2");
        parkingLot.parkCar("DEF456", "Bob Smith", "blue", "Toyota", "B1", "Plate789", "Attendant3");

        // Retrieve the location of parked white cars
        Map<String, String> whiteCarsLocations = parkingLot.getLocationOfParkedWhiteCars();

        // Check the locations
        assertEquals(2, whiteCarsLocations.size());
        assertEquals("A1", whiteCarsLocations.get("ABC123"));
        assertEquals("B1", whiteCarsLocations.get("DEF456"));
        assertNull(whiteCarsLocations.get("XYZ789")); // Black car should not be included
    }

    @Test
    public void testGetInfoOfParkedBlueToyotaCars() {
        ParkingLot parkingLot = new ParkingLot(3);

        // Park cars with different colors and makes
        parkingLot.parkCar("ABC123", "John Doe", "blue", "Toyota", "A1", "Plate123", "Attendant1");
        parkingLot.parkCar("XYZ789", "Jane Doe", "black", "Toyota", "A2", "Plate456", "Attendant2");
        parkingLot.parkCar("DEF456", "Bob Smith", "blue", "Toyota", "B1", "Plate789", "Attendant3");

        // Retrieve information about parked blue Toyota cars
        Map<String, ParkingLot.CarInfo> blueToyotaCarsInfo = parkingLot.getInfoOfParkedBlueToyotaCars();

        // Check the information
        assertEquals(2, blueToyotaCarsInfo.size());

        ParkingLot.CarInfo carInfoABC123 = blueToyotaCarsInfo.get("ABC123");
        assertNotNull(carInfoABC123);
        assertEquals("A1", carInfoABC123.getLocation());
        assertEquals("Plate123", carInfoABC123.getPlateNumber());
        assertEquals("Attendant1", carInfoABC123.getAttendantName());

        ParkingLot.CarInfo carInfoDEF456 = blueToyotaCarsInfo.get("DEF456");
        assertNotNull(carInfoDEF456);
        assertEquals("B1", carInfoDEF456.getLocation());
        assertEquals("Plate789", carInfoDEF456.getPlateNumber());
        assertEquals("Attendant3", carInfoDEF456.getAttendantName());

        assertNull(blueToyotaCarsInfo.get("XYZ789")); // Black car should not be included
    }

    @Test
    public void testGetInfoOfParkedBMW() {
        ParkingLot parkingLot = new ParkingLot(3);

        // Park cars with different makes
        parkingLot.parkCar("ABC123", "John Doe", "blue", "BMW", "A1", "Plate123", "Attendant1");
        parkingLot.parkCar("XYZ789", "Jane Doe", "black", "Toyota", "A2", "Plate456", "Attendant2");
        parkingLot.parkCar("DEF456", "Bob Smith", "white", "BMW", "B1", "Plate789", "Attendant3");

        // Retrieve information about parked BMW cars
        Map<String, ParkingLot.CarInfo> bmwCarsInfo = parkingLot.getInfoOfParkedBMW();

        // Check the information
        assertEquals(2, bmwCarsInfo.size());

        ParkingLot.CarInfo carInfoABC123 = bmwCarsInfo.get("ABC123");
        assertNotNull(carInfoABC123);
        assertEquals("A1", carInfoABC123.getLocation());
        assertEquals("Plate123", carInfoABC123.getPlateNumber());
        assertEquals("Attendant1", carInfoABC123.getAttendantName());

        ParkingLot.CarInfo carInfoDEF456 = bmwCarsInfo.get("DEF456");
        assertNotNull(carInfoDEF456);
        assertEquals("B1", carInfoDEF456.getLocation());
        assertEquals("Plate789", carInfoDEF456.getPlateNumber());
        assertEquals("Attendant3", carInfoDEF456.getAttendantName());

        assertNull(bmwCarsInfo.get("XYZ789")); // Toyota car should not be included
    }

    @Test
    public void testGetInfoOfCarsParkedLast30Minutes() {
        ParkingLot parkingLot = new ParkingLot(3);

        // Park cars with different timestamps
        long currentTime = System.currentTimeMillis();
        parkingLot.parkCar("ABC123", "John Doe", "blue", "BMW", "A1", "Plate123", "Attendant1");
        parkingLot.parkingTimestamps.put("ABC123", currentTime - 20 * 60 * 1000); // Parked 20 minutes ago

        parkingLot.parkCar("XYZ789", "Jane Doe", "black", "Toyota", "A2", "Plate456", "Attendant2");
        parkingLot.parkingTimestamps.put("XYZ789", currentTime - 40 * 60 * 1000); // Parked 40 minutes ago

        parkingLot.parkCar("DEF456", "Bob Smith", "white", "BMW", "B1", "Plate789", "Attendant3");
        parkingLot.parkingTimestamps.put("DEF456", currentTime - 10 * 60 * 1000); // Parked 10 minutes ago

        // Retrieve information about cars parked in the last 30 minutes
        Map<String, ParkingLot.CarInfo> carsParkedLast30Minutes = parkingLot.getInfoOfCarsParkedLast30Minutes();

        // Check the information
        assertEquals(2, carsParkedLast30Minutes.size());

        ParkingLot.CarInfo carInfoABC123 = carsParkedLast30Minutes.get("ABC123");
        assertNotNull(carInfoABC123);
        assertEquals("A1", carInfoABC123.getLocation());

        ParkingLot.CarInfo carInfoDEF456 = carsParkedLast30Minutes.get("DEF456");
        assertNotNull(carInfoDEF456);
        assertEquals("B1", carInfoDEF456.getLocation());

        assertNull(carsParkedLast30Minutes.get("XYZ789")); // Parked 40 minutes ago
    }
}
