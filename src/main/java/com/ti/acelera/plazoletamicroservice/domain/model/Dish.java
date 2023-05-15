package com.ti.acelera.plazoletamicroservice.domain.model;

public class Dish {
    private String name;
    private String idCategory;
    private String description;
    private Long price;
    private Restaurant restaurant;
    private String urlImage;
    private boolean active;

    public Dish(String name, String idCategory, String description, Long price, Restaurant restaurant, String urlImage, boolean active) {
        this.name = name;
        this.idCategory = idCategory;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
        this.urlImage = urlImage;
        this.active = active;
    }

    public Dish() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(String idCategory) {
        this.idCategory = idCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
