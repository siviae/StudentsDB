package ru.ifmo.ctddev.isaev.studentsdb.enums;

/**
 * @author iisaev
 */
public enum MilitaryRank implements MyEnum {
    CAP_1_RANK("Капитан 1 ранга", "C1"),
    CAP_2_RANK("Капитан 2 ранга", "C2"),
    CAP_3_RANK("Капитан 3 ранга", "C3");

    private final String dbKey;

    private final String name;

    MilitaryRank(String name, String dbKey) {
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
