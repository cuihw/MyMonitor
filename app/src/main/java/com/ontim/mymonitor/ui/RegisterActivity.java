package com.ontim.mymonitor.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ontim.mymonitor.MyApp;
import com.ontim.mymonitor.R;
import com.ontim.mymonitor.Util.ToastUtil;
import com.ontim.mymonitor.dao.UserInfoDao;
import com.ontim.mymonitor.data.UserInfo;
import com.ontim.mymonitor.data.model.LoggedInUser;
import com.ontim.mymonitor.databinding.ActivityRegister2Binding;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    EditText mUsername ;
    EditText mPassword ;

    private ActivityRegister2Binding binding;

    Button mRegister ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegister2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setContentView(R.layout.activity_register2);

        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mRegister = binding.register;
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "mRegister ");
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {
        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();
        if (!isUserNameValid(username)) {
            Log.d(TAG, "invalid username");
            ToastUtil.toast(this, getResources().getString(R.string.invalid_username));
        }
        if (!isPasswordValid(password)) {
            Log.d(TAG, "invalid password");
            ToastUtil.toast(this, getResources().getString(R.string.invalid_password));
        }

        createNewUser(username, password);
    }

    private void createNewUser(String username, String password) {
        Log.d(TAG, "createNewUser!");
        UserInfoDao userInfoDao = MyApp.getMyApp().getDaoSession().getUserInfoDao();
        UserInfo userInfo = new UserInfo(username, password);
        userInfoDao.insertOrReplace(userInfo);
        Log.d(TAG, "createNewUser successful!");

        LoggedInUser.getInstance().setmUserInfo(userInfo);
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}

