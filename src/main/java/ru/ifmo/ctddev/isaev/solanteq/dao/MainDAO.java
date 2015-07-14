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

    public Pair<List<Employee>,Collection<Position>> getAllEmployeesAndPositions(Integer employeeID,
                                          String firstName,
                                          String surname,
                                          String patronymic,
                                          Date dateOfBirth,
                                          Integer positionID) {
        final HashMap<Integer, Position> positions = new HashMap<>();
        List<Object> params = new ArrayList<>();
        StringBuilder queryString = new StringBuilder("SELECT * FROM employee JOIN position USING(employeeID) WHERE ");
        if (employeeID != null) {
            queryString.append(" employeeID = ? AND ");
            params.add(employeeID);
        }
        if (firstName != null) {
            queryString.append(" firstName LIKE %?% AND ");
            params.add(firstName);
        }
        if (surname != null) {
            queryString.append(" surname LIKE %?% AND ");
            params.add(surname);
        }
        if (patronymic != null) {
            queryString.append(" patronymic LIKE %?% AND ");
            params.add(patronymic);
        }
        if (dateOfBirth != null) {
            queryString.append(" dateOfBirth = ? AND ");
            params.add(dateOfBirth);
        }
        if (positionID != null) {
            queryString.append(" positionID = ? AND ");
            params.add(positionID);
        }
        queryString.append(" 1");


        List<Employee> result =  jdbcTemplate.query(queryString.toString(), params.toArray(), new RowMapper<Employee>() {
            @Override
            public Employee mapRow(ResultSet rs, int i) throws SQLException {
                Position pos = new Position(rs.getInt("positionID"), rs.getString("title"));
                if(!positions.containsKey(pos.getPositionID())) positions.put(pos.getPositionID(), pos);
                pos = positions.get(pos.getPositionID());
                return new Employee(
                        rs.getInt("employeeID"),
                        rs.getString("firstName"),
                        rs.getString("surname"),
                        rs.getString("patronymic"),
                        rs.getDate("dateOfBirth"),
                        pos);
            }
        });
        return new Pair<>(result, positions.values());
    }

    public List<Employee> getAllEmployees(Integer employeeID,
                                          String firstName,
                                          String surname,
                                          String patronymic,
                                          Date dateOfBirth,
                                          Integer positionID) {
        return getAllEmployeesAndPositions(employeeID, firstName, surname, patronymic, dateOfBirth, positionID).getFirst();
    }

    public Pair<List<Employee>, Collection<Position>> getAllEmployeesAndPositions() {
        return getAllEmployeesAndPositions(null, null, null, null, null, null);
    }

}
