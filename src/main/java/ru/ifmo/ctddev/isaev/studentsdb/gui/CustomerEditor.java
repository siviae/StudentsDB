package ru.ifmo.ctddev.isaev.studentsdb.gui;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;
import ru.ifmo.ctddev.isaev.studentsdb.dao.StudentsDao;
import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;


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
public class CustomerEditor extends VerticalLayout {

    private final StudentsDao repository;

    /**
     * The currently edited student
     */
    private Student student;

    /* Fields to edit properties in Customer entity */
    private TextField name = new TextField("First name");

    private TextField surname = new TextField("Last name");

    /* Action buttons */
    private Button save = new Button("Save", FontAwesome.SAVE);

    private Button cancel = new Button("Cancel");

    private Button delete = new Button("Delete", FontAwesome.TRASH_O);

    private CssLayout actions = new CssLayout(save, cancel, delete);

    private Binder<Student> binder = new Binder<>(Student.class);

    @Autowired
    public CustomerEditor(StudentsDao repository) {
        this.repository = repository;

        addComponents(name, surname, actions);

        // bind using naming convention
        binder.bindInstanceFields(this);

        // Configure and style components
        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        // wire action buttons to save, delete and reset
        save.addClickListener(e -> repository.save(student));
        delete.addClickListener(e -> repository.remove(student));
        cancel.addClickListener(e -> editCustomer(student));
        setVisible(false);
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
            student = repository.findById(c.getId());
        } else {
            student = c;
        }
        cancel.setVisible(persisted);

        // Bind student properties to similarly named fields
        // Could also use annotation or "manual binding" or programmatically
        // moving values from fields to entities before saving
        binder.setBean(student);

        setVisible(true);

        // A hack to ensure the whole form is visible
        save.focus();
        // Select all text in firstName field automatically
        name.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        // ChangeHandler is notified when either save or delete
        // is clicked
        save.addClickListener(e -> h.onChange());
        delete.addClickListener(e -> h.onChange());
    }

}