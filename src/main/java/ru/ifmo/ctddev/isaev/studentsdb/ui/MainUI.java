package ru.ifmo.ctddev.isaev.studentsdb.ui;

import com.vaadin.data.HasValue;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.renderers.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.ctddev.isaev.studentsdb.controller.DemoDbPopulator;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;
import ru.ifmo.ctddev.isaev.studentsdb.enums.Fleet;
import ru.ifmo.ctddev.isaev.studentsdb.enums.MilitaryRank;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.asList;


@SpringUI
public class MainUI extends UI {

    private final StudentDao repo;

    private final StudentEditor editor;

    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final TextField lastNameFilter;

    private final ComboBox<MilitaryRank> rankFilter;

    private final TextField graduationYearFilter;

    private final ComboBox<Fleet> fleetFilter;

    final Grid<Student> grid;

    private AtomicBoolean isModalOpened = new AtomicBoolean(false);

    private String placeHolderImagebase64;

    private volatile List<Student> allStudents;

    @Autowired
    public MainUI(DemoDbPopulator populator, StudentDao repo, StudentEditor editor) throws IOException {
        this.repo = repo;
        this.editor = editor;
        this.grid = new Grid<>();
        this.placeHolderImagebase64 = Base64.getEncoder().encodeToString(
                Files.readAllBytes(Paths.get("src/main/resources/icons/photo_placeholder.jpg"))
        );
        lastNameFilter = new TextField("ФИО");
        rankFilter = new ComboBox<>("Воинское звание", asList(MilitaryRank.values()));
        graduationYearFilter = new TextField("Выпуск");
        fleetFilter = new ComboBox<>("Флот", asList(Fleet.values()));
        //populator.populate(5000);
    }

    @Override
    protected void init(VaadinRequest request) {
        Button addNewBtn = new Button(FontAwesome.PLUS);
        // build layout
        VerticalLayout gridLayout = new VerticalLayout(grid);
        gridLayout.setWidth("100%");
        grid.setWidth("100%");
        grid.setHeightUndefined();
        grid.setSizeFull();
        grid.setRowHeight(75.0);
        gridLayout.setHeightUndefined();
        HorizontalLayout header = new HorizontalLayout(
                new Label("Состав кафедры"),
                addNewBtn,
                lastNameFilter,
                graduationYearFilter,
                rankFilter,
                fleetFilter
        );
        VerticalLayout mainLayout = new VerticalLayout(header, grid);
        mainLayout.setExpandRatio(grid, 1.0f);
        mainLayout.setSizeFull();
        setContent(mainLayout);

        //Grid.Column idColumn = grid.addColumn(Student::getId).setCaption("ID");
        Column<Student, String> photoColumn = grid.addColumn(
                this::getGridPicture,
                new HtmlRenderer())
                .setCaption("Фото")
                .setWidth(110.0)
                .setSortable(false)
                .setResizable(false);

        grid.addColumn(this::formatFIO)
                .setCaption("ФИО")
                .setWidth(300.0)
                .setResizable(false);
        grid.addColumn(Student::getGraduationYear)
                .setCaption("Выпуск")
                .setWidth(85.0)
                .setResizable(false);
        grid.addColumn(this::formatMilitaryRank)
                .setCaption("В/зв.")
                .setWidth(180.0)
                .setResizable(false);
        grid.addColumn(this::formatMilitaryRankAssignment)
                .setCaption("Присв. зв")
                .setWidth(136.0)
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(student -> dateFormat.format(student.getDateOfBirth()))
                .setCaption("Дата рождения")
                .setWidth(140.0)
                .setResizable(false);
        grid.addColumn(Student::getNationality)
                .setCaption("Нац.")
                .setResizable(false)
                .setWidth(75.0);
        grid.addColumn(Student::getFleet)
                .setCaption("Флот")
                .setResizable(false)
                .setWidth(75.0);
        grid.addColumn(Student::getAchievementList)
                .setCaption("Послужной список")
                .setWidth(300.0)
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(Student::getPosition)
                .setCaption("Должность")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(this::formatUniversity)
                .setResizable(false)
                .setCaption("Окончил ВУЗ");
        grid.addColumn(Student::getAveragePoints)
                .setResizable(false)
                .setCaption("Ср. балл атт.");
        grid.addColumn(Student::getForeignLanguage)
                .setCaption("Ин. яз.")
                .setResizable(false)
                .setWidth(100.0);
        grid.addColumn(Student::getIdentificationSeriesNumber)
                .setCaption("Серия и номер удост. личн.")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(Student::getPersonalNumber)
                .setCaption("Личный номер")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(this::formatAdmission)
                .setResizable(false)
                .setCaption("Форма допуска");
        grid.addColumn(Student::getPassportNumber)
                .setCaption("Паспорт допуска")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(this::formatPassportIssue)
                .setCaption("Выдан")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(Student::getInternationalPassportNumber)
                .setCaption("Загранпаспорт")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(Student::getFamilyInfo)
                .setCaption("Ф.И.О чл. семьи (№свид. о браке и рожд., кем, когда выд.)")
                .setResizable(false)
                .setWidth(300.0)
                .setSortable(false);
        grid.addColumn(Student::getWifeNationality)
                .setResizable(false)
                .setCaption("Гр. жены");
        grid.addColumn(Student::getAddress)
                .setCaption("Адрес")
                .setWidth(300.0)
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(Student::getStateRewards)
                .setCaption("Гос. нагр.")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(Student::getDiplomaTopic)
                .setCaption("Направление дипл. работы")
                .setResizable(false)
                .setSortable(false);
        grid.addColumn(Student::getPreliminaryAllocation)
                .setCaption("Предв. распред.")
                .setResizable(false)
                .setWidth(300.0)
                .setSortable(false);
        grid.addColumn(Student::getFinalAllocation)
                .setCaption("Оконч. распред.")
                .setResizable(false)
                .setWidth(300.0)
                .setSortable(false);
        grid.addColumn(Student::getAdditionalInfo)
                .setResizable(false)
                .setWidth(300.0)
                .setCaption("Примечания");
        grid.setFrozenColumnCount(2);


        // Instantiate and edit new Customer the new button is clicked
        addNewBtn.addClickListener(e -> {
            editor.editStudent(new Student());
            editor.makeVisible();
        });

        final HasValue.ValueChangeListener updateGrid = valueChangeEvent -> updateGrid();

        // Replace listing with filtered content when user changes lastNameFilter
        lastNameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        lastNameFilter.addValueChangeListener(updateGrid);
        rankFilter.addValueChangeListener(updateGrid);
        fleetFilter.addValueChangeListener(updateGrid);
        graduationYearFilter.addValueChangeListener(updateGrid);


        grid.addItemClickListener((ItemClickListener) itemClick -> {
            if (itemClick.getMouseEventDetails().isDoubleClick()) {
                editor.editStudent((Student) itemClick.getItem());
                editor.makeVisible();
            }
        });


        // Listen changes made by the editor, refresh data from backend
        editor.setChangeHandler(() -> {
            editor.setVisible(false);
            loadAll();
            updateGrid();
        });

        // Initialize listing
        loadAll();
        updateGrid();
    }

