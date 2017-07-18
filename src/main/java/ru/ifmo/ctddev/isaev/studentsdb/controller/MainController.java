package ru.ifmo.ctddev.isaev.studentsdb.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.ctddev.isaev.studentsdb.dao.MainDao;
import ru.ifmo.ctddev.isaev.studentsdb.helpers.Pair;
import ru.ifmo.ctddev.isaev.studentsdb.pojo.Employee;
import ru.ifmo.ctddev.isaev.studentsdb.pojo.Position;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("/")
public class MainController {
    private final MainDao dao;

    @Autowired
    public MainController(MainDao dao) {
        this.dao = dao;
    }

    private static Map<String, String> getResponseObject(boolean ok, String goodMsg, String badMsg) {
        Map<String, String> result = new HashMap<>();
        result.put("status", ok ? "success" : "danger");
        result.put("message", ok ? goodMsg : badMsg);
        return result;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String getPage() {
        return "main.jsp";
    }


    private boolean sortOk(String sort, String sortOrder) {
        boolean result = false;
        for (String sorting : new String[] {"firstName", "surname", "patronymic", "dateOfBirth"}) {
            if (sort.equals(sorting)) {
                result = true;
                break;
            }
        }
        result &= sortOrder.equals("asc") || sortOrder.equals("desc");
        return result;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getAll(@RequestParam(value = "employeeID", required = false) Integer employeeID,
                               @RequestParam(value = "firstName", required = false) String firstName,
                               @RequestParam(value = "surname", required = false) String surname,
                               @RequestParam(value = "patronymic", required = false) String patronymic,
                               @RequestParam(value = "dateOfBirth", required = false) String date,
                               @RequestParam(value = "positionID", required = false) Integer positionID,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "sortOrder", required = false) String sortOrder,
                               @RequestParam(value = "limit", required = false) Integer limit) {
        Map<String, Object> result = new HashMap<>();
        Date dateOfBirth = null;
        if (sort != null && sortOrder != null && !sortOk(sort, sortOrder)) {
            return result;
        }
        try {
            dateOfBirth = new SimpleDateFormat("dd.MM.yyyy").parse(date);
        } catch (ParseException ignored) {
        }
        Pair<List<Employee>, Collection<Position>> pair = dao.getAllEmployeesAndPositions(
                employeeID, firstName, surname, patronymic, dateOfBirth, positionID, sort, sortOrder, limit
        );
        result.put("employees", pair.getFirst());
        result.put("positions", pair.getSecond());
        return result;
    }

    @RequestMapping(value = "/editEmployee", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Map<String, String> editEmployee(@RequestBody Employee employee) {
        boolean ok = dao.updateEmployee(employee);
        return getResponseObject(ok,
                String.format("Сотрудник с ID %s успешно изменён", employee.getEmployeeID()),
                String.format("Не удалось изменить сотрудника с ID %s", employee.getEmployeeID()));
    }

    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Map<String, String> addEmployee(@RequestBody Employee employee) {
        boolean ok = dao.addEmployee(employee);
        return getResponseObject(ok,
                String.format("Добавлен новый сотрудник: %s %s %s",
                        employee.getSurname(),
                        employee.getFirstName(),
                        employee.getPatronymic()),
                "Не удалось добавить сотрудника");
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, String> deleteEmployee(@RequestParam("employeeID") int employeeID) {
        boolean ok = dao.deleteEmployee(employeeID);
        return getResponseObject(ok,
                String.format("Сотрудник с ID %s успешно удалён", employeeID),
                String.format("Не удалось удалить сотрудника с ID %s", employeeID));
    }
}