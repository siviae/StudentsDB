package ru.ifmo.ctddev.isaev.studentsdb.enums;

import ru.ifmo.ctddev.isaev.studentsdb.converter.MyEnumDbConverterUtils;

import javax.persistence.AttributeConverter;


/**
 * @author iisaev
 */
public enum MilitaryRank implements MyEnum {
    CADET("Курсант"),
    PRIVATE("Рядовой"),
    SAILOR("Матрос"),
    EFREITOR("Ефрейтор"),
    ST_SAILOR("Старший матрос"),
    JUN_SERJ("Младший сержант"),
    SERJ("Сержант"),
    ST_SERJ("Старший сержант"),
    STARSH("Старшина"),
    STARSH_2("Старшина 2 статьи"),
    STARSH_1("Старшина 1 статьи"),
    MAJ_STARSH("Главный старшина"),
    SHIP_STARSH("Главный корабельный старшина"),
    PRAPOR("Прапорщик"),
    ST_PRAPOR("Старший прапорщик"),
    MICHMAN("Мичман"),
    ST_MICHMAN("Старший мичман"),
    JUN_LEUT("Младший лейтенант"), // мальчик молодой
    LEUT("Лейтенант"),
    ST_LEUT("Старший лейтенант"),
    CAPT("Капитан"),
    CAP_LEUT("Капитан-лейтенант"),
    MAJOR("Майор"),
    PODPOLK("Подполковник"),
    POLK("Полковник"),
    CAP_1_RANK("Капитан 1 ранга"),
    CAP_2_RANK("Капитан 2 ранга"),
    CAP_3_RANK("Капитан 3 ранга"),
    GEN_MAJ("Генерал-майор"),
    GEN_LEUT("Генерал-лейтенант"),
    GEN_POLK("Генерал-полковник"),
    GEN_ARMY("Генерал армии"),
    MARSHAL("Маршал Российской Федерации"),
    CONTR_ADM("Контр-адмирал"),
    VICE_ADM("Вице-адмирал"),
    ADM_FLEET("Адмирал флота");


    private final String name;

    MilitaryRank(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDbKey() {
        return this.name();
    }

    @Override
    public String toString() {
        return getName();
    }

    public static class Converter implements AttributeConverter<MilitaryRank, String> {

        @Override
        public String convertToDatabaseColumn(MilitaryRank attribute) {
            return MyEnumDbConverterUtils.convertToDatabaseColumn(attribute);
        }

        @Override
        public MilitaryRank convertToEntityAttribute(String dbData) {
            return MyEnumDbConverterUtils.convertToEntityAttribute(dbData, values());
        }
    }
}
