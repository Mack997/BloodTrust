package com.example.mayankagarwal.thechatapp;

/**
 * Created by Mayank Agarwal on 10-02-2018.
 */

public class Request {
    String name, blood_group;
    public Request(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String status) {
        this.blood_group = blood_group;
    }

    public Request(String name, String blood_group) {

        this.name = name;
        this.blood_group = blood_group;
    }
}
