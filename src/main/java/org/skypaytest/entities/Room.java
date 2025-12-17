package org.skypaytest.entities;

import org.skypaytest.enums.RoomType;

public class Room {
    private int roomNumber;
    private RoomType roomType;
    private int pricePerNight;

    public Room(int roomNumber, RoomType roomType, int pricePerNight) {
        if(pricePerNight < 0){
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");
        }
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.pricePerNight = pricePerNight;
    }

    public int getRoomNumber() {return roomNumber;}
    public RoomType getRoomType() {return roomType;}
    public int getPricePerNight() {return pricePerNight;}

    public void setRoomNumber(int roomNumber) {this.roomNumber = roomNumber;}
    public void setRoomType(RoomType roomType) {this.roomType = roomType;}
    public void setPricePerNight(int pricePerNight) {this.pricePerNight = pricePerNight;}

    @Override
    public String toString() {
        return String.format("Room #%d - Type: %s - Price/Night: %d", roomNumber, roomType, pricePerNight);
    }
}
