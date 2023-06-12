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

package com.ontim.mymonitor.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 用于读写配置的通用类
 */
public class SharedPreferencesUtil {
    /**
     * 写入key,value对
     * */
    public static void writeString(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 写入key,value对
     * */
    public static void writeInt(Context context, String key, int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    /**
     * 读出value值
     * */
    public static String readString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }


    /**
     * 读出value值
     * */
    public static int readInt(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }

    /**
     * 读出value值
     * */
    public static int readInt(Context context, String key, int def){
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,def);
    }
}
