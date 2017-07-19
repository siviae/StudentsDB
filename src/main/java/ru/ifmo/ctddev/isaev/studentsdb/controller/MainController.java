package ru.ifmo.ctddev.isaev.studentsdb.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentsDao;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class MainController {
    private final StudentsDao dao;

    @Autowired
    public MainController(StudentsDao dao) {
        this.dao = dao;
    }

    private static Map<String, String> getResponseObject(boolean ok, String goodMsg, String badMsg) {
        Map<String, String> result = new HashMap<>();
        result.put("status", ok ? "success" : "danger");
        result.put("message", ok ? goodMsg : badMsg);
        return result;
    }

    @RequestMapping(value = "/studentsDB", method = RequestMethod.GET)
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
    Map<String, Object> getAll(@RequestParam(value = "employeeID", required = false) Long id,
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
        List<Student> students = dao.find(
                id, firstName, surname, patronymic, dateOfBirth, sort, sortOrder, limit
        );
        result.put("employees", students);
        return result;
    }

    @RequestMapping(value = "/editEmployee", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Map<String, String> editEmployee(@RequestBody Student student) {
        dao.save(student);
        return getResponseObject(true,
                String.format("Сотрудник с ID %s успешно изменён", student.getId()),
                String.format("Не удалось изменить сотрудника с ID %s", student.getId()));
    }

    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Map<String, String> addEmployee(@RequestBody Student student) {
        dao.save(student);
        return getResponseObject(true,
                String.format("Добавлен новый сотрудник: %s %s %s",
                        student.getSurname(),
                        student.getName(),
                        student.getPatronymic()),
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