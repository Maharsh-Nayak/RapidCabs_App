package com.example.rapidcabs;

public class User {
    private int num;
    private String name;
    private String email;

    public User(String name, String email, int pass){
        this.name=name;
        this.email=email;
        this.num=pass;
    }

    // Getters and setters
    public int getNum() { return num; }
    public void setId(int id) { this.num = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

