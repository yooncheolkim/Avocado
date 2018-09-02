package com.planet.avocado.data.local;


public class User {
    public String name;
    public String photoUrl;

    public User(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }
}
