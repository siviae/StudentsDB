package ru.ifmo.ctddev.isaev.studentsdb.sample;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentsDao;
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

    private final StudentsDao repository;

    /**
     * The currently edited customer
     */
    private Student customer;

    private TextField firstName = new TextField("Имя");

    private TextField lastName = new TextField("Фамилия");

    private TextField patronymic = new TextField("Отчество");

    private DateField dateOfBirth = new DateField("Дата рождения");

    private DateField dateOfGraduation = new DateField("Дата выпуска");

    private ComboBox<EducationForm> educationForm = new ComboBox<>("Форма обучения", Arrays.asList(EducationForm.values()));

    private ComboBox<GraduationType> graduationType = new ComboBox<>("Образование", Arrays.asList(GraduationType.values()));

    /* Action buttons */
    private Button save = new Button("Сохранить", FontAwesome.SAVE);

    private Button cancel = new Button("Отмена");

    private Button delete = new Button("Удалить", FontAwesome.TRASH_O);

    private CssLayout actions = new CssLayout(save, cancel, delete);

    Binder<Student> binder = new Binder<>(Student.class);

    @Autowired
    public StudentEditor(StudentsDao repository) {
        this.repository = repository;

        addComponents(firstName, lastName, patronymic, dateOfBirth, dateOfGraduation, educationForm, graduationType, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);
        bindEntityFields();

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> repository.save(customer));
        delete.addClickListener(e -> repository.remove(customer));
        cancel.addClickListener(e -> editCustomer(customer));
        setVisible(false);
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
        cancel.setVisible(persisted);

        // Bind customer properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(customer);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
        // Select all text in firstName field automatically
        firstName.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }

}
