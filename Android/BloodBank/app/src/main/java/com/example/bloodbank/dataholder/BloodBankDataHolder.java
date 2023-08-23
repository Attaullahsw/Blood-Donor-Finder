package com.example.bloodbank.dataholder;

public class BloodBankDataHolder {

    private int id;
    private String name;
    private String address;
    private String email;
    private String contact;
    private int city_id;

    public BloodBankDataHolder(int id, String name, String address, String email, String contact,int city_id) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contact = contact;
        this.city_id = city_id;

    }

    public BloodBankDataHolder(String name, String address, String email, String contact) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
        this.contact = contact;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getContact() {
        return contact;
    }

    public int getCity_id() {
        return city_id;
    }
}
