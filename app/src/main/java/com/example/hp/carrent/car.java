package com.example.hp.carrent;

public class car {
    private int id_car;
    private String num_car;
    private String url_car;
    private String status_car;

    public car(int id_car, String num_car, String url_car, String status_car) {
        this.id_car = id_car;
        this.num_car = num_car;
        this.url_car = url_car;
        this.status_car = status_car;
    }

    public int getId_car() {
        return id_car;
    }

    public void setId_car(int id_car) {
        this.id_car = id_car;
    }

    public String getNum_car() {
        return num_car;
    }

    public void setNum_car(String num_car) {
        this.num_car = num_car;
    }

    public String getUrl_car() {
        return url_car;
    }

    public void setUrl_car(String url_car) {
        this.url_car = url_car;
    }

    public String getStatus_car() {
        return status_car;
    }

    public void setStatus_car(String status_car) {
        this.status_car = status_car;
    }
}
