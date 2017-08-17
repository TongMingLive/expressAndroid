package com.example.tong.expressandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tong.expressandroid.Util.App;
import com.example.tong.expressandroid.Util.HttpUtil;
import com.example.tong.expressandroid.bean.Place;
import com.example.tong.expressandroid.bean.Send;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import static com.example.tong.expressandroid.R.layout.send;

/**
 * Created by tong- on 2017/4/17.
 */

public class PlacePage extends AppCompatActivity {

    private TextView sendname;
    private TextView sendaddress;
    private TextView sendphone;
    private Button sendbtn;
    private Place place;
    private String str;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_page);
        this.sendbtn = (Button) findViewById(R.id.send_btn);
        this.sendphone = (TextView) findViewById(R.id.send_phone);
        this.sendaddress = (TextView) findViewById(R.id.send_address);
        this.sendname = (TextView) findViewById(R.id.send_name);

        place = (Place) getIntent().getSerializableExtra("place");

        new Thread() {
            @Override
            public void run() {
                super.run();
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("sendId", place.getSendid());
                str = HttpUtil.doGet(HttpUtil.pathstr + "SelectSendByIdServlet", map);
                if ("error".equals(str) || "".equals(str)) {
                    handler.sendEmptyMessage(0x000);
                } else {
                    handler.sendEmptyMessage(0x123);
                }
            }
        }.start();


    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Gson gson = new Gson();
                Send send = gson.fromJson(str, Send.class);

                sendname.setText(send.getCompanyName()+"快递");
                sendaddress.setText("配送地址"+send.getUseraddress());
                sendphone.setText("用户电话"+send.getUserphone());

                sendbtn.setTag(send);

                sendbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Send send = (Send) v.getTag();
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("userId", App.user.getUserid());
                                map.put("sendId", send.getSendid());
                                String str = HttpUtil.doGet(HttpUtil.pathstr + "PushPlaceServlet", map);
                                if ("error".equals(str)) {
                                    handler.sendEmptyMessage(0x000);
                                } else {
                                    if ("true".equals(str)) {
                                        handler.sendEmptyMessage(0x223);
                                    } else {
                                        handler.sendEmptyMessage(0x224);
                                    }
                                }
                            }
                        }.start();
                    }
                });

            } else if (msg.what == 0x223){
                Toast.makeText(PlacePage.this, "代领申请成功！待审核后即可配送您的订单", Toast.LENGTH_LONG).show();
                finish();
            } else if (msg.what == 0x224){
                Snackbar.make(sendbtn, "代领申请失败，请稍后再试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(sendbtn, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
