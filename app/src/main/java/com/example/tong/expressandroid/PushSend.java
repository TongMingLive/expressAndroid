package com.example.tong.expressandroid;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tong.expressandroid.Util.App;
import com.example.tong.expressandroid.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tong- on 2017/4/17.
 */

public class PushSend extends AppCompatActivity {
    private android.widget.TextView sendcompany;
    private android.widget.EditText sendphone;
    private android.widget.EditText sendaddress;
    private android.widget.Button sendbtn;
    private int companyId;
    private String address,phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_send);
        this.sendbtn = (Button) findViewById(R.id.send_btn);
        this.sendaddress = (EditText) findViewById(R.id.send_address);
        this.sendphone = (EditText) findViewById(R.id.send_phone);
        this.sendcompany = (TextView) findViewById(R.id.send_company);

        sendaddress.setHint(App.user.getUseraddress());
        if (!("".equals(App.user.getUserphone()) || App.user.getUserphone().length() == 0)){
            sendphone.setHint(App.user.getUserphone());
        }

        sendcompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setItems(new String[]{"东风","国顺","长城","韵通"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                companyId = 1;
                                sendcompany.setText("东风");
                                break;
                            case 1:
                                companyId = 2;
                                sendcompany.setText("国顺");
                                break;
                            case 2:
                                companyId = 3;
                                sendcompany.setText("长城");
                                break;
                            case 3:
                                companyId = 4;
                                sendcompany.setText("韵通");
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
        sendbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = sendaddress.getText().toString();
                phone = sendphone.getText().toString();

                boolean cheack = false;

                if (companyId == 0){
                    Snackbar.make(sendbtn, "请选择快递公司", Snackbar.LENGTH_LONG).show();
                    cheack = false;
                }else {
                    cheack = true;
                }

                if (TextUtils.isEmpty(address)){
                    if ("".equals(App.user.getUseraddress()) || "暂未填写住宅".equals(App.user.getUseraddress())){
                        Snackbar.make(sendbtn, "请填写收货地址", Snackbar.LENGTH_LONG).show();
                        cheack = false;
                    }else {
                        address = App.user.getUseraddress();
                        cheack = true;
                    }
                }else {
                    if ("".equals(address) || "暂未填写住宅".equals(address) || address.length() == 0){
                        Snackbar.make(sendbtn, "请填写收货地址", Snackbar.LENGTH_LONG).show();
                        cheack = false;
                    }else {
                        address = App.user.getUseraddress();
                        cheack = true;
                    }
                }

                if (TextUtils.isEmpty(phone)){
                    if ("".equals(App.user.getUserphone()) || App.user.getUserphone().length() != 11){
                        Snackbar.make(sendbtn, "请填写正确的电话号码", Snackbar.LENGTH_LONG).show();
                        cheack = false;
                    }
                    else {
                        phone = App.user.getUserphone();
                        cheack = true;
                    }
                }else {
                    if (phone.length() != 11){
                        Snackbar.make(sendbtn, "请填写正确的电话号码", Snackbar.LENGTH_LONG).show();
                        cheack = false;
                    }else {
                        cheack = true;
                    }
                }

                if (cheack && companyId != 0){
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("companyId", companyId);
                            map.put("userId", App.user.getUserid());
                            map.put("userPhone", phone);
                            map.put("userAddress", address);
                            String str = HttpUtil.doGet(HttpUtil.pathstr + "PushSendServlet", map);
                            if ("error".equals(str)) {
                                handler.sendEmptyMessage(0x000);
                            } else {
                                if ("false".equals(str)) {
                                    handler.sendEmptyMessage(0x001);
                                } else {
                                    handler.sendEmptyMessage(0x123);
                                }
                            }
                        }
                    }.start();
                }
            }
        });
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Toast.makeText(PushSend.this, "提交成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 0x124) {
                Snackbar.make(sendbtn, "提交失败，请重试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(sendbtn, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
