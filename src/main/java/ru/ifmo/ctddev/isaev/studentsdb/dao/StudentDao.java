package ru.ifmo.ctddev.isaev.studentsdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import javax.persistence.EntityManager;
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

    public List<Student> find(Long id,
                              String firstName,
                              String lastName,
                              String patronymic,
                              Date dateOfBirth,
                              String sort,
                              String sortOrder, Integer limit) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        Root<Student> root = cq.from(Student.class);
        List<Predicate> predicates = new ArrayList<>();
        if (id != null) {
            predicates.add(
                    cb.equal(root.get("id"), cb.parameter(Long.class, "id"))
            );
        }
        if (firstName != null) {
            predicates.add(
                    cb.like(root.get("firstName"), cb.parameter(String.class, "firstName"))
            );
        }
        if (lastName != null) {
            predicates.add(
                    cb.like(root.get("lastName"), cb.parameter(String.class, "lastName"))
            );
        }
        if (patronymic != null) {
            predicates.add(
                    cb.like(root.get("patronymic"), cb.parameter(String.class, "patronymic"))
            );
        }
        if (dateOfBirth != null) {
            predicates.add(
                    cb.equal(root.get("dateOfBirth"), cb.parameter(LocalDate.class, "dateOfBirth"))
            );
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(cq)
                .setParameter("id", id)
                .setParameter("firstName", "%" + firstName + "%")
                .setParameter("lastName", "%" + lastName + "%")
                .setParameter("patronymic", "%" + patronymic + "%")
                .setParameter("dateOfBirth", dateOfBirth)
                .setMaxResults(limit)
                .getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Student save(Student student) {
        return entityManager.merge(student);
    }

    public boolean deleteEmployee(int id) {
        if (id == 0) {
            return false;
        }
        entityManager.remove(id);
        return true;
    }

    public List<Student> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Student> cq = cb.createQuery(Student.class);
        cq.from(Student.class);
        return entityManager.createQuery(cq)
                .getResultList();
    }

    public Student findById(Long id) {
        return entityManager.find(Student.class, id);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void remove(Student student) {
        entityManager.remove(student);
    }
}
