package com.ht.fyforandroid.widget.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.ht.fyforandroid.R;
import com.ht.fyforandroid.base.BaseActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by niehongtao on 16/11/4.
 */
public class FilterTestActivity extends BaseActivity {


    @InjectView(R.id.tv_show)
    TextView mTvShow;
    @InjectView(R.id.expand_tabview)
    ExpandTabView expandTabView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filter_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

    }

    @Override
    protected void initView() {
        super.mLoadingDialog.hideLoading();
        initFilterView();
    }

    @Override
    protected void initData() {

    }


    private void initFilterView() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        Fragment1 fragment1 = new Fragment1();
        Fragment2 fragment2 = new Fragment2();
        fragments.add(fragment1);
        fragments.add(fragment2);

        CommonFragmentAdapter mCommonFragmentAdapter = new CommonFragmentAdapter(getSupportFragmentManager(), fragments);
        expandTabView.setFilterAdapter(mCommonFragmentAdapter);

        ArrayList<String> nameList = new ArrayList<>();
        nameList.add("性别");
        nameList.add("地点");
        expandTabView.setNameList(nameList);

        fragment1.setSelectListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvShow.setText("111111111111111");
                expandTabView.setTabTxt("1111111");
            }
        });
    }


}
