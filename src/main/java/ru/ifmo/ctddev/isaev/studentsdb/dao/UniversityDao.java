package ru.ifmo.ctddev.isaev.studentsdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.ifmo.ctddev.isaev.studentsdb.entity.University;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


/**
 * @author iisaev
 */
@Repository
public class UniversityDao {

    private final EntityManager entityManager;

    @Autowired
    public UniversityDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<University> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<University> cq = cb.createQuery(University.class);
        cq.from(University.class);
        return entityManager.createQuery(cq)
                .getResultList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public University save(University student) {
        return entityManager.merge(student);
    }
}
