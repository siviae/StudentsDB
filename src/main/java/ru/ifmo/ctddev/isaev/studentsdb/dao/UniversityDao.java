package ru.ifmo.ctddev.isaev.studentsdb.dao;

import org.springframework.stereotype.Repository;
import ru.ifmo.ctddev.isaev.studentsdb.entity.University;

import java.util.Collections;
import java.util.List;


/**
 * @author iisaev
 */
@Repository
public class UniversityDao {

    public List<University> findAll() {
        return Collections.emptyList();
    }
}
