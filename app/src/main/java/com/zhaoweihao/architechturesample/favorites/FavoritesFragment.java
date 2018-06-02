package com.zhaoweihao.architechturesample.favorites;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhaoweihao.architechturesample.R;

public class FavoritesFragment extends Fragment {

    public FavoritesFragment() {

    }

    public static FavoritesFragment newInstance() { return new FavoritesFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fav_container, new FavoritesPreferenceFragment())
                .commit();


        return view;
    }
}
