package ru.ifmo.ctddev.isaev.studentsdb.ui;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.codec.binary.Base64;
import org.vaadin.dialogs.ConfirmDialog;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.editor.ImageUploader;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;


public class StudentEditor {

    public static final String DATE_FORMAT = "dd.MM.yyyy";

    private final StudentDao repository;

    private final AtomicBoolean isVisible = new AtomicBoolean(false);

    /**
     * The currently edited customer
     */
    private Student customer;

    private final TextField firstName;

    private final TextField lastName;

    private final TextField patronymic;

    private final DateField dateOfBirth = new DateField("Дата рождения");

    private final ComboBox<String> militaryRank;

    private final DateField militaryRankAwardDate = new DateField("Дата присвоения");

    private final TextField militaryRankOrderName;

    private final ComboBox<String> nationality;

    private final ComboBox<String> fleet;

    private final TextField graduationYear = new TextField("Год выпуска");

    private final TextArea achievementList;

    private final TextArea position;

    private final ComboBox<String> university;

    private final TextField averagePoints;

    private final ComboBox<String> foreignLanguage;

    private final TextField identificationNumber;

    private final TextField personalNumber;

    private final TextField admissionForm;

    private final DateField admissionDate = new DateField("Дата получения");

    private final TextField passportNumber = new TextField("Серия и номер");

    private final TextField passportIssuer = new TextField("Выдан");

    private final DateField passportIssueDate = new DateField("Дата выдачи");

    private final CheckBox internationalPassport = new CheckBox("Загран. паспорт");

    private final TextArea familyInfo;

    private final ComboBox<String> wifeNationality;

    private final TextField address = new TextField("Адрес места жительства");

    private final TextField stateRewards;

    private final TextField diplomaTopic;

    private final TextArea allocation = new TextArea("Распределение");

    private final TextArea additionalInfo;

    private final Button saveButton = new Button("Сохранить", FontAwesome.SAVE);

    private final Button removeButton = new Button("Удалить", FontAwesome.TRASH_O);

    private final Button closeButton = new Button("Закрыть");

    private final Image photo;

    private final Binder<Student> binder = new Binder<>(Student.class);

    private final VerticalLayout mainLayout;

    private final MainUI mainUI;

