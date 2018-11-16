package com.oose.group6.backend.persistence;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "workday", schema = "oose", catalog = "")
public class WorkdayEntity {
    private long id;
    private String day;
    private List<StaffWorkdayMappingEntity> staffWorkdayMappings;

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
    @Column(name = "day", nullable = false, length = 50)
    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkdayEntity that = (WorkdayEntity) o;

        if (id != that.id) return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (day != null ? day.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "workday")
    public List<StaffWorkdayMappingEntity> getStaffWorkdayMappings() {
        return staffWorkdayMappings;
    }

    public void setStaffWorkdayMappings(List<StaffWorkdayMappingEntity> staffWorkdayMappings) {
        this.staffWorkdayMappings = staffWorkdayMappings;
    }
}
