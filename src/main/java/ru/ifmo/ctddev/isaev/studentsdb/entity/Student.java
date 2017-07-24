package ru.ifmo.ctddev.isaev.studentsdb.entity;


import ru.ifmo.ctddev.isaev.studentsdb.converter.TimestampToLocalDateConverter;
import ru.ifmo.ctddev.isaev.studentsdb.enums.EducationForm;
import ru.ifmo.ctddev.isaev.studentsdb.enums.GraduationType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "student")
public class Student {
    private Long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private LocalDate dateOfBirth;

    private Integer graduationYear;

    private EducationForm educationForm;

    private GraduationType graduationType;

    public Student(Long id,
                   String firstName,
                   String lastName,
                   String patronymic,
                   LocalDate dateOfBirth,
                   Integer graduationYear,
                   EducationForm educationForm,
                   GraduationType graduationType
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.graduationYear = graduationYear;
        this.educationForm = educationForm;
        this.graduationType = graduationType;
    }

    public Student() {//do not remove, used for deserializing
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "seq_student", allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @Basic
    @Column(name = "patronymic")
    public String getPatronymic() {
        return patronymic;
    }

    @Basic
    @Column(name = "date_of_birth")
    @Convert(converter = TimestampToLocalDateConverter.class)
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Basic
    @Column(name = "date_of_graduation")
    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    @Basic
    @Column(name = "education_form")
    @Convert(converter = EducationForm.Converter.class)
    public EducationForm getEducationForm() {
        return educationForm;
    }

    @Basic
    @Column(name = "graduation_type")
    @Convert(converter = GraduationType.Converter.class)
    public GraduationType getGraduationType() {
        return graduationType;
    }

    public void setGraduationType(GraduationType graduationType) {
        this.graduationType = graduationType;
    }

    public void setEducationForm(EducationForm educationForm) {
        this.educationForm = educationForm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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
        return id != null && student.id != null && Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
