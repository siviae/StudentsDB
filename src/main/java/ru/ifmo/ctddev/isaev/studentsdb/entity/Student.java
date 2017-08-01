package ru.ifmo.ctddev.isaev.studentsdb.entity;


import ru.ifmo.ctddev.isaev.studentsdb.converter.TimestampToLocalDateConverter;
import ru.ifmo.ctddev.isaev.studentsdb.enums.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "student")
public class Student {
    private Long id;

    private String firstName;

    private String lastName;

    private String patronymic;

    private LocalDate dateOfBirth;

    private Integer graduationYear;

    private EducationForm educationForm;

    private GraduationType graduationType;

    private MilitaryRank militaryRank;

    private LocalDate militaryRankAwardDate;

    private String militaryRankOrderName;

    private Nationality nationality;

    private Fleet fleet;

    private String achievementList;

    private String position;

    private University university;

    private Integer averagePoints;

    private Language foreignLanguage;

    private String identificationSeriesNumber;

    private String personalNumber;

    private String admissionForm;

    private LocalDate admissionDate;

    private String passportNumber;

    private LocalDate passportIssueDate;

    private String passportIssuer;

    private String internationalPassportNumber;

    private String familyInfo;

    private Nationality wifeNationality;

    private String address;

    private String stateRewards;

    private String diplomaTopic;

    private String preliminaryAllocation;

    private String finalAllocation;

    private String additionalInfo;

    public Student(Long id,
                   String firstName,
                   String lastName,
                   String patronymic,
                   LocalDate dateOfBirth,
                   Integer graduationYear,
                   EducationForm educationForm,
                   GraduationType graduationType
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.dateOfBirth = dateOfBirth;
        this.graduationYear = graduationYear;
        this.educationForm = educationForm;
        this.graduationType = graduationType;
    }

    public Student() {//do not remove, used for deserializing
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_seq")
    @SequenceGenerator(name = "student_seq", sequenceName = "seq_student", allocationSize = 1)
    public Long getId() {
        return id;
    }

    @Basic
    @Column(name = "first_name")
    public String getFirstName() {
        return firstName;
    }

    @Basic
    @Column(name = "last_name")
    public String getLastName() {
        return lastName;
    }

    @Basic
    @Column(name = "patronymic")
    public String getPatronymic() {
        return patronymic;
    }

    @Basic
    @Column(name = "date_of_birth")
    @Convert(converter = TimestampToLocalDateConverter.class)
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    @Basic
    @Column(name = "graduation_year")
    public Integer getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }

    @Basic
    @Column(name = "education_form")
    @Convert(converter = EducationForm.Converter.class)
    public EducationForm getEducationForm() {
        return educationForm;
    }

    @Basic
    @Column(name = "graduation_type")
    @Convert(converter = GraduationType.Converter.class)
    public GraduationType getGraduationType() {
        return graduationType;
    }

    public void setGraduationType(GraduationType graduationType) {
        this.graduationType = graduationType;
    }

    public void setEducationForm(EducationForm educationForm) {
        this.educationForm = educationForm;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


    @Basic
    @Column(name = "military_rank")
    @Convert(converter = MilitaryRank.Converter.class)
    public MilitaryRank getMilitaryRank() {
        return militaryRank;
    }

    public void setMilitaryRank(MilitaryRank militaryRank) {
        this.militaryRank = militaryRank;
    }

    @Basic
    @Column(name = "military_rank_award_date")
    @Convert(converter = TimestampToLocalDateConverter.class)
    public LocalDate getMilitaryRankAwardDate() {
        return militaryRankAwardDate;
    }

    public void setMilitaryRankAwardDate(LocalDate militaryRankAwardDate) {
        this.militaryRankAwardDate = militaryRankAwardDate;
    }

    @Basic
    @Column(name = "military_rank_order_name")
    public String getMilitaryRankOrderName() {
        return militaryRankOrderName;
    }

    public void setMilitaryRankOrderName(String militaryRankOrderName) {
        this.militaryRankOrderName = militaryRankOrderName;
    }

    @Basic
    @Column(name = "nationality")
    @Convert(converter = Nationality.Converter.class)
    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
    }