    public StudentEditor(StudentDao repository, MainUI mainUI) {
        this.repository = repository;
        this.mainUI = mainUI;
        this.photo = new Image("Фотография");
        removeButton.setStyleName(ValoTheme.BUTTON_DANGER);
        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        this.additionalInfo = new TextArea("Примечания");
        binder.forField(additionalInfo)
                .withNullRepresentation("")
                .bind(Student::getAdditionalInfo, Student::setAdditionalInfo);
        binder.forField(allocation)
                .withNullRepresentation("")
                .bind(Student::getAllocation, Student::setAllocation);
        this.diplomaTopic = new TextField("Направление дипломной работы");
        this.stateRewards = new TextField("Гос. награды");
        this.wifeNationality = new ComboBox<>("Гражданство жены");
        this.wifeNationality.setWidth("100px");
        this.familyInfo = new TextArea("Информация о семье");
        passportIssueDate.setDateFormat(DATE_FORMAT);
        admissionDate.setDateFormat(DATE_FORMAT);
        this.admissionForm = new TextField("Форма допуска");
        this.personalNumber = new TextField("Личный номер");
        this.identificationNumber = new TextField("Серия и номер уд. личности");
        this.averagePoints = new TextField("Средний балл аттестата");
        binder.forField(averagePoints)
                .withNullRepresentation("")
                .withConverter(
                        new StringToIntegerConverter(0, "Введите число"))
                .bind(Student::getAveragePoints, Student::setAveragePoints);
        this.university = new ComboBox<>("Окончил ВУЗ");
        this.foreignLanguage = new ComboBox<>("Ин. яз.");
        this.position = new TextArea("Должность");
        this.achievementList = new TextArea("Послужной список");
        this.fleet = new ComboBox<>("Флот");
        this.nationality = new ComboBox<>("Национальность");
        nationality.setWidth("100px");
        this.militaryRankOrderName = new TextField("Приказ");
        this.firstName = new TextField("Имя");
        binder.forField(firstName)
                .asRequired("Необходимо заполнить")
                .bind(Student::getFirstName, Student::setFirstName);
        this.lastName = new TextField("Фамилия");
        binder.forField(lastName)
                .asRequired("Необходимо заполнить")
                .bind(Student::getLastName, Student::setLastName);
        this.patronymic = new TextField("Отчество");
        binder.forField(patronymic)
                .asRequired("Необходимо заполнить")
                .bind(Student::getPatronymic, Student::setPatronymic);
        dateOfBirth.setDateFormat(DATE_FORMAT);
        binder.forField(dateOfBirth)
                .asRequired("Необходимо заполнить")
                .bind(Student::getDateOfBirth, Student::setDateOfBirth);
        this.militaryRank = new ComboBox<>("Воинское звание");
        binder.forField(militaryRank)
                .bind(Student::getMilitaryRank, Student::setMilitaryRank);
        militaryRankAwardDate.setDateFormat(DATE_FORMAT);
        photo.setWidth("200px");
        ImageUploader receiver = new ImageUploader(binder, photo);
        Upload upload = new Upload("Загрузить фотографию", receiver);
        upload.addSucceededListener(receiver);
        VerticalLayout photoLayout = new VerticalLayout(photo, upload, saveButton, removeButton, closeButton);
        photoLayout.setWidth("250px");

        Panel basicInfo = new Panel("Основная информация",
                new VerticalLayout(
                        new HorizontalLayout(lastName, firstName, patronymic, dateOfBirth, nationality)
                )
        );
        basicInfo.setWidth("100%");
        HorizontalLayout achievementPos = new HorizontalLayout(achievementList, position);
        achievementPos.setWidth("100%");
        achievementPos.setHeight("200px");
        achievementPos.setExpandRatio(achievementList, 0.5f);
        achievementList.setSizeFull();
        achievementList.setWordWrap(true);
        achievementPos.setExpandRatio(position, 0.5f);
        position.setSizeFull();
        position.setWordWrap(true);
        Panel militaryInfo = new Panel("Должность",
                new VerticalLayout(
                        new HorizontalLayout(militaryRank, militaryRankAwardDate, militaryRankOrderName, fleet),
                        achievementPos
                )
        );


        Panel accessPanel = new Panel("Допуск",
                new VerticalLayout(
                        new HorizontalLayout(identificationNumber, admissionForm, admissionDate)
                )
        );

        Panel educationInfo = new Panel("Образование",
                new VerticalLayout(
                        new HorizontalLayout(university, graduationYear, averagePoints, foreignLanguage),
                        diplomaTopic
                )
        );
        diplomaTopic.setWidth("100%");

        Panel passport = new Panel("Паспорт",
                new VerticalLayout(
                        new HorizontalLayout(passportNumber, passportIssuer, passportIssueDate, internationalPassport)
                )
        );

        HorizontalLayout wifeNationalityAddress = new HorizontalLayout(wifeNationality, address);
        wifeNationalityAddress.setWidth("100%");
        address.setSizeFull();
        wifeNationalityAddress.setExpandRatio(address, 1.0f);

        Panel familyInfoPanel = new Panel("Семья",
                new VerticalLayout(
                        familyInfo,
                        wifeNationalityAddress
                )
        );
        familyInfo.setWordWrap(true);
        familyInfo.setWidth("100%");

        HorizontalLayout allocations = new HorizontalLayout(allocation);
        allocations.setHeight("200px");
        allocations.setWidth("100%");
        allocation.setSizeFull();
        allocation.setWordWrap(true);
        allocations.setExpandRatio(allocation, 0.5f);
        Panel allocationPanel = new Panel("Распределение",
                new VerticalLayout(
                        allocations
                )
        );

        additionalInfo.setWidth("100%");
        additionalInfo.setHeight("300px");
        VerticalLayout editorPanel = new VerticalLayout(
                basicInfo, militaryInfo, accessPanel,
                educationInfo, passport,
                familyInfoPanel, allocationPanel,
                additionalInfo
        );
        editorPanel.setWidth("1000px");
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                photoLayout,
                editorPanel
        );
        mainLayout = new VerticalLayout(horizontalLayout);
        mainLayout.setSpacing(true);
        //setContent(mainLayout);

