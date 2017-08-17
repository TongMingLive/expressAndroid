package com.example.tong.expressandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tong.expressandroid.Util.App;
import com.example.tong.expressandroid.Util.HttpUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tong on 2017/3/19.
 */

public class UpdatePhoneAddress extends AppCompatActivity {
    private TextView phone, address;
    private Button button;
    private String ph, ad;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_phone_address);
        phone = (TextView) findViewById(R.id.update_phone);
        address = (TextView) findViewById(R.id.update_address);
        button = (Button) findViewById(R.id.update_btn);

        phone.setHint(App.user.getUserphone());
        address.setHint(App.user.getUseraddress());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ph = phone.getText().toString();
                ad = address.getText().toString();

                if (ph == null || "".equals(ph) || "null".equals(ph)){
                    ph = App.user.getUserphone();
                }
                if (ad == null || "".equals(ad) || "null".equals(ad)){
                    ad = App.user.getUseraddress();
                }

                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Map<String, Object> map = new HashMap<>();
                        map.put("userId", App.user.getUserid());
                        map.put("userPhone", ph);
                        map.put("userAddress",ad);
                        String str = HttpUtil.doPost(HttpUtil.path + "UpdateUserPhoneAddress", map);
                        if ("error".equals(str)) {
                            handler.sendEmptyMessage(0x000);
                        } else if ("true".equals(str)) {
                            handler.sendEmptyMessage(0x123);
                        } else {
                            handler.sendEmptyMessage(0x124);
                        }
                    }
                }.start();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                App.user.setUserphone(ph);
                App.user.setUseraddress(ad);
                Toast.makeText(UpdatePhoneAddress.this, "修改成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 0x124) {
                Snackbar.make(button, "修改失败，请重试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(button, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
