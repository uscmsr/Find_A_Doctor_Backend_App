package com.oose.group6.backend.persistence;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "patient", schema = "oose", catalog = "")
public class PatientEntity {
    private long id;
    private String name;
    private String email;
    private String address;
    private String phone;
    private String gender;
    private String race;
    private String photoUrl;
    private Date dob;
    private List<AppointmentEntity> appointments;
    private List<FeedbackEntity> feedbacks;
    private UserEntity user;
    private MedicalStaffEntity medicalStaff;
    private InsuranceEntity insurance;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String communicationPref;
    private String medicalPref;
    private String staffPref;

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
    @Column(name = "name", nullable = false, length = 50)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "email", nullable = true, length = 50)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "address", nullable = true, length = 100)
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "phone", nullable = true, length = 50)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Basic
    @Column(name = "gender", nullable = true, length = 1)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "race", nullable = true, length = 50)
    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    @Basic
    @Column(name = "photo_url", nullable = true, length = 500)
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Basic
    @Column(name = "dob", nullable = true)
    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    @Basic
    @Column(name = "emergency_contact_name", nullable = true, length = 50)
    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    @Basic
    @Column(name = "emergency_contact_phone", nullable = true, length = 20)
    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    @Basic
    @Column(name = "communication_pref", nullable = true, length = 200)
    public String getCommunicationPref() {
        return communicationPref;
    }

    public void setCommunicationPref(String communicationPref) {
        this.communicationPref = communicationPref;
    }

    @Basic
    @Column(name = "medical_pref", nullable = true, length = 200)
    public String getMedicalPref() {
        return medicalPref;
    }

    public void setMedicalPref(String medicalPref) {
        this.medicalPref = medicalPref;
    }

    @Basic
    @Column(name = "staff_pref", nullable = true, length = 200)
    public String getStaffPref() {
        return staffPref;
    }

    public void setStaffPref(String staffPref) {
        this.staffPref = staffPref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientEntity that = (PatientEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (race != null ? !race.equals(that.race) : that.race != null) return false;
        if (photoUrl != null ? !photoUrl.equals(that.photoUrl) : that.photoUrl != null) return false;
        if (dob != null ? !dob.equals(that.dob) : that.dob != null) return false;
        if (emergencyContactName != null ? !emergencyContactName.equals(that.emergencyContactName) :
                that.emergencyContactName != null) return false;
        if (emergencyContactPhone != null ? !emergencyContactPhone.equals(that.emergencyContactPhone) :
                that.emergencyContactPhone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (race != null ? race.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        result = 31 * result + (dob != null ? dob.hashCode() : 0);
        result = 31 * result + (emergencyContactName != null ? emergencyContactName.hashCode() : 0);
        result = 31 * result + (emergencyContactPhone != null ? emergencyContactPhone.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "patient")
    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    @OneToMany(mappedBy = "patient")
    public List<FeedbackEntity> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackEntity> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @OneToOne
    @JoinColumn(name = "userid", referencedColumnName = "id", nullable = false)
    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "pcp_id", referencedColumnName = "id")
    public MedicalStaffEntity getMedicalStaff() {
        return medicalStaff;
    }

    public void setMedicalStaff(MedicalStaffEntity medicalStaff) {
        this.medicalStaff = medicalStaff;
    }

    @ManyToOne
    @JoinColumn(name = "insurance_id", referencedColumnName = "id")
    public InsuranceEntity getInsurance() {
        return insurance;
    }

    public void setInsurance(InsuranceEntity insurance) {
        this.insurance = insurance;
    }

}
