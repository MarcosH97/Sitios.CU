package com.mahostudios.sitioscu;

public class Sitio {
    private int id, cost, down;
    private String name, url, description;

    public Sitio(int id, int cost, String name, String url, String description, int down) {
        this.id = id;
        this.cost = cost;
        this.name = name;
        this.url = url;
        this.description = description;
        this.down = down;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
