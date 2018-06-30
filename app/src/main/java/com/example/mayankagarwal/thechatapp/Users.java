package com.example.mayankagarwal.thechatapp;

/**
 * Created by Mayank Agarwal on 18-01-2018.
 */

public class Users {
    public String name;
    public String city;



    public Users(){

    }

    public Users(String name, String city) {
        this.name = name;

        this.city = city;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
