package com.ontim.mymonitor;

import com.ontim.mymonitor.data.UserInfo;
import com.ontim.mymonitor.data.model.LoggedInUser;

public class CachedInfo {

    public static boolean isLogin;

    static {
        LoggedInUser loggedInUser = LoggedInUser.getInstance();
        loggedInUser.setmUserInfo(new UserInfo("chris","123456"));
        isLogin = loggedInUser.isLogin();
    }


}
