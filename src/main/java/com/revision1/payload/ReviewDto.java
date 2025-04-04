package com.revision1.payload;

public class ReviewDto {
    private int rating;
    private String description;
    private String hotelNmae;
    private String city;
    private String country;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHotelNmae() {
        return hotelNmae;
    }

    public void setHotelNmae(String hotelNmae) {
        this.hotelNmae = hotelNmae;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
