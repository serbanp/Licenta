package com.mvrcm.model.Utils;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class Persoana {

    @Column(name="full_name",nullable = false)
    private String fullName;

    @Column(name="birth_day")
    private Date birthday;

    @Column(name="bio")
    private String bio;

    public Persoana() {}

    public Persoana(String fullName) {
        this.fullName = fullName;
    }

    public Persoana(String fullName, Date birthday) {
        this.fullName = fullName;
        this.birthday = birthday;
    }

    public Persoana(String fullName, Date birthday, String bio) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.bio = bio;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
