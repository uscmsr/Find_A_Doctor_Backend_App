package com.oose.group6.backend.persistence;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "user", schema = "oose", catalog = "")
public class UserEntity {
    private long id;
    private String username;
    private String password;
    private Timestamp registrationDate;
    private Timestamp lastLogin;
    private String role;
    private MedicalStaffEntity medicalStaff;
    private PatientEntity patient;
    private List<UserLanguageMappingEntity> userLanguageMappings;
    private List<UserEventLogEntity> userEventLogs;

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
    @Column(name = "username", nullable = false, length = 50)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Basic
    @Column(name = "password", nullable = false, length = 50)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "registration_date", nullable = false)
    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Basic
    @Column(name = "last_login", nullable = true)
    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Basic
    @Column(name = "role", nullable = false, length = 1)
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (registrationDate != null ? !registrationDate.equals(that.registrationDate) : that.registrationDate != null)
            return false;
        if (lastLogin != null ? !lastLogin.equals(that.lastLogin) : that.lastLogin != null) return false;
        if (role != null ? !role.equals(that.role) : that.role != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (lastLogin != null ? lastLogin.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @OneToOne(mappedBy = "user")
    public MedicalStaffEntity getMedicalStaff() {
        return medicalStaff;
    }

    public void setMedicalStaff(MedicalStaffEntity medicalStaff) {
        this.medicalStaff = medicalStaff;
    }

    @OneToOne(mappedBy = "user")
    public PatientEntity getPatient() {
        return patient;
    }

    public void setPatient(PatientEntity patient) {
        this.patient = patient;
    }

    @OneToMany(mappedBy = "user")
    public List<UserLanguageMappingEntity> getUserLanguageMappings() {
        return userLanguageMappings;
    }

    public void setUserLanguageMappings(List<UserLanguageMappingEntity> userLanguageMappings) {
        this.userLanguageMappings = userLanguageMappings;
    }

    @OneToMany(mappedBy = "user")
    public List<UserEventLogEntity> getUserEventLogs() {
        return userEventLogs;
    }

    public void setUserEventLogs(List<UserEventLogEntity> userEventLogs) {
        this.userEventLogs = userEventLogs;
    }
}
