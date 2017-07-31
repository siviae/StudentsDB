package ru.ifmo.ctddev.isaev.studentsdb.sample;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.renderers.ImageRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.ifmo.ctddev.isaev.studentsdb.controller.DemoDbPopulator;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.editor.StudentEditor;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;


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
        VerticalLayout gridLayout = new VerticalLayout(grid);
        gridLayout.setWidth("100%");
        grid.setWidth("100%");
        grid.setHeightUndefined();
        grid.setSizeFull();
        gridLayout.setHeightUndefined();
        VerticalLayout mainLayout = new VerticalLayout(
                new Label("Состав кафедры"), grid);
        mainLayout.setExpandRatio(grid, 1.0f);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        //Grid.Column idColumn = grid.addColumn(Student::getId).setCaption("ID");
        Grid.Column<Student, ExternalResource> photoColumn = grid.addColumn(
                this::getGridPicture,
                new ImageRenderer<>())
                .setCaption("Фото");
        photoColumn.setWidth(100.0);

        Grid.Column lastNameColumn = grid.addColumn(st ->
                format("%s\n%s\n%s", st.getLastName(), st.getFirstName(), st.getPatronymic())
        ).setCaption("ФИО");
        grid.addColumn(Student::getDateOfBirth).setCaption("Дата рождения");
        grid.addColumn(Student::getGraduationYear).setCaption("Выпуск");
        grid.addColumn(student -> student.getEducationForm().getName())
                .setCaption("Форма обучения");
        grid.addColumn(student -> student.getGraduationType().getName())
                .setCaption("Образование");

        HeaderRow headerRow = grid.prependHeaderRow();
        Button addNewBtn = new Button(FontAwesome.PLUS);
        headerRow.getCell(photoColumn).setComponent(addNewBtn);
        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> {
            editor.editCustomer(new Student());
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

    private ExternalResource getGridPicture(Student student) {
        return new ExternalResource("https://vignette1.wikia.nocookie.net/cutemariobro/images/5/59/Person-placeholder.jpg/revision/latest?cb=20170131092134");
    }

}
