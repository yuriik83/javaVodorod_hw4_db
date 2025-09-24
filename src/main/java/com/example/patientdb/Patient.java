package com.example.patientdb;

public class Patient {
    private int id;
    private String firstName;
    private String lastName;
    private String diagnosis;
    private int room;
    private int age;

    public Patient(int id, String firstName, String lastName, String diagnosis, int room, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.diagnosis = diagnosis;
        this.room = room;
        this.age = age;
    }

    public Patient(String firstName, String lastName, String diagnosis, int room, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.diagnosis = diagnosis;
        this.room = room;
        this.age = age;
    }

    public int getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getDiagnosis() { return diagnosis; }
    public int getRoom() { return room; }
    public int getAge() { return age; }
}
