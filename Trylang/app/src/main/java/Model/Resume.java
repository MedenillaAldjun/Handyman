package Model;

import java.util.Date;

public class Resume extends PostId {

    private String image, user, fullname, age, gender, address, contact;
    private Date time;

    public String getImage() {
        return image;
    }

    public String getUser() {
        return user;
    }

    public String getClient_name() {
        return fullname;
    }

    public String getClient_age() {
        return age;
    }

    public String getClient_gender() {
        return gender;
    }

    public String getClient_address() {
        return address;
    }

    public String getClient_contact() {
        return contact;
    }

    public Date getTime() {
        return time;
    }
}
