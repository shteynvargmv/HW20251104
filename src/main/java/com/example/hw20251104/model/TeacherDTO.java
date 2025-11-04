package com.example.hw20251104.model;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class TeacherDTO {
    private String firstName;
    private String lastName;
    private String subject;
    private Integer experience;
    private Double salary;
    private String email;
    private Boolean isActive;
    private static List<String> validationErrors;
    private static Boolean required;

    public TeacherDTO(String firstName, Boolean isActive, String email, Double salary, Integer experience, String subject, String lastName) {
        this.firstName = firstName;
        this.isActive = isActive;
        this.email = email;
        this.salary = salary;
        this.experience = experience;
        this.subject = subject;
        this.lastName = lastName;
    }

    public static void setRequired(Boolean required) {
        TeacherDTO.required = required;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getSubject() {
        return subject;
    }

    public Integer getExperience() {
        return experience;
    }

    public Double getSalary() {
        return salary;
    }

    public Boolean getActive() {
        return isActive;
    }

    public String getEmail() {
        return email;
    }

    public static List<String> validationAdd(TeacherDTO teacher) {
        validationErrors = new ArrayList<>();
        setRequired(Boolean.TRUE);
        firstNameValidate(teacher.getFirstName());
        lastNameValidate(teacher.getLastName());
        subjectValidate(teacher.getSubject());
        experienceValidate(teacher.getExperience());
        salaryValidate(teacher.getSalary());
        emailValidate(teacher.getEmail());
        return validationErrors;
    }
    public static List<String> validationUpdateById(Integer id, TeacherDTO teacher) {
        validationErrors = new ArrayList<>();
        setRequired(Boolean.TRUE);
        idValidate(id);
        firstNameValidate(teacher.getFirstName());
        lastNameValidate(teacher.getLastName());
        subjectValidate(teacher.getSubject());
        experienceValidate(teacher.getExperience());
        salaryValidate(teacher.getSalary());
        emailValidate(teacher.getEmail());
        return validationErrors;
    }

    public static List<String> validationUpdatePartialById(Integer id, TeacherDTO teacher) {
        validationErrors = new ArrayList<>();
        setRequired(Boolean.FALSE);
        idValidate(id);
        firstNameValidate(teacher.getFirstName());
        lastNameValidate(teacher.getLastName());
        subjectValidate(teacher.getSubject());
        experienceValidate(teacher.getExperience());
        salaryValidate(teacher.getSalary());
        emailValidate(teacher.getEmail());
        return validationErrors;
    }

    public static List<String> validationSearch(String firstName, String lastName) {
        validationErrors = new ArrayList<>();
        setRequired(Boolean.TRUE);
        if (firstName != null && !firstName.isEmpty()) {
            firstNameValidate(firstName);
        }
        if (lastName != null && !lastName.isEmpty()) {
            lastNameValidate(lastName);
        }
        return validationErrors;
    }

    public static List<String> validationSubject(String subject) {
        setRequired(Boolean.TRUE);
        validationErrors = new ArrayList<>();
        subjectValidate(subject);
        return validationErrors;
    }

    public static List<String> validationId(Integer id) {
        setRequired(Boolean.TRUE);
        validationErrors = new ArrayList<>();
        idValidate(id);
        return validationErrors;
    }

    public static void idValidate(Integer id) {
        if (id == null || id < 0) {
            validationErrors.add("Id mast be > 0");
        }
    }

    public static void firstNameValidate(String firstName) {
        if (required || (firstName != null)) {
            if (firstName == null ||
                    firstName.length() < 2 ||
                    firstName.length() > 50 ||
                    firstName.chars().anyMatch(Character::isDigit)) {
                validationErrors.add("First name must be 2-50 characters and contain only letters");
            }
        }
    }

    public static void lastNameValidate(String lastName) {
        if (required || (lastName != null)) {
            if (lastName == null ||
                    lastName.length() < 2 ||
                    lastName.length() > 50 ||
                    lastName.chars().anyMatch(Character::isDigit)) {
                validationErrors.add("Last name must be 2-50 characters and contain only letters");
            }
        }
    }

    public static void subjectValidate(String subject) {
        if (required || (subject != null)) {
            if (subject == null ||
                    subject.isEmpty()) {
                validationErrors.add("Subject is required");
            }
        }
    }

    public static void experienceValidate(Integer experience) {
        if (required || (experience != null)) {
            if (experience == null ||
                    experience < 0 ||
                    experience > 50) {
                validationErrors.add("Experience must be between 0 and 50 years");
            }
        }
    }

    public static void salaryValidate(Double salary) {
        if (required || (salary != null)) {
            if (salary == null ||
                    salary <= 0 ||
                    salary > 100000) {
                validationErrors.add("Salary must be between 0 and 100000");
            }
        }
    }

    public static void emailValidate(String email) {
        if (required || (email != null)) {
            if (email == null ||
                    !email.matches("[\\w.]+@[\\w.]+\\.(com|ru)")) {
                validationErrors.add("Invalid email format");
            }
        }
    }
}

