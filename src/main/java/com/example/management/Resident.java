package com.example.management;

public class Resident {

//    name, house number, Cluster and email

    private  int id;
    private String name;
    private String houseNumber;
    private String cluster;
    private String email;

    public Resident(String name, String houseNumber, String cluster, String email, int id) {
        this.name = name;
        this.houseNumber = houseNumber;
        this.cluster = cluster;
        this.email = email;
        this.id = id;
    }

    public Resident() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getCluster() {
        return cluster;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Resident [name=" + name + ", houseNumber=" + houseNumber + ", cluster=" + cluster + ", email=" + email + "]";
    }
}
