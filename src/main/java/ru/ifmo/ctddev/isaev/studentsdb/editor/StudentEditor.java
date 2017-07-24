package ru.ifmo.ctddev.isaev.studentsdb.editor;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentDao;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;
import ru.ifmo.ctddev.isaev.studentsdb.enums.EducationForm;
import ru.ifmo.ctddev.isaev.studentsdb.enums.GraduationType;

import java.util.Arrays;


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
public class StudentEditor extends VerticalLayout {

    private final StudentDao repository;

    /**
     * The currently edited customer
     */
    private Student customer;

    private final TextField firstName = new TextField("Имя");

    private final TextField lastName = new TextField("Фамилия");

    private final TextField patronymic = new TextField("Отчество");

    private final DateField dateOfBirth = new DateField("Дата рождения");

    private final TextField graduationYear = new TextField("Дата выпуска");

    private final ComboBox<EducationForm> educationForm = new ComboBox<>("Форма обучения", Arrays.asList(EducationForm.values()));

    private final ComboBox<GraduationType> graduationType = new ComboBox<>("Образование", Arrays.asList(GraduationType.values()));

    private final Button saveButton = new Button("Сохранить", FontAwesome.SAVE);

    private final Button delete = new Button("Удалить", FontAwesome.TRASH_O);

    private final Image photo = new Image("Фотография");

    private CssLayout actions = new CssLayout(saveButton, delete);

    Binder<Student> binder = new Binder<>(Student.class);

    @Autowired
    public StudentEditor(StudentDao repository) {
        this.repository = repository;
        ImageUploader receiver = new ImageUploader(photo);

        Upload upload = new Upload("Загрузить фотографию", receiver);
        upload.addSucceededListener(receiver);
        VerticalLayout editorPanel = new VerticalLayout(
                firstName, lastName, patronymic, dateOfBirth, graduationYear, educationForm, graduationType, upload, actions
        );
        VerticalLayout photoPanel = new VerticalLayout(photo);

        HorizontalLayout horizontalLayout = new HorizontalLayout(
                editorPanel,
                photoPanel
        );
        addComponent(horizontalLayout);

        // bind using naming convention
        binder.forField(graduationYear)
                .withNullRepresentation("")
                .withConverter(
                        new StringToIntegerConverter(0, "Год"))
                .bind(Student::getGraduationYear, Student::setGraduationYear);
        binder.bindInstanceFields(this);
        bindEntityFields();

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        saveButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
        saveButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to saveButton, delete and reset
        saveButton.addClickListener(e -> {
            binder.setBean(repository.save(customer));
        });
        delete.addClickListener(e -> repository.remove(customer));
    }

    private void bindEntityFields() {
        educationForm.setItemCaptionGenerator(EducationForm::getName);
        graduationType.setItemCaptionGenerator(GraduationType::getName);
    }

    public interface ChangeHandler {
        void onChange();
    }

    public final void editCustomer(Student c) {
        if (c == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = c.getId() != null;
        if (persisted) {
            // Find fresh entity for editing
            customer = repository.findById(c.getId());
        } else {
            customer = c;
        }

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        setVisible(true);

        // A hack to ensure the whole form is visible
        saveButton.focus();
        // Select all text in firstName field automatically
        firstName.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either saveButton or delete
        // is clicked
        saveButton.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }

    public Button getSaveButton() {
        return saveButton;
    }
}
