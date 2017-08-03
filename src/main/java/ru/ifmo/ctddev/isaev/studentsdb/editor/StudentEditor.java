package ru.ifmo.ctddev.isaev.studentsdb.editor;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.dao.UniversityDao;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;
import ru.ifmo.ctddev.isaev.studentsdb.entity.University;
import ru.ifmo.ctddev.isaev.studentsdb.enums.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * A simple example to introduce building forms. As your real application is probably much
 * more complicated than this example, you could re-use this form in multiple places. This
 * example component is only used in VaadinUI.
 * <p>
 * In a real world application you'll most likely using a common super class for all your
 * forms - less code, better UX. See e.g. AbstractForm in Viritin
 * (https://vaadin.com/addon/viritin).
 */
@SpringComponent
@UIScope
public class StudentEditor extends Window {

    private final StudentDao repository;

    private final UniversityDao universityDao;

    private final AtomicBoolean isVisible = new AtomicBoolean(false);

    /**
     * The currently edited customer
     */
    private Student customer;

    private final TextField firstName;

    private final TextField lastName;

    private final TextField patronymic;

    private final DateField dateOfBirth;

    private final TextField graduationYear;

    private final ComboBox<EducationForm> educationForm;

    private final ComboBox<MilitaryRank> militaryRank;

    private final DateField militaryRankAwardDate;

    private final TextField militaryRankOrderName;

    private final ComboBox<Nationality> nationality;

    private final ComboBox<Fleet> fleet;

    private final ComboBox<GraduationType> graduationType;

    private final TextArea achievementList;

    private final TextArea position;

    private final ComboBox<University> universityDropdown;

    private final TextField averagePoints;

    private final ComboBox<Language> foreignLanguage;

    private final TextField identificationNumber;

    private final TextField personalNumber;

    private final TextField admissionForm;

    private final DateField admissionDate;

    private final TextField passportNumber;

    private final TextField passportIssuer;

    private final DateField passportIssueDate;

    private final TextField internationalPassportNumber;

    private final TextArea familyInfo;

    private final ComboBox<Nationality> wifeNationality;

    private final TextField address;

    private final TextField stateRewards;

    private final TextField diplomaTopic;

    private final TextField preliminaryAllocation;

    private final TextField finalAllocation;

    private final TextArea additionalInfo;

    private final Button saveButton;

    private final Button removeButton;

    private final Image photo;

    private final List<University> universities = new ArrayList<>();

    Binder<Student> binder = new Binder<>(Student.class);

    @Autowired
    public StudentEditor(StudentDao repository, UniversityDao universityDao) {
        super("Добавить сотрудника/студента");
        this.repository = repository;
        this.universityDao = universityDao;
        this.photo = new Image("Фотография");
        this.removeButton = new Button("Удалить", FontAwesome.TRASH_O);
        removeButton.setStyleName(ValoTheme.BUTTON_DANGER);
        this.saveButton = new Button("Сохранить", FontAwesome.SAVE);
        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        this.additionalInfo = new TextArea("Примечания");
        binder.forField(additionalInfo)
                .withNullRepresentation("")
                .bind(Student::getAdditionalInfo, Student::setAdditionalInfo);
        this.finalAllocation = new TextField("Окончательное распределение");
        binder.forField(finalAllocation)
                .withNullRepresentation("")
                .bind(Student::getFinalAllocation, Student::setFinalAllocation);
        this.preliminaryAllocation = new TextField("Предварительное распределение");
        this.diplomaTopic = new TextField("Направление дипломной работы");
        this.stateRewards = new TextField("Гос. награды");
        this.address = new TextField("Адрес");
        this.wifeNationality = new ComboBox<>("Гражданство жены", Arrays.asList(Nationality.values()));
        this.familyInfo = new TextArea("Информация о семье");
        this.internationalPassportNumber = new TextField("Номер");
        this.passportIssueDate = new DateField("Дата выдачи");
        this.passportIssuer = new TextField("Выдан");
        this.passportNumber = new TextField("Серия и номер");
        this.admissionDate = new DateField("Дата получения");
        this.admissionForm = new TextField("Форма допуска");
        this.personalNumber = new TextField("Личный номер");
        this.identificationNumber = new TextField("Серия и номер уд. личности");
        this.averagePoints = new TextField("Средний балл аттестата");
        binder.forField(averagePoints)
                .withNullRepresentation("")
                .withConverter(
                        new StringToIntegerConverter(0, "Средний балл"))
                .bind(Student::getGraduationYear, Student::setGraduationYear);
        this.universityDropdown = new ComboBox<>("Окончил ВУЗ", universities);
        this.foreignLanguage = new ComboBox<>("Ин. яз.", Arrays.asList(Language.values()));
        this.position = new TextArea("Должность");
        this.achievementList = new TextArea("Послужной список");
        this.graduationType = new ComboBox<>("Образование", Arrays.asList(GraduationType.values()));
        this.fleet = new ComboBox<>("Флот", Arrays.asList(Fleet.values()));
        this.nationality = new ComboBox<>("Национальность", Arrays.asList(Nationality.values()));
        this.militaryRankOrderName = new TextField("Приказ");
        this.educationForm = new ComboBox<>("Форма обучения", Arrays.asList(EducationForm.values()));
        this.firstName = new TextField("Имя");
        this.lastName = new TextField("Фамилия");
        this.patronymic = new TextField("Отчество");
        this.dateOfBirth = new DateField("Дата рождения");
        this.graduationYear = new TextField("Год выпуска");
        binder.forField(graduationYear)
                .withNullRepresentation("")
                .withConverter(
                        new StringToIntegerConverter(0, "Год"))
                .bind(Student::getGraduationYear, Student::setGraduationYear);
        this.militaryRank = new ComboBox<>("Воинское звание", Arrays.asList(MilitaryRank.values()));
        binder.forField(militaryRank)
                .bind(Student::getMilitaryRank, Student::setMilitaryRank);
        this.militaryRankAwardDate = new DateField("Дата присвоения");

        init();
    }

    private void init() {
        photo.setWidth("200px");
        ImageUploader receiver = new ImageUploader(binder, photo);
        Upload upload = new Upload("Загрузить фотографию", receiver);
        upload.addSucceededListener(receiver);
        VerticalLayout photoLayout = new VerticalLayout(photo, upload, saveButton, removeButton);
        photoLayout.setWidth("250px");

        center();

        Panel basicInfo = new Panel("Основная информация",
                new HorizontalLayout(lastName, firstName, patronymic, dateOfBirth, nationality)
        );


        HorizontalLayout achievementPos = new HorizontalLayout(achievementList, position);
        achievementPos.setWidthUndefined();
        achievementList.setWidth("70%");
        achievementList.setHeight("100px");
        Panel militaryInfo = new Panel("Должность",
                new VerticalLayout(
                        new HorizontalLayout(militaryRank, militaryRankAwardDate, militaryRankOrderName, fleet),
                        achievementPos
                )
        );


        Panel accessPanel = new Panel("Допуск",
                new HorizontalLayout(identificationNumber, admissionForm, admissionDate)
        );

        Panel educationInfo = new Panel("Образование",
                new HorizontalLayout(universityDropdown, averagePoints, foreignLanguage, diplomaTopic)
        );

        Panel passport = new Panel("Паспорт",
                new HorizontalLayout(passportNumber, passportIssuer, passportIssueDate)
        );

        Panel internationalPassport = new Panel("Загран. паспорт",
                new HorizontalLayout(internationalPassportNumber)
        );

        Panel familyInfoPanel = new Panel("Семья",
                new VerticalLayout(
                        familyInfo,
                        new HorizontalLayout(wifeNationality, address)
                )
        );

        Panel allocationPanel = new Panel("Распределение",
                new HorizontalLayout(preliminaryAllocation, finalAllocation)
        );

        VerticalLayout editorPanel = new VerticalLayout(
                basicInfo, militaryInfo, accessPanel,
                educationInfo, passport, internationalPassport,
                familyInfoPanel, allocationPanel, additionalInfo
        );
        editorPanel.setExpandRatio(militaryInfo, 1.0f);
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                photoLayout,
                editorPanel
        );
        VerticalLayout mainLayout = new VerticalLayout(horizontalLayout);
        mainLayout.setSpacing(true);
        setContent(mainLayout);

        // bind using naming convention
        binder.bindInstanceFields(this);
        bindEntityFields();

        // Configure and style components
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to saveButton, removeButton and reset
        saveButton.addClickListener(e -> {
            repository.save(customer);
            binder.removeBean();
            hide();
        });
        addCloseListener((CloseListener) closeEvent -> hide());
    }


    private void bindEntityFields() {
        educationForm.setItemCaptionGenerator(EducationForm::getName);
        graduationType.setItemCaptionGenerator(GraduationType::getName);
        universityDropdown.setItemCaptionGenerator(University::getTitle);
        universityDropdown.setNewItemHandler((ComboBox.NewItemHandler) s -> {
            University university = new University();
            university.setTitle(s);
            University savedUniversity = universityDao.save(university);
            universities.add(savedUniversity);
            binder.getBean().setUniversity(savedUniversity);
        });
        foreignLanguage.setItemCaptionGenerator(Language::getName);
        nationality.setItemCaptionGenerator(Nationality::getName);
        wifeNationality.setItemCaptionGenerator(Nationality::getName);
        militaryRank.setItemCaptionGenerator(MilitaryRank::getName);
        fleet.setItemCaptionGenerator(Fleet::getName);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editStudent(Student c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        if (c.getId() != null) {
            customer = repository.findById(c.getId());
        } else {
            customer = c;
        }
        universities.clear();
        universities.addAll(universityDao.findAll());
        universityDropdown.clear();

        binder.setBean(customer);
        if (customer.getPhotoBase64() == null) {
            photo.setSource(new FileResource(new File("src/main/resources/icons/photo_placeholder.jpg")));
        } else {
            byte[] imageBytes;
            try {
                imageBytes = Base64.decode(customer.getPhotoBase64());
            } catch (Base64DecodingException e) {
                imageBytes = new byte[0];
            }
            byte[] finalImageBytes = imageBytes;
            photo.setSource(new StreamResource(() -> new ByteArrayInputStream(finalImageBytes), ""));
        }

        setVisible(true);
    }

    public void setChangeHandler(ChangeHandler h) {
        saveButton.addClickListener(e -> h.onChange());
        removeButton.addClickListener(e -> {
            Student currentStudent = binder.getBean();
            String fio = String.format("%s %s %s", currentStudent.getLastName(), currentStudent.getFirstName(), currentStudent.getPatronymic());
            ConfirmDialog.show(this.getUI(), String.format("Удалить \"%s\"", fio), "Вы уверены?",
                    "Удалить", "Отмена", (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            repository.remove(customer);
                        }
                        h.onChange();
                    });
        });
    }

    public void makeVisible() {
        if (isVisible.compareAndSet(false, true)) {
            UI.getCurrent().addWindow(this);
        }
    }

    private void hide() {
        if (isVisible.compareAndSet(true, false)) {
            UI.getCurrent().removeWindow(this);
        }
    }
}
