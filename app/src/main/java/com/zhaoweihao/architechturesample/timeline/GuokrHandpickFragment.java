package com.zhaoweihao.architechturesample.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoweihao.architechturesample.R;

public class GuokrHandpickFragment extends Fragment {

    public GuokrHandpickFragment() {

    }

    public static GuokrHandpickFragment newInstance() {
        return new GuokrHandpickFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_page, container, false);

        return view;
    }
}
