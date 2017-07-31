package ru.ifmo.ctddev.isaev.studentsdb.enums;

/**
 * @author iisaev
 */
public enum Nationality implements MyEnum {
    RUS("Рус.", "RUS"),
    BEL("Бел.", "BEL"),
    KAZ("Каз.", "KAZ"),
    TAT("Тат.", "TAT"),
    UZB("Узб.", "UZB");

    private final String dbKey;

    private final String name;

    Nationality(String name, String dbKey) {
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
