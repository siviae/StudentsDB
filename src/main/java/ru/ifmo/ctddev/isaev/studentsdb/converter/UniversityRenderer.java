package ru.ifmo.ctddev.isaev.studentsdb.converter;

import com.vaadin.ui.renderers.AbstractRenderer;
import elemental.json.JsonValue;
import ru.ifmo.ctddev.isaev.studentsdb.entity.University;


/**
 * @author iisaev
 */
public class UniversityRenderer extends AbstractRenderer<Object, Object> {
    public UniversityRenderer() {
        super(Object.class, "");
    }

    @Override
    public JsonValue encode(Object value) {
        String result;
        if (value == null) {
            result = this.getNullRepresentation();
        } else {
            result = ((University) value).getTitle();
        }

        return this.encode(result, String.class);
    }
}
