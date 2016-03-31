package com.example.infolinetestapplication.entity;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by suleyman on 31.3.2016.
 */

// her bir kullanıcı için bir kayıt tutulur
// her yeni loginde logintime update edilir
public class Login {
    public static String PERSON_ID_TEXT_FIELD = "personId";
    public static String LOGIN_TIME_TEXT_FIELD = "loginTime";

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    int personId;

    @DatabaseField
    Long loginTime;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public Long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Long loginTime) {
        this.loginTime = loginTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("personId=").append(personId);
        sb.append(", ").append("loginTime= ").append(loginTime);
        return sb.toString();
    }
}
