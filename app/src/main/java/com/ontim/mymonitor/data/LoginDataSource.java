/*
 * Copyright (C) 2023 The ONTIM Technologies Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ontim.mymonitor.data;

import android.util.Log;

import com.ontim.mymonitor.MyApp;
import com.ontim.mymonitor.dao.UserInfoDao;
import com.ontim.mymonitor.data.model.LoggedInUser;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    private static final String TAG = "LoginDataSource";

    public Result<LoggedInUser> login(String username, String password) {

        Log.d(TAG, "username： " + username + ", password: " + password);
        try {

            if (verifyUser(username, password)) {
                // TODO: handle loggedInUser authentication
                LoggedInUser loggedInUser = LoggedInUser.getInstance();
                loggedInUser.setmUserInfo(new UserInfo(username, password));

                return new Result.Success<>(loggedInUser);
            } else {
                return new Result.Error(new IOException("Error name or password"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    private boolean verifyUser(String username, String password) {

        UserInfoDao userInfoDao = MyApp.getMyApp().getDaoSession().getUserInfoDao();
        QueryBuilder<UserInfo> result = userInfoDao.queryBuilder();

        //借助Property属性类提供的筛选方法
        result = result.where(UserInfoDao.Properties.Username.eq(username),
                              UserInfoDao.Properties.Password.eq(password));
        if (result.count() > 0) {
            Log.d(TAG, "verify successful!");
            return true;
        }
        if ("chris".equalsIgnoreCase(username) && "123456".equals(password)) {
            Log.d(TAG, "Test user verify successful!");
            return true;
        }

        return false;
    }


    public void logout() {
        // TODO: revoke authentication
    }
}