package ru.ifmo.ctddev.isaev.studentsdb.sample;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import ru.ifmo.ctddev.isaev.studentsdb.controller.DemoDbPopulator;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.editor.StudentEditor;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;


@SpringUI
public class MainUI extends UI {

    private final StudentDao repo;

    private final StudentEditor editor;

    final Grid<Student> grid;

    private AtomicBoolean isModalOpened = new AtomicBoolean(false);

    private String placeHolderImagebase64;

    @Autowired
    public MainUI(DemoDbPopulator populator, StudentDao repo, StudentEditor editor) throws IOException {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>();
        this.placeHolderImagebase64 = Base64.getEncoder().encodeToString(
                Files.readAllBytes(Paths.get("src/main/resources/icons/photo_placeholder.jpg"))
        );
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
        grid.setRowHeight(100.0);
        gridLayout.setHeightUndefined();
        VerticalLayout mainLayout = new VerticalLayout(
                new Label("Состав кафедры"), grid);
        mainLayout.setExpandRatio(grid, 1.0f);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        //Grid.Column idColumn = grid.addColumn(Student::getId).setCaption("ID");
        Column<Student, String> photoColumn = grid.addColumn(
                this::getGridPicture,
                new HtmlRenderer())
                .setCaption("Фото");
        photoColumn.setWidth(100.0);

        Column lastNameColumn = grid.addColumn(this::formatFIO)
                .setCaption("ФИО")
                .setStyleGenerator((student) -> "text-align: center");
        grid.addColumn(Student::getGraduationYear)
                .setCaption("Выпуск")
                .setStyleGenerator((student) -> "text-align: center");
        grid.addColumn(this::formatMilitaryRank)
                .setCaption("В/зв.");
        grid.addColumn(this::formatMilitaryRankAssignment)
                .setCaption("Присв.зв");
        grid.addColumn(Student::getDateOfBirth)
                .setCaption("Дата рождения");
        grid.addColumn(Student::getNationality)
                .setCaption("Нац.");
        grid.addColumn(Student::getFleet)
                .setCaption("Флот");
        grid.addColumn(Student::getAchievementList)
                .setCaption("Послужной список");
        grid.addColumn(Student::getPosition)
                .setCaption("Должность");
        grid.addColumn(this::formatUniversity)
                .setCaption("Окончил ВУЗ");
        grid.addColumn(Student::getAveragePoints)
                .setCaption("Ср. балл атт.");
        grid.addColumn(Student::getForeignLanguage)
                .setCaption("Ин. яз.");
        grid.addColumn(Student::getIdentificationSeriesNumber)
                .setCaption("Серия и номер удост. Личн.");
        grid.addColumn(Student::getPersonalNumber)
                .setCaption("Личный номер");
        grid.addColumn(this::formatAdmission)
                .setCaption("Форма допуска");
        grid.addColumn(Student::getPassportNumber)
                .setCaption("Паспорт допуска");
        grid.addColumn(this::formatPassportIssue)
                .setCaption("Выдан");
        grid.addColumn(Student::getInternationalPassportNumber)
                .setCaption("Загранпаспорт");
        grid.addColumn(Student::getFamilyInfo)
                .setCaption("Ф.И.О чл. семьи (№свид. о браке и рожд., кем, когда выд.)");
        grid.addColumn(Student::getWifeNationality)
                .setCaption("Гр. жены");
        grid.addColumn(Student::getAddress)
                .setCaption("Адрес");
        grid.addColumn(Student::getStateRewards)
                .setCaption("Гос. нагр.");
        grid.addColumn(Student::getDiplomaTopic)
                .setCaption("Направление дипл. работы");
        grid.addColumn(Student::getPreliminaryAllocation)
                .setCaption("Предв. распред.");
        grid.addColumn(Student::getFinalAllocation)
                .setCaption("Оконч. распред.");
        grid.addColumn(Student::getAdditionalInfo)
                .setCaption("Примечания");
        grid.setFrozenColumnCount(2);


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

        grid.addItemClickListener((ItemClickListener) itemClick -> {
            if (itemClick.getMouseEventDetails().isDoubleClick()) {
                editor.editCustomer((Student) itemClick.getItem());
                editor.makeVisible();
            }
        });


        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            listCustomers(filter.getValue());
        });

        // Initialize listing
        listCustomers(null);
    }

    private String formatFIO(Student st) {
        return format("%s\n%s\n%s", st.getLastName(), st.getFirstName(), st.getPatronymic());
    }

    private String formatPassportIssue(Student student) {
        String passportIssuer = student.getPassportIssuer() == null ? "" : student.getPassportIssuer();
        String passportIssueDate = student.getPassportIssueDate() == null ? "" : student.getPassportIssueDate().toString();
        return passportIssuer + "\n" + passportIssueDate;
    }

    private String formatAdmission(Student student) {
        String admissionForm = student.getAdmissionForm() == null ? "" : student.getAdmissionForm();
        String admissionDate = student.getAdmissionDate() == null ? "" : student.getAdmissionDate().toString();
        return admissionForm + "\n" + admissionDate;
    }

    private String formatMilitaryRank(Student student) {
        String rankName = student.getMilitaryRank() == null ? "" : student.getMilitaryRank().getName();
        return rankName;
    }

    private String formatMilitaryRankAssignment(Student student) {
        String militaryRankAwardDate = student.getMilitaryRankAwardDate() == null ? "" : student.getMilitaryRankAwardDate().toString();
        String militaryRankOrderName = student.getMilitaryRankOrderName() == null ? "" : student.getMilitaryRankOrderName();
        return militaryRankAwardDate + "\n" + militaryRankOrderName;
    }

    private String formatUniversity(Student student) {
        String universityTitle = student.getUniversity() == null ? "" : student.getUniversity().getTitle();
        String graduationYear = student.getGraduationYear() == null ? "" : String.valueOf(student.getGraduationYear());
        return format("%s в %s г.", universityTitle, graduationYear);
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

    private String getGridPicture(Student student) {
        final String imageBase64;
        if (student.getPhotoBase64() != null) {
            imageBase64 = student.getPhotoBase64();
        } else {
            imageBase64 = placeHolderImagebase64;
        }
        return String.format("<img height=\"100px\" width=\"100px\" src=\"data:image/jpeg;base64,%s\" />", imageBase64);
    }

}
