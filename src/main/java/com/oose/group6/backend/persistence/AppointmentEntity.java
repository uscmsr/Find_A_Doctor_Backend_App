package com.oose.group6.backend.persistence;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "appointment", schema = "oose", catalog = "")
public class AppointmentEntity {
    private long id;
    private Timestamp startTime;
    private Timestamp endTime;
    private String location;
    private String symptom;
    private String vitals;
    private String allergies;
    private String followUp;
    private MedicalStaffEntity medicalStaff;
    private PatientEntity patient;
    private ApptStatusEntity apptStatus;
    private List<FeedbackEntity> feedbacks;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "start_time", nullable = false)
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time", nullable = false)
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "location", nullable = true, length = 100)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Basic
    @Column(name = "symptom", nullable = true, length = 1000)
    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    @Basic
    @Column(name = "vitals", nullable = true, length = 100)
    public String getVitals() {
        return vitals;
    }

    public void setVitals(String vitals) {
        this.vitals = vitals;
    }

    @Basic
    @Column(name = "allergies", nullable = true, length = 200)
    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    @Basic
    @Column(name = "follow_up", nullable = true, length = 1000)
    public String getFollowUp() {
        return followUp;
    }

    public void setFollowUp(String followUp) {
        this.followUp = followUp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppointmentEntity entity = (AppointmentEntity) o;

        if (id != entity.id) return false;
        if (startTime != null ? !startTime.equals(entity.startTime) : entity.startTime != null) return false;
        if (endTime != null ? !endTime.equals(entity.endTime) : entity.endTime != null) return false;
        if (location != null ? !location.equals(entity.location) : entity.location != null) return false;
        if (symptom != null ? !symptom.equals(entity.symptom) : entity.symptom != null) return false;
        if (vitals != null ? !vitals.equals(entity.vitals) : entity.vitals != null) return false;
        if (allergies != null ? !allergies.equals(entity.allergies) : entity.allergies != null) return false;
        if (followUp != null ? !followUp.equals(entity.followUp) : entity.followUp != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (startTime != null ? startTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (symptom != null ? symptom.hashCode() : 0);
        result = 31 * result + (vitals != null ? vitals.hashCode() : 0);
        result = 31 * result + (allergies != null ? allergies.hashCode() : 0);
        result = 31 * result + (followUp != null ? followUp.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "id", nullable = false)
    public MedicalStaffEntity getMedicalStaff() {
        return medicalStaff;
    }

    public void setMedicalStaff(MedicalStaffEntity medicalStaff) {
        this.medicalStaff = medicalStaff;
    }

    @ManyToOne
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    @ManyToOne
    @JoinColumn(name = "status_code", referencedColumnName = "id", nullable = false)
    public ApptStatusEntity getApptStatus() {
        return apptStatus;
    }

    public void setApptStatus(ApptStatusEntity apptStatus) {
        this.apptStatus = apptStatus;
    }

    @OneToMany(mappedBy = "appointment")
    public List<FeedbackEntity> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackEntity> feedbacks) {
        this.feedbacks = feedbacks;
    }
}
