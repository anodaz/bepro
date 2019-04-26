package com.anodaz.bepro.Service;

public class Item {
    private int id;
    private String date;
    private String image;
    private Boolean status;

    public Item(int id, String date, String image, Boolean status) {
        this.id = id;
        this.date = date;
        this.image = image;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
