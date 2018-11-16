package com.oose.group6.backend.persistence;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "medical_staff", schema = "oose", catalog = "")
public class MedicalStaffEntity {
    private long id;
    private String name;
    private String email;
    private String speciality;
    private String phone;
    private String gender;
    private String photoUrl;
    private List<AppointmentEntity> appointments;
    private List<FeedbackEntity> feedbacks;
    private UserEntity user;
    private DepartmentEntity department;
    private MedicalCenterEntity medicalCenter;
    private List<PatientEntity> patients;
    private List<StaffWorkdayMappingEntity> staffWorkday;

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
    @Column(name = "speciality", nullable = true, length = 100)
    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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
    @Column(name = "photo_url", nullable = true, length = 500)
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MedicalStaffEntity entity = (MedicalStaffEntity) o;

        if (id != entity.id) return false;
        if (name != null ? !name.equals(entity.name) : entity.name != null) return false;
        if (email != null ? !email.equals(entity.email) : entity.email != null) return false;
        if (speciality != null ? !speciality.equals(entity.speciality) : entity.speciality != null) return false;
        if (phone != null ? !phone.equals(entity.phone) : entity.phone != null) return false;
        if (gender != null ? !gender.equals(entity.gender) : entity.gender != null) return false;
        if (photoUrl != null ? !photoUrl.equals(entity.photoUrl) : entity.photoUrl != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (speciality != null ? speciality.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (photoUrl != null ? photoUrl.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "medicalStaff")
    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }

    @OneToMany(mappedBy = "medicalStaff")
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
    @JoinColumn(name = "department_id", referencedColumnName = "id")
    public DepartmentEntity getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    @ManyToOne
    @JoinColumn(name = "medical_center_id", referencedColumnName = "id")
    public MedicalCenterEntity getMedicalCenter() {
        return medicalCenter;
    }

    public void setMedicalCenter(MedicalCenterEntity medicalCenter) {
        this.medicalCenter = medicalCenter;
    }

    @OneToMany(mappedBy = "medicalStaff")
    public List<PatientEntity> getPatients() {
        return patients;
    }

    public void setPatients(List<PatientEntity> patients) {
        this.patients = patients;
    }

    @OneToMany(mappedBy = "medicalStaff")
    public List<StaffWorkdayMappingEntity> getStaffWorkday() {
        return staffWorkday;
    }

    public void setStaffWorkday(List<StaffWorkdayMappingEntity> staffWorkday) {
        this.staffWorkday = staffWorkday;
    }
}
