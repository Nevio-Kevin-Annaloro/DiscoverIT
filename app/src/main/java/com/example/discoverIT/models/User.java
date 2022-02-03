package com.example.discoverIT.models;

import java.io.Serializable;

public class User implements Serializable, Comparable<User> {
    public String name, image, email, token, id;
    public long score;

    public User(){
        name = "";
        image = "";
        email = "";
        token = "";
        id = "";
    }

    public User(String n, String e, String t, String i){
        name = n;
        email = e;
        token = t;
        image = i;
    }

    public User(String n, String e, String t) {
        name = n;
        email = e;
        token = t;
    }

    public String getName(){
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public long getScore() { return score; }

    public void setName(String n) {
        name = n;
    }

    public void setImage(String i) {
        image = i;
    }

    public void setEmail(String e) {
        email = e;
    }

    public void setToken(String t) {
        token = t;
    }

    public void setScore(long s) { score = s; }

    @Override
    public int compareTo(User o) {
        return Long.compare(getScore(), o.getScore());
    }
}
