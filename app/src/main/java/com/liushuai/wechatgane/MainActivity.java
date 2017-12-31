package com.liushuai.wechatgane;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.liushuai.wechatgane.bean.VersionBean;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {

    public final static String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //testRoot();

        //检测更新
        check();

        findViewById(R.id.checkRoot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testRoot();
            }
        });

        findViewById(R.id.addWindow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initView();

            }
        });

        findViewById(R.id.exit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initView();
                System.exit(0);
            }
        });
    }


    protected void testRoot() {
        try {
            Process p = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(p.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("wechatmomentexport", e.getMessage());

            Toast.makeText(MainActivity.this, "您的手机未root,不能使用本工具", Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        //root完成添加页面
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                TouchHolder.getInstance();
            }
        });
    }

    private void check() {
        final int versionCode = Utils.versionCode();

        OkHttpUtils.get()
                .url(Constant.URL_VERSION)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!TextUtils.isEmpty(response)) {
                            VersionBean versionBean = JSON.parseObject(response, VersionBean.class);
                            if (versionBean.getVersion() > 0 && versionBean.getVersion() > versionCode) {
                                showDialog(versionBean);
                            }

                        }
                    }
                });
    }

    private void showDialog(final VersionBean versionBean) {
        new MaterialDialog.Builder(this)
                .title(TextUtils.isEmpty(versionBean.getTitle()) ? "更新" : versionBean.getTitle())
                .content(TextUtils.isEmpty(versionBean.getContent()) ? "重要更新" : versionBean.getContent())
                .positiveText("更新")
                .negativeText("取消")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (versionBean.getType() == 1) {//强制更新
                            MainActivity.this.finish();
                        }
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        downloadAPK(versionBean.getUrl());
                    }
                })
                .show();
    }

    private void downloadAPK(String url) {
        if (TextUtils.isEmpty(url)) {
            url = Constant.BASEURL;
        }
        final MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title("更新进度")
                .progress(true, 0)
                .show();


        OkHttpUtils.get()
                .url(url)
                .build()
                .execute(new FileCallBack(Constant.PATH_APK_DIR, Constant.PARAM_APK_NAME) {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        toInstall(response);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        Log.i(TAG, "progress = " + progress + "total=" + total);
                        dialog.setProgress((int) progress);

                    }
                });

    }

    private void toInstall(File file) {
        // 跳转到系统安装页面
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivityForResult(intent, 0);// 如果用户取消安装的话,
    }
}
