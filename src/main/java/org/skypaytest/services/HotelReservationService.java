package org.skypaytest.services;

import org.skypaytest.entities.Booking;
import org.skypaytest.entities.Room;
import org.skypaytest.entities.User;
import org.skypaytest.enums.RoomType;

import java.util.ArrayList;
import java.util.Date;

public class HotelReservationService {
    private final ArrayList<Room> rooms;
    private final ArrayList<User> users;
    private final ArrayList<Booking> bookings;
    private int nextBookingId;

    public HotelReservationService() {
        this.rooms = new ArrayList<>();
        this.users = new ArrayList<>();
        this.bookings = new ArrayList<>();
        this.nextBookingId = 1;
    }

    public void setRoom(int roomNumber, RoomType roomType, int roomPricePerNight) {
        Room existingRoom = findRoomByNumber(roomNumber);

        if (existingRoom != null) {
            // Mise à jour de la chambre existante (n'affecte pas les bookings passés)
            existingRoom.setRoomType(roomType);
            existingRoom.setPricePerNight(roomPricePerNight);
        } else {
            // Création d'une nouvelle chambre
            rooms.add(new Room(roomNumber, roomType, roomPricePerNight));
        }
    }

    public void setUser(int userId, int balance) {
        User existingUser = findUserById(userId);

        if (existingUser == null) {
            users.add(new User(userId, balance));
        }
    }

    public void bookRoom(int userId, int roomNumber, Date checkIn, Date checkOut) {
        try {
            // Validation des dates
            if (checkOut.before(checkIn) || checkOut.equals(checkIn)) {
                throw new IllegalArgumentException("La date de départ doit être après la date d'arrivée");
            }

            // Trouver l'utilisateur et la chambre
            User user = findUserById(userId);
            Room room = findRoomByNumber(roomNumber);

            if (user == null) {
                throw new IllegalArgumentException("Utilisateur introuvable : " + userId);
            }
            if (room == null) {
                throw new IllegalArgumentException("Chambre introuvable : " + roomNumber);
            }

            // Vérifier la disponibilité de la chambre
            if (!isRoomAvailable(roomNumber, checkIn, checkOut)) {
                throw new IllegalArgumentException("La chambre n'est pas disponible pour cette période");
            }

            // Calculer le coût total
            long nights = (checkOut.getTime() - checkIn.getTime()) / (1000 * 60 * 60 * 24);
            int totalCost = (int) (nights * room.getPricePerNight());

            // Vérifier le solde
            if (user.getBalance() < totalCost) {
                throw new IllegalArgumentException("Solde insuffisant. Coût: " + totalCost + ", Solde: " + user.getBalance());
            }

            // Créer la réservation avec snapshot des données
            Booking booking = new Booking(nextBookingId++, userId, roomNumber,
                    checkIn, checkOut, totalCost,
                    room.getRoomType(), room.getPricePerNight(), user.getBalance());

            // Débiter l'utilisateur
            user.deductBalance(totalCost);

            // Ajouter la réservation
            bookings.add(booking);

            System.out.println("✓ Réservation effectuée avec succès : " + booking);

        } catch (IllegalArgumentException e) {
            System.out.println("✗ Échec de la réservation : " + e.getMessage());
        }
    }

    private boolean isRoomAvailable(int roomNumber, Date checkIn, Date checkOut) {
        for (Booking booking : bookings) {
            if (booking.getRoomNumber() == roomNumber) {
                if (booking.overlaps(checkIn, checkOut)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Room findRoomByNumber(int roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }

    private User findUserById(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }
        return null;
    }

    public void printAll() {
        System.out.println("\n========== ALL ROOMS ==========");
        // Du plus récent au plus ancien
        for (int i = rooms.size() - 1; i >= 0; i--) {
            System.out.println(rooms.get(i));
        }

        System.out.println("\n========== ALL BOOKINGS ==========");
        // Du plus récent au plus ancien
        for (int i = bookings.size() - 1; i >= 0; i--) {
            System.out.println(bookings.get(i));
        }
    }

    public void printAllUsers() {
        System.out.println("\n========== ALL USERS ==========");
        // Du plus récent au plus ancien
        for (int i = users.size() - 1; i >= 0; i--) {
            System.out.println(users.get(i));
        }
    }
}
