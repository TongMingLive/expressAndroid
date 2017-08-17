package com.example.tong.expressandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.tong.expressandroid.PlaceAll;
import com.example.tong.expressandroid.PushSend;
import com.example.tong.expressandroid.R;

/**
 *
 * Created by Tong on 2017/3/16.
 */

public class Fragment1 extends Fragment {

    private android.widget.Button mainsend;
    private android.widget.Button mainplace;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main,null);
        this.mainplace = (Button) view.findViewById(R.id.main_place);
        this.mainsend = (Button) view.findViewById(R.id.main_send);

        mainsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PushSend.class));
            }
        });

        mainplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PlaceAll.class));
            }
        });

        return view;
    }
}
