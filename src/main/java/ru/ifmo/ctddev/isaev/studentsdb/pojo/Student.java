package ru.ifmo.ctddev.isaev.studentsdb.pojo;


import java.util.Date;
import java.util.Objects;


public class Student {
    private Long id;

    private String firstName;

    private String surname;

    private String patronymic;

    private Date dateOfBirth;

    public Student(long id, String firstName, String surname, String patronymic, Date dateOfBirth) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
    }

    public Student() {//do not remove, used for deserializing
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public boolean isValid() {
        return firstName != null && !firstName.isEmpty()
                && surname != null && !surname.isEmpty()
                && patronymic != null && !patronymic.isEmpty()
                && dateOfBirth != null;
    }
}
