package Model;

import java.util.Date;

public class Post extends PostId {

    private String image, user, product, price, fbname, gcashname, gcashnum;
    private Date time;


    public String getImage() {
        return image;
    }

    public String getUser() {
        return user;
    }

    public String getProduct() {
        return product;
    }

    public String getPrice() {
        return price;
    }

    public Date getTime() {
        return time;
    }

    public String getFbname() {
        return fbname;
    }

    public String getGcashname() {
        return gcashname;
    }

    public String getGcashnum() {
        return gcashnum;
    }
}
