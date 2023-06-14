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
 * save the config settings.
 */
public class SharedPreferencesUtil {

    //  1. front, back camera.   back: 0  front: 1
    //  2. setting the Quality of video
    /*
    public static final int QUALITY_1080P = 6;
    public static final int QUALITY_480P = 4;
    public static final int QUALITY_720P = 5;
    public static final int QUALITY_2160P = 8;
    public static final int QUALITY_CIF = 3;
    public static final int QUALITY_HIGH = 1;
    public static final int QUALITY_LOW = 0;
    */



    private static Context mContext;

    public static void initSharedPerferences(Context context) {
        mContext = context;
    }
    /**
     * wirte String type
     * */
    public static void writeString(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * writeInt
     * */
    public static void writeInt(Context context, String key, int value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("config",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }

    /**
     * readString
     * */
    public static String readString(Context context, String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }


    /**
     * readInt
     * */
    public static int readInt(Context context, String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }

    /**
     * readInt
     * */
    public static int readInt(Context context, String key, int def){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,def);
    }
}
