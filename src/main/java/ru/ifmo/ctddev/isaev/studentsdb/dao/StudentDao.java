package ru.ifmo.ctddev.isaev.studentsdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class StudentDao {

    private final EntityManager entityManager;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentDao(EntityManager entityManager, JdbcTemplate jdbcTemplate) {
        this.entityManager = entityManager;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Student> find(Long id,
                              String firstName,
                              String surname,
                              String patronymic,
                              Date dateOfBirth,
                              String sort,
                              String sortOrder, Integer limit) {

        List<Object> params = new ArrayList<>();
        StringBuilder queryString = new StringBuilder("SELECT * FROM student WHERE ");
        if (id != null) {
            queryString.append(" id = ? AND ");
            params.add(id);
        }
        if (firstName != null) {
            queryString.append(" name LIKE ? AND ");
            params.add(String.format("%%%s%%", firstName));
        }
        if (surname != null) {
            queryString.append(" surname LIKE ? AND ");
            params.add(String.format("%%%s%%", surname));
        }
        if (patronymic != null) {
            queryString.append(" patronymic LIKE ? AND ");
            params.add(String.format("%%%s%%", patronymic));
        }
        if (dateOfBirth != null) {
            queryString.append(" date_of_birth = ? AND ");
            params.add(dateOfBirth);
        }
        queryString.append(" 1 ");
        if (sort != null && sortOrder != null) {
            queryString.append(String.format(" ORDER BY %s %s ", sort, sortOrder));
        }

        if (limit != null) {
            queryString.append(" LIMIT 0, ? ");
            params.add(limit);
        }

        return jdbcTemplate.query(queryString.toString(), params.toArray(), (rs, i) -> new Student(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("patronymic"),
                rs.getDate("date_of_birth").toLocalDate()
        ));
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

    public void remove(Student student) {
        entityManager.remove(student);
    }
}
