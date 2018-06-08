package com.zhaoweihao.architechturesample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zhaoweihao.architechturesample.R;
import com.zhaoweihao.architechturesample.database.User;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InfoFragment extends Fragment {

    @BindView(R.id.fl_login)
    FrameLayout login;
    @BindView(R.id.fl_ps)
    FrameLayout personal;
    @BindView(R.id.fl_ab)
    FrameLayout about;

    public InfoFragment() {

    }

    public static InfoFragment newInstance() { return new InfoFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_fragment_info, container, false);

        ButterKnife.bind(this, view);

        login.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });
        personal.setOnClickListener(v -> {
            User user3 = DataSupport.findLast(User.class);
            if (user3 == null) {
                Toast.makeText(getActivity(), "请先登录！", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getActivity(), UserInformation.class);
                startActivity(intent);
            }
        });
        about.setOnClickListener(v -> {

        });

//        getChildFragmentManager().beginTransaction()
//                .replace(R.id.info_container, new InfoPreferenceFragment())
//                .commit();


        return view;
    }

}

