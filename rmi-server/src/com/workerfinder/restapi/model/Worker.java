package com.workerfinder.restapi.model;

import java.io.Serializable;

 public class Worker implements Serializable {
     private static final long serialVersionUID = 1L;
    private String id, name, skill, area, phone;
    private double rating;
    private boolean available;

    public Worker(String id, String name, String skill, String area, String phone) {
        this.id = id; this.name = name; this.skill = skill;
        this.area = area; this.phone = phone;
        this.rating = 4.0; this.available = true;
    }

    public String getId()        { return id; }
    public String getName()      { return name; }
    public String getSkill()     { return skill; }
    public String getArea()      { return area; }
    public String getPhone()     { return phone; }
    public double getRating()    { return rating; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean v) { available = v; }
    public void setRating(double r)     { rating = r; }
}