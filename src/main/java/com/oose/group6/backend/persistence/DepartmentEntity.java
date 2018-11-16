package com.oose.group6.backend.persistence;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "department", schema = "oose", catalog = "")
public class DepartmentEntity {
    private long id;
    private String name;
    private List<MedicalStaffEntity> medicalStaffs;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DepartmentEntity that = (DepartmentEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "department")
    public List<MedicalStaffEntity> getMedicalStaffs() {
        return medicalStaffs;
    }

    public void setMedicalStaffs(List<MedicalStaffEntity> medicalStaffs) {
        this.medicalStaffs = medicalStaffs;
    }
}
