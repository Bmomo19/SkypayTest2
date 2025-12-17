package org.skypaytest.entities;

import org.skypaytest.enums.RoomType;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Booking {
    private final int bookingId;
    private final int userId;
    private final int roomNumber;
    private final Date checkIn;
    private final Date checkOut;
    private final int totalCost;
    private final Date bookingDate;

    private final RoomType roomTypeAtBooking;
    private final int pricePerNightAtBooking;
    private final int userBalanceBeforeBooking;

    public Booking(int bookingId, int userId, int roomNumber, Date checkIn, Date checkOut, int totalCost, RoomType roomTypeAtBooking, int pricePerNightAtBooking, int userBalanceBeforeBooking) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.roomNumber = roomNumber;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalCost = totalCost;
        this.bookingDate = new Date();

        this.roomTypeAtBooking = roomTypeAtBooking;
        this.pricePerNightAtBooking = pricePerNightAtBooking;
        this.userBalanceBeforeBooking = userBalanceBeforeBooking;
    }

    public int getBookingId() { return bookingId; }
    public int getUserId() { return userId; }
    public int getRoomNumber() { return roomNumber;}
    public Date getCheckIn() { return checkIn;}
    public Date getCheckOut() { return checkOut;}
    public int getTotalCost() { return totalCost; }
    public Date getBookingDate() { return bookingDate;}
    public RoomType getRoomTypeAtBooking() { return roomTypeAtBooking;}
    public int getPricePerNightAtBooking() { return pricePerNightAtBooking;}
    public int getUserBalanceBeforeBooking() { return userBalanceBeforeBooking;}

    public long getNights() {
        long diffInMillies = checkOut.getTime() -  checkIn.getTime();
        return diffInMillies / (1000*60*60*24);
    }

    // Vérifie si cette réservation chevauche une période donnée
    public boolean overlaps(Date newCheckIn, Date newCheckOut) {
        return !(newCheckOut.before(checkIn) || newCheckOut.equals(checkIn) ||
                newCheckIn.after(checkOut) || newCheckIn.equals(checkOut));
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("Booking #%d - User #%d - Room #%d (%s @ %d/night) - %s to %s - %d nights - Total: %d",
                bookingId, userId, roomNumber, roomTypeAtBooking, pricePerNightAtBooking,
                sdf.format(checkIn), sdf.format(checkOut), getNights(), totalCost);
    }

}
