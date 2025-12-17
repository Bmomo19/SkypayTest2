package org.skypaytest;

import org.skypaytest.enums.RoomType;
import org.skypaytest.services.HotelReservationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("========================================");
            System.out.println("  HOTEL RESERVATION SYSTEM - TEST CASE");
            System.out.println("========================================\n");

            // Initialisation du service
            HotelReservationService hotelReservationService = new HotelReservationService();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // ============================================
            // ÉTAPE 1 : Création de 3 chambres
            // ============================================
            System.out.println("--- Creating Rooms ---");
            hotelReservationService.setRoom(1, RoomType.STANDARD_SUITE, 1000);
            System.out.println("✓ Room 1 created: Standard Suite @ 1000/night");

            hotelReservationService.setRoom(2, RoomType.JUNIOR_SUITE, 2000);
            System.out.println("✓ Room 2 created: Junior Suite @ 2000/night");

            hotelReservationService.setRoom(3, RoomType.MASTER_SUITE, 3000);
            System.out.println("✓ Room 3 created: Master Suite @ 3000/night");

            // ============================================
            // ÉTAPE 2 : Création de 2 utilisateurs
            // ============================================
            System.out.println("\n--- Creating Users ---");
            hotelReservationService.setUser(1, 5000);
            System.out.println("✓ User 1 created with balance: 5000");

            hotelReservationService.setUser(2, 10000);
            System.out.println("✓ User 2 created with balance: 10000");

            // ============================================
            // ÉTAPE 3 : Tentatives de réservation
            // ============================================
            System.out.println("\n--- Booking Attempts ---\n");

            // Test 1 : User 1 tente de réserver Room 2 du 30/06/2026 au 07/07/2026 (7 nuits)
            // Coût : 7 * 2000 = 14000, mais User 1 n'a que 5000 → ÉCHEC
            System.out.println("1. User 1 tries booking Room 2 from 30/06/2026 to 07/07/2026:");
            Date checkIn1 = sdf.parse("30/06/2026");
            Date checkOut1 = sdf.parse("07/07/2026");
            hotelReservationService.bookRoom(1, 2, checkIn1, checkOut1);

            // Test 2 : User 1 tente de réserver Room 2 du 07/07/2026 au 30/06/2026
            // Dates inversées → ÉCHEC
            System.out.println("\n2. User 1 tries booking Room 2 from 07/07/2026 to 30/06/2026:");
            Date checkIn2 = sdf.parse("07/07/2026");
            Date checkOut2 = sdf.parse("30/06/2026");
            hotelReservationService.bookRoom(1, 2, checkIn2, checkOut2);

            // Test 3 : User 1 tente de réserver Room 1 du 07/07/2026 au 08/07/2026 (1 nuit)
            // Coût : 1 * 1000 = 1000, User 1 a 5000 → SUCCÈS
            System.out.println("\n3. User 1 tries booking Room 1 from 07/07/2026 to 08/07/2026:");
            Date checkIn3 = sdf.parse("07/07/2026");
            Date checkOut3 = sdf.parse("08/07/2026");
            hotelReservationService.bookRoom(1, 1, checkIn3, checkOut3);

            // Test 4 : User 2 tente de réserver Room 1 du 07/07/2026 au 09/07/2026 (2 nuits)
            // Conflit avec la réservation précédente → ÉCHEC
            System.out.println("\n4. User 2 tries booking Room 1 from 07/07/2026 to 09/07/2026:");
            Date checkIn4 = sdf.parse("07/07/2026");
            Date checkOut4 = sdf.parse("09/07/2026");
            hotelReservationService.bookRoom(2, 1, checkIn4, checkOut4);

            // Test 5 : User 2 tente de réserver Room 3 du 07/07/2026 au 08/07/2026 (1 nuit)
            // Coût : 1 * 3000 = 3000, User 2 a 10000 → SUCCÈS
            System.out.println("\n5. User 2 tries booking Room 3 from 07/07/2026 to 08/07/2026:");
            Date checkIn5 = sdf.parse("07/07/2026");
            Date checkOut5 = sdf.parse("08/07/2026");
            hotelReservationService.bookRoom(2, 3, checkIn5, checkOut5);

            // ============================================
            // ÉTAPE 4 : Modification de la chambre 1
            // ============================================
            System.out.println("\n--- Modifying Room 1 ---");
            hotelReservationService.setRoom(1, RoomType.MASTER_SUITE, 10000);
            System.out.println("✓ Room 1 modified: Master Suite @ 10000/night");
            System.out.println("  (Previous bookings should NOT be affected)\n");

            // ============================================
            // ÉTAPE 5 : Affichage des résultats
            // ============================================
            System.out.println("\n========================================");
            System.out.println("           FINAL RESULTS");
            System.out.println("========================================");

            hotelReservationService.printAll();
            hotelReservationService.printAllUsers();

        } catch (ParseException e) {
            System.err.println("Erreur de parsing de date : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
