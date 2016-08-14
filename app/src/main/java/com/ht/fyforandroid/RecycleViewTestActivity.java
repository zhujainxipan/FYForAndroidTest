package com.ht.fyforandroid;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ht.fyforandroid.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by niehongtao on 16/8/14.
 */
public class RecycleViewTestActivity extends BaseActivity {
    @InjectView(R.id.view_recycler)
    RecyclerView mRecyclerView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_rv;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        super.mLoadingDialog.hideLoading();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, getListdata());
        mRecyclerView.setAdapter(viewAdapter);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public List<String> getListdata() {
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            strings.add(i, i + "ddddd");
        }
        return strings;
    }
}
