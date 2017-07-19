package ru.ifmo.ctddev.isaev.studentsdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ifmo.ctddev.isaev.studentsdb.pojo.Student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Repository
public class StudentsDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentsDao(JdbcTemplate jdbcTemplate) {
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


        List<Student> students = jdbcTemplate.query(queryString.toString(), params.toArray(), (rs, i) -> new Student(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("surname"),
                rs.getString("patronymic"),
                rs.getDate("date_of_birth")
        ));
        return students;
    }

    public boolean updateEmployee(Student student) {
        if (student.getId() == null || !student.isValid()) {
            return false;
        }
        int modified = jdbcTemplate.update("UPDATE student SET " +
                        "name=?, " +
                        "surname = ?, " +
                        "date_of_birth = ?, " +
                        "patronymic = ? WHERE id=?",
                student.getFirstName(), student.getSurname(),
                student.getDateOfBirth(),
                student.getPatronymic(), student.getId());
        return modified == 1;
    }

    public boolean addEmployee(Student student) {
        if (!student.isValid()) {
            return false;
        }
        int modified = jdbcTemplate.update("INSERT INTO student(name,surname,patronymic,date_of_birth) " +
                        "VALUES (?,?,?,?,?) ",
                student.getFirstName(), student.getSurname(),
                student.getPatronymic(),
                student.getDateOfBirth());
        return modified == 1;
    }

    public boolean deleteEmployee(int employeeID) {
        if (employeeID == 0) {
            return false;
        }
        int modified = jdbcTemplate.update("DELETE FROM student WHERE id=? ",
                employeeID);
        return modified == 1;
    }
}
