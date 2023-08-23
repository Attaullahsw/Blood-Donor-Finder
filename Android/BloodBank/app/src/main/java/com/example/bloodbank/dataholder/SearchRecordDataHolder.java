package com.example.bloodbank.dataholder;

public class SearchRecordDataHolder {

    private int id;
    private String name;
    private String Address;
    private String blood_gropu;
    private String image;
    private String age;
    private String contact_no;
    private String email;
    private boolean universal_accepter;

    public SearchRecordDataHolder(int id, String name, String address, String blood_gropu, String image, String age, String contact_no, String email,boolean universal_accepter) {
        this.id = id;
        this.name = name;
        Address = address;
        this.blood_gropu = blood_gropu;
        this.image = image;
        this.age = age;
        this.contact_no = contact_no;
        this.email = email;
        this.universal_accepter = universal_accepter;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return Address;
    }

    public String getBlood_gropu() {
        return blood_gropu;
    }

    public String getImage() {
        return image;
    }

    public String getAge() {
        return age;
    }

    public String getContact_no() {
        return contact_no;
    }

    public String getEmail() {
        return email;
    }

    public boolean isUniversal_accepter() {
        return universal_accepter;
    }
}
