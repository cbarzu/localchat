/**
 * Localchat
 *
 * @author Ignacio Molina Cuquerella
 * @author Claudiu Barzu
 */

package es.upm.fi.muii.localchat.profile;

import java.io.Serializable;

/**
 * Created by Titanium on 15/12/15.
 */
public class Profile implements Serializable {

    private static final long serialVersionUID = 1201992789L;

    private String nickname;
    private String age;
    private String surename;
    private String givenname;
    private String description;

    public Profile(String nickname) {

        this.nickname = nickname;
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

    public String getAge() {
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

    public void setAge(String age) {
        this.age = age;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
