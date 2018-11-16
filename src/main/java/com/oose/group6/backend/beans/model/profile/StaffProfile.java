package com.oose.group6.backend.beans.model.profile;

import com.oose.group6.backend.beans.model.Department;
import com.oose.group6.backend.beans.model.MedicalCenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StaffProfile extends BaseProfile implements Serializable, ICommonProfile {

    public StaffProfile() {
        super();
        workdays = new ArrayList<>();
    }

    // staff
    private String speciality;
    private String email;
    private String phone;
    private String gender;
    private String photoUrl;

    // HTTP unique
    private Department department;
    private MedicalCenter medicalCenter;
    private List<Workday> workdays;

    public static class Workday {
        private Long id;
        private String name;

        public Workday(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public MedicalCenter getMedicalCenter() {
        return medicalCenter;
    }

    public void setMedicalCenter(MedicalCenter medicalCenter) {
        this.medicalCenter = medicalCenter;
    }

    public List<Workday> getWorkdays() {
        return workdays;
    }

    public void setWorkdays(List<Workday> workdays) {
        this.workdays = workdays;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }


}
