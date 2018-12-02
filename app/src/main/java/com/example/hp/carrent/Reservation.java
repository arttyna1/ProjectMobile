package com.example.hp.carrent;

public class Reservation {
    private int id;
    private String name;
    private String url_user;
    private String url_car;
    private String date;
    private String backdate;
    private String gotime;
    private String backtime;
    private String status;
    private String agent;

    public Reservation(int id, String name, String url_user, String url_car, String date, String backdate, String gotime, String backtime, String status, String agent) {
        this.id = id;
        this.name = name;
        this.url_user = url_user;
        this.url_car = url_car;
        this.date = date;
        this.backdate = backdate;
        this.gotime = gotime;
        this.backtime = backtime;
        this.status = status;
        this.agent = agent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl_user() {
        return url_user;
    }

    public void setUrl_user(String url_user) {
        this.url_user = url_user;
    }

    public String getUrl_car() {
        return url_car;
    }

    public void setUrl_car(String url_car) {
        this.url_car = url_car;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBackdate() {
        return backdate;
    }

    public void setBackdate(String backdate) {
        this.backdate = backdate;
    }

    public String getGotime() {
        return gotime;
    }

    public void setGotime(String gotime) {
        this.gotime = gotime;
    }

    public String getBacktime() {
        return backtime;
    }

    public void setBacktime(String backtime) {
        this.backtime = backtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
