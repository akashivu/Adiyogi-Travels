package com.example.Adiyogi_Travels.hotel.dto;

public class HotelResult {

    private String id;
    private String name;
    private String location;

    private String imageUrl;

    private Double rating;

    private Integer reviewCount;

    private Double pricePerNight;

    private String currency;

    private String roomType;

    private String bookingUrl;

    public HotelResult() {
    }

    public HotelResult(
            String id,
            String name,
            String location,
            String imageUrl,
            Double rating,
            Integer reviewCount,
            Double pricePerNight,
            String currency,
            String roomType,
            String bookingUrl
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.pricePerNight = pricePerNight;
        this.currency = currency;
        this.roomType = roomType;
        this.bookingUrl = bookingUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(Double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBookingUrl() {
        return bookingUrl;
    }

    public void setBookingUrl(String bookingUrl) {
        this.bookingUrl = bookingUrl;
    }
}