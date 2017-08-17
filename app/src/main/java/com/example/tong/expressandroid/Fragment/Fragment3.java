package com.example.tong.expressandroid.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tong.expressandroid.FindAll;
import com.example.tong.expressandroid.Login;
import com.example.tong.expressandroid.R;
import com.example.tong.expressandroid.UpdatePassword;
import com.example.tong.expressandroid.UpdatePhoneAddress;
import com.example.tong.expressandroid.Util.App;

/**
 * Created by Tong on 2017/3/17.
 */

public class Fragment3 extends Fragment {
    private TextView userName,userId,friend,userPassword,out,userPhoneAddress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more, null);
        userName = (TextView) view.findViewById(R.id.user_userName);
        userId = (TextView) view.findViewById(R.id.user_userId);
        friend = (TextView) view.findViewById(R.id.user_friend);
        userPhoneAddress = (TextView) view.findViewById(R.id.user_updatePhoneAddress);
        userPassword = (TextView) view.findViewById(R.id.user_updatePassword);
        out = (TextView) view.findViewById(R.id.user_out);

        userName.setText("帐号："+App.user.getUsername());
        userId.setText("UID："+ App.user.getUserid());

        friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FindAll.class));
            }
        });

        userPhoneAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdatePhoneAddress.class));
            }
        });

        userPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdatePassword.class));
            }
        });

        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Login.class));
                App.user = null;
                getActivity().finish();
            }
        });

        return view;
    }
}
