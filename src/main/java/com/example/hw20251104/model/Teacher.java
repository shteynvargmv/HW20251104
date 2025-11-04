package com.example.hw20251104.model;

public class Teacher {
    private Integer id;
    private Boolean isActive;
    private String firstName;
    private String lastName;
    private String subject;
    private Integer experience;
    private Double salary;
    private String email;

    private static int count = 0;

    public Teacher(String firstName, String lastName, Integer experience, String subject, Double salary, String email, Boolean isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.experience = experience;
        this.subject = subject;
        this.salary = salary;
        this.email = email;
        this.isActive = isActive;
        this.id = ++count;
    }

    public Teacher(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
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

    public String getEmail() {
        return email;
    }

    public Boolean getActive() {
        return isActive;
    }

    public static int getCount() {
        return count;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public static void setCount(int count) {
        Teacher.count = count;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
