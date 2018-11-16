package com.oose.group6.backend.persistence;

import javax.persistence.*;

@Entity
@Table(name = "staff_workday_mapping", schema = "oose", catalog = "")
public class StaffWorkdayMappingEntity {
    private long id;
    private MedicalStaffEntity medicalStaff;
    private WorkdayEntity workday;

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StaffWorkdayMappingEntity that = (StaffWorkdayMappingEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
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
    @JoinColumn(name = "day_id", referencedColumnName = "id", nullable = false)
    public WorkdayEntity getWorkday() {
        return workday;
    }

    public void setWorkday(WorkdayEntity workday) {
        this.workday = workday;
    }
}
