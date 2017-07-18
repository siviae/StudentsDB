package ru.ifmo.ctddev.isaev.studentsdb.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.ifmo.ctddev.isaev.studentsdb.helpers.Pair;
import ru.ifmo.ctddev.isaev.studentsdb.pojo.Employee;
import ru.ifmo.ctddev.isaev.studentsdb.pojo.Position;

import java.util.*;


@Repository
public class MainDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MainDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Pair<List<Employee>, Collection<Position>> getAllEmployeesAndPositions(Integer employeeID,
                                                                                  String firstName,
                                                                                  String surname,
                                                                                  String patronymic,
                                                                                  Date dateOfBirth,
                                                                                  Integer positionID,
                                                                                  String sort,
                                                                                  String sortOrder, Integer limit) {

        final Map<Integer, Position> positions = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM position WHERE 1", new Object[] {}, (rs, i) -> {
            Position pos = new Position(rs.getInt("positionID"), rs.getString("title"));
            if (!positions.containsKey(pos.getPositionID())) {
                positions.put(pos.getPositionID(), pos);
            }
            return pos;
        });
        List<Object> params = new ArrayList<>();
        StringBuilder queryString = new StringBuilder("SELECT * FROM employee WHERE ");
        if (employeeID != null) {
            queryString.append(" employeeID = ? AND ");
            params.add(employeeID);
        }
        if (firstName != null) {
            queryString.append(" firstName LIKE ? AND ");
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
            queryString.append(" dateOfBirth = ? AND ");
            params.add(dateOfBirth);
        }
        if (positionID != null) {
            queryString.append(" positionID = ? AND ");
            params.add(positionID);
        }
        queryString.append(" 1 ");
        if (sort != null && sortOrder != null) {
            queryString.append(String.format(" ORDER BY %s %s ", sort, sortOrder));
        }

        if (limit != null) {
            queryString.append(" LIMIT 0, ? ");
            params.add(limit);
        }


        List<Employee> employees = jdbcTemplate.query(queryString.toString(), params.toArray(), (rs, i) -> new Employee(
                rs.getInt("employeeID"),
                rs.getString("firstName"),
                rs.getString("surname"),
                rs.getString("patronymic"),
                rs.getDate("dateOfBirth"),
                positions.get(rs.getInt("positionID"))));
        return new Pair<>(employees, positions.values());
    }

    public boolean updateEmployee(Employee employee) {
        if (employee.getEmployeeID() == 0 || !employee.isValid()) {
            return false;
        }
        int modified = jdbcTemplate.update("UPDATE employee SET " +
                        "firstName=?, " +
                        "surname = ?, " +
                        "dateOfBirth = ?, " +
                        "positionID = ?, " +
                        "patronymic = ? WHERE employeeID=?",
                employee.getFirstName(), employee.getSurname(),
                employee.getDateOfBirth(), employee.getPosition().getPositionID(),
                employee.getPatronymic(), employee.getEmployeeID());
        return modified == 1;
    }

    public boolean addEmployee(Employee employee) {
        if (!employee.isValid()) {
            return false;
        }
        int modified = jdbcTemplate.update("INSERT INTO employee(firstName,surname,dateOfBirth,positionID,patronymic) " +
                        "VALUES (?,?,?,?,?) ",
                employee.getFirstName(), employee.getSurname(),
                employee.getDateOfBirth(), employee.getPosition().getPositionID(),
                employee.getPatronymic());
        return modified == 1;
    }

    public boolean deleteEmployee(int employeeID) {
        if (employeeID == 0) {
            return false;
        }
        int modified = jdbcTemplate.update("DELETE FROM employee WHERE employeeID=? ",
                employeeID);
        return modified == 1;
    }
}
