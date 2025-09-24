package com.example.patientdb;

import com.example.patientdb.service.PatientService;

public class Application {
    public static void main(String[] args) {
        PatientService service = new PatientService();
        service.getData();
    }
}
