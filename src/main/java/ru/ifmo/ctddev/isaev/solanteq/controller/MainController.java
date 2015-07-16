package ru.ifmo.ctddev.isaev.solanteq.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.ifmo.ctddev.isaev.solanteq.dao.MainDAO;
import ru.ifmo.ctddev.isaev.solanteq.helpers.Pair;
import ru.ifmo.ctddev.isaev.solanteq.pojo.Employee;
import ru.ifmo.ctddev.isaev.solanteq.pojo.Position;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class MainController {
    @Autowired
    MainDAO dao;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getPage(HttpServletRequest request,
                                HttpServletResponse response) {
        ModelAndView mav = new ModelAndView("main.jsp");
        Pair<List<Employee>, Collection<Position>> pair = dao.getAllEmployeesAndPositions();
        mav.addObject("employees", pair.getFirst());
        mav.addObject("positions", pair.getSecond());
        return mav;
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getAll(HttpServletRequest request,
                               HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        Pair<List<Employee>, Collection<Position>> pair = dao.getAllEmployeesAndPositions();
        result.put("employees", pair.getFirst());
        result.put("positions", pair.getSecond());
        return result;
    }

    @RequestMapping(value = "/editEmployee", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Map<String, String> editEmployee(@RequestBody Employee employee,
                        HttpServletRequest request,
                        HttpServletResponse response) {
        boolean ok = dao.updateEmployee(employee);
        Map<String,String> result = new HashMap<>();
        result.put("status",ok ? "success" : "danger");
        String message = ok ?"Сотрудник с ID %s успешно изменён" : "Не удалось изменить сотрудника с ID %s";
        result.put("message", String.format(message, employee.getEmployeeID()));
        return result;
    }

    @RequestMapping(value = "/addEmployee", method = RequestMethod.POST, consumes = "application/json")
    public
    @ResponseBody
    Map<String, String> addEmployee(@RequestBody Employee employee,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        boolean ok = dao.addEmployee(employee);
        Map<String,String> result = new HashMap<>();
        result.put("status",ok ? "success" : "danger");
        String message = ok ? "Добавлен новый сотрудник" : "Не удалось добавить сотрудника";
        result.put("message", message);
        return result;
    }

    @RequestMapping(value = "/deletEmployee", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, String> deleteEmployee(@RequestParam("employeeID") int employeeID,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        boolean ok = dao.deleteEmployee(employeeID);
        Map<String,String> result = new HashMap<>();
        result.put("status",ok ? "success" : "danger");
        String message = ok ?"Сотрудник с ID %s успешно удалён" : "Не удалось удалить сотрудника с ID %s";
        result.put("message", String.format(message, employeeID));
        return result;
    }
}