package com.myideaway.coupon.model.user.service.biz;

import android.content.Context;
import com.myideaway.coupon.Config;
import com.myideaway.coupon.model.user.User;
import com.myideaway.easyapp.core.service.BizService;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Created by duanchang on 13-12-11.
 */
public class UserBSGetSaveLogin extends BizService {
    private User user;

    public UserBSGetSaveLogin(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {

        String filePath = Config.PATH_CACHE + "/user.dat";
        File userFile = new File(filePath);
        if (userFile.exists()) {
            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(userFile));
            user = (User) objectInputStream.readObject();
            objectInputStream.close();
            return user;
        } else {
            return null;
        }

    }
}
