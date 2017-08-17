package com.example.tong.expressandroid;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tong.expressandroid.Util.HttpUtil;
import com.example.tong.expressandroid.bean.Send;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tong- on 2017/4/17.
 */

public class SendPage extends AppCompatActivity {
    private android.widget.TextView sendname;
    private android.widget.TextView sendtype;
    private android.widget.TextView sendaddress;
    private android.widget.RatingBar sendratingBar;
    private android.widget.EditText friendpushpage;
    private android.widget.Button sendbtn;
    private LinearLayout sendpf;
    private LinearLayout sendpl;
    private Send send;
    private String type;
    private String page;
    private TextView sendpfnum;
    private android.support.design.widget.TextInputLayout sendpage;
    private TextView sendplpage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_page);
        this.sendplpage = (TextView) findViewById(R.id.send_pl_page);
        this.sendpage = (TextInputLayout) findViewById(R.id.sendpage);
        this.sendpfnum = (TextView) findViewById(R.id.send_pf_num);
        this.sendpl = (LinearLayout) findViewById(R.id.send_pl);
        this.sendpf = (LinearLayout) findViewById(R.id.send_pf);
        this.sendbtn = (Button) findViewById(R.id.send_btn);
        this.friendpushpage = (EditText) findViewById(R.id.friend_push_page);
        this.sendratingBar = (RatingBar) findViewById(R.id.send_ratingBar);
        this.sendaddress = (TextView) findViewById(R.id.send_address);
        this.sendtype = (TextView) findViewById(R.id.send_type);
        this.sendname = (TextView) findViewById(R.id.send_name);

        send = (Send) getIntent().getSerializableExtra("send");
        type = getIntent().getStringExtra("type");

        sendname.setText(send.getCompanyName());
        sendtype.setText(type);
        sendaddress.setText("配送地址：" + send.getUseraddress());
        if (send.getSendappraise() == -1 && send.getSendtype() == 3) {
            sendpf.setVisibility(View.VISIBLE);
            sendpl.setVisibility(View.VISIBLE);
            sendbtn.setVisibility(View.VISIBLE);
            sendbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    page = friendpushpage.getText().toString();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("sendId", send.getSendid());
                            map.put("sendAppraise", sendratingBar.getRating());
                            map.put("sendPage", page);
                            String str = HttpUtil.doGet(HttpUtil.pathstr + "UpdateSendAppraiseServlet", map);
                            if ("error".equals(str)) {
                                handler.sendEmptyMessage(0x000);
                            } else {
                                if ("true".equals(str)) {
                                    handler.sendEmptyMessage(0x123);
                                } else {
                                    handler.sendEmptyMessage(0x124);
                                }
                            }
                        }
                    }.start();
                }
            });
        }else if (send.getSendappraise() != -1 && send.getSendtype() == 3){
            sendpf.setVisibility(View.VISIBLE);
            sendpl.setVisibility(View.VISIBLE);
            sendratingBar.setVisibility(View.GONE);
            sendpage.setVisibility(View.GONE);
            sendpfnum.setVisibility(View.VISIBLE);
            sendplpage.setVisibility(View.VISIBLE);
            sendpfnum.setText(send.getSendappraise()+"星");
            sendplpage.setText(send.getSendpage());
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123) {
                Toast.makeText(SendPage.this, "评论成功", Toast.LENGTH_SHORT).show();
                finish();
            } else if (msg.what == 0x124) {
                Snackbar.make(sendbtn, "评论失败，请重试", Snackbar.LENGTH_LONG).show();
            } else if (msg.what == 0x000) {
                Snackbar.make(sendbtn, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
