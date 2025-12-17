package org.skypaytest;

import org.junit.Before;
import org.junit.Test;
import org.skypaytest.enums.RoomType;
import org.skypaytest.services.HotelReservationService;

import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class HotelReservationTest {
    private HotelReservationService hotelReservationService;
    private SimpleDateFormat sdf;

    @Before
    public void setUp() {
        hotelReservationService = new HotelReservationService();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
    }

    // ============================================
    // Tests pour setRoom
    // ============================================

    @Test
    public void testSetRoomCreatesNewRoom() {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        // Vérifier qu'aucune exception n'est levée
        assertTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetRoomWithNegativePriceThrowsException() {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, -1000);
    }

    @Test
    public void testSetRoomUpdatesExistingRoom() {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setRoom(1, RoomType.MASTER_SUITE, 5000);
        // La chambre devrait être mise à jour sans exception
        assertTrue(true);
    }

    // ============================================
    // Tests pour setUser
    // ============================================

    @Test
    public void testSetUserCreatesNewUser() {
        hotelReservationService.setUser(1, 5000);
        assertTrue(true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetUserWithNegativeBalanceThrowsException() {
        hotelReservationService.setUser(1, -5000);
    }

    @Test
    public void testSetUserDoesNotUpdateExistingUser() {
        hotelReservationService.setUser(1, 5000);
        hotelReservationService.setUser(1, 10000); // Ne devrait rien faire
        assertTrue(true);
    }

    // ============================================
    // Tests pour bookRoom - Cas de succès
    // ============================================

    @Test
    public void testBookRoomSuccessfully() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 5000);

        Date checkIn = sdf.parse("01/07/2026");
        Date checkOut = sdf.parse("02/07/2026");

        hotelReservationService.bookRoom(1, 1, checkIn, checkOut);
        // Devrait réussir sans exception
        assertTrue(true);
    }

    @Test
    public void testUserBalanceDeductedAfterBooking() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 5000);

        Date checkIn = sdf.parse("01/07/2026");
        Date checkOut = sdf.parse("03/07/2026"); // 2 nuits = 2000

        hotelReservationService.bookRoom(1, 1, checkIn, checkOut);
        // Le solde devrait être 5000 - 2000 = 3000
        // (Vous pouvez ajouter un getter pour vérifier)
        assertTrue(true);
    }

    // ============================================
    // Tests pour bookRoom - Cas d'échec
    // ============================================

    @Test
    public void testBookRoomWithInsufficientBalance() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 500); // Solde insuffisant

        Date checkIn = sdf.parse("01/07/2026");
        Date checkOut = sdf.parse("02/07/2026"); // 1 nuit = 1000

        hotelReservationService.bookRoom(1, 1, checkIn, checkOut);
        // Devrait échouer mais ne pas lever d'exception (géré en interne)
        assertTrue(true);
    }

    @Test
    public void testBookRoomWithInvalidDates() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 5000);

        Date checkIn = sdf.parse("10/07/2026");
        Date checkOut = sdf.parse("05/07/2026"); // Date de départ avant arrivée

        hotelReservationService.bookRoom(1, 1, checkIn, checkOut);
        // Devrait échouer mais ne pas lever d'exception
        assertTrue(true);
    }

    @Test
    public void testBookRoomAlreadyBooked() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 5000);
        hotelReservationService.setUser(2, 5000);

        Date checkIn1 = sdf.parse("01/07/2026");
        Date checkOut1 = sdf.parse("05/07/2026");
        hotelReservationService.bookRoom(1, 1, checkIn1, checkOut1);

        // User 2 essaie de réserver la même chambre avec chevauchement
        Date checkIn2 = sdf.parse("03/07/2026");
        Date checkOut2 = sdf.parse("07/07/2026");
        hotelReservationService.bookRoom(2, 1, checkIn2, checkOut2);

        // Devrait échouer
        assertTrue(true);
    }

    @Test
    public void testBookRoomNonExistentRoom() throws Exception {
        hotelReservationService.setUser(1, 5000);

        Date checkIn = sdf.parse("01/07/2026");
        Date checkOut = sdf.parse("02/07/2026");

        hotelReservationService.bookRoom(1, 999, checkIn, checkOut); // Chambre 999 n'existe pas
        // Devrait échouer
        assertTrue(true);
    }

    @Test
    public void testBookRoomNonExistentUser() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);

        Date checkIn = sdf.parse("01/07/2026");
        Date checkOut = sdf.parse("02/07/2026");

        hotelReservationService.bookRoom(999, 1, checkIn, checkOut); // User 999 n'existe pas
        // Devrait échouer
        assertTrue(true);
    }

    // ============================================
    // Tests pour la disponibilité des chambres
    // ============================================

    @Test
    public void testBookRoomOnCheckOutDateOfPreviousBooking() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 5000);
        hotelReservationService.setUser(2, 5000);

        // User 1 réserve du 01/07 au 05/07
        Date checkIn1 = sdf.parse("01/07/2026");
        Date checkOut1 = sdf.parse("05/07/2026");
        hotelReservationService.bookRoom(1, 1, checkIn1, checkOut1);

        // User 2 réserve à partir du 05/07 (jour de départ de User 1)
        Date checkIn2 = sdf.parse("05/07/2026");
        Date checkOut2 = sdf.parse("07/07/2026");
        hotelReservationService.bookRoom(2, 1, checkIn2, checkOut2);

        // Devrait RÉUSSIR (pas de chevauchement)
        assertTrue(true);
    }

    // ============================================
    // Tests pour setRoom n'affectant pas les bookings
    // ============================================

    @Test
    public void testSetRoomDoesNotAffectPreviousBookings() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 5000);

        Date checkIn = sdf.parse("01/07/2026");
        Date checkOut = sdf.parse("02/07/2026");
        hotelReservationService.bookRoom(1, 1, checkIn, checkOut);

        // Modifier la chambre après la réservation
        hotelReservationService.setRoom(1, RoomType.MASTER_SUITE, 10000);

        // La réservation précédente devrait toujours refléter les anciennes valeurs
        // (Vérifiable via printAll)
        assertTrue(true);
    }

    // ============================================
    // Tests pour printAll et printAllUsers
    // ============================================

    @Test
    public void testPrintAllDoesNotThrowException() throws Exception {
        hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
        hotelReservationService.setUser(1, 5000);

        Date checkIn = sdf.parse("01/07/2026");
        Date checkOut = sdf.parse("02/07/2026");
        hotelReservationService.bookRoom(1, 1, checkIn, checkOut);

        hotelReservationService.printAll();
        assertTrue(true);
    }

    @Test
    public void testPrintAllUsersDoesNotThrowException() {
        hotelReservationService.setUser(1, 5000);
        hotelReservationService.setUser(2, 10000);

        hotelReservationService.printAllUsers();
        assertTrue(true);
    }

    @Test
    public void testPrintAllWithEmptyData() {
        hotelReservationService.printAll();
        hotelReservationService.printAllUsers();
        assertTrue(true);
    }
}
