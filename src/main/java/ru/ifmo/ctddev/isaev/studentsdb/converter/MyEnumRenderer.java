package ru.ifmo.ctddev.isaev.studentsdb.converter;

import com.vaadin.ui.renderers.AbstractRenderer;
import elemental.json.JsonValue;
import ru.ifmo.ctddev.isaev.studentsdb.enums.MyEnum;


/**
 * @author iisaev
 */
public class MyEnumRenderer extends AbstractRenderer<Object, Object> {
    public MyEnumRenderer() {
        super(Object.class, "");
    }

    @Override
    public JsonValue encode(Object value) {
        String result;
        if (value == null) {
            result = this.getNullRepresentation();
        } else {
            result = ((MyEnum) value).getName();
        }

        return this.encode(result, String.class);
    }
}
