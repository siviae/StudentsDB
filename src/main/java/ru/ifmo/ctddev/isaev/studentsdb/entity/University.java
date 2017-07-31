package ru.ifmo.ctddev.isaev.studentsdb.entity;


import javax.persistence.*;


@Entity
@Table(name = "university")
public class University {
    private Long id;

    private String title;

    public University() {//do not remove, used for deserializing
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "university_seq")
    @SequenceGenerator(name = "university_seq", sequenceName = "seq_university", allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