    private void updateGrid() {
        String lastNameFilterValue = lastNameFilter.getValue() == null ? "" : lastNameFilter.getValue().toLowerCase();
        grid.setItems(allStudents.stream()
                .filter(person -> {
                            String onDisplay = String.format("%s %s", person.getLastName(), person.getFirstName());
                            return onDisplay.toLowerCase().contains(lastNameFilterValue);
                        }
                )
                .filter(person -> rankFilter.getValue() == null || Objects.equals(person.getMilitaryRank(), rankFilter.getValue()))
                .filter(person -> fleetFilter.getValue() == null || Objects.equals(person.getFleet(), fleetFilter.getValue()))
                .filter(person -> graduationYearFilter.getValue() == null ||
                        graduationYearFilter.getValue().isEmpty() ||
                        String.valueOf(person.getGraduationYear()).startsWith(graduationYearFilter.getValue())
                )
                .collect(Collectors.toList())
        );
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
        String admissionDate = student.getAdmissionDate() == null ? "" : dateFormat.format(student.getAdmissionDate());
        return admissionForm + "\n" + admissionDate;
    }

    private String formatMilitaryRank(Student student) {
        String rankName = student.getMilitaryRank() == null ? "" : student.getMilitaryRank().getName();
        return rankName;
    }

    private String formatMilitaryRankAssignment(Student student) {
        String militaryRankAwardDate = student.getMilitaryRankAwardDate() == null ? "" : dateFormat.format(student.getMilitaryRankAwardDate());
        String militaryRankOrderName = student.getMilitaryRankOrderName() == null ? "" : student.getMilitaryRankOrderName();
        return militaryRankAwardDate + "\n" + militaryRankOrderName;
    }

    private String formatUniversity(Student student) {
        String universityTitle = student.getUniversity() == null ? "" : student.getUniversity().getTitle();
        String graduationYear = student.getGraduationYear() == null ? "" : String.valueOf(student.getGraduationYear());
        return format("%s в %s г.", universityTitle, graduationYear);
    }


    void loadAll() {
        allStudents = repo.findAll();
    }

    private String getGridPicture(Student student) {
        final String imageBase64;
        if (student.getPhotoBase64() != null) {
            imageBase64 = student.getPhotoBase64();
        } else {
            imageBase64 = placeHolderImagebase64;
        }
        return String.format("<img height=\"75px\" width=\"75px\" src=\"data:image/jpeg;base64,%s\" />", imageBase64);
    }

}
