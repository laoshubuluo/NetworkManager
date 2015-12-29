package com.rat.nm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.rat.networkmanager.R;
import com.rat.nm.activity.base.BaseActivity;
import com.rat.nm.common.MessageSignConstant;
import com.rat.nm.controller.LoginController;
import com.rat.nm.util.AppUtils;
import com.rat.nm.util.LogUtil;
import com.rat.nm.util.StringUtils;
import com.rat.nm.view.dialog.CustomProgressDialog;
import com.rat.nm.view.dialog.PromptDialog;

public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.loginBtn)
    private Button loginBtn;
    @ViewInject(R.id.userNameET)
    private EditText userNameET;
    @ViewInject(R.id.passwordET)
    private EditText passwordET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 基础框架初始化
        ViewUtils.inject(this);//xUtils框架注解注入view和事件

        // 判断是否需要登录
        if (StringUtils.isNotBlank(AppUtils.getInstance().getUserId())) {
            notNeedLogin();
            Intent i = new Intent(LoginActivity.this, MenuActivity.class);
            startActivity(i);
            finish();
            return;
        }
        initView();
        initData();
    }

    public void initView() {
        loginBtn.setOnClickListener(this);
    }

    public void initData() {
    }


    /**
     * Handler发送message的逻辑处理方法
     *
     * @param msg
     * @return
     */
    @Override
    public boolean handleMessage(Message msg) {
        if (customProgressDialog != null)
            customProgressDialog.dismiss();
        if (promptDialog == null || promptDialog.isShowing())
            promptDialog = new PromptDialog(LoginActivity.this);
        switch (msg.what) {
            case MessageSignConstant.LOGIN_SUCCESS:
//                User user = (User) msg.getData().getSerializable("user");
                Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                startActivity(i);
                finish();
                break;
            case MessageSignConstant.LOGIN_FAILURE:
                int code = msg.getData().getInt("code");
                String message = msg.getData().getString("message");
                promptDialog.initData(getString(R.string.login_failure), message);
                promptDialog.show();
                break;
            case MessageSignConstant.SERVER_OR_NETWORK_ERROR:
                promptDialog.initData(getString(R.string.login_error), msg.getData().getString("message"));
                promptDialog.show();
                break;
            case MessageSignConstant.UNKNOWN_ERROR:
                // 登录失败
                Toast.makeText(getApplicationContext(), getString(R.string.unknown_error), Toast.LENGTH_LONG).show();
//                //userId和token清空
//                AppUtils.getInstance().setUserId("");
//                AppUtils.getInstance().setUserToken("");
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.loginBtn:
                if (TextUtils.isEmpty(userNameET.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.user_name_is_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordET.getText().toString())) {
                    Toast.makeText(getApplicationContext(), getString(R.string.password_is_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                customProgressDialog = new CustomProgressDialog(this, getString(R.string.loading));
                customProgressDialog.show();
                customProgressDialog.setCancelable(false);
                LoginController loginController = new LoginController(getApplication(), handler);
                loginController.login(userNameET.getText().toString(), passwordET.getText().toString());
                break;
            default:
                break;
        }
    }

    /**
     * 是否需要登录
     */
    private void notNeedLogin() {
        LogUtil.i("login skip: userId and token is exist");
//        // 登录成功之后数据库指向用户自己的数据库
//        DbConnectionManager.getInstance().reload();
        return;
    }

}