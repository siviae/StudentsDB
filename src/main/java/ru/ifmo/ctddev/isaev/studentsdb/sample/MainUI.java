package ru.ifmo.ctddev.isaev.studentsdb.sample;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.ifmo.ctddev.isaev.studentsdb.controller.DemoDbPopulator;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.editor.StudentEditor;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import java.util.concurrent.atomic.AtomicBoolean;


@SpringUI
public class MainUI extends UI {

    private final StudentDao repo;

    private final StudentEditor editor;

    final Grid<Student> grid;

    private AtomicBoolean isModalOpened = new AtomicBoolean(false);

    @Autowired
    public MainUI(DemoDbPopulator populator, StudentDao repo, StudentEditor editor) {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>();
        //populator.populate(200);
    }

    @Override
    protected void init(VaadinRequest request) {
        // build layout
        HorizontalLayout gridLayout = new HorizontalLayout(grid);
        gridLayout.setSizeFull();
        VerticalLayout mainLayout = new VerticalLayout(
                new Label("Студенты"), gridLayout);
        grid.setSizeFull();
        setContent(mainLayout);
        setSizeFull();

        Grid.Column idColumn = grid.addColumn(Student::getId).setCaption("ID");
        Grid.Column lastNameColumn = grid.addColumn(Student::getLastName).setCaption("Фамилия");
        grid.addColumn(Student::getFirstName).setCaption("Имя");
        grid.addColumn(Student::getPatronymic).setCaption("Отчество");
        grid.addColumn(Student::getDateOfBirth).setCaption("Дата рождения");
        grid.addColumn(Student::getGraduationYear).setCaption("Выпуск");
        grid.addColumn(student -> student.getEducationForm().getName())
                .setCaption("Форма обучения");
        grid.addColumn(student -> student.getGraduationType().getName())
                .setCaption("Образование");

        HeaderRow headerRow = grid.prependHeaderRow();
        Button addNewBtn = new Button(FontAwesome.PLUS);
        headerRow.getCell(idColumn).setComponent(addNewBtn);
        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> {
            editor.editCustomer(new Student(null, "", "", "", null, null, null, null));
            editor.makeVisible();
        });


        TextField filter = new TextField();
        headerRow.getCell(lastNameColumn)
                .setComponent(filter);

        filter.setPlaceholder("Фильтр по фамилии");

        // Hook logic to components

        // Replace listing with filtered content when user changes filter
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listCustomers(e.getValue()));

        // Connect selected Customer to editor or hide if none is selected
        grid.asSingleSelect().addValueChangeListener(e -> {
            editor.editCustomer(e.getValue());
            editor.makeVisible();
        });


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
