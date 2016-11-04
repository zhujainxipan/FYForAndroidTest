package com.ht.fyforandroid.widget.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by niehongtao on 16/10/31.
 */
public class Fragment1 extends Fragment {

    private View view1;

    private View.OnClickListener mSelectListener;

    public void setSelectListener(View.OnClickListener selectListener) {
        mSelectListener = selectListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false);
        view1 = view.findViewById(R.id.tv_1);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectListener != null) {
                    mSelectListener.onClick(v);
                }
            }
        });
    }
}
