package ru.ifmo.ctddev.isaev.studentsdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class StudentDao {

    private final EntityManager entityManager;

    @Autowired
    public StudentDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Student save(Student student) {
        return entityManager.merge(student);
    }

    public List<Student> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);
        cq.orderBy(cb.asc(root.get("lastName")));
        return entityManager.createQuery(cq)
                .getResultList();
    }

    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(Student student) {
        if (student.getId() != null) {
            entityManager.createQuery("delete from Student s where s.id = :id")
                    .setParameter("id", student.getId())
                    .executeUpdate();
        }
    }
}
