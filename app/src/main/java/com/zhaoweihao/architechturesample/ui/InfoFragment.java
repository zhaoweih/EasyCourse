package com.zhaoweihao.architechturesample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhaoweihao.architechturesample.R;

public class InfoFragment extends Fragment {

    //View的引用
    private Button mButton;

    public InfoFragment() {

    }

    public static InfoFragment newInstance() { return new InfoFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        initViews(view);

        mButton.setOnClickListener( v -> {
            Intent intent = new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        });


        return view;
    }

    public void initViews(View view) {
        mButton = view.findViewById(R.id.btn_login);
    }
}

