package com.example.tong.expressandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tong.expressandroid.Util.HttpUtil;
import com.example.tong.expressandroid.bean.Place;
import com.example.tong.expressandroid.bean.Send;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tong- on 2017/4/18.
 */

public class PlaceMePage extends AppCompatActivity {
    private android.widget.TextView sendname;
    private android.widget.TextView sendtype;
    private android.widget.TextView sendid;
    private android.widget.TextView sendaddress;
    private android.widget.TextView sendphone;
    private android.widget.Button sendbtn;
    private Place place;
    private String type,str;
    private TextView sendusername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.place_me_page);
        this.sendusername = (TextView) findViewById(R.id.send_username);
        this.sendbtn = (Button) findViewById(R.id.send_btn);
        this.sendphone = (TextView) findViewById(R.id.send_phone);
        this.sendaddress = (TextView) findViewById(R.id.send_address);
        this.sendid = (TextView) findViewById(R.id.send_id);
        this.sendtype = (TextView) findViewById(R.id.send_type);
        this.sendname = (TextView) findViewById(R.id.send_name);

        place = (Place) getIntent().getSerializableExtra("place");
        type = getIntent().getStringExtra("type");

        switch (place.getPlacetype()){
            case 0:
                sendtype.setText("状态："+"待审核");
                break;
            case 1:
                sendtype.setText("状态："+"审核通过，请尽快配送");
                break;
            case 2:
                sendtype.setText("状态："+"已签收");
                break;
        }

        sendtype.setMovementMethod(LinkMovementMethod.getInstance());

        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map =new HashMap<>();
                map.put("sendId", place.getSendid());
                str = HttpUtil.doPost(HttpUtil.path+"SelectSendByIdServlet",map);
                if (str.equals("error")) {
                    handler.sendEmptyMessage(0x000);
                }else {
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
                final Send send = gson.fromJson(str,Send.class);

                sendname.setText(send.getCompanyName()+"快递");
                sendid.setText("快递单号："+send.getSendid());
                sendusername.setText("收件人："+send.getUserName());
                sendphone.setText("联系电话："+send.getUserphone());
                sendaddress.setText("配送地址："+send.getUseraddress());

                if (place.getPlacetype() == 0 || place.getPlacetype() == 2){
                    sendbtn.setVisibility(View.GONE);
                }else {
                    sendbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    Map<String, Object> map = new HashMap<String, Object>();
                                    map.put("placeId", place.getPlaceid());
                                    map.put("placeType", "2");
                                    map.put("sendId", send.getSendid());
                                    map.put("sendType", "3");
                                    String str1 = HttpUtil.doGet(HttpUtil.pathstr + "UpdateSendTypeServlet", map);
                                    String str2 = HttpUtil.doGet(HttpUtil.pathstr + "UpdatePlaceTypeServlet", map);
                                    if ("error".equals(str1) || "error".equals(str2)) {
                                        handler.sendEmptyMessage(0x000);
                                    } else {
                                        if ("true".equals(str1) && "true".equals(str2)) {
                                            handler.sendEmptyMessage(0x223);
                                        } else {
                                            handler.sendEmptyMessage(0x224);
                                        }
                                    }
                                }
                            }.start();
                        }
                    });
                }

            } else if (msg.what == 0x223){
                Toast.makeText(PlaceMePage.this, "成功标记为已签收", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 0x224) {
                Snackbar.make(sendbtn, "签收状态标记失败，请重试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(sendbtn, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
