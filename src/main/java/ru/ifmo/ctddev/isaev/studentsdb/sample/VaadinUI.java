package ru.ifmo.ctddev.isaev.studentsdb.sample;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.ifmo.ctddev.isaev.studentsdb.controller.DemoDbPopulator;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;


@SpringUI
public class VaadinUI extends UI {

    private final StudentDao repo;

    private final StudentEditor editor;

    final Grid<Student> grid;

    final TextField filter;

    private final Button addNewBtn;

    @Autowired
    public VaadinUI(DemoDbPopulator populator, StudentDao repo, StudentEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>(Student.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Добавить студента", FontAwesome.PLUS);
        populator.populate(50);
    }

    @Override
    protected void init(VaadinRequest request) {
        // build layout
        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
        setContent(mainLayout);

        grid.setHeight(480, Unit.PIXELS);
        grid.setColumns("id", "firstName", "lastName", "patronymic", "dateOfBirth", "dateOfGraduation", "educationForm", "graduationType");

        filter.setPlaceholder("Фильтр по фамилии");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
        });

        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> editor.editCustomer(new Student(null, "", "", "", null)));

        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        // Initialize listing
        listCustomers(null);
    }

    // tag::listCustomers[]
    void listCustomers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.find(null, null, filterText, null, null, null, null, null));
        }
    }
    // end::listCustomers[]

}
