package com.example.patientdb.service;

import com.example.patientdb.Patient;
import com.example.patientdb.repository.PatientRepository;

import java.util.List;

public class PatientService {
    private final PatientRepository repository = new PatientRepository();

    public void getData() {
        // Добавляем тестовые данные
        repository.addPatient(new Patient("Иван", "Иванов", "Грипп", 1, 25));
        repository.addPatient(new Patient("Иван", "Петров", "ОРВИ", 1, 40));
        repository.addPatient(new Patient("Иван", "Сидоров", "Грипп", 2, 35));
        repository.addPatient(new Patient("Анна", "Иванова", "Грипп", 3, 29));
        repository.addPatient(new Patient("Анна", "Сидорова", "ОРВИ", 3, 50));

        // Вывод статистики
        System.out.println("\nОбщее количество пациентов: " + repository.getTotalPatients());

        System.out.println("\nВсе пациенты:");
        printPatients(repository.getAllPatients());

        int testRoom = 1;
        System.out.println("\nПациенты из палаты " + testRoom + ":");
        printPatients(repository.getPatientsByRoom(testRoom));

        System.out.println("\nСредний возраст в палате " + testRoom + ": " +
                repository.getAverageAgeInRoom(testRoom));

        String testDiagnosis = "ОРВИ";
        int minAge = 30;
        System.out.println("\nПациенты с диагнозом '" + testDiagnosis + "' старше " + minAge + ":");
        printPatients(repository.getPatientsByDiagnosisAndAge(testDiagnosis, minAge));

        System.out.println("\nПациенты старше " + minAge + ", сортировка по убыванию возраста:");
        printPatients(repository.getPatientsOlderThanSorted(minAge));
    }

    private void printPatients(List<Patient> patients) {
        for (Patient p : patients) {
            System.out.printf("%d %s %s %s Палата:%d Возраст:%d%n",
                    p.getId(), p.getFirstName(), p.getLastName(),
                    p.getDiagnosis(), p.getRoom(), p.getAge());
        }
    }
}
