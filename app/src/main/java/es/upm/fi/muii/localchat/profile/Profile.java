package es.upm.fi.muii.localchat.profile;

import java.io.Serializable;

/**
 * Created by Titanium on 15/12/15.
 */
public class Profile implements Serializable {

    private String networkAddress;
    private String nickname;
    private int age;
    private String surename;
    private String givenname;
    private String description;

    public Profile(String networkAddress, String nickname) {

        this.networkAddress = networkAddress;
        this.nickname = nickname;
    }

    public String getNetworkAddress() {
        return networkAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public String getGivenname() {
        return givenname;
    }

    public String getSurename() {
        return surename;
    }

    public int getAge() {
        return age;
    }

    public String getDescription() {
        return description;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public void setSurename(String surename) {
        this.surename = surename;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
