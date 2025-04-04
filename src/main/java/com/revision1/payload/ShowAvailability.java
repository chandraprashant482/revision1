package com.revision1.payload;

import java.time.LocalDate;

public class ShowAvailability {
    private String hotelname;
    private String hotelCity;
    private String hotelCountry;
    private int roomAvalible;
    private String roomType;
    private int nightlyPrice;
    private LocalDate checkIn;
    private LocalDate checkOut;

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public String getHotelCity() {
        return hotelCity;
    }

    public void setHotelCity(String hotelCity) {
        this.hotelCity = hotelCity;
    }

    public String getHotelCountry() {
        return hotelCountry;
    }

    public void setHotelCountry(String hotelCountry) {
        this.hotelCountry = hotelCountry;
    }

    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public int getNightlyPrice() {
        return nightlyPrice;
    }

    public void setNightlyPrice(int nightlyPrice) {
        this.nightlyPrice = nightlyPrice;
    }

    public int getRoomAvalible() {
        return roomAvalible;
    }

    public void setRoomAvalible(int roomAvalible) {
        this.roomAvalible = roomAvalible;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
}
