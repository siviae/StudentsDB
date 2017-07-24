package ru.ifmo.ctddev.isaev.studentsdb.sample;

import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import ru.ifmo.ctddev.isaev.studentsdb.editor.StudentEditor;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author iisaev
 */
class ModalUI extends Window {

    private final StudentEditor editor;

    public ModalUI(StudentEditor editor, AtomicBoolean isModalOpened) {
        super("Добавить студента"); // Set window caption
        this.editor = editor;
        center();
        VerticalLayout mainLayout = new VerticalLayout(
                editor
        );
        setContent(mainLayout);
        addCloseListener((CloseListener) closeEvent -> isModalOpened.set(false));
        editor.getSaveButton().addClickListener((Button.ClickListener) clickEvent -> close());
    }
}