        // bind using naming convention
        binder.bindInstanceFields(this);
        bindEntityFields();
        init();
    }

    public VerticalLayout getMainLayout() {
        return mainLayout;
    }


    private void bindEntityFields() {
        university.setNewItemHandler((ComboBox.NewItemHandler) string -> binder.getBean().setUniversity(string));
        foreignLanguage.setNewItemHandler((ComboBox.NewItemHandler) string -> binder.getBean().setForeignLanguage(string));
        nationality.setNewItemHandler((ComboBox.NewItemHandler) string -> binder.getBean().setNationality(string));
        wifeNationality.setNewItemHandler((ComboBox.NewItemHandler) string -> binder.getBean().setWifeNationality(string));
        militaryRank.setNewItemHandler((ComboBox.NewItemHandler) string -> binder.getBean().setMilitaryRank(string));
        fleet.setNewItemHandler((ComboBox.NewItemHandler) string -> binder.getBean().setFleet(string));
    }

    public final void editStudent(Student c) {
        customer = c;
        binder.setBean(customer);
        if (customer.getPhotoBase64() == null) {
            photo.setSource(new FileResource(new File("src/main/resources/icons/photo_placeholder.jpg")));
        } else {
            byte[] imageBytes;
            imageBytes = Base64.decodeBase64(customer.getPhotoBase64());
            byte[] finalImageBytes = imageBytes;
            photo.setSource(new StreamResource(() -> new ByteArrayInputStream(finalImageBytes), ""));
        }
        setDropdownItems();
    }

    private void setDropdownItems() {
        university.setItems(mainUI.getComboBoxAggregator().getUniversities());
        foreignLanguage.setItems(mainUI.getComboBoxAggregator().getForeignLanguages());
        nationality.setItems(mainUI.getComboBoxAggregator().getNationalities());
        wifeNationality.setItems(mainUI.getComboBoxAggregator().getWifeNationalities());
        militaryRank.setItems(mainUI.getComboBoxAggregator().getMilitaryRanks());
        fleet.setItems(mainUI.getComboBoxAggregator().getFleets());
    }

    private void init() {
        saveButton.addClickListener(e -> {
            binder.removeBean();
            this.repository.save(customer);
            if (customer.getId() == null) {
                mainUI.reloadUpdateAndShow();
            } else {
                mainUI.updateAndShow();
            }
        });
        // Configure and style components
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        closeButton.addClickListener(e -> {
            binder.removeBean();
            mainUI.openGrid();
        });
        closeButton.setClickShortcut(ShortcutAction.KeyCode.ESCAPE);

        removeButton.addClickListener(e -> {
            Student currentStudent = binder.getBean();
            String fio = String.format("%s %s %s", currentStudent.getLastName(), currentStudent.getFirstName(), currentStudent.getPatronymic());
            ConfirmDialog.show(mainUI, String.format("Удалить \"%s\"", fio), "Вы уверены?",
                    "Удалить", "Отмена", (ConfirmDialog.Listener) dialog -> {
                        if (dialog.isConfirmed()) {
                            repository.remove(customer);
                            mainUI.reloadUpdateAndShow();
                        }
                    });
        });
    }

    public void makeVisible() {
        mainLayout.setVisible(true);
    }

    public void hide() {
        mainLayout.setVisible(false);
    }
}
