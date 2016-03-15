package com.myideaway.coupon.model.user.service.biz;

import android.content.Context;
import com.myideaway.coupon.Config;
import com.myideaway.coupon.model.user.User;
import com.myideaway.easyapp.core.service.BizService;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * Created by duanchang on 13-12-11.
 */
public class UserBSSaveLogin extends BizService {
    private User user;

    public UserBSSaveLogin(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {
        String filePath = Config.PATH_CACHE + "/user.dat";
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(filePath));
        objectOutputStream.writeObject(user);
        objectOutputStream.close();
        return null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
