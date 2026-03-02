package com.template.DATABASE;

import com.template.DOMAIN.StudentClass;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Studentdatabase {

    private static final String DB_URL = "jdbc:sqlite:studentClasses.db";

    // Connect to SQLite database
    private static Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    // Create table if it does not exist
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS studentClasses (
                    studentId TEXT PRIMARY KEY,
                    fullName TEXT,
                    programme TEXT,
                    level TEXT,
                    gpa TEXT,
                    email TEXT,
                    phone TEXT
                );
                """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Insert student
    public static void insert(StudentClass studentClass) {
        String sql = """
                INSERT INTO studentClasses(studentId, fullName, programme, level, gpa, email, phone)
                VALUES (?, ?, ?, ?, ?, ?, ?);
                """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentClass.getStudentId());
            pstmt.setString(2, studentClass.getFullName());
            pstmt.setString(3, studentClass.getProgramme());
            pstmt.setString(4, studentClass.getLevel());
            pstmt.setString(5, studentClass.getGpa());
            pstmt.setString(6, studentClass.getEmail());
            pstmt.setString(7, studentClass.getPhone());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }

    // Retrieve all studentClasses
    public List<StudentClass> getAllStudents() {
        List<StudentClass> studentClasses = new ArrayList<>();
        String sql = "SELECT * FROM studentClasses";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                StudentClass studentClass = new StudentClass(
                        rs.getString("studentId"),
                        rs.getString("fullName"),
                        rs.getString("programme"),
                        rs.getString("level"),
                        rs.getString("gpa"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                studentClasses.add(studentClass);
            }

        } catch (SQLException e) {
            System.out.println("Read failed: " + e.getMessage());
        }

        return studentClasses;
    }

    // Update student
    public void update(StudentClass studentClass) {
        String sql = """
                UPDATE studentClasses
                SET fullName = ?, programme = ?, level = ?, gpa = ?, email = ?, phone = ?
                WHERE studentId = ?;
                """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentClass.getFullName());
            pstmt.setString(2, studentClass.getProgramme());
            pstmt.setString(3, studentClass.getLevel());
            pstmt.setString(4, studentClass.getGpa());
            pstmt.setString(5, studentClass.getEmail());
            pstmt.setString(6, studentClass.getPhone());
            pstmt.setString(7, studentClass.getStudentId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    // Delete student
    public void delete(String studentId) {
        String sql = "DELETE FROM studentClasses WHERE studentId = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    public static boolean studentExists(String studentId) {
        String sql = "SELECT 1 FROM studentClasses WHERE studentId = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            return false;
        }
    }

    public static List<StudentClass> findStudentsBelowGpa(double threshold) {
        List<StudentClass> result = new ArrayList<>();
        String sql = "SELECT * FROM studentClasses WHERE gpa < ?";

        try (Connection conn =   connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDouble(1, threshold);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                StudentClass studentClass = new StudentClass(
                        rs.getString("studentId"),
                        rs.getString("fullName"),
                        rs.getString("programme"),
                        rs.getString("level"),
                        rs.getString("gpa"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                result.add(studentClass);
            }
        } catch (SQLException e) {
            System.out.println("Error loading At Risk studentClasses: " + e.getMessage());
        }

        return result;
    }

}
