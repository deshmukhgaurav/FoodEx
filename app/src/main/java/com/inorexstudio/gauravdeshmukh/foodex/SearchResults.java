package com.inorexstudio.gauravdeshmukh.foodex;

/**
 * Created by gauravdeshmukh on 3/23/16.
 */
public class SearchResults {
    private String name = "";
    private String cityState = "";
    private Number rating;
    private String cuisine = "";
    private String topCuisine = "";

    public void setTopCuisine(String topCuisine) {
        this.topCuisine = topCuisine;
    }

    public String getTopCuisine() {

        return topCuisine;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public void setRating(Number rating) {
        this.rating = rating;
    }

    public String getCuisine() {
        return cuisine;
    }

    public Number getRating() {
        return rating;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getCityState() {
        return cityState;
    }
}
