package com.example.tong.expressandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tong.expressandroid.Util.App;
import com.example.tong.expressandroid.Util.HttpUtil;
import com.example.tong.expressandroid.bean.Send;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tong- on 2017/4/17.
 */

public class SendAll extends AppCompatActivity {
    private android.widget.ListView sendlv;
    private String str,type;
    private List<Send> list = new ArrayList<>();
    private ListAdapter adapter = new myAdapte();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_all);
        this.sendlv = (ListView) findViewById(R.id.send_lv);
    }
    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        new Thread(){
            @Override
            public void run() {
                super.run();
                Map<String,Object> map =new HashMap<>();
                map.put("userId", App.user.getUserid());
                str = HttpUtil.doPost(HttpUtil.path+"SelectSendUserServlet",map);
                if (str.equals("error")) {
                    handler.sendEmptyMessage(0x000);
                }else {
                    handler.sendEmptyMessage(0x123);
                }
            }
        }.start();
    }
    private class myAdapte extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(SendAll.this);
            View v = layoutInflater.inflate(R.layout.find_item, null);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.friend_item);
            TextView title = (TextView) v.findViewById(R.id.friend_item_title);
            TextView page = (TextView) v.findViewById(R.id.friend_item_page);

            title.setText(list.get(position).getCompanyName()+"快递");
            switch (list.get(position).getSendtype()){
                case 0:
                    type = "待揽收";
                    break;
                case 1:
                    type = "运输中";
                    break;
                case 2:
                    type = "待配送";
                    break;
                case 3:
                    type = "已签收";
            }
            page.setText(type);

            linearLayout.setTag(position);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(SendAll.this,SendPage.class);
                    intent.putExtra("send",list.get(position));
                    intent.putExtra("type",type);
                    startActivity(intent);
                }
            });

            return v;
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x123){
                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(str).getAsJsonArray();
                for (JsonElement json : jsonArray) {
                    list.add(gson.fromJson(json , Send.class));
                }
                sendlv.setAdapter(adapter);
                if (list.size() == 0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SendAll.this);
                    builder.setTitle("提示");
                    builder.setMessage("暂无快递订单");
                    builder.setNeutralButton("确定",null);
                    builder.show();
                }
            }else if (msg.what == 0x000){
                Snackbar.make(sendlv, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };
}
