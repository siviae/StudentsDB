package ru.ifmo.ctddev.isaev.solanteq.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.ifmo.ctddev.isaev.solanteq.helpers.Pair;
import ru.ifmo.ctddev.isaev.solanteq.pojo.Employee;
import ru.ifmo.ctddev.isaev.solanteq.pojo.Position;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by root on 7/14/15.
 */
@Repository
public class MainDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Pair<List<Employee>, Collection<Position>> getAllEmployeesAndPositions(Integer employeeID,
                                                                                  String firstName,
                                                                                  String surname,
                                                                                  String patronymic,
                                                                                  Date dateOfBirth,
                                                                                  Integer positionID,
                                                                                  String sort,
                                                                                  String sortOrder) {

        final Map<Integer, Position> positions = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM position WHERE 1", new Object[]{}, new RowMapper<Position>() {
            @Override
            public Position mapRow(ResultSet rs, int i) throws SQLException {
                Position pos = new Position(rs.getInt("positionID"), rs.getString("title"));
                if (!positions.containsKey(pos.getPositionID())) positions.put(pos.getPositionID(), pos);
                return pos;
            }
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
            queryString.append(String.format(" ORDER BY %s %s", sort, sortOrder));
        }


        List<Employee> employees = jdbcTemplate.query(queryString.toString(), params.toArray(), new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int i) throws SQLException {
                return new Employee(
                        rs.getInt("employeeID"),
                        rs.getString("firstName"),
                        rs.getString("surname"),
                        rs.getString("patronymic"),
                        rs.getDate("dateOfBirth"),
                        positions.get(rs.getInt("positionID")));
            }
        });
        return new Pair<>(employees, positions.values());
    }

    public boolean updateEmployee(Employee employee) {
        if (employee.getEmployeeID() == 0 || !employee.isValid()) return false;
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
        if (!employee.isValid()) return false;
        int modified = jdbcTemplate.update("INSERT INTO employee(firstName,surname,dateOfBirth,positionID,patronymic) " +
                        "VALUES (?,?,?,?,?) ",
                employee.getFirstName(), employee.getSurname(),
                employee.getDateOfBirth(), employee.getPosition().getPositionID(),
                employee.getPatronymic());
        return modified == 1;
    }

    public boolean deleteEmployee(int employeeID) {
        if (employeeID == 0) return false;
        int modified = jdbcTemplate.update("DELETE FROM employee WHERE employeeID=? ",
                employeeID);
        return modified == 1;
    }
}
