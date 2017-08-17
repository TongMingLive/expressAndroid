package com.example.tong.expressandroid;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tong.expressandroid.Util.HttpUtil;
import com.example.tong.expressandroid.bean.Find;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tong on 17-3-30.
 */

public class FindAll extends AppCompatActivity {
    private ListView lv;
    private String str;
    private List<Find> list = new ArrayList<>();
    private ListAdapter adapter = new myAdapte();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_all);
        lv = (ListView) findViewById(R.id.friend_lv);
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
                str = HttpUtil.doPost(HttpUtil.path+"SelectFindServlet",map);
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
            LayoutInflater layoutInflater = LayoutInflater.from(FindAll.this);
            View v = layoutInflater.inflate(R.layout.find_item, null);
            LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.friend_item);
            TextView title = (TextView) v.findViewById(R.id.friend_item_title);
            TextView page = (TextView) v.findViewById(R.id.friend_item_page);

            title.setText(list.get(position).getFindtitle());
            page.setText(list.get(position).getFindpage());


            linearLayout.setTag(position);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    Intent intent = new Intent();
                    intent.setClass(FindAll.this,FindPage.class);
                    intent.putExtra("friend",list.get(position));
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
                    list.add(gson.fromJson(json , Find.class));
                }
                lv.setAdapter(adapter);
            }else if (msg.what == 0x000){
                Snackbar.make(lv, "网络连接失败，请检查您的网络", Snackbar.LENGTH_LONG).show();
            }
        }
    };

}
