package ru.ifmo.ctddev.isaev.studentsdb;

import ru.ifmo.ctddev.isaev.studentsdb.entity.Student;

import java.util.HashSet;
import java.util.Set;


/**
 * @author iisaev
 */
public class ComboBoxAggregator {
    private final Set<String> universities = new HashSet<>();

    private final Set<String> foreignLanguages = new HashSet<>();

    private final Set<String> nationalities = new HashSet<>();

    private final Set<String> wifeNationalities = new HashSet<>();

    private final Set<String> militaryRanks = new HashSet<>();

    private final Set<String> fleets = new HashSet<>();

    public Set<String> getUniversities() {
        return universities;
    }

    public Set<String> getForeignLanguages() {
        return foreignLanguages;
    }

    public Set<String> getNationalities() {
        return nationalities;
    }

    public Set<String> getWifeNationalities() {
        return wifeNationalities;
    }

    public Set<String> getMilitaryRanks() {
        return militaryRanks;
    }

    public Set<String> getFleets() {
        return fleets;
    }

    public void refresh(Student student) {
        if (student.getUniversity() != null) {
            universities.add(student.getUniversity());
        }
        if (student.getForeignLanguage() != null) {
            foreignLanguages.add(student.getForeignLanguage());
        }
        if (student.getNationality() != null) {
            nationalities.add(student.getNationality());
        }
        if (student.getWifeNationality() != null) {
            wifeNationalities.add(student.getWifeNationality());
        }
        if (student.getMilitaryRank() != null) {
            militaryRanks.add(student.getMilitaryRank());
        }
        if (student.getFleet() != null) {
            fleets.add(student.getFleet());
        }
    }
}
