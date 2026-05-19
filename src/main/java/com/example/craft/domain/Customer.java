package com.example.craft.domain;

public class Customer {
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final CustomerType type;

    public Customer(String name, String email, String phoneNumber, CustomerType type) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.type = type;
    }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public CustomerType getType() { return type; }
}
