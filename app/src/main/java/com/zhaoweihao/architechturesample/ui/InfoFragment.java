package com.zhaoweihao.architechturesample.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoweihao.architechturesample.R;

public class InfoFragment extends Fragment {

    public InfoFragment() {

    }

    public static InfoFragment newInstance() { return new InfoFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.info_container, new InfoPreferenceFragment())
                .commit();


        return view;
    }

}

