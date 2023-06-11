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

package com.ontim.mymonitor.data.model;

import com.ontim.mymonitor.data.UserInfo;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String displayName;

    private UserInfo mUserInfo;

    private static LoggedInUser instance;

    //
    private LoggedInUser() {}

    public static synchronized LoggedInUser getInstance() {

        if (instance == null) {
            instance = new LoggedInUser();
        }

        return instance;
    }

    public String getDisplayName() {
        return displayName;
    }

    public UserInfo getmUserInfo() {
        return mUserInfo;
    }

    public void setmUserInfo(UserInfo mUserInfo) {
        this.mUserInfo = mUserInfo;
        displayName = mUserInfo.getUsername();
    }

    public boolean isLogin() {
        if (mUserInfo == null) return false;
        return true;
    }

    public void logout() {
        mUserInfo = null;
    }
}