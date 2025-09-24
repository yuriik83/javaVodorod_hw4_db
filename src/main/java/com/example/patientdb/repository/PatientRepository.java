package com.example.patientdb.repository;

import com.example.patientdb.Patient;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepository {

    public void addPatient(Patient patient) {
        String sql = "INSERT INTO patient (first_name, last_name, diagnosis, ward, age) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(sql,
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDiagnosis(),
                patient.getRoom(),
                patient.getAge()
        );
    }

    public List<Patient> getAllPatients() {
        String sql = "SELECT id, first_name, last_name, diagnosis, ward, age FROM patient";
        return executeQuery(sql, rs -> new Patient(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("diagnosis"),
                rs.getInt("room"),
                rs.getInt("age")
        ));
    }

    public Optional<Patient> getPatientById(int id) {
        String sql = "SELECT id, first_name, last_name, diagnosis, ward, age FROM patient WHERE id = ?";
        List<Patient> patients = executeQuery(sql, rs -> new Patient(
                rs.getInt("id"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("diagnosis"),
                rs.getInt("ward"),
                rs.getInt("age")
        ), id);
        return patients.stream().findFirst();
    }

    public void deletePatient(int id) {
        String sql = "DELETE FROM patient WHERE id = ?";
        executeUpdate(sql, id);
    }

    public void updatePatient(Patient patient) {
        String sql = "UPDATE patient SET first_name = ?, last_name = ?, diagnosis = ?, ward = ?, age = ? WHERE id = ?";
        executeUpdate(sql,
                patient.getFirstName(),
                patient.getLastName(),
                patient.getDiagnosis(),
                patient.getRoom(),
                patient.getAge(),
                patient.getId()
        );
    }

    public List<Patient> getPatientsByRoom(int room) {
        String sql = "SELECT * FROM patient WHERE room = ?";
        List<Patient> patients = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public List<Patient> getPatientsByDiagnosisAndAge(String diagnosis, int minAge) {
        String sql = "SELECT * FROM patient WHERE diagnosis = ? AND age > ?";
        List<Patient> patients = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, diagnosis);
            stmt.setInt(2, minAge);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public List<Patient> getPatientsOlderThanSorted(int minAge) {
        String sql = "SELECT * FROM patient WHERE age > ? ORDER BY age DESC";
        List<Patient> patients = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, minAge);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToPatient(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    public double getAverageAgeInRoom(int room) {
        String sql = "SELECT AVG(age) AS avg_age FROM patient WHERE room = ?";
        double avgAge = 0;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, room);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    avgAge = rs.getDouble("avg_age");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return avgAge;
    }

    public int getTotalPatients() {
        String sql = "SELECT COUNT(*) AS total FROM patient";
        int total = 0;

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getInt("total");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    private Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        return new Patient(
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("diagnosis"),
                rs.getInt("room"),
                rs.getInt("age")
        );
    }

    private void executeUpdate(String sql, Object... params) {
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            setParameters(stmt, params);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Database update error: " + e.getMessage(), e);
        }
    }

    private <T> List<T> executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) {
        List<T> result = new ArrayList<>();
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            setParameters(stmt, params);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    result.add(mapper.map(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Database query error: " + e.getMessage(), e);
        }
        return result;
    }

    private void setParameters(PreparedStatement stmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            stmt.setObject(i + 1, params[i]);
        }
    }

    @FunctionalInterface
    private interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }
}


