package com.myideaway.coupon.model.user.service.biz;

import android.content.Context;
import com.myideaway.coupon.Config;
import com.myideaway.easyapp.core.service.BizService;

import java.io.File;

/**
 * Created by duanchang on 13-12-11.
 */
public class UserBSRemoveSaveLogin extends BizService {

    public UserBSRemoveSaveLogin(Context context) {
        super(context);
    }

    @Override
    protected Object onExecute() throws Exception {

        String filePath = Config.PATH_CACHE + "/user.dat";

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        return null;
    }
}