    @Basic
    @Column(name = "fleet")
    @Convert(converter = Fleet.Converter.class)
    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    @Basic
    @Column(name = "achievement_list")
    public String getAchievementList() {
        return achievementList;
    }

    public void setAchievementList(String achievementList) {
        this.achievementList = achievementList;
    }

    @Basic
    @Column(name = "position")
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id")
    public University getUniversity() {
        return university;
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    @Basic
    @Column(name = "average_points")
    public Integer getAveragePoints() {
        return averagePoints;
    }

    public void setAveragePoints(Integer averagePoints) {
        this.averagePoints = averagePoints;
    }

    @Basic
    @Column(name = "foreign_lang")
    @Convert(converter = Language.Converter.class)
    public Language getForeignLanguage() {
        return foreignLanguage;
    }

    public void setForeignLanguage(Language foreignLanguage) {
        this.foreignLanguage = foreignLanguage;
    }

    @Basic
    @Column(name = "identification_series_number")
    public String getIdentificationSeriesNumber() {
        return identificationSeriesNumber;
    }

    public void setIdentificationSeriesNumber(String identificationSeriesNumber) {
        this.identificationSeriesNumber = identificationSeriesNumber;
    }

    @Basic
    @Column(name = "personal_number")
    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    @Basic
    @Column(name = "admission_form")
    public String getAdmissionForm() {
        return admissionForm;
    }

    public void setAdmissionForm(String admissionForm) {
        this.admissionForm = admissionForm;
    }

    @Basic
    @Column(name = "admission_date")
    @Convert(converter = TimestampToLocalDateConverter.class)
    public LocalDate getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    @Basic
    @Column(name = "passport_number")
    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Basic
    @Column(name = "passport_issue_date")
    @Convert(converter = TimestampToLocalDateConverter.class)
    public LocalDate getPassportIssueDate() {
        return passportIssueDate;
    }

    public void setPassportIssueDate(LocalDate passportIssueDate) {
        this.passportIssueDate = passportIssueDate;
    }

    @Basic
    @Column(name = "passport_issuer")
    public String getPassportIssuer() {
        return passportIssuer;
    }

    public void setPassportIssuer(String passportIssuer) {
        this.passportIssuer = passportIssuer;
    }

    @Basic
    @Column(name = "international_passport_number")
    public String getInternationalPassportNumber() {
        return internationalPassportNumber;
    }

    public void setInternationalPassportNumber(String internationalPassportNumber) {
        this.internationalPassportNumber = internationalPassportNumber;
    }

    @Basic
    @Column(name = "family_info")
    public String getFamilyInfo() {
        return familyInfo;
    }

    public void setFamilyInfo(String familyInfo) {
        this.familyInfo = familyInfo;
    }

    @Basic
    @Column(name = "wife_nationality")
    @Convert(converter = Nationality.Converter.class)
    public Nationality getWifeNationality() {
        return wifeNationality;
    }

    public void setWifeNationality(Nationality wifeNationality) {
        this.wifeNationality = wifeNationality;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "state_rewards")
    public String getStateRewards() {
        return stateRewards;
    }

    public void setStateRewards(String stateRewards) {
        this.stateRewards = stateRewards;
    }

    @Basic
    @Column(name = "diploma_topic")
    public String getDiplomaTopic() {
        return diplomaTopic;
    }

    public void setDiplomaTopic(String diplomaTopic) {
        this.diplomaTopic = diplomaTopic;
    }

    @Basic
    @Column(name = "preliminary_allocation")
    public String getPreliminaryAllocation() {
        return preliminaryAllocation;
    }

    public void setPreliminaryAllocation(String preliminaryAllocation) {
        this.preliminaryAllocation = preliminaryAllocation;
    }

    @Basic
    @Column(name = "final_allocation")
    public String getFinalAllocation() {
        return finalAllocation;
    }

    public void setFinalAllocation(String finalAllocation) {
        this.finalAllocation = finalAllocation;
    }

    @Basic
    @Column(name = "additional_info")
    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student student = (Student) o;
        return id != null && student.id != null && Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
