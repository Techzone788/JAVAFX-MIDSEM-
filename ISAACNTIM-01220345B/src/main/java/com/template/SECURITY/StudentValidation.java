package com.template.SECURITY;

import com.template.DOMAIN.StudentClass;
import com.template.LOGGING.DataLogg;
import com.template.DATABASE.Studentdatabase;

import java.util.List;

public class StudentValidation {

    public void validateStudent(StudentClass studentClass) {

        // com.template.DOMAIN.Student ID
        if (studentClass.getStudentId() == null || studentClass.getStudentId().isEmpty()) {
            throw new IllegalArgumentException("com.template.DOMAIN.Student ID is required");
        }
        if (!studentClass.getStudentId().matches("[A-Za-z0-9]{4,9}")) {
            throw new IllegalArgumentException("com.template.DOMAIN.Student ID must be 4–20 letters or digits");
        }

        // Full name
        if (studentClass.getFullName() == null || studentClass.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (!studentClass.getFullName().matches("[A-Za-z ]{2,60}")) {
            throw new IllegalArgumentException("Full name must not contain digits");
        }

        // Programme
        if (studentClass.getProgramme() == null || studentClass.getProgramme().isEmpty()) {
            throw new IllegalArgumentException("Programme is required");
        }

        // Level
        String level = studentClass.getLevel();
        if (!level.matches("100|200|300|400")) {
            throw new IllegalArgumentException("Level must be 100–400");
        }

        // GPA
        try {
            double gpa = Double.parseDouble(studentClass.getGpa());
            if (gpa < 0.0 || gpa > 5.0) {
                throw new IllegalArgumentException("GPA must be between 0.0 and 5.0");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("GPA must be a number");
        }

        // Email
        if (!studentClass.getEmail().contains("@") || !studentClass.getEmail().contains(".")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        // Phone
        if (!studentClass.getPhone().matches("\\d{10}")) {
            throw new IllegalArgumentException("Phone must be 10 digits");
        }
    }

    public List<StudentClass> getAtRiskStudents(double threshold) {
        return Studentdatabase.findStudentsBelowGpa(threshold);
    }

    public boolean addStudent(StudentClass studentClass){
        if(Studentdatabase.studentExists(studentClass.getStudentId())){
            DataLogg.log("Duplicate student skipped:" + studentClass.getStudentId());
            return false;
        }
        Studentdatabase.insert(studentClass);
        DataLogg.log("Student added:" + studentClass.getStudentId());
        return true;
    }

}
