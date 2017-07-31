package ru.ifmo.ctddev.isaev.studentsdb.enums;

/**
 * @author iisaev
 */
public enum Fleet implements MyEnum {
    BSF("ЧФ", "BSF"),
    POF("ТОФ", "POF"),
    CS("ЧЦП", "CS");

    private final String dbKey;

    private final String name;

    Fleet(String name, String dbKey) {
        this.name = name;
        this.dbKey = dbKey;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDbKey() {
        return dbKey;
    }
}
